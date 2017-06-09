package Entidades;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by ogil on 02/09/2016.
 */
public class Producto implements Serializable {

    private String NombreProducto;
    private int ImagenProducto;
    private int Position;
    private String VideoServer;
    private String VideoLocal;
    private boolean IsPurchased;
    private String IdGoogleCompra;
    private String Precio;
    private String NombrePersonaje;
    private boolean IsFree;

    public Producto(String pNombreProducto,int pImagenProducto,int pPosition,String pVideoServer,String pVideoLocal,boolean pIsPurchased,String pIdGoogleCompra,String pPrecio,String pNombrePersonaje,boolean pIsFree){
        setNombreProducto(pNombreProducto);
        setImagenProducto(pImagenProducto);
        setPosition(pPosition);
        setVideoServer(pVideoServer);
        setVideoLocal(pVideoLocal);
        setPurchased(pIsPurchased);
        setIdGoogleCompra(pIdGoogleCompra);
        setPrecio(pPrecio);
        setNombrePersonaje(pNombrePersonaje);
        setFree(pIsFree);
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        NombreProducto = nombreProducto;
    }

    public int getImagenProducto() {
        return ImagenProducto;
    }

    public void setImagenProducto(int imagenProducto) {
        ImagenProducto = imagenProducto;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public String getVideoServer() {
        return VideoServer;
    }

    public void setVideoServer(String videoServer) {
        VideoServer = videoServer;
    }

    public String getVideoLocal() {
        return VideoLocal;
    }

    public void setVideoLocal(String videoLocal) {
        VideoLocal = videoLocal;
    }

    public boolean isPurchased() {
        return IsPurchased;
    }

    public void setPurchased(boolean purchased) {
        IsPurchased = purchased;
    }

    public String getIdGoogleCompra() {
        return IdGoogleCompra;
    }

    public void setIdGoogleCompra(String idGoogleCompra) {
        IdGoogleCompra = idGoogleCompra;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getNombrePersonaje() {
        return NombrePersonaje;
    }

    public void setNombrePersonaje(String nombrePersonaje) {
        NombrePersonaje = nombrePersonaje;
    }

    public boolean isFree() {
        return IsFree;
    }

    public void setFree(boolean free) {
        IsFree = free;
    }
}
