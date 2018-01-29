package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class finaliza_compra_1 extends AppCompatActivity {
    //Declaracion de Componentes
    EditText etnombre, etapellidos, etemail, ettelefono, etdireccion, etcodigopostal, etciudad, etnif;
    Spinner spprovincias, sppais;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    final String URL_DIRECCION = "http://faunanimal.com/api/android/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_compra_1);
        setui();
    }

    private void setui() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        preferencessettings = getApplicationContext().getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();

        //Personalizar Action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View v = getSupportActionBar().getCustomView();
        TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
        titulo.setText(R.string.finalizarcompra);
        //Personalizar Action bar end

        //Definir componenetes start
        Button btnsiguiente = (Button) findViewById(R.id.btnsiguiente);
        ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressBar);
        etnombre = (EditText) findViewById(R.id.etnombre);
        etapellidos = (EditText) findViewById(R.id.etapellidos);
        etemail = (EditText) findViewById(R.id.etemail);
        ettelefono = (EditText) findViewById(R.id.ettelefono);
        etdireccion = (EditText) findViewById(R.id.etdireccion);
        etcodigopostal = (EditText) findViewById(R.id.etcodigopostal);
        etciudad = (EditText) findViewById(R.id.etciudad);
        spprovincias = (Spinner) findViewById(R.id.spprovincias);
        etnif = (EditText) findViewById(R.id.etnif);
        sppais = (Spinner) findViewById(R.id.sppais);
        //Definir componentes end

        //Poner valor a progressbar start

        ProgressBarDrawable bgProgress = new ProgressBarDrawable(3);
        progressbar.setProgressDrawable(bgProgress);
        progressbar.setMax(3);
        progressbar.setProgress(1);
        //Poner valor a progressbar end

        etnombre.setText(preferencessettings.getString("nombre", ""));
        etapellidos.setText(preferencessettings.getString("apellidos", ""));
        etemail.setText(preferencessettings.getString("email", ""));


        //Listener para btnsiguiente start
        btnsiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comprobar que en los campos se a introducido algo
                if (etnombre.length() < 1) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.nombre_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etapellidos.length() < 1) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.apellido_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etemail.length() < 1) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.email_nohay, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (validaremail(etemail.getText().toString())) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.email_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (ettelefono.length() < 9) {//cantidad minima de digitos para el telefono
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.telefono_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etdireccion.length() < 1) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.direccion_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etcodigopostal.length() < 5) {//cantidad minima de digitos para el codigopostal
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.cp_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etciudad.length() < 1) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.ciudad_novalida, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etnif.length() <= 7) {
                    Toast toast = Toast.makeText(finaliza_compra_1.this, R.string.nifnovalido, Toast.LENGTH_SHORT);
                    toast.show();
                }  else {//si es correcto pasa la informacion

                    Intent i = new Intent(finaliza_compra_1.this, finaliza_compra_2.class);
                    i.putExtra("total", getIntent().getStringExtra("total"));
                    i.putExtra("nombreyapellidos", etnombre.getText().toString() + " , " + etapellidos.getText().toString() + ",NIF: " + etnif.getText().toString());
                    i.putExtra("direccionycodigopostal", etdireccion.getText().toString() + " ,CP: " + etcodigopostal.getText().toString() + ",Ciudad: " + etciudad.getText().toString() + ",Provincia: " + spprovincias.getSelectedItem().toString() + ",Pais: " + sppais.getSelectedItem().toString());
                    i.putExtra("email", etemail.getText().toString());
                    i.putExtra("telefono", ettelefono.getText().toString());
                    cargardatos();
                    startActivity(i);
                }


            }
        });
        //Listener para btnsiguiente end

    }

    public static boolean validaremail(String email) {//mascara para validar el correo
        boolean isValid = true;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }


    private void cargardatos() {
        if (conexionactiva()) {
            URL url = null;
            try {


                url = new URL(URL_DIRECCION + "?customer=" + preferencessettings.getString("codigo_cli", "1")
                        + "&name=" + etnombre.getText().toString()
                        + "&lastname=" + etapellidos.getText().toString()
                        + "&phone=" + ettelefono.getText().toString()
                        + "&street=" + etdireccion.getText().toString()
                        + "&postcode=" + etcodigopostal.getText().toString()
                        + "&city=" + etciudad.getText().toString()
                        + "&nif=" + etnif.getText().toString()
                        + "&region=" + spprovincias.getSelectedItem().toString()

                );
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
        protected Boolean doInBackground(URL... url) {//guardar informacion del json y tratarla correctamente
            Boolean result = false;
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url[0].openConnection();
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setReadTimeout(READ_TIMEOUT);
                if (urlConnection.getResponseCode() == 200) {
                    String resultStream = readStream(urlConnection.getInputStream());

                }

                result = true;

            } catch (IOException e) {
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
