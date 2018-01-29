package com.example.devomac02.faunanimal_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class fragment_cart extends Fragment {

    //Declaracion de Componentes
    View rootView;
    String getTitle;
    public ListView lista_carrito;
    Button btnsiguiente;
    TextView tvtotal;
    //Declaracion de Variables
    double sumatotal = 0;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    fragment_carrito_info fragcarrito;


    public static Fragment newInstance() {
        fragment_cart fragment = new fragment_cart();

        return fragment;
    }

    public fragment_cart() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sumatotal = 0;//inicializar variable para prevenir errores de calculo

        int borrarlista = getActivity().getIntent().getIntExtra("borrar", 1);

        //recoger info almacenada en el archivo preferences start
        preferencessettings = getActivity().getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();


        Gson gson = new Gson();
        String json = preferencessettings.getString("fragment_info_obj", null);
        fragcarrito = gson.fromJson(json, fragment_carrito_info.class);


        //recoger info almacenada en el archivo preferences end

        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        //Definir componentes start
        lista_carrito = (ListView) rootView.findViewById(R.id.listView_fragment_carrito);
        btnsiguiente = (Button) rootView.findViewById(R.id.btnsiguiente);
        tvtotal = (TextView) rootView.findViewById(R.id.tvtotal_fragment_carrito);
        //Definir componentes end


        //Listener para el boton siguiente start
        lista_carrito.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView titulo = (TextView) view.findViewById(R.id.tv_fragment_titulo);
                getTitle = titulo.getText().toString();
                alert(position);

            }
        });
        //Listener para el boton siguiente end

        //Inicializar los componenestes como si no hubiera nada en el carrito start
        tvtotal.setText(R.string.nohayproductosaun);
        btnsiguiente.setEnabled(false);
        btnsiguiente.setBackgroundResource(R.drawable.botonredondeado_desactivado);
        //Inicializar los componenestes como si no hubiera nada en el carrito end

        //Comprobamos si el carrito tiene algo y cambiamos el estado de los componentes start
        if (fragcarrito != null) {
            ArrayList<String> sumacantidad = fragcarrito.getCantidad();
            ArrayList<String> sumaprecio = fragcarrito.getPrecio();

            for (int i = 0; i < sumacantidad.size(); i++) {
                String[] preciodividido = sumaprecio.get(i).split(" ");
                preciodividido[0] = preciodividido[0].replace(",", ".");
                double preciocalculado = Double.parseDouble(preciodividido[0]) * Integer.parseInt(sumacantidad.get(i));

                sumatotal = sumatotal + preciocalculado;
            }
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int alto = displaymetrics.heightPixels / 4;
            int ancho = displaymetrics.widthPixels / 4;
            lista_carrito.setAdapter(new fragment_carrito_adapter(getContext(), fragcarrito.getImagen(), fragcarrito.getTitulo(), fragcarrito.getCantidad(), fragcarrito.getPrecio(), ancho, alto));
            if (sumatotal == 0) {
                tvtotal.setText(R.string.nohayproductosaun);
                btnsiguiente.setEnabled(false);
                btnsiguiente.setBackgroundResource(R.drawable.botonredondeado_desactivado);
            } else {
                tvtotal.setText("Precio total: " + String.format(" %.2f", sumatotal) + "€ IVA incluido");
                btnsiguiente.setEnabled(true);
                btnsiguiente.setBackgroundResource(R.drawable.boton_redondeado);

            }


        } else {
            tvtotal.setText(R.string.nohayproductosaun);
            btnsiguiente.setEnabled(false);
            btnsiguiente.setBackgroundResource(R.drawable.botonredondeado_desactivado);
        }
        //Comprobamos si el carrito tiene algo y cambiamos el estado de los componentes end

        //Comprobamos si hay que borrar la lista o no start
        if (borrarlista == 0) {
            sumatotal = 0;
            fragcarrito.limpiardatos();
            tvtotal.setText(R.string.nohayproductosaun);
            btnsiguiente.setEnabled(false);
            btnsiguiente.setBackgroundResource(R.drawable.botonredondeado_desactivado);
        }
        //Comprobamos si hay que borrar la lista o no start

        //Listener para el boton siguiente start
        btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preferencessettings.getBoolean("login",false)) {
                    Intent i = new Intent(rootView.getContext(), finaliza_compra_1.class);
                    i.putExtra("total", String.format(" %.2f", sumatotal));
                    startActivity(i);
                } else {
                    Toast toast = Toast.makeText(getActivity(),"Es necesario loguearse antes", Toast.LENGTH_LONG);
                    toast.show();

                }

            }
        });
        //Listener para el boton siguiente end

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


    private void alert(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Crear alerta a la hora de borrar un producto
        builder.setTitle("¿Borrar artículo?");
        builder.setMessage("¿Quiere quitar el artículo? " + getTitle);
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sumatotal = 0;
                        if (fragcarrito.getTitulo().size() != position) {
                            fragcarrito.getTitulo().remove(position);
                            fragcarrito.getPrecio().remove(position);
                            fragcarrito.getCantidad().remove(position);
                            fragcarrito.getImagen().remove(position);
                            fragcarrito.getStock().remove(position);

                            Gson gson = new Gson();
                            String json = gson.toJson(fragcarrito);
                            preferenceseditor.putString("fragment_info_obj", json);
                            DisplayMetrics displaymetrics = new DisplayMetrics();
                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                            int alto = displaymetrics.heightPixels / 4;
                            int ancho = displaymetrics.widthPixels / 4;
                            lista_carrito.setAdapter(new fragment_carrito_adapter(getContext(), fragcarrito.getImagen(), fragcarrito.getTitulo(), fragcarrito.getCantidad(), fragcarrito.getPrecio(), ancho, alto));


                            ArrayList<String> sumacantidad = fragcarrito.getCantidad();
                            ArrayList<String> sumaprecio = fragcarrito.getPrecio();

                            for (int i = 0; i < sumacantidad.size(); i++) {
                                String[] preciodividido = sumaprecio.get(i).split(" ");
                                preciodividido[0] = preciodividido[0].replace(",", ".");
                                double preciocalculado = Double.parseDouble(preciodividido[0]) * Integer.parseInt(sumacantidad.get(i));
                                sumatotal = sumatotal + preciocalculado;
                            }

                            if (lista_carrito.getCount() == 0) {
                                tvtotal.setText(R.string.nohayproductosaun);
                                btnsiguiente.setEnabled(false);
                                btnsiguiente.setBackgroundResource(R.drawable.botonredondeado_desactivado);


                            } else {
                                tvtotal.setText("Precio total: " + String.format(" %.2f", sumatotal) + "€ IVA incluido");
                                btnsiguiente.setEnabled(true);
                                btnsiguiente.setBackgroundResource(R.drawable.boton_redondeado);
                            }


                            preferenceseditor.commit();
                        }
                    }

                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();
    }


}