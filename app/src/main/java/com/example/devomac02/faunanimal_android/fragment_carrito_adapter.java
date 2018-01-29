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


public class fragment_carrito_adapter extends BaseAdapter {
    //Definicion de variables
    ArrayList<String> titulos;
    ArrayList<Bitmap> imagenesurl;
    ArrayList<String> cantidades;
    ArrayList<String> precios;
    int ancho,alto;

    Context context;
    private static LayoutInflater inflater = null;

    public fragment_carrito_adapter(Context c, ArrayList imagenes, ArrayList titulo, ArrayList cantidad, ArrayList precio,int anch,int alt) {
        titulos = titulo;
        imagenesurl = imagenes;
        cantidades = cantidad;
        precios = precio;
        context = c;
        ancho=anch;
        alto=alt;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titulos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tvtitulo, tvcantidad, tvprecio;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.fragment_carrito_celda, null);
        holder.tvtitulo = (TextView) rowView.findViewById(R.id.tv_fragment_titulo);
        holder.tvtitulo.setText(titulos.get(position));

        holder.tvcantidad = (TextView) rowView.findViewById(R.id.tv_fragment_cantidad);
        holder.tvcantidad.setText("Cantidad: " + cantidades.get(position));

        holder.tvprecio = (TextView) rowView.findViewById(R.id.tv_fragment_precio);

        String[] preciodividido = precios.get(position).split(" ");

        preciodividido[0]=preciodividido[0].replace(",", ".");

        double preciocalculado = Double.parseDouble(preciodividido[0]) * Integer.parseInt(cantidades.get(position));


        holder.tvprecio.setText("Precio: " + String.format(" %.2f", preciocalculado) + "€");


        holder.img = (ImageView) rowView.findViewById(R.id.img_fragment_compra);

        //wqholder.img.setImageBitmap(imagenesurl.get(position));
        holder.img.setImageBitmap(Bitmap.createScaledBitmap(imagenesurl.get(position), ancho, alto, false));

        return rowView;
    }


}
