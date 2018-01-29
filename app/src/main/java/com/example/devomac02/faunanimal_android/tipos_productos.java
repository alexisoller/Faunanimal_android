package com.example.devomac02.faunanimal_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;


public class tipos_productos extends AppCompatActivity {

    //Declaracion de Variables
    String titulo;
    ListView list_view_catogorias_tipo;
    ArrayList<String> subcatmodelo;
    ArrayList<String> subcatmodelo_imagenes;
    ArrayList<String> subcatmodelo_id;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipos_productos);
        titulo = getIntent().getStringExtra("tipo_producto");
        Bundle b = this.getIntent().getExtras();
        subcatmodelo = new ArrayList<String>(Arrays.asList(b.getStringArray("hijos")));
        subcatmodelo_imagenes = new ArrayList<String>(Arrays.asList(b.getStringArray("imagen")));
        subcatmodelo_id = new ArrayList<String>(Arrays.asList(b.getStringArray("subcategoria")));
        setui();
    }

    private void setui() {
        //Personalizacion de action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View v = getSupportActionBar().getCustomView();
        TextView titulotv = (TextView) v.findViewById(R.id.actionbar_title);
        titulotv.setText(titulo);
        //Personalizacion de action bar start




        //Definicion de componentes start

        list_view_catogorias_tipo = (ListView) findViewById(R.id.list_view_catogorias_tipo);
        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();
        //Definicion de componentes start

        //Cargar datos en el list view start
        //listview_productos_adapter itemsAdapter = new listview_productos_adapter(tipos_productos.this, subcatmodelo, subcatmodelo_imagenes);
        listview_productos_adapter itemsAdapter = new listview_productos_adapter(tipos_productos.this, subcatmodelo);
        list_view_catogorias_tipo.setAdapter(itemsAdapter);

        //Cargar datos en el list view end

        //Definir listener para el list view start
        list_view_catogorias_tipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(tipos_productos.this, modelo_producto.class);
                i.putExtra("categoria", getIntent().getStringExtra("categoria"));
                i.putExtra("subcategoria", subcatmodelo_id.get(position));
                i.putExtra("filtroactivo", "no");
                startActivity(i);
            }
        });
        //Definir listener para el list view end


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
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
