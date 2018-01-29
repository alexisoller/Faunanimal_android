package com.example.devomac02.faunanimal_android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class login extends AppCompatActivity {
    //Declaracion de Componentes
    Button btniniciarsesion, btnregistro;
    TextView tvolpass;
    EditText etemail, etpass;
    //Declaracion de Variables
    final String URL_LOGIN = "http://faunanimal.com/api/android/login";
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setUI();
    }

    private void setUI() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        preferencessettings = getApplicationContext().getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();

        if (preferencessettings.getBoolean("login", false)) {
            Toast toast = Toast.makeText(login.this, R.string.yalogin, Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(login.this, MainActivity.class);
            finish();
            intent.putExtra("carrito", 0);
            startActivity(intent);

        } else {

            //Personalizacion Action bar start

            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.action_bar_custom);
            View v = getSupportActionBar().getCustomView();
            TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
            titulo.setText(R.string.iniciarsesion);
            //Personalizacion Action bar start

            //Definir componenetes Start
            btniniciarsesion = (Button) findViewById(R.id.btniniciarsesion);
            tvolpass = (TextView) findViewById(R.id.tvolpass);
            btnregistro = (Button) findViewById(R.id.btnregistro);
            etemail = (EditText) findViewById(R.id.etemail);
            etpass = (EditText) findViewById(R.id.etpass);

            //Definir componenetes end


            //Añadir Listeners a los componenetes start
            btniniciarsesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (etpass.length() >= 6) {
                            cargardatos();
                        } else {
                            Toast toast = Toast.makeText(login.this, R.string.contraseñanovalida, Toast.LENGTH_LONG);
                            toast.show();
                        }
                }
            });


            tvolpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://faunanimal.com/customer/account/forgotpassword/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            btnregistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(login.this, registro.class);
                    finish();
                    startActivity(intent);
                }
            });
            //Añadir Listeners a los componenetes end
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.removeItem(R.id.usuario);
        return true;
    }

    private void cargardatos() {
        if (conexionactiva()) {
            URL url = null;
            try {
                url = new URL(URL_LOGIN + "?&username=" + etemail.getText() + "&password=" + etpass.getText());
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
                Intent intent = new Intent(login.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("carrito", 0);
                startActivity(intent);
                Toast toast = Toast.makeText(login.this, R.string.logincorrecto, Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(login.this, R.string.loginerror, Toast.LENGTH_LONG);
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
                        login_correcto = true;
                        JSONObject item = new JSONObject(resultStream);

                        preferenceseditor.putBoolean("login", true);
                        preferenceseditor.putString("nombre", item.getString("firstname"));
                        preferenceseditor.putString("apellidos", item.getString("lastname"));
                        preferenceseditor.putString("email", item.getString("email"));
                        preferenceseditor.putString("codigo_cli", item.getString("entity_id"));

                        preferenceseditor.commit();

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

