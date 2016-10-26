package cancionesinfantiles.toycantando;

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
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.util.ArrayList;

import Adaptadores.GalleryImageAdapter;
import Entidades.Producto;
import Negocio.Constantes;
import Negocio.ProductoEntity;
import Negocio.Recursos;
import cancionesinfantiles.toycantando.util.IabHelper;
import cancionesinfantiles.toycantando.util.IabResult;
import cancionesinfantiles.toycantando.util.Inventory;

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
    private int  controltextazul = 0;
    private long DownloadManagerId;
    private Button imagelogo;
    private BroadcastReceiver receiver;
    private ProgressBar mProgressBar;
    private TextView lbdescargando;
    private boolean DownloadInProgress = false;
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
    public static Producto producto_comprado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery__activity);
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

        /*
        * Valida si se compro algo
        * */
        Bundle extras = getIntent().getExtras();
        ///Compro un solo producto
        if (extras != null && extras.getInt("TODO") == 0) {
            if(producto_comprado != null)
                //Descargar(producto_comprado);
                new DowloadVideo(producto_comprado,false).execute();
            else
                Toast.makeText(this,"No hay un producto para comprar",Toast.LENGTH_SHORT).show();
        }
        ///Compro toda la coleccion
        else if(extras != null && extras.getInt("TODO") == 1){
            new DowloadVideo(producto_comprado,true).execute();
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /*Lista los productos*/
    private void DoListProducts() {
        ArrayList<String> listaProductos = new ArrayList();
        listaProductos.add(Constantes.pimponKey);
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
                ///Recorre el Array principal de productos y valida las compras que tiene el usuario
                if(Gallery_ACtivity.mis_productos != null){
                    ///Antes de iniciar valida si el usuario compro toda la coleccion
                    if(inventory.hasPurchase(Constantes.TodaColeccionKey)){
                        for (Producto item : Gallery_ACtivity.mis_productos) {
                            if (item.getPosition() == 0 || item.getPosition() == 20) {
                            } else {
                                item.setPurchased(true);
                            }
                        }
                    }
                    else {
                        for (Producto item : Gallery_ACtivity.mis_productos) {
                            if (item.getPosition() == 0) {
                            } else {
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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gze.es/RedirectAppFacebook"));
                startActivity(browserIntent);
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
        this.stopService(new Intent(this, MusicService.class));
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
                textoderecha.setText("Pinocho");
                break;
            case 1:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Pinocho");
                textoizquierda.setText("Pin Pon");
                textoderecha.setText("Mi Carita");
                break;
            case 2:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("Mi Carita");
                textoizquierda.setText("Pinocho");
                textoderecha.setText("El Barquito Chiquitito");
                break;
            case 3:
                btncaja.setTextColor(getResources().getColor(R.color.colorPrimary));
                btncaja.setText("El Barquito Chiquitito");
                textoizquierda.setText("Mi Carita");
                textoderecha.setText("Sol Solecito");
                break;
            case 4:
                btncaja.setText("Sol Solecito");
                textoizquierda.setText("El Barquito Chiquitito");
                textoderecha.setText("Patico, Patico");
                break;
            case 5:
                btncaja.setText("Patico, Patico");
                textoizquierda.setText("Sol Solecito");
                textoderecha.setText("Tres Elefantes");
                break;
            case 6:
                btncaja.setText("Tres Elefantes");
                textoizquierda.setText("Patico, Patico");
                textoderecha.setText("A Mi Burro");
                break;
            case 7:
                btncaja.setText("A Mi Burro");
                textoizquierda.setText("Tres Elefantes");
                textoderecha.setText("Los Pollitos Dicen");
                break;
            case 8:
                btncaja.setText("Los Pollitos Dicen");
                textoizquierda.setText("A Mi Burro");
                textoderecha.setText("Arroz Con Leche");
                break;
            case 9:
                btncaja.setText("Arroz Con Leche");
                textoizquierda.setText("Los Pollitos Dicen");
                textoderecha.setText("A La Víbora De La Mar");
                break;
            case 10:
                btncaja.setText("A La Víbora De La Mar");
                textoizquierda.setText("Arroz Con Leche");
                textoderecha.setText("Cucú");
                break;
            case 11:
                btncaja.setText("Cucú");
                textoizquierda.setText("A La Víbora De La Mar");
                textoderecha.setText("La Vaca Lechera");
                break;
            case 12:
                btncaja.setText("La Vaca Lechera");
                textoizquierda.setText("Cucú");
                textoderecha.setText("Debajo De Un Botón");
                break;
            case 13:
                btncaja.setText("Debajo De Un Botón");
                textoizquierda.setText("La Vaca Lechera");
                textoderecha.setText("Juguemos En El Bosque");
                break;
            case 14:
                btncaja.setText("Juguemos En El Bosque");
                textoizquierda.setText("Debajo De Un Botón");
                textoderecha.setText("La Pájara Pinta");
                break;
            case 15:
                btncaja.setText("La Pájara Pinta");
                textoizquierda.setText("Juguemos En El Bosque");
                textoderecha.setText("Cuando Tengas Muchas Ganas");
                break;
            case 16:
                btncaja.setText("Cuando Tengas Muchas Ganas");
                textoizquierda.setText("La Pájara Pinta");
                textoderecha.setText("El Patio De Mi Casa");
                break;
            case 17:
                btncaja.setText("El Patio De Mi Casa");
                textoizquierda.setText("Cuando Tengas Muchas Ganas");
                textoderecha.setText("La Muñeca Vestida De Azul");
                break;
            case 18:
                btncaja.setText("La Muñeca Vestida De Azul");
                textoizquierda.setText("El Patio De Mi Casa");
                textoderecha.setText("Este Dedito Compró Un Huevito");
                break;
            case 19:
                controltextazul = 1;
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
        for (Producto item:Gallery_ACtivity.mis_productos) {
            if(item.getPosition() == pPosition)
                return  item;
        }
        return null;
    }



    /*
    * Aca debe validar si manda a la pantalla de compras o si muestra el video
    * */
    private void ValidarVideo(int position) {
        if(DownloadInProgress){
            Toast.makeText(getBaseContext(),"Hay una descarga en progreso, por favor espera...",Toast.LENGTH_SHORT).show();
        }
        else {
            Compras_Activity.PositionShow = position;
            Producto pdr = SearchProducto(position);
            ///El producto esta comprado
            if (pdr != null && pdr.isPurchased()) {
                if(pdr.getPosition() != 0) {
                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + Constantes.PathLocal);
                    if (dir.exists() == false) {
                        dir.mkdirs();
                    }
                    dir = new File(root.getAbsolutePath() + Constantes.PathLocal + pdr.getNombreProducto() + ".mp4");
                    if (!dir.exists()) {
                        new DowloadVideo(pdr,false).execute();
                    } else {
                        VideoActivity.Position = position;
                        Intent initCompra = new Intent(getBaseContext(), VideoActivity.class);
                        startActivity(initCompra);
                    }
                }
                else{
                    VideoActivity.Position = position;
                    Intent initCompra = new Intent(getBaseContext(), VideoActivity.class);
                    startActivity(initCompra);
                }
            } else {
                Intent initCompra = new Intent(getBaseContext(), Compras_Activity.class);
                startActivity(initCompra);
            }
        }
    }


    /*Cuando la actividad queda en backgroud y el usuario retorna, ejecuta este codigo*/
    @Override
    public void onResume() {
        super.onResume();
        this.startService(new Intent(this, MusicService.class));
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
        this.startService(new Intent(this, MusicService.class));
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

    @Override protected void onStart() {
        super.onStart();
        this.startService(new Intent(this, MusicService.class));
    }

    /*Cuando la actividad queda en backgroud e invisible*/
    @Override
    public void onStop() {
        super.onStop();
        this.stopService(new Intent(this, MusicService.class));
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
    public void onPause() {
        super.onPause();
        this.stopService(new Intent(this, MusicService.class));
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

            positionObjectGallery = positionObjectGallery+1;
            g.setSelection(positionObjectGallery);
        }
        else  if(v.getId()==izquierda.getId()){
            if (mp1.isPlaying()) {
            }
            else {
                mp1.start();
            }
            if(positionObjectGallery == 0)
                return;;
            controltextazul = 0;
            positionObjectGallery = positionObjectGallery-1;
            g.setSelection(positionObjectGallery);
        }
        else if(v.getId()==btnsonido.getId()){

            if(MusicService.player != null){
                if(MusicService.player.isPlaying())
                {
                    MusicService.player.pause();
                    btnsonido.setBackgroundResource(R.drawable.silencio);
                }
                else{
                    MusicService.player.start();
                    btnsonido.setBackgroundResource(R.drawable.sonido);
                }

            }
            /*AudioManager am;
            am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            btnsonido.setBackgroundResource(R.drawable.silencio);
            if(am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){

            }
            else if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                btnsonido.setBackgroundResource(R.drawable.sonido);
            }*/
            //For Vibrate mode
            //am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000).show();
            } else {
                Log.e("Error", "This device is not supported.");
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
            request.setDestinationInExternalPublicDir(Constantes.PathLocal, prd.getNombreProducto() + ".mp4");

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
                            prd.setVideoLocal(Constantes.PathLocal + prd.getNombreProducto() + ".mp4");
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
            if(IsTodo){
                for (Producto item:mis_productos) {
                    if(item.getPosition() != 0 && item.getPosition() != 20)
                        DescargarVideo(item);
                }
            }
            else
            {
                DescargarVideo(this.producto);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object params) {
            mProgressBar.setVisibility(View.GONE);
            lbdescargando.setVisibility(View.GONE);
        }
    }
}
