package Negocio;

import android.content.Context;

import java.util.ArrayList;

import Entidades.Producto;
import cancionesinfantiles.toycantando.R;

/**
 * Created by ogil on 28/09/2016.
 */
public class ProductoEntity {


    /*
    * Arma el objeto principal de la aplicacion
    * */
    public  ArrayList<Producto> DoMisProductos(Context pContext){
        ArrayList<Producto> misproductos= new ArrayList<>();
        Producto prd = new Producto("Pimpon",R.drawable.pimpon,0,
                 "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/1-pin-pon.mp4",
                 "",
                 true,
                 Constantes.pimponKey
                 ,""
                 ,"Pin Pon");
        misproductos.add(prd);
        prd = new Producto("Pinocho",R.drawable.pinocho,1,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/2-Pinocho.mp4",
                "",
                false,
                Constantes.pinochoKey
                ,""
                ,"Pinocho");
        misproductos.add(prd);
        prd = new Producto("mi_carita",R.drawable.mi_carita,2,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/3-mi-carita.mp4",
                "",
                false,
                Constantes.micaritaKey
                ,""
        ,"Mi Carita");
        misproductos.add(prd);
        prd = new Producto("barquito",R.drawable.barquito,3,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/4-barquito-chiquitico.mp4",
                "",
                false,
                Constantes.barquitoKey
                ,""
        ,"El Barquito Chiquitito");
        misproductos.add(prd);
        prd = new Producto("sol_solecito",R.drawable.sol_solecito,4,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/5-sol-solecito.mp4",
                "",
                false,
                Constantes.solsolecitoKey
                ,"",
                "Sol Solecito");
        misproductos.add(prd);
        prd = new Producto("patico_patico",R.drawable.patico_patico,5,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/6-patico-patico.mp4",
                "",
                false,
                Constantes.paticoKey
                ,""
        ,"Patico, Patico");
        misproductos.add(prd);
        prd = new Producto("tres_elefantes",R.drawable.patico_patico,6,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/7-tres-elefantes.mp4",
                "",
                false,
                Constantes.treselefantesKey
                ,"",
                "Tres Elefantes");
        misproductos.add(prd);
        prd = new Producto("a_mi_burro",R.drawable.a_mi_burro,7,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/8-a-mi-burro.mp4",
                "",
                false,
                Constantes.amiburroKey
                ,"",
                "A Mi Burro");
        misproductos.add(prd);
        prd = new Producto("los_pollitos",R.drawable.los_pollitos,8,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/9-los-pollitos-dicen.mp4",
                "",
                false,
                Constantes.lospollitosKey
                ,"",
                "Los Pollitos Dicen");
        misproductos.add(prd);
        prd = new Producto("arroz_con_leche",R.drawable.arroz_con_leche,9,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/10-arroz-con-leche.mp4",
                "",
                false,
                Constantes.arrozconleche
                ,"",
                "Arroz Con Leche");
        misproductos.add(prd);
        prd = new Producto("a_la_vibora_de_la_mar",R.drawable.a_la_vibora_de_la_mar,10,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/11-a-la-vibora-de-la-mar.mp4",
                "",
                false,
                Constantes.alaviboradelamarKey
                ,"",
                "A La Víbora De La Mar");
        misproductos.add(prd);
        prd = new Producto("cucu",R.drawable.a_la_vibora_de_la_mar,11,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/12-cu-cu.mp4",
                "",
                false,
                Constantes.cucuKey
                ,"",
                "Cucú");
        misproductos.add(prd);
        prd = new Producto("vaca_lechera",R.drawable.vaca_lechera,12,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/13-la-vaca-lechera.mp4",
                "",
                false,
                Constantes.vacalecheraKey
                ,"",
                "La Vaca Lechera");
        misproductos.add(prd);
        prd = new Producto("debajo_de_un_boton",R.drawable.debajo_de_un_boton,13,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/14-debajo-de-un-boton.mp4",
                "",
                false,
                Constantes.debajodeunbotonKey
                ,"",
                "Debajo De Un Botón");
        misproductos.add(prd);
        prd = new Producto("juguemos_en_el_bosque",R.drawable.juguemos_en_el_bosque,14,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/15-juguemos-en-el-bosque.mp4",
                "",
                false,
                Constantes.juguemosenelbosqueKey
                ,"",
                "Juguemos En El Bosque");
        misproductos.add(prd);
        prd = new Producto("la_pajara_pinta",R.drawable.la_pajara_pinta,15,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/16-la-pajara-pinta.mp4",
                "",
                false,
                Constantes.lapajarapintaKey
                ,"",
                "La Pájara Pinta");
        misproductos.add(prd);
        prd = new Producto("cuando_tengas_muchas_ganas",R.drawable.cuando_tengas_muchas_ganas,16,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/18-cuando-tengas-muchas-ganas.mp4",
                "",
                false,
                Constantes.cuandotengasmuchasganasKey
                ,"",
                "Cuando Tengas Muchas Ganas");
        misproductos.add(prd);
        prd = new Producto("el_patio_de_mi_casa",R.drawable.el_patio_de_mi_casa,17,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/17-el-patio-de-mi-casa.mp4",
                "",
                false,
                Constantes.elpatiodemicasaKey
                ,"",
                "El Patio De Mi Casa");
        misproductos.add(prd);
        prd = new Producto("la_muneca_vestida_de_azul",R.drawable.la_muneca_vestida_de_azul,18,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/19-la-muneca-vestida-de-azul.mp4",
                "",
                false,
                Constantes.lamunecavestidadeazul
                ,"",
                "La Muñeca Vestida De Azul");
        misproductos.add(prd);
        prd = new Producto("este_dedito",R.drawable.este_dedito,19,
                "http://descargastoycantando.s3.amazonaws.com/apps/cancionesinfantiles/20-este-dedito.mp4",
                "",
                false,
                Constantes.estededitoKey
                ,"",
                "Este Dedito Compró Un Huevito");
        misproductos.add(prd);
        prd = new Producto("colleccioncompleta",-1,20,
                "",
                "",
                false,
                Constantes.TodaColeccionKey
                ,"",
                "Colección Completa");
        misproductos.add(prd);
        return misproductos;
    }
}
