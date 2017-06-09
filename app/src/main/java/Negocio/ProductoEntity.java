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
                 "http://www.toycantando.club/apps/Canciones%20Infantiles%202/1-pin-pon.mp4",
                 "",
                 true,
                 Constantes.pimponKey
                 ,""
                 ,"Pin Pon"
                 ,true);
        misproductos.add(prd);

        prd = new Producto("mi_carita",R.drawable.mi_carita,1,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/3-mi-carita.mp4",
                "",
                true,
                Constantes.micaritaKey
                ,""
                ,"Mi Carita"
                ,true);
        misproductos.add(prd);

        prd = new Producto("Ronda de los conejos",R.drawable.ronda_conejos,2,
                "http://www.toycantando.club/apps/appsindividuales/baileconejos.mp4",
                "",
                true,
                Constantes.rondaconejos
                ,""
                ,"Ronda de los conejos"
                ,true);
        misproductos.add(prd);

        prd = new Producto("Tio mario",R.drawable.tio_mario,3,
                "http://www.toycantando.club/apps/appsindividuales/tiomario.mp4",
                "",
                true,
                Constantes.tiomario
                ,""
                ,"Tio mario"
                ,true);
        misproductos.add(prd);

        prd = new Producto("Pinocho",R.drawable.pinocho,4,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/2-Pinocho.mp4",
                "",
                false,
                Constantes.pinochoKey
                ,""
                ,"Pinocho"
                ,false);
        misproductos.add(prd);

        prd = new Producto("cuando_tengas_muchas_ganas",R.drawable.cuando_tengas_muchas_ganas,5,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/18-cuando-tengas-muchas-ganas.mp4",
                "",
                false,
                Constantes.cuandotengasmuchasganasKey
                ,"",
                "Cuando Tengas Muchas Ganas"
                ,false);
        misproductos.add(prd);


        prd = new Producto("barquito",R.drawable.barquito,6,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/4-barquito-chiquitico.mp4",
                "",
                false,
                Constantes.barquitoKey
                ,""
        ,"El Barquito Chiquitito"
                ,false);
        misproductos.add(prd);



        prd = new Producto("patico_patico",R.drawable.patico_patico,7,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/6-patico-patico.mp4",
                "",
                false,
                Constantes.paticoKey
                ,""
        ,"Patico, Patico"
                ,false);
        misproductos.add(prd);

        prd = new Producto("tres_elefantes",R.drawable.tres_elefantes,8,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/7-tres-elefantes.mp4",
                "",
                false,
                Constantes.treselefantesKey
                ,"",
                "Tres Elefantes"
                ,false);
        misproductos.add(prd);

        prd = new Producto("a_mi_burro",R.drawable.a_mi_burro,9,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/8-a-mi-burro.mp4",
                "",
                false,
                Constantes.amiburroKey
                ,"",
                "A Mi Burro"
                ,false);
        misproductos.add(prd);

        prd = new Producto("arroz_con_leche",R.drawable.arroz_con_leche,10,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/10-arroz-con-leche.mp4",
                "",
                false,
                Constantes.arrozconleche
                ,"",
                "Arroz Con Leche"
                ,false);
        misproductos.add(prd);

        prd = new Producto("El auto de papa",R.drawable.auto_papa,11,
                "http://www.toycantando.club/apps/appsindividuales/autodepapa.mp4",
                "",
                false,
                Constantes.autopapa
                ,""
                ,"El auto de papá"
                ,false);
        misproductos.add(prd);

        prd = new Producto("a_la_vibora_de_la_mar",R.drawable.a_la_vibora_de_la_mar,12,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/11-a-la-vibora-de-la-mar.mp4",
                "",
                false,
                Constantes.alaviboradelamarKey
                ,"",
                "A La Víbora De La Mar"
                ,false);
        misproductos.add(prd);

        prd = new Producto("cucu",R.drawable.cucu,13,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/12-cu-cu.mp4",
                "",
                false,
                Constantes.cucuKey
                ,"",
                "Cucú"
                ,false);
        misproductos.add(prd);

        prd = new Producto("debajo_de_un_boton",R.drawable.debajo_de_un_boton,14,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/14-debajo-de-un-boton.mp4",
                "",
                false,
                Constantes.debajodeunbotonKey
                ,"",
                "Debajo De Un Botón"
                ,false);
        misproductos.add(prd);

        prd = new Producto("juguemos_en_el_bosque",R.drawable.juguemos_en_el_bosque,15,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/15-juguemos-en-el-bosque.mp4",
                "",
                false,
                Constantes.juguemosenelbosqueKey
                ,"",
                "Juguemos En El Bosque"
                ,false);
        misproductos.add(prd);

        prd = new Producto("la_pajara_pinta",R.drawable.la_pajara_pinta,16,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/16-la-pajara-pinta.mp4",
                "",
                false,
                Constantes.lapajarapintaKey
                ,"",
                "La Pájara Pinta"
                ,false);
        misproductos.add(prd);

        prd = new Producto("el_patio_de_mi_casa",R.drawable.el_patio_de_mi_casa,17,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/17-el-patio-de-mi-casa.mp4",
                "",
                false,
                Constantes.elpatiodemicasaKey
                ,"",
                "El Patio De Mi Casa"
                ,false);
        misproductos.add(prd);

        prd = new Producto("la_muneca_vestida_de_azul",R.drawable.la_muneca_vestida_de_azul,18,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/19-la-muneca-vestida-de-azul.mp4",
                "",
                false,
                Constantes.lamunecavestidadeazul
                ,"",
                "La Muñeca Vestida De Azul"
                ,false);
        misproductos.add(prd);

        prd = new Producto("este_dedito",R.drawable.este_dedito,19,
                "http://www.toycantando.club/apps/Canciones%20Infantiles%202/20-este-dedito.mp4",
                "",
                false,
                Constantes.estededitoKey
                ,"",
                "Este Dedito Compró Un Huevito"
                ,false);
        misproductos.add(prd);

        prd = new Producto("colleccioncompleta",-1,20,
                "",
                "",
                false,
                Constantes.TodaColeccionKey
                ,"",
                "Colección Completa"
                ,false);
        misproductos.add(prd);

        return misproductos;
    }
}
