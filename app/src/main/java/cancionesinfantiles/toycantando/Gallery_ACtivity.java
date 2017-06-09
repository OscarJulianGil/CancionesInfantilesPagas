package cancionesinfantiles.toycantando;

import android.*;
import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Path;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import Adaptadores.GalleryImageAdapter;
import Entidades.Producto;
import Negocio.Constantes;
import Negocio.ProductoEntity;
import Negocio.Recursos;
import Negocio.Session;
import cancionesinfantiles.toycantando.util.IabHelper;
import cancionesinfantiles.toycantando.util.IabResult;
import cancionesinfantiles.toycantando.util.Inventory;
import cancionesinfantiles.toycantando.util.Purchase;

public class Gallery_ACtivity extends AppCompatActivity implements  View.OnClickListener {

    private  Gallery g;
    public  static ArrayList<Producto> mis_productos;
    IabHelper mHelper;
    private MediaPlayer mp1;
    private TextView btncaja;
    private Button derecha;
    private Button izquierda;
    private Button btnsonido;
    private Button btncandado;
    private View   seleccionado;
    private Button textoderecha;
    private Button textoizquierda;
    private int positionObjectGallery=0;
    private long DownloadManagerId;
    private Button imagelogo;
    private BroadcastReceiver receiver;
    private ProgressBar mProgressBar;
    private TextView lbdescargando;
    private boolean DownloadInProgress = false;
    private AlertDialog alert;
    private ImageButton btn_jugar;
    private Integer[] mImageIds = {
            R.drawable.pimpon,
            R.drawable.mi_carita,
            R.drawable.ronda_conejos,
            R.drawable.tio_mario,
            R.drawable.pinocho,
            R.drawable.cuando_tengas_muchas_ganas,
            R.drawable.barquito,
            R.drawable.patico_patico,
            R.drawable.tres_elefantes,
            R.drawable.a_mi_burro,
            R.drawable.arroz_con_leche,
            R.drawable.auto_papa,
            R.drawable.a_la_vibora_de_la_mar,
            R.drawable.cucu,
            R.drawable.debajo_de_un_boton,
            R.drawable.juguemos_en_el_bosque,
            R.drawable.la_pajara_pinta,
            R.drawable.el_patio_de_mi_casa,
            R.drawable.la_muneca_vestida_de_azul,
            R.drawable.este_dedito
    };
    public static Producto producto_comprado;
    ///Control del sonido de la app
    public static MediaPlayer player;

