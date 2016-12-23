package Negocio;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import Entidades.Producto;

/**
 * Created by ogil on 28/09/2016.
 */
public class Session {

    /*
    * Valida que existan los productos
    * */
    public static boolean CreateArrayPreference(Context pContext, ArrayList<Producto> misproductos){
        SharedPreferences prefs =   pContext.getSharedPreferences(Constantes.Preferences,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        if(prefs != null && prefs.contains("misproductos")){
            return true;
        }
        else{
            if(prefs != null ){
                Gson gson = new Gson();
                String json = gson.toJson(misproductos);
                prefsEditor.putString("misproductos", json);
                prefsEditor.commit();
                return  true;
            }
            else{
                /*Gson gson = new Gson();
                String json = mPrefs.getString("MyObject", "");
                MyObject obj = gson.fromJson(json, MyObject.class);*/
                return false;
            }
        }
    }

    /*
    * Guarda las configuraciones de sonido
    * */
    public static void ValidateSonido(Context pcontext,boolean IsMute){
        SharedPreferences prefs =   pcontext.getSharedPreferences(Constantes.Preferences,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean("IsMute", IsMute);
        prefsEditor.commit();
    }


    /*
    * Valida si la app esta en silencio o si tiene sonido
    * Si retorna verdadero la aplicacion estara en silencio sino la aplicacion tiene sonido
    * */
    public static boolean IsMute (Context pcontext){
        SharedPreferences prefs =   pcontext.getSharedPreferences(Constantes.Preferences,Context.MODE_PRIVATE);
        if(prefs != null && prefs.contains("IsMute")){
            return prefs.getBoolean("IsMute",false);
        }
        else{
            return false;
        }
    }
}
