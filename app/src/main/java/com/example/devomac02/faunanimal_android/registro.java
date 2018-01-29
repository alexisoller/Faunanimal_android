package com.example.devomac02.faunanimal_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class registro extends AppCompatActivity {
    //Declarar componenetes
    Button btnregistrarse;
    TextView tvinfo;
    TextView tvverterminosycondiciones;
    CheckBox chacepto, chmegustaria;
    public EditText etnombre, etapellidos, etemail, etpassword;
    final String URL_PRODUCTOS = "http://faunanimal.com/api/android/register";
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    String nombre, apellidos, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        setUI();
    }

    private void setUI() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Personalizar action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View v = getSupportActionBar().getCustomView();
        TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
        titulo.setText(R.string.registrate);
        //Personalizar action bar end

        //Definicion de componentes start
        chacepto = (CheckBox) findViewById(R.id.chacepto);
        chmegustaria = (CheckBox) findViewById(R.id.chmegustaria);
        tvverterminosycondiciones = (TextView) findViewById(R.id.tvverterminosycondiciones);
        tvinfo = (TextView) findViewById(R.id.tvinfo);
        tvinfo.setVisibility(View.INVISIBLE);
        ToggleButton tbinfo = (ToggleButton) findViewById(R.id.tbinfo);
        btnregistrarse = (Button) findViewById(R.id.btnregistrarse);

        etnombre = (EditText) findViewById(R.id.etnombre);
        etapellidos = (EditText) findViewById(R.id.etapellidos);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpassword);


        //Definicion de componentes end
        chmegustaria.setChecked(true);
        //Listeners de los componenetes start

        tvverterminosycondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta_Terminos_Y_Condiciones();
            }
        });
        tbinfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvinfo.setVisibility(View.VISIBLE);
                } else {
                    tvinfo.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnregistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etnombre.length() < 1) {
                    Toast toast = Toast.makeText(registro.this, R.string.nombre_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etapellidos.length() < 1) {
                    Toast toast = Toast.makeText(registro.this, R.string.apellido_novalido, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etemail.length() < 1) {
                    Toast toast = Toast.makeText(registro.this, R.string.email_nohay, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (etpassword.length() < 6) {
                    Toast toast = Toast.makeText(registro.this, R.string.contraseñanovalida, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (chacepto.isChecked()) {
                    nombre = etnombre.getText().toString();
                    nombre = nombre.replace(" ", "");
                    apellidos = etapellidos.getText().toString();
                    apellidos = apellidos.replace(" ", "");
                    email = etemail.getText().toString();
                    cargardatos();
                } else {
                    Toast toast = Toast.makeText(registro.this, R.string.tienequeaceptar, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });
        //Listeners de los componenetes end

    }

    private void alerta_Terminos_Y_Condiciones() {


        //Creacion de alerta para los terminos y condiciones
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Política de privacidad");
        builder.setMessage(R.string.terminosycondiciones);

        builder.setPositiveButton("Acepto",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chacepto.setChecked(true);

                    }
                });

        builder.setNegativeButton("No Acepto",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        chacepto.setChecked(false);


                    }
                });

        builder.show();


    }

    private void cargardatos() {
        if (conexionactiva()) {
            URL url = null;
            try {

                url = new URL(URL_PRODUCTOS + "?name=" + nombre + "&lastname=" + apellidos + "&email=" + etemail.getText() + "&password=" + etpassword.getText());

                System.out.println("la url de registro es" + url.toString());
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
        private boolean login_correcto = false;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (login_correcto) {
                Intent intent = new Intent(registro.this, MainActivity.class);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("carrito", 0);
                startActivity(intent);
                Toast toast = Toast.makeText(registro.this, R.string.logincorrecto, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(registro.this, "Ya existe este email", Toast.LENGTH_LONG);
                toast.show();
            }
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
                    if (resultStream.equals("Usuario o password no correctos.")) {
                        login_correcto = false;
                    } else {
                        if (resultStream.equals("Ya existe este email")) {
                            login_correcto = false;
                            result = false;
                            return result;

                        } else {

                            login_correcto = true;
                            try {
                                JSONObject item = new JSONObject(resultStream);
                                preferencessettings = getApplicationContext().getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
                                preferenceseditor = preferencessettings.edit();
                                preferenceseditor.putBoolean("login", true);
                                preferenceseditor.putString("nombre", nombre);
                                preferenceseditor.putString("apellidos", apellidos);
                                preferenceseditor.putString("email", email);
                                preferenceseditor.putString("codigo_cli", item.getString("entity_id"));

                                preferenceseditor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }
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
