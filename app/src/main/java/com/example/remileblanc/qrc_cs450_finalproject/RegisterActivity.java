package com.example.remileblanc.qrc_cs450_finalproject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?

        CheckBox student = findViewById(R.id.checkbox_student);
        CheckBox mentor = findViewById(R.id.checkbox_mentor);
        CheckBox professor = findViewById(R.id.checkbox_professor);

        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_student:
                if (checked) {
                    // the user is a student
                    student.setChecked(true);
                    mentor.setChecked(false);
                    professor.setChecked(false);
                }else {
                    student.setChecked(false);
                }
                break;
            case R.id.checkbox_mentor:
                if (checked) {
                    // the user is a mentor
                    student.setChecked(false);
                    mentor.setChecked(true);
                    professor.setChecked(false);
                }else {
                    // I'm lactose intolerant
                    mentor.setChecked(false);
                }
                break;
            case R.id.checkbox_professor:
                if (checked) {
                    // the user is a professor
                    student.setChecked(false);
                    mentor.setChecked(false);
                    professor.setChecked(true);
                }else {
                    professor.setChecked(false);
                }
                break;
            // TODO: Veggie sandwich
        }
    }

    public String getUserType(){
        CheckBox student = findViewById(R.id.checkbox_student);
        CheckBox mentor = findViewById(R.id.checkbox_mentor);
        CheckBox professor = findViewById(R.id.checkbox_professor);

        if(student.isChecked()){
            return "Student";
        }else if(professor.isChecked()){
            return "Professor";
        }else if(mentor.isChecked()){
            return "Mentor";
        }
        return null;
    }
}
