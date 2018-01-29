package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class animales_productos extends AppCompatActivity {

    // Declaracion componentes Globales
    ListView list_view_catogorias_animal;
    ProgressBar progressBar_animales;


    // Declaracion variables Globales
    String titulo, nombre_categoria;
    HashMap<String, String[]> lista_hijos_de_hijos = new HashMap<String, String[]>();
    HashMap<String, String[]> lista_hijos_de_hijos_imagen = new HashMap<String, String[]>();
    HashMap<String, String[]> lista_hijos_de_hijos_id = new HashMap<String, String[]>();
    ArrayList<String> catnombre = new ArrayList<String>();
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    //ArrayList<String> catnombre_imagen = new ArrayList<String>();
    ArrayList<String> cat_id = new ArrayList<String>();

    private final String URL_PRODUCTOS = "http://faunanimal.com/api/android/categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animales_productos);
        titulo = getIntent().getStringExtra("nombre");
        setui();
    }

    private void setui() {
        //Personalizar action bar start

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View v = getSupportActionBar().getCustomView();
        TextView titulotv = (TextView) v.findViewById(R.id.actionbar_title);
        titulotv.setText(titulo);
        //Personalizar action bar end

        //Definir componentes start
        list_view_catogorias_animal = (ListView) findViewById(R.id.list_view_catogorias_animal);
        progressBar_animales = (ProgressBar) findViewById(R.id.progressbar_animales);
        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();
        //Definir componentes start

        cargardatos();//lanza la tarea asyncrona(Thread) para poder cargar los datos, tambien deja visible la barra de carga indeterminda
        //y la quita una vez ya estan los datos cargados


        //Listener de listview start
        list_view_catogorias_animal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titulo_row = (TextView) view.findViewById(R.id.tvtitulo_layout);
                String[] hijos = lista_hijos_de_hijos.get(titulo_row.getText());
                String[] hijos_imagen = lista_hijos_de_hijos_imagen.get(titulo_row.getText());
                String[] hijos_id = lista_hijos_de_hijos_id.get(titulo_row.getText());

                if (hijos != null && hijos_imagen != null && hijos.length != 0) {
                    Intent i = new Intent(animales_productos.this, tipos_productos.class);
                    i.putExtra("tipo_producto", titulo_row.getText());
                    i.putExtra("hijos", hijos);
                    i.putExtra("imagen", hijos_imagen);
                    i.putExtra("categoria", cat_id.get(position));
                    i.putExtra("subcategoria", hijos_id);
                    startActivity(i);
                } else {
                    Intent i = new Intent(animales_productos.this, modelo_producto.class);
                    i.putExtra("categoria", cat_id.get(position));
                    i.putExtra("subcategoria", "-1");
                    i.putExtra("filtroactivo", "no");
                    startActivity(i);
                }
            }
        });
        //Listener de listview end


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //accion para el boton de home (<)
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void cargardatos() {
        if (conexionactiva()) {
            URL url = null;
            try {
                url = new URL(URL_PRODUCTOS);
                GetHttpDataTask taskGetData = new GetHttpDataTask();//clase asyncrona para cargar los datos
                taskGetData.execute(url);//ejecucion de la tarea asyncrona
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.nodatos, Toast.LENGTH_LONG).show();
        }
    }

    private boolean conexionactiva() {//comprobacion de la conexion
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        Boolean networkactiva = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkactiva = true;
        }
        return networkactiva;
    }


    public class GetHttpDataTask extends AsyncTask<URL, Void, Boolean> {
        private final int CONNECTION_TIMEOUT = 15000000;
        private final int READ_TIMEOUT = 15000000;

        @Override
        protected void onPostExecute(Boolean aBoolean) {//despues de cargar los datos quita la barra y muestra los datos
            super.onPostExecute(aBoolean);
            //listview_productos_adapter itemsAdapter = new listview_productos_adapter(animales_productos.this, catnombre, catnombre_imagen);
            listview_productos_adapter itemsAdapter = new listview_productos_adapter(animales_productos.this, catnombre);
            list_view_catogorias_animal.setAdapter(itemsAdapter);
            progressBar_animales.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {//antes de cargar los datos deja visible la barra indeterminada
            super.onPreExecute();
            Toast toast = Toast.makeText(animales_productos.this, R.string.cargando, Toast.LENGTH_LONG);
            toast.show();
            progressBar_animales.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(URL... url) {//guardar informacion del json y tratarla correctamente
            Boolean result = false;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url[0].openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                if (urlConnection.getResponseCode() == 200) {
                    String resultStream = readStream(urlConnection.getInputStream());
                    JSONArray jsonArray = new JSONArray(resultStream);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);//coger todos las especies de animales
                            nombre_categoria = item.getString("name");

                            if (nombre_categoria.equals(titulo)) {
                                JSONArray jsonArrayhijos = new JSONArray(item.getString("children"));//coger categorias y subcategorias solo de la especie elegida

                                for (int z = 0; z < jsonArrayhijos.length(); z++) {
                                    JSONObject hijo = jsonArrayhijos.getJSONObject(z);
                                    String nombrehijo = hijo.getString("name");//datos de categoria con las subcategorias y todos los datos correspondientes
                                    catnombre.add(nombrehijo);
                                    //catnombre_imagen.add("http://e03-elmundo.uecdn.es/assets/multimedia/imagenes/2015/11/13/14474300157302.jpg");
                                    String id = hijo.getString("entity_id");
                                    cat_id.add(id);

                                    if (Integer.parseInt(hijo.getString("children_count")) > 0) {
                                        JSONArray jsonArrayhijoshijos = new JSONArray(hijo.getString("children"));
                                        String[] listahijos = new String[jsonArrayhijoshijos.length()];
                                        String[] listahijos_imagen = new String[jsonArrayhijoshijos.length()];
                                        String[] listahijos_id = new String[jsonArrayhijoshijos.length()];

                                        for (int j = 0; j < jsonArrayhijoshijos.length(); j++) {
                                            JSONObject hijohijo = jsonArrayhijoshijos.getJSONObject(j);//coger datos de subcategoria
                                            listahijos[j] = hijohijo.getString("name");
                                            listahijos_imagen[j] = "http://i.kinja-img.com/gawker-media/image/upload/s--7098ugfu--/c_scale,fl_progressive,q_80,w_800/dociw4fbzf2fs4tvywgl.jpg";
                                            listahijos_id[j] = hijohijo.getString("entity_id");
                                        }
                                        //guardar datos start
                                        lista_hijos_de_hijos.put(nombrehijo, listahijos);
                                        lista_hijos_de_hijos_imagen.put(nombrehijo, listahijos_imagen);
                                        lista_hijos_de_hijos_id.put(nombrehijo, listahijos_id);
                                        //guardar datos end
                                    }
                                }
                            }
                        }
                    }
                }

                result = true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result;

        }
    }

    private String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //Borrar filtro start
        preferenceseditor.putString("tvmin", "");
        preferenceseditor.putString("tvmax", "");
        preferenceseditor.putBoolean("swenviogratis", false);
        preferenceseditor.commit();
        //Borrar filtro end
    }

}