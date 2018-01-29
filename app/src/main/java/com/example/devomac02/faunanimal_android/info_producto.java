package com.example.devomac02.faunanimal_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import me.relex.circleindicator.CircleIndicator;


public class info_producto extends AppCompatActivity {

    //Declaracion de Componentes
    TextView tvdescripcion, tvenvio, tvprecio;
    EditText tvcantidad;
    ToggleButton tgdescripcion, tgmetodosdepago, tgenvio;
    ViewPager vp;
    CustomPagerAdapter mCustomPagerAdapter;
    Button btnmas, btnmenos;

    //Declaracion de Variables
    String producto;
    String precio;
    String cantidad, descripcion;
    double cantidadnum;
    fragment_carrito_info fragcarrito;
    boolean cantidadcorrecta = true;
    boolean algunorepetido = false;
    DisplayMetrics displaymetrics;


    ArrayList<Bitmap> imagen_array;
    ArrayList<String> titulo_array;
    ArrayList<String> cantidad_array;
    ArrayList<String> precio_array;
    ArrayList<String> stock_array;

    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private static final String Mypreferences = "Mypreferences";
    private SharedPreferences preferencessettings;
    private SharedPreferences.Editor preferenceseditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //Inicializar variables start
        producto = getIntent().getStringExtra("info_producto_nombre");
        precio = getIntent().getStringExtra("info_producto_precio");
        cantidadnum = Double.parseDouble(getIntent().getStringExtra("info_producto_cantidad"));
        cantidadnum = Math.round(cantidadnum);
        cantidad = String.valueOf((int) cantidadnum);
        descripcion = getIntent().getStringExtra("info_producto_descripcion").replaceAll("\\<.*?>", "");
        limpiarentidadeshtml();
        //Inicializar variables end

