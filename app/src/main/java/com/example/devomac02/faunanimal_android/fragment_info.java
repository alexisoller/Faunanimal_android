package com.example.devomac02.faunanimal_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class fragment_info extends Fragment {

    View rootView;

    public static Fragment newInstance() {
        fragment_info fragment = new fragment_info();
        return fragment;
    }

    public fragment_info() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        //Definicion de componenetes start
        TextView email = (TextView) rootView.findViewById(R.id.tv_email);
        TextView blog = (TextView) rootView.findViewById(R.id.tv_blog_dir);
        ImageView imgbtn_facebook = (ImageView) rootView.findViewById(R.id.imgbtn_facebook);
        ImageView imgbtn_twitter = (ImageView) rootView.findViewById(R.id.imgbtn_twitter);
        ImageView imgbtn_instagram = (ImageView) rootView.findViewById(R.id.imgbtn_instagram);
        ImageView imgbtn_pinterest = (ImageView) rootView.findViewById(R.id.imgbtn_pinterest);
        //Definicion de componenetes end

        //Listeners de los diferentes componentes start

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://faunanimal.com/contacts";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://faunanimal.com/blog/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        imgbtn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/faunanimal";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        imgbtn_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/FaunAnimal";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        imgbtn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/faunanimal/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        imgbtn_pinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.pinterest.com/faunanimal/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        //Listeners de los diferentes componentes end

        return rootView;
    }


}