    public static InterstitialAd interstitial;
    public InMobiInterstitial interstitialAd;
    public boolean mCanShowAd;
    public static boolean CallFromVideo = false;
    private RatingBar ratingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery__activity);
        interstitialAd = new InMobiInterstitial(Gallery_ACtivity.this, 1491021223831L, mInterstitialListener);
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.ERROR);

        player = MediaPlayer.create(this, R.raw.audiofondo);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        player.start();
        ///Oculta la barra de notificaciones del telefono
        getSupportActionBar().hide();
        Recursos.DoFullScreen(this);
        checkPlayServices();
        g = (Gallery) findViewById(R.id.gallery1);

        g.setAdapter(new GalleryImageAdapter(this));
        SharedPreferences prefs = getSharedPreferences(Constantes.Preferences, Context.MODE_PRIVATE);
        if(prefs != null && prefs.contains("mis_productos")){
            mis_productos = new ProductoEntity().DoMisProductos(this);//Recursos.Getmis_productos(this);
            //Se asegura que mis_productos no quede nulo
            if(mis_productos == null)
                mis_productos = new ProductoEntity().DoMisProductos(this);
        }
        else{
            mis_productos= new ProductoEntity().DoMisProductos(this);
            Recursos.SavePreferences(mis_productos,this);
        }

        ///Anuncio antes del video
        AdRequest adRequest1 = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(Gallery_ACtivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitial.loadAd(adRequest1);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdClosed() {
                Intent goReproductor= new Intent(getBaseContext(),VideoActivity.class);
                startActivity(goReproductor);
        }});

        ///Inicializa el servicio de compras con Google Play
        mHelper = new IabHelper(this, Constantes.keyCompras);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh no, there was a problem.
                    Toast.makeText(getBaseContext(),"No puedes facturar, valida tus servicios de Google Play Store" + result,Toast.LENGTH_SHORT).show();
                    //Log.e("ERROR", "Problem setting up In-app Billing: " + result);
                }
                else {
                    Log.e("OK", "Comunicacion exitosa con google play Store");
                    DoListProducts();
                }
            }
        });
        InitControls();
        ValidateCompra();
        g.setSelection(1);

        if(CallFromVideo) {
            show_calification_modal();
            CallFromVideo=false;
        }

    }


    ///Valida si debe mostrar el modal de calificacion de la app
    public void show_calification_modal(){
        ///Valida si debe mostrar el popup de calificacion
        if(!Session.get_IsRanking(this)){
            try {
                int veces_visto= Session.get_times_watched_video_finish(this);
                int veces_debe_ver = (Integer.parseInt(getResources().getString(R.string.veces_visto_calificacion)) - 1);
                Date cDate = new Date();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                Log.e("Visto publicidad",veces_visto + "");
                if(veces_visto == veces_debe_ver && !date.equals(Session.get_times_open_app_ranking(this))){
                    /// va a mostrar el modal de calificacion de la aplicacion
                    Session.set_times_open_app_ranking(this,date);
                    Session.set_times_watched_video_finish(this,0);
                    ///Muestra el modal de quitar publicidad y va al video
                    showCalification();
                }
                else{
                    if(veces_visto >= veces_debe_ver)
                        Session.set_times_watched_video_finish(this,0);
                    else
                        Session.set_times_watched_video_finish(this,veces_visto + 1);
                }
            }
            catch (Exception e){}
        }
    }


    ///Modal de calificacion de la aplicacion
    private void showCalification() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setView(R.layout.popup_calificacion);

        alert = builder.create();
        alert.show();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            alert.getWindow().setLayout(((width * 80)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        else{
            alert.getWindow().setLayout(((width * 60)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        ratingbar = (RatingBar) alert.findViewById(R.id.ratingbar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                alert.dismiss();
                if(rating > 3){
                    final String my_package_name = "cancionesinfantiles.toycantando";  // <- HERE YOUR PACKAGE NAME!!
                    String url = "";

                    try {
                        //Check whether Google Play store is installed or not:
                        getPackageManager().getPackageInfo("com.android.vending", 0);
                        url = "market://details?id=" + my_package_name;
                    } catch ( final Exception e ) {
                        url = "https://play.google.com/store/apps/details?id=" + my_package_name;
                    }
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(intent);
                }
                else {
                    /*Uri uri = Uri.parse(Constantes.form_contact);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);*/
                    Intent goWebView= new Intent(getBaseContext(),webViewActivityBar.class);
                    startActivity(goWebView);
                }
                Session.set_IsRanking(getBaseContext(),true);
            }
        });
        Button btn_cancelar= (Button)alert.findViewById(R.id.btn_cancelar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    ///INICIO: Metodos para Comprar algun producto de la aplicacion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
        }
    }

    private void GoComprar(String ItemID){
        try {
            mHelper.launchPurchaseFlow(this, ItemID, 10001,mPurchaseFinishedListener, "");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                mHelper = new IabHelper(getBaseContext(), Constantes.keyCompras);
                mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    public void onIabSetupFinished(IabResult result) {
                        if (!result.isSuccess()) {
                            Toast.makeText(getBaseContext(),"No puedes facturar, valida tus servicios de Google Play Store" + result,Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("Dato","Comunicacion exitosa con google play services en las compras");
                        }
                    }
                });
            }
            else if (purchase.getSku().equals(Constantes.nopublicidad)) {
                Session.setShowAds(getBaseContext(),true);
                new DowloadVideo(true).execute();
            }
        }
    };
    ///FIN: Metodos para Comprar algun producto de la aplicacion


    InMobiInterstitial.InterstitialAdListener2 mInterstitialListener = new InMobiInterstitial.InterstitialAdListener2() {
        @Override
        public void onAdLoadFailed(InMobiInterstitial inMobiInterstitial, InMobiAdRequestStatus inMobiAdRequestStatus) {
            Log.e("Intersitial", "Failed intersitial");
        }

        @Override
        public void onAdReceived(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "recibido");
        }

        // implementation for other events
        // onAdReceived, onAdLoaFailed, etc
        @Override
        public void onAdLoadSucceeded(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "Ad can now be shown!");
            interstitialAd.load();
        }

        @Override
        public void onAdRewardActionCompleted(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
            Log.e("Intersitial", "onAdRewardActionCompleted");
        }

        @Override
        public void onAdDisplayFailed(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "onAdDisplayFailed!");
        }

        @Override
        public void onAdWillDisplay(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "onAdWillDisplay");
        }

        @Override
        public void onAdDisplayed(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "onAdDisplayed");
        }

        @Override
        public void onAdInteraction(InMobiInterstitial inMobiInterstitial, Map<Object, Object> map) {
            Log.e("Intersitial", "onAdInteraction");
        }

        @Override
        public void onAdDismissed(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "onAdDismissed");
        }

        @Override
        public void onUserLeftApplication(InMobiInterstitial inMobiInterstitial) {
            Log.e("Intersitial", "onUserLeftApplication");
        }
    };
    void prepareGameLevel() {
        interstitialAd.load();
    }
    void handleGameLevelCompleted() {
        if(mCanShowAd) interstitialAd.show();
    }



    private  void ValidateCompra(){

        /*
        * Valida si se compro algo
        * */
        Bundle extras = getIntent().getExtras();
        ///Compro un solo producto
        if (extras != null && extras.getInt("TODO") == 0) {
            if(producto_comprado != null)
                new DowloadVideo(producto_comprado,false).execute();
            else
                Toast.makeText(this,"No hay un producto para comprar",Toast.LENGTH_SHORT).show();
        }
        ///Compro toda la coleccion
        else if(extras != null && extras.getInt("TODO") == 1){
            new DowloadVideo(producto_comprado,true).execute();
        }

        /*
        * Para android M o superior debe pedir autorizacion directa al usuario
        * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ValidatePermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /*
     * Muestra el anuncio
     * */
    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded() && !Session.ShowAds(this)) {
            interstitial.show();
        }
        else{
            Intent initCompra = new Intent(getBaseContext(), VideoActivity.class);
            startActivity(initCompra);
        }
    }

    /*
    * Verifica si tiene los permisos necesarios para la aplicacion
    * Esto lo usa android M
    * */
    private void ValidatePermission(String permiso){
        if (ContextCompat.checkSelfPermission(this,permiso)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permiso)) {

                Toast.makeText(this,"Para acceder a todos los recursos de la aplicación debes proporcionar los siguientes permisos",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{permiso},2016);

            } else {
                ActivityCompat.requestPermissions(this,new String[]{permiso},2016);
            }
        }
    }

    /*
    * Abre la modal de permisos a la aplicacion
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2016: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /*Lista los productos*/
    private void DoListProducts() {
        ArrayList<String> listaProductos = new ArrayList();
        listaProductos.add(Constantes.pimponKey);
        listaProductos.add(Constantes.rondaconejos);
        listaProductos.add(Constantes.tiomario);
        listaProductos.add(Constantes.autopapa);
        listaProductos.add(Constantes.pinochoKey);
        listaProductos.add(Constantes.micaritaKey);
        listaProductos.add(Constantes.barquitoKey);
        listaProductos.add(Constantes.solsolecitoKey);
        listaProductos.add(Constantes.paticoKey);
        listaProductos.add(Constantes.treselefantesKey);
        listaProductos.add(Constantes.amiburroKey);
        listaProductos.add(Constantes.lospollitosKey);
        listaProductos.add(Constantes.arrozconleche);
        listaProductos.add(Constantes.alaviboradelamarKey);
        listaProductos.add(Constantes.cucuKey);
        listaProductos.add(Constantes.vacalecheraKey);
        listaProductos.add(Constantes.debajodeunbotonKey);
        listaProductos.add(Constantes.juguemosenelbosqueKey);
        listaProductos.add(Constantes.lapajarapintaKey);
        listaProductos.add(Constantes.cuandotengasmuchasganasKey);
        listaProductos.add(Constantes.elpatiodemicasaKey);
        listaProductos.add(Constantes.lamunecavestidadeazul);
        listaProductos.add(Constantes.estededitoKey);
        listaProductos.add(Constantes.TodaColeccionKey);
        try {
            mHelper.queryInventoryAsync(true,listaProductos,listaProductos,mGotInventoryListener);
        } catch (Exception e) {
            Log.e("Error","Servicios de play store" + e.getMessage());
        }
    }

    /*
    * Listener que valida si los items ya fueron comprados por el usuario y consultar sus precios
    * */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                Log.e("Error","Servicios de play store" + result.getMessage());
            }
            else {
                ///Valida primero si el usuario compro la opcion de no ver la publicidad
                Session.setShowAds(getBaseContext(),inventory.hasPurchase(Constantes.nopublicidad));
                ///Recorre el Array principal de productos y valida las compras que tiene el usuario
                if(Gallery_ACtivity.mis_productos != null){
                    ///Antes de iniciar valida si el usuario compro toda la coleccion
                    if(inventory.hasPurchase(Constantes.TodaColeccionKey)){
                        Session.setShowAds(getBaseContext(),true);
                        for (Producto item : Gallery_ACtivity.mis_productos) {
                            if (item.isFree() || item.getPosition() == 20) {
                            } else {
                                item.setPurchased(true);
                            }
                        }
                    }
                    else {
                        for (Producto item : Gallery_ACtivity.mis_productos) {
                            if (item.isFree()) {

                            }
                            else
                            {
                                item.setPurchased(inventory.hasPurchase(item.getIdGoogleCompra()));
                                if (inventory.getSkuDetails(item.getIdGoogleCompra()) != null) {
                                    String Precio = inventory.getSkuDetails(item.getIdGoogleCompra()).getPrice();
                                    item.setPrecio(Precio);
                                }
                            }
                        }
                    }
                    Recursos.SavePreferences(Gallery_ACtivity.mis_productos,getBaseContext());///Actualiza las preferencias de usuario
                }
            }
        }
    };


    ///Inicializa los controles de la actividad
    private void InitControls(){
        mp1 = MediaPlayer.create(this, R.raw.plop);
        ///Evento de click en algun elemento de la galeria
        ///Llama la animacion  para subir al personaje y validar si debe comprarlo o ir al video
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ///Para que valla a hacer cosas, debe estar seleccionado
                int item = parent.getSelectedItemPosition();
                if(position == item){
                    positionObjectGallery = position;
                    if (position >= mImageIds.length) {
                        position = position % mImageIds.length;
                    }
                    AnimationImage(v,position);
                }
            }
        });

        ///Evento de la galeria cuando el item es seleccionado
        g.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                if (position >= mImageIds.length) {
                    position = position % mImageIds.length;
                }

                SetTextImage(position);
                if(seleccionado != null) {
                    AnimationInButton(seleccionado);
                }
                seleccionado = v;
                positionObjectGallery = position;

                Producto prd = SearchProducto(position);
                if(prd != null && prd.isPurchased())
                    btncandado.setVisibility(View.INVISIBLE);
                else
                    btncandado.setVisibility(View.VISIBLE);

                AnimationDownButton(v,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
    });
        btncandado=(Button) findViewById(R.id.btncandado);
        btn_jugar=(ImageButton) findViewById(R.id.btn_jugar);
        btnsonido=(Button) findViewById(R.id.btnsonido);
        btncaja=(TextView) findViewById(R.id.btncaja);
        textoderecha =(Button) findViewById(R.id.textoderecha);
        textoizquierda =(Button) findViewById(R.id.textoizquierda);
        imagelogo = (Button) findViewById(R.id.imagelogo);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/boogaloo.otf");
        imagelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + "https://www.facebook.com/ToyCantando"));
                //startActivity(intent);
                if(DownloadInProgress){
                    Toast.makeText(getBaseContext(),"Hay una descarga en progreso, por favor espera...",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gze.es/RedirectAppFacebook"));
                    startActivity(browserIntent);
                }
            }
        });
        textoderecha.setTypeface(custom_font);

        textoizquierda.setTypeface(custom_font);
        btncaja.setTypeface(custom_font);
        btncaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarVideo(positionObjectGallery);
            }
        });
        derecha=(Button)findViewById(R.id.derecha);
        derecha.setOnClickListener(this);
        izquierda=(Button)findViewById(R.id.izquierda);
        izquierda.setOnClickListener(this);
        btnsonido.setOnClickListener(this);
        btn_jugar.setOnClickListener(this);

    }


    ///Animacion de salto de la imagen
    private void AnimationImage(final View pObject, final int position){
        ObjectAnimator animScrollToTop;
        switch (Recursos.GetSizeScreen(getBaseContext())){
            case 1://Small
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY",  (-20f), (-10f));
                break;
            case 2://Normal
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", -80f, -60f);
                break;
            case 3://Large
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY",  (-80f/1.5f), (-60f/1.5f));
                break;
            case 4://Extra Large
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY",  -(80f/2), (-60f/2));
                break;
            default:
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY",  -80f, -60f);
                break;
        }
        animScrollToTop.setDuration(1000);

        animScrollToTop.setInterpolator(new BounceInterpolator());
        animScrollToTop.setRepeatCount(0);
        animScrollToTop.start();
        animScrollToTop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                ValidarVideo(position);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }



    ///Baja la imagen a la posicion inicial
    private void AnimationInButton(final View pObject){
        int valor=186;
        int valor2 = 151;
        int width;
        int height;
        ObjectAnimator animScrollToTop;
        switch (Recursos.GetSizeScreen(getBaseContext())){
            case 1://Small
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor*0.75),getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor2*0.75),getResources().getDisplayMetrics());
                pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", (-20f), 0f);
                break;
            case 2://Normal
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", -70f, 0f);
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor,getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor2,getResources().getDisplayMetrics());
                pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
            case 3://Large
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor*1.5),getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(int)(valor2*1.5),getResources().getDisplayMetrics());
                pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", (-70f/1.5f), 0f);
                break;
            case 4://Extra Large
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor * 2,getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,(valor2*2),getResources().getDisplayMetrics());
                pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", (-70f/2), 0f);
                break;
            default:
                animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", -70f, 0f);
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor,getResources().getDisplayMetrics());
                width=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valor2,getResources().getDisplayMetrics());
                pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                break;
        }
        animScrollToTop.start();
    }


    /*
    * Animacion que sube la imagen encima de la caja
    * */
    private void AnimationDownButton(final View pObject, int Position){
        int valor=200;
        int valor2=165;
        int width;
        int height;
        ObjectAnimator animScrollToTop;
        if(pObject != null) {
            switch (Recursos.GetSizeScreen(getBaseContext())) {
                case 1://Small
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (valor * 0.75), getResources().getDisplayMetrics());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (valor2 * 0.75), getResources().getDisplayMetrics());
                    pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                    animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", 0f,  0f);
                    break;
                case 2://Normal
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valor, getResources().getDisplayMetrics());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valor2, getResources().getDisplayMetrics());
                    pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                    animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", 0f, -70f);
                    break;
                case 3://Large
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (valor * 1.5), getResources().getDisplayMetrics());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (valor2 * 1.5), getResources().getDisplayMetrics());
                    pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                    animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", 0f, (-70f / 1.5f));
                    break;
                case 4://Extra Large
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valor * 2, getResources().getDisplayMetrics());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (valor2 * 2), getResources().getDisplayMetrics());
                    pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                    animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", 0f, (-70f / 2f));
                    break;
                default:
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valor, getResources().getDisplayMetrics());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valor2, getResources().getDisplayMetrics());
                    pObject.setLayoutParams(new Gallery.LayoutParams(width, height));
                    animScrollToTop = ObjectAnimator.ofFloat(pObject, "translationY", 0f, -70f);
                    break;
            }
            animScrollToTop.setDuration(200);
            animScrollToTop.setRepeatCount(0);
            animScrollToTop.start();
        }
    }

    /*Cuando la actividad se destruye*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null){
            player.stop();
        }
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
        if(mis_productos != null)
            mis_productos=null;
    }

    private void SetTextImage(int position){
       switch (position){
            case 0:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Pin Pon");
                //En la vista infinita habilitar esto
                //if(controltextazul == 1)
                //    textoizquierda.setText("Este Dedito Compró Un Huevito");
                //else
                textoizquierda.setText("");
                textoderecha.setText("Mi carita");
                break;
            case 1:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Mi carita");
                textoizquierda.setText("Pin Pon");
                textoderecha.setText("Ronda de los conejos");
                break;
            case 2:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Ronda de los conejos");
                textoizquierda.setText("Mi carita");
                textoderecha.setText("Tio mario");
                break;
            case 3:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Tio Mario");
                textoizquierda.setText("Ronda de los conejos");
                textoderecha.setText("Pinocho");
                break;
            case 4:
                btncaja.setText("Pinocho");
                textoizquierda.setText("Tio Mario");
                textoderecha.setText("Cuando tengas muchas ganas");
                break;
            case 5:
                btncaja.setText("Cuando tengas muchas ganas");
                textoizquierda.setText("Pinocho");
                textoderecha.setText("Barquito, barquito");
                break;
            case 6:
                btncaja.setText("Barquito, barquito");
                textoizquierda.setText("Cuando tengas muchas ganas");
                textoderecha.setText("Patico patico");
                break;
            case 7:
                btncaja.setText("Patico patico");
                textoizquierda.setText("Barquito, barquito");
                textoderecha.setText("3 Elefantes");
                break;
            case 8:
                btncaja.setText("3 Elefantes");
                textoizquierda.setText("Patico patico");
                textoderecha.setText("A mi burro");
                break;
            case 9:
                btncaja.setText("A mi burro");
                textoizquierda.setText("3 Elefantes");
                textoderecha.setText("Arroz con leche");
                break;
            case 10:
                btncaja.setText("Arroz con leche");
                textoizquierda.setText("A mi burro");
                textoderecha.setText("El auto de papá");
                break;
            case 11:
                btncaja.setText("El auto de papá");
                textoizquierda.setText("Arroz con leche");
                textoderecha.setText("A la vivora de la mar");
                break;
            case 12:
                btncaja.setText("A la vivora de la mar");
                textoizquierda.setText("El auto de papá");
                textoderecha.setText("Cu cu");
                break;
            case 13:
                btncaja.setText("Cu cu");
                textoizquierda.setText("A la vivora de la mar");
                textoderecha.setText("Debajo de un boton");
                break;
            case 14:
                btncaja.setText("Debajo de un boton");
                textoizquierda.setText("Cu cu");
                textoderecha.setText("Juguemos en el bosque");
                break;
            case 15:
                btncaja.setText("Juguemos en el bosque");
                textoizquierda.setText("Debajo de un boton");
                textoderecha.setText("Pajara pinta");
                break;
           case 16:
               btncaja.setText("Pajara pinta");
               textoizquierda.setText("Juguemos en el bosque");
               textoderecha.setText("Patio de mi casa");
               break;
           case 17:
               btncaja.setText("Patio de mi casa");
               textoizquierda.setText("Pajara pinta");
               textoderecha.setText("Muñeca vestida azul");
               break;
           case 18:
               btncaja.setText("Muñeca vestida azul");
               textoizquierda.setText("Patio de mi casa");
               textoderecha.setText("Este Dedito Compró Un Huevito");
               break;
            case 19:
                btncaja.setText("Este Dedito Compró Un Huevito");
                textoizquierda.setText("La Muñeca Vestida De Azul");
                //En la vista infinita habilitar esto
                //textoderecha.setText("Pin Pon");
                textoderecha.setText("");
                break;
        }
    }

    /*
    * Retorna un producto de acuerdo a la posicion
    * */
    public static Producto SearchProducto(int pPosition){
        try {
            if(mis_productos != null && mis_productos.size() > 0) {
                for (Producto item : Gallery_ACtivity.mis_productos) {
                    if (item.getPosition() == pPosition)
                        return item;
                }
            }
            return  null;
        }
        catch (Exception e) {
            return null;
        }
    }

    /*
    * Aca debe validar si manda a la pantalla de compras o si muestra el video
    * */
    private void ValidarVideo(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int Permisos= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(Permisos !=  PackageManager.PERMISSION_GRANTED)
            {
                ValidatePermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int Permisos= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(Permisos !=  PackageManager.PERMISSION_GRANTED)
            {
                ValidatePermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
        }

        if(DownloadInProgress){
            Toast.makeText(getBaseContext(),"Hay una descarga en progreso, por favor espera...",Toast.LENGTH_SHORT).show();
        }
        else {
            Compras_Activity.PositionShow = position;
            Producto pdr = SearchProducto(position);
            ///El producto esta comprado
            if (pdr != null && pdr.isPurchased()) {
                ///Son los videos gratis
                if(!pdr.isFree())
                {
                    File dir =Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local);
                    if (dir.exists() == false) {
                        dir.mkdirs();
                    }
                    dir = Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local + pdr.getNombreProducto() + ".mp4");
                    if (!dir.exists()) {
                        new DowloadVideo(pdr,false).execute();
                    } else {
                        VideoActivity.Position = position;
                        goVideo();
                    }
                }
                //Es el video gratis
                else
                {
                    VideoActivity.Position = position;
                    goVideo();
                }
            }
            else
            {
               Intent goStore = new Intent(this,Compras_Activity.class);
                startActivity(goStore);
            }
        }
    }

    ///Metodo para ir a los videos de la aplicacion
    private void goVideo()
    {
        int veces_visto= Session.get_set_times_watched_video(this);
        Date cDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        if(veces_visto == 2 && !date.equals(Session.get_video_watched(this)) && !Session.ShowAds(this)){
            Session.set_video_watched(this,date);
            Session.set_set_times_watched_video(this,0);
            ///Muestra el modal de quitar publicidad y va al video
            showModalBuyNoAds();
        }
        else {
            Session.set_set_times_watched_video(this,veces_visto + 1);
            Producto prd = SearchProducto(VideoActivity.Position);
            ///Valida si son los videos gratis
            if (prd != null && prd.isFree()) {
                File file = new File(getDir("filesdir", Context.MODE_PRIVATE) + prd.getNombrePersonaje() + ".mp4");
                if (!file.exists()) {
                    ///Antes de iniciar la descarga del video, valida el espacio que tiene el usuario para
                    ///almacenar el video
                    if (Constantes.size_video > getInternalFreeSpace()) {
                        show_modal_information();
                    } else {
                        new DownloadVideo(this, prd).execute();
                    }
                } else {
                    displayInterstitial();
                  }
            } else {
                displayInterstitial();
            }
        }
    }

    ///Modal para validar si desea comprar toda la coleccion
    private void showModalBuyNoAds() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        LayoutInflater inflater = (LayoutInflater)getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflater.inflate(R.layout.popup_publicidad, null);
        builder.setView(vista);

        alert = builder.create();
        alert.show();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        ///Valida la orientacion de la pantalla
        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            alert.getWindow().setLayout(((width * 80)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        else{
            alert.getWindow().setLayout(((width * 60)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Button btn_cancelar= (Button)alert.findViewById(R.id.btn_cancelar);
        Button btn_aceptar= (Button)alert.findViewById(R.id.btn_aceptar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goVideo();
                alert.dismiss();
            }
        });

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoComprar(Constantes.TodaColeccionKey);
                alert.dismiss();
            }
        });
    }



    ////Modal para mostrar informacion
    private void show_modal_information() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setView(R.layout.popup_informativo);

        alert = builder.create();
        alert.show();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        int orientation=this.getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            alert.getWindow().setLayout(((width * 80)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        else{
            alert.getWindow().setLayout(((width * 60)/100), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        Button btn_aceptar= (Button)alert.findViewById(R.id.btn_aceptar);

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }


    ///Valida el espacio de almacenamiento en el dispositivo
    public  long getInternalFreeSpace(){
        try {
            return new File(getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        }
        catch (Exception e){
            return 22000000;
        }
    }


    /*Cuando la actividad queda en backgroud y el usuario retorna, ejecuta este codigo*/
    @Override
    protected void onResume() {
        super.onResume();
        if(player != null){
            if(!Session.IsMute(getBaseContext()))
                player.start();
        }
        //this.startService(new Intent(this, MusicService.class));
        ///Oculta los controles laterales de la pantalla
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override protected void onRestart() {
        super.onRestart();
        if(player != null){
            if(!Session.IsMute(getBaseContext()))
                player.start();
        }
        //this.startService(new Intent(this, MusicService.class));
        SharedPreferences prefs = getSharedPreferences(Constantes.Preferences, Context.MODE_PRIVATE);
        if(prefs != null && prefs.contains("mis_productos")){
            mis_productos = Recursos.Getmis_productos(this);
            //Se asegura que mis_productos no quede nulo
            if(mis_productos == null)
                mis_productos = new ProductoEntity().DoMisProductos(this);
        }
        else{
            mis_productos= new ProductoEntity().DoMisProductos(this);
            Recursos.SavePreferences(mis_productos,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //this.startService(new Intent(this, MusicService.class));
    }

    /*Cuando la actividad queda en backgroud e invisible*/
    @Override
    protected void onStop() {
        super.onStop();
        if(player != null){
            player.pause();
        }
        //this.stopService(new Intent(this, MusicService.class));
        ///Oculta los controles laterales de la pantalla
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(player != null){
            player.pause();
        }
        //this.stopService(new Intent(this, MusicService.class));
        ///Oculta los controles laterales de la pantalla
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /*Maneja los eventos de click de los botones*/
    @Override
    public void onClick(View v) {
        if(v.getId()==derecha.getId()){
            if (mp1.isPlaying()) {
            }
            else {
                mp1.start();
            }
            //Quitar esta validacion para habilitar el infinito
            if(positionObjectGallery == 19)
                return;

            positionObjectGallery = positionObjectGallery+1; //Oscar
            g.setSelection(positionObjectGallery);
        }
        else  if(v.getId()==izquierda.getId()){
            if (mp1.isPlaying()) {
            }
            else {
                mp1.start();
            }
            if(positionObjectGallery == 0)
                return;
            positionObjectGallery = positionObjectGallery-1;
            g.setSelection(positionObjectGallery);
        }
        else if(v.getId()==btn_jugar.getId()){
            if(DownloadInProgress){
                Toast.makeText(getBaseContext(),"Hay una descarga en progreso, por favor espera...",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent GoGame = new Intent(getBaseContext(), MainActivity.class);
                startActivity(GoGame);
            }
        }
        ///Evento click en el boton de sonido
        else if(v.getId()==btnsonido.getId()){
            if(player != null){
                if(player.isPlaying())
                {
                    player.pause();
                    btnsonido.setBackgroundResource(R.drawable.silencio);
                    Session.ValidateSonido(getBaseContext(),true);
                }
                else{
                    player.start();
                    btnsonido.setBackgroundResource(R.drawable.sonido);
                    Session.ValidateSonido(getBaseContext(),false);
                }
            }
        }

    }


    /*
    * Valida que el dispositivo soporte los servicios de Google play
    * */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Log.e("Error", "Este dispositivo no soporta Google Play Services");
                finish();
            }
            return false;
        }
        return true;
    }

    /*
    * Al oprimir el boton de atras sale de la aplicación
    * */
    @SuppressLint("NewApi") @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(player != null){
                player.stop();
            }
            Gallery_ACtivity.this.finishAffinity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
    * Descarga el video
    * */
    class  DowloadVideo extends AsyncTask{

        Producto producto;
        boolean IsTodo;

        public DowloadVideo(Producto pPdr,boolean IsTodo){
            this.producto = pPdr;
            this.IsTodo = IsTodo;
        }

        public DowloadVideo(boolean IsTodo){
            this.IsTodo = IsTodo;
        }

        private  void DescargarVideo(final Producto prd) {
            mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
            lbdescargando=(TextView)  findViewById(R.id.lbdescargando);
            final Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/boogaloo.otf");
            DownloadInProgress = true;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(prd.getVideoServer()));
            request.setDescription("Estamos descargando tu compra");
            request.setTitle("Descargando...");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            File dir =Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local);
            if (dir.exists() == false) {
                dir.mkdirs();
            }
            request.setDestinationInExternalPublicDir(Constantes.PathLocal(getBaseContext()), File.separator + Constantes.local + File.separator + prd.getNombreProducto() + ".mp4");

            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManagerId = manager.enqueue(request);
            boolean downloading = true;

            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(DownloadManagerId);
                Cursor cursor = manager.query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor
                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }
                final double dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                final boolean finalDownloading = downloading;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lbdescargando.setTypeface(custom_font);
                        mProgressBar.setVisibility(View.VISIBLE);
                        lbdescargando.setVisibility(View.VISIBLE);
                        lbdescargando.setText("Descargando: " + prd.getNombrePersonaje());
                        mProgressBar.setProgress((int) dl_progress);
                        if (!finalDownloading) {
                            prd.setVideoLocal(Constantes.PathLocal(getBaseContext()) + Constantes.local + prd.getNombreProducto() + ".mp4");
                            prd.setPurchased(true);
                            DownloadInProgress = false;
                            Gallery_ACtivity.mis_productos.set(prd.getPosition(), prd);
                            Recursos.SavePreferences(Gallery_ACtivity.mis_productos, getBaseContext());
                        }
                    }
                });
                cursor.close();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Object data = "0";
                if (IsTodo) {
                    for (Producto item : mis_productos) {
                        if (!item.isFree() && item.getPosition() != 20) {
                            DescargarVideo(item);
                        }
                    }
                } else {
                    DescargarVideo(this.producto);
                }
                return data;
            }
            catch (Exception ex){
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Object params) {
            if(params.toString().equals("-2")){
                Toast.makeText(getBaseContext(),"La aplicación no tiene permisos para descargar",Toast.LENGTH_SHORT);
            }
            if(params.toString().equals("-1")){
                Toast.makeText(getBaseContext(),"Error el video no se puede descargar",Toast.LENGTH_SHORT);
            }
            mProgressBar.setVisibility(View.GONE);
            lbdescargando.setVisibility(View.GONE);
        }
    }



    ///Descarga el video gratis
    private class DownloadVideo extends AsyncTask<Integer, Integer, Integer> {

        private Context ctx;
        private ProgressDialog progress;
        private Producto producto;

        public DownloadVideo(Context pCtx,Producto pProducto) {
            this.ctx = pCtx;
            this.producto = pProducto;
        }

        @Override
        protected void onPreExecute(){
            progress = new ProgressDialog(ctx);
            progress.setTitle("Descargando...");
            progress.setMessage("Un momento por favor, estamos descargando el contenido de la aplicación");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.setMax(100);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.show();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int vRet = -1;
            try {

                URL url = new URL(this.producto.getVideoServer());
                URLConnection ucon = url.openConnection();
                ucon.setReadTimeout(5000);
                ucon.setConnectTimeout(10000);

                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                File file = new File(this.ctx.getDir("filesdir", Context.MODE_PRIVATE) + this.producto.getNombrePersonaje() + ".mp4");

                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();

                FileOutputStream outStream = new FileOutputStream(file);
                byte[] buff = new byte[5 * 1024];
                int totalRead = 0;
                int len;
                //Log.e("longitud",""+ucon.getContentLength());
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                    totalRead += len;
                    int progress = (int) ((totalRead * 100L)/Constantes.size_video);
                    publishProgress(progress);
                }
                outStream.flush();
                outStream.close();
                inStream.close();
                vRet = 0;
            }
            catch (Exception e){
                Log.e("Error descargando","Error");
                vRet = -1;
            }
            return vRet;
        }

        @Override
        protected void onPostExecute(Integer result) {
            try {
                if (result == 0) {
                    // To dismiss the dialog
                    progress.dismiss();
                    goVideo();
                } else {
                    progress.dismiss();
                    Toast.makeText(this.ctx, "Error el contenido no se puede descargar", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){}
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("Porcentaje descarga", values[0]+"");
            progress.setProgress(values[0]);
        }
    }
}
