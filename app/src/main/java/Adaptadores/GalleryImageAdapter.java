package Adaptadores;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import Negocio.Recursos;
import cancionesinfantiles.toycantando.R;

/**
 * Created by ogil on 02/09/2016.
 */
public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.pimpon,
            R.drawable.pinocho,
            R.drawable.mi_carita,
            R.drawable.barquito,
            R.drawable.sol_solecito,
            R.drawable.patico_patico,
            R.drawable.tres_elefantes,
            R.drawable.a_mi_burro,
            R.drawable.los_pollitos,
            R.drawable.arroz_con_leche,
            R.drawable.a_la_vibora_de_la_mar,
            R.drawable.cucu,
            R.drawable.vaca_lechera,
            R.drawable.debajo_de_un_boton,
            R.drawable.juguemos_en_el_bosque,
            R.drawable.la_pajara_pinta,
            R.drawable.cuando_tengas_muchas_ganas,
            R.drawable.el_patio_de_mi_casa,
            R.drawable.la_muneca_vestida_de_azul,
            R.drawable.este_dedito
    };

    public GalleryImageAdapter(Context context)
    {

        mContext = context;
    }

    public int getCount() {
        return mImageIds.length;
        //return Integer.MAX_VALUE;  Habilitar para el infinito
    }


    public Object getItem(int position) {
        /*if (position >= mImageIds.length) {
            position = position % mImageIds.length;
        }*/ //Habilitar para el infinito
        return position;
    }

    public long getItemId(int position) {
        /*if (position >= mImageIds.length) {
            position = position % mImageIds.length;
        }*/ //Habilitar para el infinito
        return position;
    }

    // Override this method according to your need
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        final ImageView imagen = new ImageView(mContext);
        /*if (position >= mImageIds.length) {
            position = position % mImageIds.length;
        }*/ //Habilitar para el infinito
        imagen.setImageResource(mImageIds[position]);
        int valor=186;
        int valor2 = 151;
        int width;
        int height;
        switch (Recursos.GetSizeScreen(mContext)){
            case 1://Small
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor*0.75),mContext.getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor2*0.75),mContext.getResources().getDisplayMetrics());
                imagen.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
            case 2://Normal
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor,mContext.getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor2,mContext.getResources().getDisplayMetrics());
                imagen.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
            case 3://Large
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor*1.5),mContext.getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor2*1.5),mContext.getResources().getDisplayMetrics());
                imagen.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
            case 4://Extra Large
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor * 2,mContext.getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(valor2*2),mContext.getResources().getDisplayMetrics());
                imagen.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
            default:
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor,mContext.getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor2,mContext.getResources().getDisplayMetrics());
                imagen.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
        }
        imagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imagen;
    }

    public int checkPosition(int position) {
        /*if (position >= mImageIds.length) {
            position = position % mImageIds.length;
        }*/ //Habilitar para el infinito
        return position;
    }
}

