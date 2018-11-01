package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfessorActivity extends AppCompatActivity implements ProfessorFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
