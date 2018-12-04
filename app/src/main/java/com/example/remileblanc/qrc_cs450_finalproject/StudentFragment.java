package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentFragment extends Fragment {


    private StudentFragment.OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    TextView tv_helloStudent;



    public StudentFragment() {
        // Required empty public constructor
    }



    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_student, container, false);
        Button b_scheduleSession = rootView.findViewById(R.id.ScheduleASessionButton);
        Button b_checkIn = rootView.findViewById(R.id.CheckInToQRCButton);
        Button b_writeEvaluation = rootView.findViewById(R.id.WriteEvaluationButton);
        Button b_studyCorner = rootView.findViewById(R.id.StudyCornerButton);
        tv_helloStudent = rootView.findViewById(R.id.HelloStudent);



        greetStudent(mAuth.getCurrentUser());

        b_scheduleSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleSessionActivity.class);
                getActivity().startActivity(intent);
            }
        });

        b_checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckInActivity.class);
                startActivity(intent);
            }
        });

        b_writeEvaluation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.stlawu.edu/pqrc/form/evaluation"));
                startActivity(browserIntent);

            }
        });

        b_studyCorner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudyCornerActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return rootView;

    }

    public void greetStudent(final FirebaseUser specificUser){
        DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(specificUser.getUid())){
                        String greeting = "Hello, " + ds.child("firstName").getValue(String.class) +"!";
                        tv_helloStudent.setText(greeting);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentFragment.OnFragmentInteractionListener) {
            mListener = (StudentFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }





}
