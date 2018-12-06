package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudentUsageActivity extends AppCompatActivity implements StudentUsageFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_usage);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