        setContentView(R.layout.info_producto);
        controldesplegables();
        setui();
    }

    private void limpiarentidadeshtml() {


        descripcion = descripcion.replace("&Aacute;", "\u00C1");
        descripcion = descripcion.replace("&aacute;", "\u00E1");
        descripcion = descripcion.replace("&Aacute;", "\u00E1");
        descripcion = descripcion.replace("&Eacute;", "\u00C9");
        descripcion = descripcion.replace("&eacute;", "\u00E9");
        descripcion = descripcion.replace("&Iacute;", "\u00CD");
        descripcion = descripcion.replace("&iacute;", "\u00ED");
        descripcion = descripcion.replace("&Oacute;", "\u00D3");
        descripcion = descripcion.replace("&oacute;", "\u00F3");
        descripcion = descripcion.replace("&Uacute;", "\u00DA");
        descripcion = descripcion.replace("&uacute;", "\u00FA");
        descripcion = descripcion.replace("&uuml;", "\u00FC");
        descripcion = descripcion.replace("&Ntilde;", "\u00D1");
        descripcion = descripcion.replace("&ntilde;", "\u00F1");
        descripcion = descripcion.replace("&iexcl;", "\u00A1");
        descripcion = descripcion.replace("&iquest;", "\u00BF");
        descripcion = descripcion.replace("&ordf;", "\u00AA");
        descripcion = descripcion.replace("&ordm;", "\u00BA");
        descripcion = descripcion.replace("&euro;", "\u20AC");
        descripcion = descripcion.replace("&nbsp;", " ");
        descripcion = descripcion.replace("&iexcl;", "¡");
        descripcion = descripcion.replace("&quot;", "\"");
        descripcion = descripcion.replace("&#039;", "'");
        descripcion = descripcion.replace("&amp;", "&");
        descripcion = descripcion.replace("&nbsp;", " ");
        descripcion = descripcion.replace("&lt;", "<");
        descripcion = descripcion.replace("&gt;", ">");
        descripcion = descripcion.replace("&trade;", "(tm)");
        descripcion = descripcion.replace("&rdquo; ", "\"");
        descripcion = descripcion.replace("&ldquo;", "\"");


    }

    private void controldesplegables() {//controlar los desplegables de informacion extra del producto, descripcion,forma-pago y envio
        tvdescripcion = (TextView) findViewById(R.id.tvdescripcion);
        tvdescripcion.setVisibility(View.GONE);
        tvdescripcion.setText(descripcion);
        tgdescripcion = (ToggleButton) findViewById(R.id.tgdescripcion);
        tgdescripcion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvdescripcion.setVisibility(View.VISIBLE);
                } else {
                    tvdescripcion.setVisibility(View.GONE);
                }
            }
        });

        final View layoutformapago = (View) findViewById(R.id.layoutformapago);
        layoutformapago.setVisibility(View.GONE);
        tgmetodosdepago = (ToggleButton) findViewById(R.id.tgmetodosdepago);
        tgmetodosdepago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutformapago.setVisibility(View.VISIBLE);
                } else {
                    layoutformapago.setVisibility(View.GONE);
                }
            }
        });
        tvenvio = (TextView) findViewById(R.id.tvenvio);
        tvenvio.setVisibility(View.GONE);
        tgenvio = (ToggleButton) findViewById(R.id.tgenvio);
        tgenvio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvenvio.setVisibility(View.VISIBLE);
                } else {
                    tvenvio.setVisibility(View.GONE);
                }
            }
        });


    }

    private void setui() {


        preferencessettings = getSharedPreferences(Mypreferences, PREFERENCE_MODE_PRIVATE);
        preferenceseditor = preferencessettings.edit();
        //Personalizar action bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvinfoproducto = (TextView) findViewById(R.id.tvinfoproducto);
        tvinfoproducto.setText(producto);
        //Personalizar action bar end

        //Definir componentes y ponerles valores start
        Button btnanadircarrito = (Button) findViewById(R.id.btnanadircarrito);

        tvprecio = (TextView) findViewById(R.id.tvprecio);
        tvprecio.setText(precio);
        tvcantidad = (EditText) findViewById(R.id.etcantidad);
        tvcantidad.setHint("Cantidad máxima: " + String.valueOf(cantidad));
        btnmas = (Button) findViewById(R.id.btnmas);
        btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvcantidad.setText(String.valueOf(Integer.parseInt(tvcantidad.getText().toString()) + 1));
            }
        });
        btnmenos = (Button) findViewById(R.id.btnmenos);
        btnmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(tvcantidad.getText().toString()) - 1 < 0) {
                    tvcantidad.setText("0");
                } else {
                    tvcantidad.setText(String.valueOf(Integer.parseInt(tvcantidad.getText().toString()) - 1));
                }
            }
        });

        if (Integer.parseInt(cantidad) == 0) {
            tvcantidad.setEnabled(false);
            tvcantidad.setText("Fuera de stock");
            btnmas.setEnabled(false);
            btnmenos.setEnabled(false);
            btnanadircarrito.setEnabled(false);
        } else {
            btnanadircarrito.setEnabled(true);
            tvcantidad.setEnabled(true);
            tvcantidad.setText("1");
            btnmas.setEnabled(true);
            btnmenos.setEnabled(true);
        }

        vp = (ViewPager) findViewById(R.id.slider);
        ViewGroup.LayoutParams params = vp.getLayoutParams();
        displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        params.height = displaymetrics.heightPixels / 4;
        vp.setLayoutParams(params);
        mCustomPagerAdapter = new CustomPagerAdapter(this, getResources());
        vp.setAdapter(mCustomPagerAdapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        //Definir componentes y ponerles valores end

        //Definir boton añadir al carrito y ponerle listener start
        btnanadircarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvcantidad.getText().length() != 0) {
                    if (Integer.parseInt(String.valueOf(tvcantidad.getText())) > 0 && Integer.parseInt(String.valueOf(tvcantidad.getText())) <= cantidadnum) {
                        Intent i = new Intent(info_producto.this, MainActivity.class);
                        i.putExtra("carrito", 1);
                        guardar_producto(tvcantidad.getText().toString());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        if (cantidadcorrecta) {
                            startActivity(i);
                        }
                    } else {
                        Toast toast = Toast.makeText(info_producto.this, "Cantidad Errónea,Modifíquela", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(info_producto.this, "Introduce una cantidad", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        //Definir boton añadir al carrito y ponerle listener end


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


    public void guardar_producto(String tvcantidad) {
        //guarda el producto en el preferences para despues desde el carrito acceder y reflejar la informacion
        //control si metes dos productos y suma sus cantidades,si la suma es mayor al stock salta un toast advirtiendo


        Gson gson = new Gson();
        String json = preferencessettings.getString("fragment_info_obj", "");
        fragcarrito = gson.fromJson(json, fragment_carrito_info.class);

        //Gson gson = new Gson();
        // String json = gson.toJson(fragcarrito);
        //preferenceseditor.putString("fragment_info_obj", null);
        //preferenceseditor.commit();

        if (fragcarrito == null) {
            //si no hay carrito inicializa los array y mete la informacion
            imagen_array = new ArrayList<Bitmap>();
            titulo_array = new ArrayList<String>();
            cantidad_array = new ArrayList<String>();
            precio_array = new ArrayList<String>();
            stock_array = new ArrayList<String>();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = null;
            try {
                url = new URL(getIntent().getStringExtra("info_producto_imagenprincipal"));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                imagen_array.add(image);
                titulo_array.add(getIntent().getStringExtra("info_producto_nombre"));
                cantidad_array.add(tvcantidad);
                precio_array.add(getIntent().getStringExtra("info_producto_precio"));
                stock_array.add(getIntent().getStringExtra("info_producto_cantidad"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //si hay carrito solo añade mas informacion a los arrays
            imagen_array = fragcarrito.getImagen();
            titulo_array = fragcarrito.getTitulo();
            cantidad_array = fragcarrito.getCantidad();
            precio_array = fragcarrito.getPrecio();
            stock_array = fragcarrito.getStock();


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = null;
            try {
                url = new URL(getIntent().getStringExtra("info_producto_imagenprincipal"));
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ArrayList<String> repetido = fragcarrito.getTitulo();
                for (int i = 0; i < titulo_array.size(); i++) {
                    if (repetido.get(i).equals(getIntent().getStringExtra("info_producto_nombre"))) {
                        algunorepetido = true;
                        String[] stock_partido = stock_array.get(i).split("\\.");
                        if ((Integer.parseInt(cantidad_array.get(i)) + Integer.parseInt(tvcantidad)) <= Integer.parseInt(stock_partido[0])) {
                            cantidad_array.set(i, String.valueOf(Integer.parseInt(cantidad_array.get(i)) + Integer.parseInt(tvcantidad)));
                        } else {
                            cantidadcorrecta = false;
                            Toast toast = Toast.makeText(info_producto.this, "Cantidad mayor que stock,revise carrito", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        break;
                    }

                }


                if (!algunorepetido) {//comprobar si hay algun articulo repetido en el carrito si o hay suma las cantidades
                    //si es menor que el stock es correcto si no lo es salta error
                    imagen_array.add(image);
                    titulo_array.add(getIntent().getStringExtra("info_producto_nombre"));
                    cantidad_array.add(tvcantidad);
                    precio_array.add(getIntent().getStringExtra("info_producto_precio"));
                    stock_array.add(getIntent().getStringExtra("info_producto_cantidad"));
                    algunorepetido = false;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

//cargar informacion en objeto y guardar este en el archivo preferences
        fragcarrito = new fragment_carrito_info(imagen_array, titulo_array, cantidad_array, precio_array, stock_array);


        gson = new Gson();
        json = gson.toJson(fragcarrito);
        preferenceseditor.putString("fragment_info_obj", json);
        preferenceseditor.commit();


    }


    class CustomPagerAdapter extends PagerAdapter {//slider de imagenes de la parte superior

        Context mContext;
        Bitmap bMap;
        LayoutInflater mLayoutInflater;

        String[] urls = getIntent().getStringExtra("info_producto_imagenes").split(" ");


        public CustomPagerAdapter(Context context, Resources resource) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.img_slider);
            /*bMap = BitmapFactory.decodeResource(resources, mResources[position]);
            imageView.setImageBitmap(bMap);
            container.addView(itemView);
            return itemView;*/
            Picasso.with(info_producto.this).load(urls[position]).into(imageView);

            container.addView(itemView);
            return itemView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
            /*bMap.recycle();
            bMap = null;*/
        }
    }
}