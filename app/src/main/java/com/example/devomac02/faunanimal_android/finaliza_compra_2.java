package com.example.devomac02.faunanimal_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;


public class finaliza_compra_2 extends AppCompatActivity {
    //Declaracion de Componentes

    RadioGroup radioGroup_pagos;
    TextView tvpagotarjeta, enviogratis;
    Button btnsiguiente;

    //Declaracion de Variables

    double sumtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_compra_2);
        setui();
    }

    private void setui() {
        //Personalizar Action bar start

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View v = getSupportActionBar().getCustomView();
        TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
        titulo.setText(R.string.finalizarcompra);
        //Personalizar Action bar end

        //Definir componentes start
        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        btnsiguiente = (Button) findViewById(R.id.btnsiguiente);
        enviogratis = (TextView) findViewById(R.id.tvsolotefaltan);
        tvpagotarjeta = (TextView) findViewById(R.id.tvpagotarjeta);
        tvpagotarjeta.setVisibility(View.VISIBLE);
        radioGroup_pagos = (RadioGroup) findViewById(R.id.radioGroup_pagos);
        //Definir componentes end

        //Poner valor a progressbar start

        ProgressBarDrawable bgProgress = new ProgressBarDrawable(3);
        progressbar.setProgressDrawable(bgProgress);
        progressbar.setMax(3);
        progressbar.setProgress(2);
        //Poner valor a progressbar end

        //Comprobar si envio es gratis o no start
        String total = getIntent().getStringExtra("total");
        total = total.replace(",", ".");
        sumtotal = Double.parseDouble(total);

        if (sumtotal > 39) {
            enviogratis.setText("ENVIO GRATIS");
        } else {
            enviogratis.setText("Solo te faltan " + String.format(" %.2f", (39 - sumtotal)).replace(".", ",") + " € para tener envio gratis");
            sumtotal = sumtotal + 3.99;
        }
        //Comprobar si envio es gratis o no end


        //Listeners start
        //Listener btnsiguiente start
        btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(finaliza_compra_2.this, finaliza_compra_3.class);
                i.putExtra("total", String.format(" %.2f", sumtotal));
                i.putExtra("nombreyapellidos", getIntent().getStringExtra("nombreyapellidos"));
                i.putExtra("direccionycodigopostal", getIntent().getStringExtra("direccionycodigopostal"));
                i.putExtra("email", getIntent().getStringExtra("email"));
                i.putExtra("telefono", getIntent().getStringExtra("telefono"));
                startActivity(i);
            }
        });
        //Listener btnsiguiente end

        //Listener para los metodos de pago (ampliable en un futuro ) start
        radioGroup_pagos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtarjeta:
                        tvpagotarjeta.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        //Listener para los metodos de pago (ampliable en un futuro ) start
        //Listeners end


    }
}
