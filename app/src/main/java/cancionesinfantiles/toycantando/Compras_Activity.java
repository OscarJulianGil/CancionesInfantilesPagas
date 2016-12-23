package cancionesinfantiles.toycantando;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import Entidades.Producto;
import Negocio.Constantes;
import Negocio.Recursos;
import cancionesinfantiles.toycantando.util.IabHelper;
import cancionesinfantiles.toycantando.util.IabResult;
import cancionesinfantiles.toycantando.util.Purchase;

public class Compras_Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * cancionesinfantiles.toycantando.fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static  int PositionShow = -1;
    private Button btnizquierda;
    private Button btnderecha;
    public static IabHelper mHelper;
    public static  Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto=this;
        InitLayout();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void InitLayout(){
        setContentView(R.layout.activity_compras);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if(PositionShow != -1)
            mViewPager.setCurrentItem(PositionShow);
        Initcontros();
        Recursos.DoFullScreen(this);
    }

    private void Initcontros(){
        btnizquierda = (Button) findViewById(R.id.btnizquierda);
        btnderecha = (Button) findViewById(R.id.btnderecha);
        btnderecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  actual = mViewPager.getCurrentItem();
                if(actual >= 0)
                {
                    actual++;
                    mViewPager.setCurrentItem(actual);
                }
            }
        });
        btnizquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  actual = mViewPager.getCurrentItem();
                if(actual <= 19)
                {
                    actual--;
                    mViewPager.setCurrentItem(actual);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("compras", "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d("compras", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Recursos.DoFullScreen(this);
    }

    @Override protected void onRestart() {
        super.onRestart();
        InitLayout();
    }
    @Override
    public void onPause() {
        super.onPause();
        //this.stopService(new Intent(this, MusicService.class));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.stopService(new Intent(this, MusicService.class));
        /*if (mService != null) {
            unbindService(mServiceConn);
        }*/
        if (mHelper != null)
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        mHelper = null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Button btncerrarpantallacompra;
        private Button btnCompraUnitaria;
        private Button btncajoncompraunidad;
        private TextView lbValorTodaColeccion;
        private Button btnComprarTodaColeccion;
        private Producto productoToComprar;
        private boolean IsTodaColeccion;
        private Button lbNombrePersonaje;
        private Button btnVerVideo;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            ///Inicializa el servicio de compras con Google Play
            mHelper = new IabHelper(getContext(), Constantes.keyCompras);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        Toast.makeText(getContext(),"No puedes facturar, valida tus servicios de Google Play Store" + result,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.e("Dato","Comunicacion exitosa con google play services en las compras");
                    }
                }
            });
            Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/boogaloo.otf");

            View rootView = inflater.inflate(R.layout.fragment_compras, container, false);
            ImageView personaje = (ImageView) rootView.findViewById(R.id.personaje);
            btnCompraUnitaria=(Button) rootView.findViewById(R.id.btnCompraUnitaria);
            btnCompraUnitaria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IsTodaColeccion=false;
                    if(productoToComprar!= null)
                        GoComprar(productoToComprar.getIdGoogleCompra());
                }
            });
            btncerrarpantallacompra = (Button) rootView.findViewById(R.id.btncerrarpantallacompra);
            btncajoncompraunidad = (Button) rootView.findViewById(R.id.btncajoncompraunidad);
            btncajoncompraunidad.setTypeface(custom_font);
            lbValorTodaColeccion =(TextView) rootView.findViewById(R.id.lbValorTodaColeccion);
            Producto prd1 = Gallery_ACtivity.SearchProducto(20);
            lbValorTodaColeccion.setText(prd1.getPrecio());
            lbValorTodaColeccion.setTypeface(custom_font);
            btncerrarpantallacompra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                    //Intent goInicio= new Intent(getContext(),Gallery_ACtivity.class);
                    //startActivity(goInicio);
                }
            });
            btnVerVideo=(Button) rootView.findViewById(R.id.btnVerVideo);
            btnVerVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.Position =productoToComprar.getPosition();
                    Intent GoVideo = new Intent(getContext(),VideoActivity.class);
                    startActivity(GoVideo);
                }
            });
            btnComprarTodaColeccion = (Button)rootView.findViewById(R.id.btnComprarTodaColeccion);
            btnComprarTodaColeccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IsTodaColeccion=true;
                    GoComprar(Constantes.TodaColeccionKey);
                }
            });
            lbNombrePersonaje = (Button) rootView.findViewById(R.id.lbNombrePersonaje);
            lbNombrePersonaje.setTypeface(custom_font);
            ///Inicio para armar los diferentes Fragment
            if(getArguments().getInt(ARG_SECTION_NUMBER)  == 1) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.pimpon));
                Producto prd = Gallery_ACtivity.SearchProducto(0);
                productoToComprar = prd;
                btncajoncompraunidad.setText(prd.getPrecio());
                lbNombrePersonaje.setText("Pin Pon");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);
                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);
                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 2) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.pinocho));
                Producto prd = Gallery_ACtivity.SearchProducto(1);
                productoToComprar = prd;
                btncajoncompraunidad.setText(prd.getPrecio());
                lbNombrePersonaje.setText("Pinocho");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 3) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.mi_carita));
                Producto prd = Gallery_ACtivity.SearchProducto(2);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Mi Carita");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 4) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.barquito));
                Producto prd = Gallery_ACtivity.SearchProducto(3);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("El Barquito Chiquitito");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            /*else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 5) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.sol_solecito));
                Producto prd = Gallery_ACtivity.SearchProducto(4);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Sol Solecito");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }*/
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 5) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.patico_patico));
                Producto prd = Gallery_ACtivity.SearchProducto(4);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Patico, Patico");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 6) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.tres_elefantes));
                Producto prd = Gallery_ACtivity.SearchProducto(5);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Tres Elefantes");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 7) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.a_mi_burro));
                Producto prd = Gallery_ACtivity.SearchProducto(6);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("A Mi Burro");
            }
            /*else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 9) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.los_pollitos));
                Producto prd = Gallery_ACtivity.SearchProducto(8);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Los Pollitos Dicen");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }*/
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 8) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.arroz_con_leche));
                Producto prd = Gallery_ACtivity.SearchProducto(7);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Arroz Con Leche");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 9) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.a_la_vibora_de_la_mar));
                Producto prd = Gallery_ACtivity.SearchProducto(8);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("A La Víbora De La Mar");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 10) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.cucu));
                Producto prd = Gallery_ACtivity.SearchProducto(9);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Cucú");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);
                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);
                }
            }
            /*else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 13) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.vaca_lechera));
                Producto prd = Gallery_ACtivity.SearchProducto(12);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("La Vaca Lechera");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }*/
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 11) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.debajo_de_un_boton));
                Producto prd = Gallery_ACtivity.SearchProducto(10);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Debajo De Un Botón");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 12) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.juguemos_en_el_bosque));
                Producto prd = Gallery_ACtivity.SearchProducto(11);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Juguemos En El Bosque");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 13) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.la_pajara_pinta));
                Producto prd = Gallery_ACtivity.SearchProducto(12);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("La Pájara Pinta");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 14) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.cuando_tengas_muchas_ganas));
                Producto prd = Gallery_ACtivity.SearchProducto(13);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Cuando Tengas Muchas Ganas");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 15) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.el_patio_de_mi_casa));
                Producto prd = Gallery_ACtivity.SearchProducto(14);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("El Patio De Mi Casa");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);
                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 16) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.la_muneca_vestida_de_azul));
                Producto prd = Gallery_ACtivity.SearchProducto(15);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("La Muñeca Vestida De Azul");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)  == 17) {
                personaje.setImageDrawable(getResources().getDrawable(R.drawable.este_dedito));
                Producto prd = Gallery_ACtivity.SearchProducto(16);
                btncajoncompraunidad.setText(prd.getPrecio());
                productoToComprar = prd;
                lbNombrePersonaje.setText("Este Dedito Compró Un Huevito");
                if(prd.isPurchased()){
                    btnVerVideo.setVisibility(View.VISIBLE);
                    btnCompraUnitaria.setVisibility(View.GONE);

                }
                else{
                    btnVerVideo.setVisibility(View.GONE);
                    btnCompraUnitaria.setVisibility(View.VISIBLE);

                }
            }
            return rootView;
        }


        private void GoComprar(String ItemID){
            try {
                mHelper.launchPurchaseFlow(getActivity(), ItemID, 10001,
                        mPurchaseFinishedListener, "");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }


        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
            {
                if (result.isFailure()) {
                }
                else if (purchase.getSku().equals(productoToComprar.getIdGoogleCompra())) {
                    new Compras_Activity().GoGallery(productoToComprar,IsTodaColeccion);
                }
                else if(IsTodaColeccion){
                    if(purchase.getSku().equals(Constantes.TodaColeccionKey)){
                        new Compras_Activity().GoGallery(productoToComprar,IsTodaColeccion);
                    }
                }
            }
        };
    }

    /*
    * Cuando se compra un producto lo envia a la actividad principal para iniciar la descarga
    * */
    public void GoGallery(Producto prd,boolean todo){
        Gallery_ACtivity.producto_comprado = prd;
        Intent goInicio= new Intent(contexto,Gallery_ACtivity.class);
        if(todo)
            goInicio.putExtra("TODO",1);
        else
            goInicio.putExtra("TODO",0);

        contexto.startActivity(goInicio);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 17;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
                case 4:
                    return "";
                case 5:
                    return "";
                case 6:
                    return "";
                case 7:
                    return "";
                case 8:
                    return "";
                case 9:
                    return "";
                case 10:
                    return "";
                case 11:
                    return "";
                case 12:
                    return "";
                case 13:
                    return "";
                case 14:
                    return "";
                case 15:
                    return "";
                case 16:
                    return "";
            }
            return null;
        }
    }
}
