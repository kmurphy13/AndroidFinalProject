package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PostEventActivity extends AppCompatActivity implements PostEventFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
