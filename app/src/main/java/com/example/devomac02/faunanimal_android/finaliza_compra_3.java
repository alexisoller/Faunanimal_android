package com.example.devomac02.faunanimal_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class finaliza_compra_3 extends AppCompatActivity {
    //Declaracion de Componentes
    CheckBox chheleido;
    TextView tvverterminosycondicionespedido;
    EditText comentario;

    //Declaracion Variables
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    fragment_carrito_info fragcarrito;
    String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_compra_3);
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


        //Poner valor a progressbar start
        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        ProgressBarDrawable bgProgress = new ProgressBarDrawable(3);
        progressbar.setProgressDrawable(bgProgress);
        progressbar.setMax(3);
        progressbar.setProgress(3);
        //Poner valor a progressbar end


        //Definir componentes start
        chheleido = (CheckBox) findViewById(R.id.chheleido);
        tvverterminosycondicionespedido = (TextView) findViewById(R.id.tvverterminosycondicionesypoliticadeprivacidad);
        TextView total = (TextView) findViewById(R.id.tvtotal);
        Button btnsiguiente = (Button) findViewById(R.id.btnsiguiente);
        ListView lv_nonscroll_list = (ListView) findViewById(R.id.lv_nonscroll_list);
        comentario = (EditText) findViewById(R.id.etcomentarios);
        //Definir componentes end


        //Cargar datos de preferences (datos de pedido) start
        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();

        Gson gson = new Gson();
        String json = preferencessettings.getString("fragment_info_obj", "");
        fragcarrito = gson.fromJson(json, fragment_carrito_info.class);
        //Cargar datos de preferences (datos de pedido) end


        //Listener para terminos y condiciones start
        tvverterminosycondicionespedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta_Terminos_Y_Condiciones();//mostrar en un dialog los terminos y condiciones
            }
        });
        //Listener para terminos y condiciones end

        //Cargar datos en la lista start
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int alto = displaymetrics.heightPixels / 4;
        int ancho = displaymetrics.widthPixels / 4;
        carritoarrayadapter adapter = new carritoarrayadapter(this, fragcarrito.getImagen(), fragcarrito.getTitulo(), fragcarrito.getCantidad(), ancho, alto);
        //Cargar datos en la lista end


        //poner valores en "total" y en el boton siguiente start
        total.setText("Total : " + getIntent().getStringExtra("total") + "€");
        lv_nonscroll_list.setAdapter(adapter);
        btnsiguiente.setText("PAGAR " + getIntent().getStringExtra("total") + "€");
        //poner valores en "total" y en el boton siguiente end

        //Listener para btnsguiente,pasar a pantalla de tarjeta de credito start
        btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chheleido.isChecked()) {
                    Intent i = new Intent(finaliza_compra_3.this, finaliza_compra_datostarjeta.class);
                    i.putExtra("order_id", order_id);
                    i.putExtra("total", getIntent().getStringExtra("total"));
                    i.putExtra("nombreyapellidos", getIntent().getStringExtra("nombreyapellidos"));
                    i.putExtra("direccionycodigopostal", getIntent().getStringExtra("direccionycodigopostal"));
                    i.putExtra("email", getIntent().getStringExtra("email"));
                    i.putExtra("telefono", getIntent().getStringExtra("telefono"));
                    if (comentario.getText().length() > 0) {
                        i.putExtra("comentario", comentario.getText().toString());
                        System.out.println("holaaaaaaaaaa"+comentario.getText());
                    }
                    startActivity(i);
                } else {
                    Toast toast = Toast.makeText(finaliza_compra_3.this, R.string.tienequeaceptar, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            //});

            //}
            // }
        });
        //Listener para btnsguiente,pasar a pantalla de tarjeta de credito end


    }


    private void alerta_Terminos_Y_Condiciones() {

//mostar Terminos_Y_Condiciones en un dialog , si acepta/no acepta el checkbox se activa/desactiva
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Términos y Condiciones y la Política de privacidad");
        builder.setMessage(R.string.terminosycondicionespoliticadeprivacidad);

        builder.setPositiveButton("Acepto",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chheleido.setChecked(true);

                    }
                });

        builder.setNegativeButton("No Acepto",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chheleido.setChecked(false);

                    }
                });

        builder.show();
    }


}
