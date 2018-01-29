package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by devomac02 on 7/4/16.
 */
public class carritoarrayadapter extends BaseAdapter {

    //Declaracon de Componentes
    LayoutInflater inflater;
    Context context;

    // Declare Variables
    ArrayList<Bitmap> imagenes;
    ArrayList<String> nom_prod;
    ArrayList<String> unidades;
    int ancho,alto;
    //Datos para el carrito
    public carritoarrayadapter(Context context, ArrayList imagenes, ArrayList nom_prod, ArrayList uds,int anch,int alt) {
        this.context = context;
        this.imagenes = imagenes;
        this.nom_prod = nom_prod;
        this.unidades = uds;
        ancho=anch;
        alto=alt;
    }

    @Override
    public int getCount() {
        return nom_prod.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imgImg;
        TextView tvtitulo;
        TextView tvcantidad;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.layout_lista_confirmcompra
                , parent, false);

        tvtitulo = (TextView) itemView.findViewById(R.id.tv_carrito_nomprod);
        tvcantidad = (TextView) itemView.findViewById(R.id.tv_carrito_unidades);
        imgImg = (ImageView) itemView.findViewById(R.id.imgcarrito);


        tvtitulo.setText(nom_prod.get(position));
        tvcantidad.setText("Cantidad: " + unidades.get(position));
        imgImg.setImageBitmap(imagenes.get(position));
        imgImg.setImageBitmap(Bitmap.createScaledBitmap(imagenes.get(position), ancho, alto, false));


        return itemView;
    }

}
