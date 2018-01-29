package com.example.devomac02.faunanimal_android;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.Timer;
import java.util.TimerTask;
import me.relex.circleindicator.CircleIndicator;


public class fragment_home extends Fragment implements View.OnClickListener {
    //Declaracion de Componentes
    View rootView;
    Handler handler = new Handler();
    ViewPager vp;
    Runnable Update;
    DisplayMetrics displaymetrics;
    CustomPagerAdapter mCustomPagerAdapter;
    //Declaracion de Variables

    int currentPage;
    int buttonWidth;
    int buttonHeight;
    private int TAM_SLIDER;
    private int TAM_ENVIO = 27;
    int NUM_PAGES;


    public static Fragment newInstance() {
        fragment_home fragment = new fragment_home();
        return fragment;
    }

    public fragment_home() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //slider de imagenes
        vp = (ViewPager) rootView.findViewById(R.id.slider);
        ViewGroup.LayoutParams params = vp.getLayoutParams();
        displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        params.height = displaymetrics.heightPixels / 4;
        TAM_SLIDER = displaymetrics.heightPixels / 4;
        vp.setLayoutParams(params);
        mCustomPagerAdapter = new CustomPagerAdapter(getContext(), TAM_SLIDER, displaymetrics.widthPixels, getResources());
        vp.setAdapter(mCustomPagerAdapter);
        CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(vp);
        //imagenes en el slider
        NUM_PAGES = mCustomPagerAdapter.getCount();
        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES + 1) {
                    currentPage = 0;
                }
                vp.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 30000);
        //imagenes en el slider
        //slider imagenes

        //botones de animales
        calculartamboton();
        botones_listenes();
        //botones de animales
        return rootView;
    }

    private void calculartamboton() {//calcular el tamaño que tendran los botones calculando el tamaño de todos
        // los componentes de la pantalla y sabiendo el tamaño de esta en px

        float density = getContext().getResources().getDisplayMetrics().density;
        int px_tabbar = Math.round(47 * density);//TAMAÑO TABS
        int px_envio = Math.round(TAM_ENVIO * density);
        int px_barradenoti = Math.round(25 * density);//TAMAÑO BARRA DE NOTIFICACIONES
        int px_actionbar = Math.round(56 * density);//TAMAÑO DE ACTIONBAR
        int width = displaymetrics.widthPixels;

        int height = displaymetrics.heightPixels - px_tabbar - px_actionbar - TAM_SLIDER - px_envio - px_barradenoti;
        buttonWidth = width / 2;
        buttonHeight = height / 3;

    }


    private void botones_listenes() {
        //definicion de botones start
        ImageButton imgbutpeces = (ImageButton) rootView.findViewById(R.id.imgbutpeces);
        ImageButton imgbutreptiles = (ImageButton) rootView.findViewById(R.id.imgbutreptiles);
        ImageButton imgbutpajaros = (ImageButton) rootView.findViewById(R.id.imgbutpajaros);
        ImageButton imgbutroedores = (ImageButton) rootView.findViewById(R.id.imgbutroedores);
        ImageButton imgbutgatos = (ImageButton) rootView.findViewById(R.id.imgbutgatos);
        ImageButton imgbutperros = (ImageButton) rootView.findViewById(R.id.imgbutperros);
        //definicion de botones end

        //Listeners de botones start
        imgbutpeces.setOnClickListener(this);
        imgbutreptiles.setOnClickListener(this);
        imgbutpajaros.setOnClickListener(this);
        imgbutroedores.setOnClickListener(this);
        imgbutgatos.setOnClickListener(this);
        imgbutperros.setOnClickListener(this);
        //Listeners de botones end
        //Poner tamaño a los botones start
        imgbutpeces.getLayoutParams().width = buttonWidth;
        imgbutpeces.getLayoutParams().height = buttonHeight;
        imgbutpeces.requestLayout();

        imgbutreptiles.getLayoutParams().width = buttonWidth;
        imgbutreptiles.getLayoutParams().height = buttonHeight;
        imgbutreptiles.requestLayout();

        imgbutpajaros.getLayoutParams().width = buttonWidth;
        imgbutpajaros.getLayoutParams().height = buttonHeight;
        imgbutpajaros.requestLayout();

        imgbutroedores.getLayoutParams().width = buttonWidth;
        imgbutroedores.getLayoutParams().height = buttonHeight;
        imgbutroedores.requestLayout();

        imgbutgatos.getLayoutParams().width = buttonWidth;
        imgbutgatos.getLayoutParams().height = buttonHeight;
        imgbutgatos.requestLayout();

        imgbutperros.getLayoutParams().width = buttonWidth;
        imgbutperros.getLayoutParams().height = buttonHeight;
        imgbutperros.requestLayout();
        //Poner tamaño a los botones end

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.usuario:
                Intent i = new Intent(getContext(), login.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        //Comprobar boton pulsado
        switch (v.getId()) {
            case R.id.imgbutpeces:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Peces");
                startActivity(i);
                break;
            case R.id.imgbutreptiles:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Reptiles");
                startActivity(i);
                break;
            case R.id.imgbutpajaros:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Pájaros");
                startActivity(i);
                break;
            case R.id.imgbutroedores:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Roedores");
                startActivity(i);
                break;
            case R.id.imgbutgatos:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Gatos");
                startActivity(i);
                break;
            case R.id.imgbutperros:
                i = new Intent(rootView.getContext(), animales_productos.class);
                i.putExtra("nombre", "Perros");
                startActivity(i);
                break;
        }
    }
}

class CustomPagerAdapter extends PagerAdapter {//slider de imagenes de la parte superior

    Context mContext;
    LayoutInflater mLayoutInflater;
    public int[] mResources = {
            R.drawable.slider_1,
            R.drawable.slider_2,
            R.drawable.slider_3,

    };
    int altoimg, anchoimg;
    Resources resources;

    public CustomPagerAdapter(Context context, int alto, int ancho, Resources resource) {
        mContext = context;
        altoimg = alto;
        anchoimg = ancho;
        resources = resource;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_slider);

        //cambiar tamaño de imagen al del slider, se estirara tanto como el slider
        Bitmap bMap = BitmapFactory.decodeResource(resources, mResources[position]);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, anchoimg, altoimg, true);
        imageView.setImageBitmap(bMapScaled);


        //imageView.setImageResource(mResources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}