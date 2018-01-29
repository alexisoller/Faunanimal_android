package com.example.devomac02.faunanimal_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class pantalla_usuario extends AppCompatActivity {
    //Declaracion de Componentes
    Button btndesconectar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_usuario);
        setUI();

    }

    private void setUI() {
        //personalizar action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View v = getSupportActionBar().getCustomView();
        TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
        titulo.setText(R.string.cuentausuario);
        //personalizar action bar end


        //Definir componentes start
        btndesconectar = (Button) findViewById(R.id.btndesconectar);

        //Definir componenetes end

        //añadir listener al boton de desconectar start
        btndesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pantalla_usuario.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
            }
        });
        //añadir listener al boton de desconectar start


    }


}
