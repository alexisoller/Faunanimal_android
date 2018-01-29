package com.example.devomac02.faunanimal_android;


import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.net.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class finaliza_compra_datostarjeta extends AppCompatActivity {

    //Declaracion de Componentes
    EditText numero_tarjeta;
    Spinner numero_mes;
    EditText numero_anyo;
    EditText numero_cvc;
    Button btn_aceptar;

    //Declaracion de Variables
    Card card;
    final int NUMEROS_TARJETA = 16;
    final int NUMEROS_ANYO = 4;
    final int NUMEROS_CVC = 3;
    String order_id;
    NotificationManager notifyMgr;


    private final String PUBLISHABLE_KEY = "pk_test_YPEHuxKKByi093ExtIkrCy4A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_compra_datostarjeta);
        notifyMgr = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        order_id = getIntent().getStringExtra("order_id");
        setui();
    }

    private void setui() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //Personalizar Action bar start


        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        View v = getSupportActionBar().getCustomView();
        TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
        titulo.setText(R.string.datosdetarjeta);
        //Personalizar Action bar end

        //Definir componentes start

        numero_tarjeta = (EditText) findViewById(R.id.et_numerotarjeta);
        numero_mes = (Spinner) findViewById(R.id.sp_mes);
        numero_anyo = (EditText) findViewById(R.id.et_anyo);
        numero_cvc = (EditText) findViewById(R.id.et_codigoverificacion);
        btn_aceptar = (Button) findViewById(R.id.btn_ok);

        //Definir componentes end

        //Listener para el boton de pagar start

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (numero_tarjeta.length() < NUMEROS_TARJETA) {
                    Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.cantidad_digitos_16, Toast.LENGTH_SHORT);
                    toast.show();

                } else if (numero_anyo.length() < NUMEROS_ANYO) {
                    Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.cantidad_digitos_4, Toast.LENGTH_SHORT);
                    toast.show();

                } else if (numero_cvc.length() < NUMEROS_CVC) {
                    Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.cantidad_digitos_3, Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    btn_aceptar.setEnabled(false);
                    card = new Card(numero_tarjeta.getText().toString(), Integer.parseInt(numero_mes.getSelectedItem().toString()), Integer.parseInt(numero_anyo.getText().toString()), numero_cvc.getText().toString());
                    card.setName(getIntent().getStringExtra("nombreyapellidos"));
                    validartarjeta_pago();
                }


            }
        });
        //Listener para el boton de pagar end


    }

    public void validartarjeta_pago() {

        if (!card.validateNumber()) {
            card = null;
            Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.numero_tarjeta_novalido, Toast.LENGTH_SHORT);
            toast.show();
        } else if (!card.validateExpiryDate()) {
            card = null;
            Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.fecha_novalida, Toast.LENGTH_SHORT);
            toast.show();
        } else if (!card.validateCVC()) {
            card = null;
            Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.cvc_novalido, Toast.LENGTH_SHORT);
            toast.show();
        } else {


            if (card.validateCard()) {//si los datos son correctos se procede al pago
                new Stripe().createToken(
                        card,
                        PUBLISHABLE_KEY,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                                order_id = s.format(new Date());
                                httphandler handler = new httphandler();
                                pago_stripe(token);
                                handler.post("http://faunanimal.com/postdumper.php", token, getIntent().getStringExtra("total"), order_id, getIntent().getStringExtra("direccionycodigopostal"), getIntent().getStringExtra("email"), getIntent().getStringExtra("telefono"), card);
                                Intent i = new Intent(finaliza_compra_datostarjeta.this, MainActivity.class);
                                ComponentName cn = i.getComponent();
                                i = IntentCompat.makeRestartActivityTask(cn);
                                i.putExtra("carrito", 1);//pasamos el valor 1 para que al pagar vuelva a la pestaña de carrito
                                i.putExtra("borrar", 0);//padamos el valor 0 para que borre todos lo datos del carrito una vez ya hemos pagado
                                notification(1, "Pedido realizado correctamente", "Pedido número:" + order_id);//enviamos una notificacion con el id de pedido al cliente
                                //y notificamos que el pedido se a recibido correctamente
                                startActivity(i);
                            }

                            public void onError(Exception error) {
                                Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.tarjeta_no_valida, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
            } else {
                Toast toast = Toast.makeText(finaliza_compra_datostarjeta.this, R.string.tarjeta_no_valida, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }

    private void pago_stripe(Token token) {
        RequestOptions requestOptions = (new RequestOptions.RequestOptionsBuilder()).setApiKey("sk_test_ZrRSGV3CQsx2tGR8QsaJwbgS").build();
        System.out.println("hola" + getIntent().getStringExtra("comentario"));
        try {
            com.stripe.Stripe.apiKey = "sk_test_ZrRSGV3CQsx2tGR8QsaJwbgS";
            //Creamos un cliente start
            Map<String, Object> customerParams = new HashMap<String, Object>();
            customerParams.put("email", getIntent().getStringExtra("email"));
            customerParams.put("source", token.getId());
            Map<String, String> initialMetadata = new HashMap<String, String>();
            initialMetadata.put("order_id", order_id);
            initialMetadata.put("direccion", getIntent().getStringExtra("direccionycodigopostal"));
            initialMetadata.put("telefono", getIntent().getStringExtra("telefono"));
            initialMetadata.put("amount", String.valueOf(pasaracentimos(getIntent().getStringExtra("total")))); // total con centimos
            initialMetadata.put("currency", "eur");
            initialMetadata.put("comentario", getIntent().getStringExtra("comentario"));
            customerParams.put("metadata", initialMetadata);
            Customer customer = Customer.create(customerParams);
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", pasaracentimos(getIntent().getStringExtra("total"))); // total con centimos
            chargeParams.put("currency", "eur");
            //Creamos un cliente end
            //Creamos un pedido y lo unimos al cliente start
            chargeParams.put("customer", customer.getId());
            //Charge.create(chargeParams);
            Charge.create(chargeParams, requestOptions);
            //Creamos un pedido y lo unimos al cliente end
            //Guardamos la info en preferences end
        } catch (com.stripe.exception.AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

    }

    private int pasaracentimos(String total) {
        //conversion a centimos ,requisito al crear el cliente
        int centimos;
        total = total.trim();
        String[] eurosycentimos = null;
        if (total.contains(".")) {
            eurosycentimos = total.split("\\.");
        } else {
            eurosycentimos = total.split(",");

        }
        centimos = Integer.parseInt(eurosycentimos[0]) * 100;
        centimos = centimos + Integer.parseInt(eurosycentimos[1]);
        return centimos;

    }


    public void notification(int id, String titulo, String contenido) {

        //Crear la notificacion de pedido correcto
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.notificacion)
                        .setContentTitle(titulo)
                        .setContentText(contenido)
                        .setColor(Color.WHITE);


        notifyMgr.notify(id, builder.build());
    }


}
