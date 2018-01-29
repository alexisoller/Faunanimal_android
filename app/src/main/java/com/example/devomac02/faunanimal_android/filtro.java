package com.example.devomac02.faunanimal_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class filtro extends AppCompatActivity {
    //Declaracion de Componentes
    Button btnaplicar, btnrestablecer;
    TextView titulotv, tvmax, tvmin;
    Switch swenviogratis;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;

    //Declaracion de Variables
    String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filtro);
        setui();
        categoria = getIntent().getStringExtra("categoria");
    }

    private void setui() {

        //Personalizar action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        btnaplicar = (Button) findViewById(R.id.btnaplicarfiltro);
        View v = getSupportActionBar().getCustomView();
        titulotv = (TextView) v.findViewById(R.id.actionbar_title);
        titulotv.setText("Filtro");
        //Personalizar action bar end

        //Definir componenetes start
        tvmax = (TextView) findViewById(R.id.etmax);
        tvmin = (TextView) findViewById(R.id.etmin);
        swenviogratis = (Switch) findViewById(R.id.swenviogratis);
        btnrestablecer = (Button) findViewById(R.id.btnrestablecerfiltro);

        //Definir componentes end


        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();
        tvmin.setText(preferencessettings.getString("tvmin", ""));
        tvmax.setText(preferencessettings.getString("tvmax", ""));
        swenviogratis.setChecked(preferencessettings.getBoolean("swenviogratis", false));

        //Listener para el boton starr
        btnrestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvmax.setText("");
                tvmin.setText("");
                swenviogratis.setChecked(false);
                preferenceseditor.putString("tvmin", "");
                preferenceseditor.putString("tvmax", "");
                preferenceseditor.putBoolean("swenviogratis", false);
                preferenceseditor.commit();


            }
        });

        btnaplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parametros = "?cat_id=" + categoria;
                if (tvmin.getText().length() != 0) {
                    parametros = parametros + "&price_min=" + tvmin.getText();
                }
                if (tvmax.getText().length() != 0) {
                    parametros = parametros + "&price_max=" + tvmax.getText();
                }
                if (swenviogratis.isChecked()) {
                    parametros = parametros + "&free_shipping=" + swenviogratis.isChecked();
                }
                System.out.println("la web que voy a filtrar es "+parametros);

                guardardatos();


                Intent i = new Intent(filtro.this, modelo_producto.class);
                i.putExtra("filtroactivo", "si");
                i.putExtra("params", parametros);
                i.putExtra("subcategoria", categoria);
                startActivity(i);
                finish();
            }
        });
        //Listener para el boton end

    }

    private void guardardatos() {
        if (tvmin.getText().length() > 0) {
            preferenceseditor.putString("tvmin", tvmin.getText().toString());
        } else {
            preferenceseditor.putString("tvmin", "");
        }
        if (tvmax.getText().length() > 0) {
            preferenceseditor.putString("tvmax", tvmax.getText().toString());
        } else {
            preferenceseditor.putString("tvmax", "");
        }
        preferenceseditor.putBoolean("swenviogratis", swenviogratis.isChecked());
        preferenceseditor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(filtro.this, modelo_producto.class);
        i.putExtra("filtroactivo", "no");
        i.putExtra("subcategoria", categoria);
        startActivity(i);
        finish();

    }
}
