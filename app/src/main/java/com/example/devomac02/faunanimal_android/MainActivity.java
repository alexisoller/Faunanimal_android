package com.example.devomac02.faunanimal_android;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //Declaracion de Componentes
    private TabLayout tabLayout;
    ViewPager viewPager;
    private android.support.v7.app.ActionBar actionbar;
    //Declaracion de Variables
    int iracarrito = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iracarrito = getIntent().getIntExtra("carrito", 0);//segun el valor se cargara una pestaña u otra
        //para cargar el carrito vacio despues de una compra por ejemplo
        setContentView(R.layout.activity_main);
        setUI();

    }


    private void setUI() {

        //Personalizar Action Bar start
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        actionbar = getSupportActionBar();
        //Personalizar Action Bar start


        //Definicion de componentes start
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        //Definicion de componentes start

        //Creacion de FragmentPagerAdapter Personalizado start
        MiFragmentPagerAdapter mfpa = new MiFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        //Creacion de FragmentPagerAdapter Personalizado end


        //Personalizar tablayout start
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(mfpa);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.search);
        tabLayout.getTabAt(2).setIcon(R.drawable.cart);
        tabLayout.getTabAt(3).setIcon(R.drawable.info_frag);
        if (iracarrito == 1) {
            tabLayout.getTabAt(2).select();
        }
        //Personalizar tablayout end

        //añadir listener para tablayout start
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View v = actionbar.getCustomView();
                TextView titulo = (TextView) v.findViewById(R.id.actionbar_title);
                switch (tab.getPosition()) {
                    case 0:
                        titulo.setText(R.string.app_name);
                        break;
                    case 1:
                        titulo.setText(R.string.search);
                        break;
                    case 2:
                        titulo.setText(R.string.cart);
                        break;
                    case 3:
                        titulo.setText(R.string.info_titulo);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //añadir listener para tablayout end


    }


    @Override
    protected void onResume() {
        super.onResume();


        iracarrito = getIntent().getIntExtra("carrito", 0);

        if (iracarrito == 0) {
            tabLayout.getTabAt(0).select();


        }
    }


}
