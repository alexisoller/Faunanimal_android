package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class listview_productos_adapter extends BaseAdapter {
    ArrayList<String> titulos;
    //ArrayList<String> imagenesurl;

    Context context;
    private static LayoutInflater inflater = null;

    public listview_productos_adapter(Context c, ArrayList titulo) {
        titulos = titulo;
       // imagenesurl = imagenes;
        context = c;
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
        TextView tv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listview_productos, null);
        holder.tv = (TextView) rowView.findViewById(R.id.tvtitulo_layout);
        holder.tv.setText(titulos.get(position));

        //new DownloadImageTask((ImageView) rowView.findViewById(R.id.imageView_row_layout))//clase asyncrona para cargar las imagenes en el list view
          //      .execute(imagenesurl.get(position));
        
        return rowView;
    }

/*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage=null;
        Bitmap mIcon11 = null;

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
            //this.cancel(true);
            bmImage.setImageBitmap(result);
        }



    }*/

}