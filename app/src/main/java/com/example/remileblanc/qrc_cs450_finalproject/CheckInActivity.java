package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class CheckInActivity extends AppCompatActivity implements CheckInFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
    }

//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//
//        CheckBox mentorHelp = findViewById(R.id.mentorHelp);
//        CheckBox bookCheckout = findViewById(R.id.bookCheckout);
//        CheckBox calculatorCheckout = findViewById(R.id.calculatorCheckout);
//        CheckBox doingHW = findViewById(R.id.doingHW);
//        CheckBox qrcEvent = findViewById(R.id.qrcEvent);
//
//
//        boolean checked = ((CheckBox) view).isChecked();
//
//        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.mentorHelp:
//                if (checked) {
//                    // the user is a student
//                    mentorHelp.setChecked(true);
//                    bookCheckout.setChecked(false);
//                    calculatorCheckout.setChecked(false);
//                    doingHW.setChecked(false);
//                    qrcEvent.setChecked(false);
//                }else {
//                    mentorHelp.setChecked(false);
//                }
//                break;
//            case R.id.bookCheckout:
//                if (checked) {
//                    // the user is a student
//                    mentorHelp.setChecked(false);
//                    bookCheckout.setChecked(true);
//                    calculatorCheckout.setChecked(false);
//                    doingHW.setChecked(false);
//                    qrcEvent.setChecked(false);
//                }else {
//                    bookCheckout.setChecked(false);
//                }
//                break;
//            case R.id.calculatorCheckout:
//                if (checked) {
//                    // the user is a student
//                    mentorHelp.setChecked(false);
//                    bookCheckout.setChecked(false);
//                    calculatorCheckout.setChecked(true);
//                    doingHW.setChecked(false);
//                    qrcEvent.setChecked(false);
//                }else {
//                    calculatorCheckout.setChecked(false);
//                }
//                break;
//            case R.id.doingHW:
//                if (checked) {
//                    // the user is a student
//                    mentorHelp.setChecked(false);
//                    bookCheckout.setChecked(false);
//                    calculatorCheckout.setChecked(false);
//                    doingHW.setChecked(true);
//                    qrcEvent.setChecked(false);
//                }else {
//                    doingHW.setChecked(false);
//                }
//                break;
//            case R.id.qrcEvent:
//                if (checked) {
//                    // the user is a student
//                    mentorHelp.setChecked(false);
//                    bookCheckout.setChecked(false);
//                    calculatorCheckout.setChecked(false);
//                    doingHW.setChecked(false);
//                    qrcEvent.setChecked(true);
//                }else {
//                    qrcEvent.setChecked(false);
//                }
//                break;
//        }
//    }

    public String getObjectiveType(){
        CheckBox mentorHelp = findViewById(R.id.mentorHelp);
        CheckBox bookCheckout = findViewById(R.id.bookCheckout);
        CheckBox calculatorCheckout = findViewById(R.id.calculatorCheckout);
        CheckBox doingHW = findViewById(R.id.doingHW);
        CheckBox qrcEvent = findViewById(R.id.qrcEvent);

        if(mentorHelp.isChecked()){
            return "Mentor help";
        }else if(bookCheckout.isChecked()){
            return "Book checkout";
        }else if(calculatorCheckout.isChecked()){
            return "Calculator checkout";
        }else if (doingHW.isChecked()){
            return "Doing homework";
        }else if(qrcEvent.isChecked()){
            return "QRC event";
        }
        return null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

