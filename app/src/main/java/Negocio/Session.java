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



    public static void setShowAds(Context pcontext,boolean ShowAds){
        SharedPreferences prefs =   pcontext.getSharedPreferences(Constantes.Preferences,Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean("ShowAds", ShowAds);
        prefsEditor.commit();
    }


    public static boolean ShowAds (Context pcontext){
        SharedPreferences prefs =   pcontext.getSharedPreferences(Constantes.Preferences,Context.MODE_PRIVATE);
        if(prefs != null && prefs.contains("ShowAds")){
            return prefs.getBoolean("ShowAds",false);
        }
        else{
            return false;
        }
    }




    ////Guarda cuantas veces el usuario a terminado de ver el video, para mostrar el popup de
    /// calificacion de la app
    public  static void set_times_watched_video_finish(Context pContext,int value){
        SharedPreferences.Editor editor = pContext.getSharedPreferences(Constantes.Preferences,pContext.MODE_PRIVATE).edit();
        editor.putInt("times_watched_video_finish", value);
        editor.commit();
    }

    ////Obtiene cuantas veces el usuario a terminado de ver el video, para mostrar el popup de
    /// calificacion de la app
    public  static int get_times_watched_video_finish(Context pContext){
        try {
            int veces = 0;
            SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE);
            veces = prefs.getInt("times_watched_video_finish", 0);
            return veces;
        }
        catch (Exception ex){
            return  0;
        }
    }

    public  static void set_set_times_watched_video(Context pContext,int value){
        try
        {
            SharedPreferences.Editor editor = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE).edit();
            editor.putInt("times_watched_video", value);
            editor.commit();
        }
        catch (Exception e){

        }
    }

    public  static int get_set_times_watched_video(Context pContext){
        try {
            int veces = 0;
            SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE);
            veces = prefs.getInt("times_watched_video", 0);
            return veces;
        }
        catch (Exception ex){
            return  0;
        }
    }

    public static  void set_video_watched(Context pContext,String value){
        SharedPreferences.Editor editor = pContext.getSharedPreferences(Constantes.Preferences,pContext.MODE_PRIVATE).edit();
        editor.putString("date_watched", value);
        editor.commit();
    }


    public  static  String get_video_watched(Context pContext){
        try {
            String number_watched = "";
            SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE);
            number_watched = prefs.getString("date_watched", "");
            return number_watched;
        }
        catch (Exception ex){
            return  "";
        }
    }


    ///Valida si la aplicacion ya fue calificada
    ///True si el usuario califico, False si aun no ha calificado
    public  static  boolean get_IsRanking(Context pContext){
        SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE);
        return prefs.getBoolean("IsRanking", false);
    }

    public static  void set_IsRanking(Context pContext, boolean value){
        SharedPreferences.Editor editor = pContext.getSharedPreferences(Constantes.Preferences,pContext.MODE_PRIVATE).edit();
        editor.putBoolean("IsRanking", value);
        editor.commit();
    }


    ///Guarda la ultima fecha en la cual se mostro el modal de calificacion de la aplicacion
    public static  void set_times_open_app_ranking(Context pContext,String value){
        SharedPreferences.Editor editor = pContext.getSharedPreferences(Constantes.Preferences,pContext.MODE_PRIVATE).edit();
        editor.putString("times_open_app_ranking", value);
        editor.commit();
    }

    ////Guarda la ultima fecha en la cual se mostro el modal de la  publicidad
    public  static  String get_times_open_app_ranking(Context pContext){
        String number_watched = "";
        SharedPreferences prefs = pContext.getSharedPreferences(Constantes.Preferences, pContext.MODE_PRIVATE);
        number_watched = prefs.getString("times_open_app_ranking", "");
        return number_watched;
    }


}
