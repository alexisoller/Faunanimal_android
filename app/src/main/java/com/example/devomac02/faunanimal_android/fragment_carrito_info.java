package com.example.devomac02.faunanimal_android;

import android.graphics.Bitmap;
import java.util.ArrayList;


public class fragment_carrito_info {
    //Definicion de variables
    public static ArrayList<Bitmap> imagen = new ArrayList<Bitmap>();
    public static ArrayList<String> titulo = new ArrayList<String>();
    public static ArrayList<String> cantidad = new ArrayList<String>();
    public static ArrayList<String> precio = new ArrayList<String>();
    public static ArrayList<String> stock = new ArrayList<String>();


    public fragment_carrito_info() {
    }

    public fragment_carrito_info(ArrayList<Bitmap> img, ArrayList<String> titu, ArrayList<String> canti, ArrayList<String> pre, ArrayList<String> sto) {
        imagen = img;
        titulo = titu;
        cantidad = canti;
        precio = pre;
        stock = sto;

    }

    public ArrayList<String> getStock() {
        return stock;
    }

    public void setStock(ArrayList<String> stock) {
        fragment_carrito_info.stock = stock;
    }


    public ArrayList<Bitmap> getImagen() {
        return imagen;
    }

    public void setImagen(ArrayList<Bitmap> imagen) {
        fragment_carrito_info.imagen = imagen;
    }

    public ArrayList<String> getTitulo() {
        return titulo;
    }

    public void setTitulo(ArrayList<String> titulo) {
        fragment_carrito_info.titulo = titulo;
    }

    public ArrayList<String> getCantidad() {
        return cantidad;
    }

    public void setCantidad(ArrayList<String> cantidad) {
        fragment_carrito_info.cantidad = cantidad;
    }

    public ArrayList<String> getPrecio() {
        return precio;
    }

    public void setPrecio(ArrayList<String> precio) {
        fragment_carrito_info.precio = precio;
    }

    public void limpiardatos() {
        imagen.clear();
        titulo.clear();
        cantidad.clear();
        precio.clear();
        stock.clear();


    }
}
