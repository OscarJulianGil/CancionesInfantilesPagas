package Negocio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Entidades.Producto;

/**
 * Created by ogil on 02/09/2016.
 */
public class Recursos {


    /*
    * Vuelve la pantalla full screen
    * */
    public static  void DoFullScreen(Activity pActivity){
        ///Oculta los controles laterales de la pantalla
        if(Build.VERSION.SDK_INT < 19){
            View v = pActivity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = pActivity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /*
    * Retorna el tamaño de la pantalla
    * 1 Es small
    * 2 Es Normal
    * 3 Es large
    * 4 Es Extra large
    * 0 indefinido
    * */
    public static int GetSizeScreen(Context pContext){
        if ((pContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            return 1;
        }
        else if ((pContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            return 2;
        }
        else if ((pContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return 3;
        }
        else if ((pContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            return 4;
        }
        else
            return 0;
    }

    /*Retorna los DPI de un tamaño*/
    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    /*
    * Guarda los productos del usuario
    * */
    public static void SavePreferences(ArrayList<Producto> mis_productos,Context pContext){
        try {
            SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mis_productos);
            editor.putString("mis_productos", json);
            editor.commit();
        }
        catch (Exception e){
            Log.e("Error","ErrorSavingdatauser" + e.getMessage());
        }
    }

    /*
    * Recupera los productos del usuario
    * */
    public  static ArrayList<Producto> Getmis_productos(Context pContext){
        try {
            Gson gson = new Gson();
            SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, Context.MODE_PRIVATE);
            String jsonText = prefs.getString("mis_productos", null);
            ArrayList<Producto> mis_productos;  //EDIT: gso to gson
            mis_productos = gson.fromJson(jsonText, new TypeToken<List<Producto>>() {
            }.getType());
            return mis_productos;
        }
        catch (Exception e){
            return  null;
        }
    }


    public static boolean isOnline(Context pContext) {
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean DeviceOnline(Context pContext) {
        if (isOnline(pContext)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.anywebsiteyouthinkwillnotbedown.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000); //choose your own timeframe
                urlc.setReadTimeout(4000); //choose your own timeframe
                urlc.connect();
                int networkcode2 = urlc.getResponseCode();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                return (false);  //connectivity exists, but no internet.
            }
        } else {
            return false;  //no connectivity
        }
    }
}
