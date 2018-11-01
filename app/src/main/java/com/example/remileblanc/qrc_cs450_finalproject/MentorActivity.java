package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MentorActivity extends AppCompatActivity implements MentorFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
