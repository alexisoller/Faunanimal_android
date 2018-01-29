package com.example.devomac02.faunanimal_android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;


public class grid_adapter_modelo_producto extends ArrayAdapter {
    private Context mContext;
    private final String[] nomproducto;
    private final String[] precio;
    private final String[] Imageid;
    int ancho;
    int alto;

    public grid_adapter_modelo_producto(Context c, String[] nomproducto, String[] precio, String[] Imageid, int anch, int alt) {
        super(c, 0);
        mContext = c;
        this.Imageid = Imageid;
        this.nomproducto = nomproducto;
        this.precio = precio;
        ancho=anch;
        alto=alt;
    }


    @Override
    public int getCount() {
        return nomproducto.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*View grid;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.layout_modelo_producto_grid, null);
        } else {
            grid = (View) convertView;
        }

        //Definicion de componentes start
        TextView tvnombre = (TextView) grid.findViewById(R.id.tvnom_producto);
        TextView tvprecio = (TextView) grid.findViewById(R.id.tv_precio_producto);
        //Definicion de componentes end
        //poner valor a los componentes del listview start
        tvnombre.setText(nomproducto[position]);
        tvprecio.setText(precio[position]);
        //poner valor a los componentes del istview end
        ImageView imageView_row_layout = (ImageView) grid.findViewById(R.id.imageView_row_layout);
        new DownloadImageTask(imageView_row_layout)//clase para descargar img desde url y convertirla en bitmap
                .execute(Imageid[position]);

        return grid;*/

        View grid;
        if(convertView == null) {
            LayoutInflater tvnombre = (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = tvnombre.inflate(R.layout.layout_modelo_producto_grid, null);
        } else {
            grid = convertView;
        }

        TextView tvnombre1 = (TextView)grid.findViewById(R.id.tvnom_producto);
        TextView tvprecio = (TextView)grid.findViewById(R.id.tv_precio_producto);
        tvnombre1.setText(this.nomproducto[position]);
        tvprecio.setText(this.precio[position]);
        ImageView imageView_row_layout = (ImageView)grid.findViewById(R.id.imageView_row_layout);

        String[] imagenes_de_producto=Imageid[position].split(" ");

        //(new grid_adapter_modelo_producto.DownloadImageTask(imageView_row_layout)).execute(new String[]{this.Imageid[position]});
        (new grid_adapter_modelo_producto.DownloadImageTask(imageView_row_layout)).execute(new String[]{imagenes_de_producto[0]});
        return grid;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Bitmap mIcon11;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(Bitmap.createScaledBitmap(mIcon11, ancho, alto, false));

        }
    }


}