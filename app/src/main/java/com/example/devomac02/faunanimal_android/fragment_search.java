package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import java.util.Arrays;


public class fragment_search extends Fragment {

    View rootView;
    Button btnborrarresultado;
    ImageButton ImageButton_search_lupa;
    ProgressBar progressBar_search;
    private String[] nomproducto = new String[0];
    private String[] precio = new String[0];
    private String[] imagen = new String[0];
    private String[] cantidad_producto = new String[0];
    private String[] descripcion = new String[0];
    final int MAX_IMAGENES = 15;
    GridView gv_modelo;
    EditText et_search_producto;
    private String URL_PRODUCTOS = "http://faunanimal.com/api/android/search";



    public static Fragment newInstance() {
        fragment_search fragment = new fragment_search();

        return fragment;
    }

    public fragment_search() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //Definicion de componentes start
        btnborrarresultado=(Button)rootView.findViewById(R.id.btnborrarresultado);
        btnborrarresultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gv_modelo.setAdapter(null);
                et_search_producto.setText("");

            }
        });
        et_search_producto=(EditText)rootView.findViewById(R.id.et_search_producto);
        gv_modelo = (GridView) rootView.findViewById(R.id.gridView_modelo);
        gv_modelo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_nombre = (TextView) view.findViewById(R.id.tvnom_producto);
                TextView tv_precio = (TextView) view.findViewById(R.id.tv_precio_producto);
                Intent i = new Intent(getActivity(), info_producto.class);
                i.putExtra("info_producto_nombre", tv_nombre.getText());
                i.putExtra("info_producto_precio", tv_precio.getText());
                i.putExtra("info_producto_imagenprincipal", imagen[position]);
                i.putExtra("info_producto_cantidad", cantidad_producto[position]);
                i.putExtra("info_producto_descripcion", descripcion[position]);
                ArrayList<String> Arraylist_imagenes = new ArrayList<String>(Arrays.asList(imagen));
                i.putExtra("info_producto_imagenes", Arraylist_imagenes.get(position));
                startActivity(i);
            }
        });


        ImageButton_search_lupa = (ImageButton) rootView.findViewById(R.id.ImageButton_search_lupa);
        ImageButton_search_lupa.getHeight();
        progressBar_search = (ProgressBar) rootView.findViewById(R.id.progressbar_search);
        progressBar_search.setVisibility(View.GONE);
        ImageButton_search_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargardatos();
            }
        });
        //Definicion de componentes end

        //Cargar imagen en el boton start
        float density = getContext().getResources().getDisplayMetrics().density;
        int px = Math.round(25 * density);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.search);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, px, px, true);
        ImageButton_search_lupa.setImageBitmap(bMapScaled);
        //Cargar imagen en el boton end

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.usuario:
                Intent i = new Intent(getContext(), login.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargardatos() {
        if (conexionactiva()) {
            URL url = null;
            try {
                url = new URL(URL_PRODUCTOS+"?keyword="+et_search_producto.getText().toString());
                GetHttpDataTask taskGetData = new GetHttpDataTask();//clase asyncrona para cargar los datos
                taskGetData.execute(url);//ejecucion de la tarea asyncrona
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.nodatos, Toast.LENGTH_LONG).show();
        }
    }

    private boolean conexionactiva() {//comprobacion de la conexion
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        Boolean networkactiva = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkactiva = true;
        }
        return networkactiva;
    }


    public class GetHttpDataTask extends AsyncTask<URL, Void, Boolean> {
        //tarea asyncrona para cargar datos
        private final int CONNECTION_TIMEOUT = 150000;
        private final int READ_TIMEOUT = 150000;
        String precioproducto;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar_search.setVisibility(View.GONE);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int alto = displaymetrics.heightPixels / 4;
            int ancho = displaymetrics.widthPixels / 4;
            if (nomproducto.length > 0) {
                gv_modelo.setAdapter(new grid_adapter_modelo_producto(getActivity(), nomproducto, precio, imagen, ancho, alto));
            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.filtronodatos, Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar_search.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(getActivity(), R.string.cargando, Toast.LENGTH_LONG);
            toast.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast toast = Toast.makeText(getActivity(), R.string.nodatos, Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        protected Boolean doInBackground(URL... url) {
            Boolean result = false;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url[0].openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                if (urlConnection.getResponseCode() == 200) {
                    String resultStream = readStream(urlConnection.getInputStream());
                    JSONArray jsonArray = new JSONArray(resultStream);
                    nomproducto = new String[jsonArray.length()];
                    precio = new String[jsonArray.length()];
                    imagen = new String[jsonArray.length()];
                    cantidad_producto = new String[jsonArray.length()];
                    descripcion = new String[jsonArray.length()];
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);//coger todos los productos
                            nomproducto[i] = item.getString("name");//datos del producto
                            precioproducto = item.getString("tax_price");
                            if (!precioproducto.equals("null")) {
                                if (precioproducto.contains(".")) {//si contiene el punto significa que hay precio si no hubiera se pone "-.-- €"

                                    precio[i] = precioproducto + " €";
                                } else {
                                    precio[i] = "-.-- €";
                                }

                            } else {
                                precio[i] = "-.-- €";
                            }

                            for (int z = 0; z < MAX_IMAGENES; z++) {
                                if (item.has("image" + (z + 1))) {
                                    if (z == 0) {
                                        imagen[i] = item.getString("image" + (z + 1));
                                    } else {
                                        imagen[i] = imagen[i] + " " + item.getString("image" + (z + 1));
                                    }
                                } else {
                                    break;
                                }
                            }
                            cantidad_producto[i] = item.getString("qty");
                            descripcion[i] = item.getString("description");
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


}