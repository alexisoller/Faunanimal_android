package com.example.devomac02.faunanimal_android;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;


public class httphandler extends AppCompatActivity {
    String order_id, total, tokenid;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;


    public String post(String posturl, Token token_card, String tot, String order, String direccionycodigopostal, String email, String telefono, Card card) {
        //Este metodo guarda la informacion en postdumper.php y de ahi sale un archivo .txt
        //tambien envia la informacion de pago necesaria del pedido,con el metodo pedido()

        try {
            tokenid = token_card.getId();
            order_id = order;
            total = tot;
            String token = token_card.toString();
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("total", total);
            chargeParams.put("order_id", order_id);

            chargeParams.put("currency", "eur");
            chargeParams.put("card", token_card.getId());
            token = token + chargeParams;


            HttpClient httpclient = HttpClientBuilder.create().build();
            HttpPost httppost = new HttpPost(posturl);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("data", token.toString()));

            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();
            String text = EntityUtils.toString(ent);


            return text;
        } catch (Exception e) {
            return "error";
        }
    }


    private int pasaracentimos(String total) {
        //conversion a centimos ,requisito al crear el cliente
        int centimos;
        total = total.trim();
        String[] eurosycentimos = total.split("\\.");
        centimos = Integer.parseInt(eurosycentimos[0]) * 100;
        centimos = centimos + Integer.parseInt(eurosycentimos[1]);
        return centimos;

    }


}
