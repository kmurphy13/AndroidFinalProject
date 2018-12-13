package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostAssignmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostAssignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAssignmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int year;
    private int day;
    private int month;
    private String date;
    private String title;
    private String additionalInfoString;
    private String assignmentType;
    private String userName;

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public PostAssignmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostAssignmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostAssignmentFragment newInstance(String param1, String param2) {
        PostAssignmentFragment fragment = new PostAssignmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_post_assignment, container, false);

        getName(mAuth.getCurrentUser());

        final Spinner assignmentSpinner = rootView.findViewById(R.id.assignmentSpinner);
        final EditText assignmentTitle = rootView.findViewById(R.id.assignmentTitle);
        final EditText additionalInformation = rootView.findViewById(R.id.additionalInformation);
        CalendarView calendarView = rootView.findViewById(R.id.profCalenderView);
        Button submitAssignment = rootView.findViewById(R.id.submitAssignmentButton);

        String[] classes = new String[]{"--","Exam","Quiz","Problem Set","Project","HW","Other"};


        ArrayAdapter<String> assignmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, classes);
        assignmentSpinner.setAdapter(assignmentAdapter);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int d) {
                year = y;
                day = d;
                month = m;
                date = m+"/"+d+"/"+y;
            }
        });

        submitAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignmentType = assignmentSpinner.getSelectedItem().toString();
                title = assignmentTitle.getText().toString();
                additionalInfoString = additionalInformation.getText().toString();
                writeNewAssignment(userName,assignmentType,title,date,additionalInfoString);
            }
        });



        return rootView;
    }

    public void writeNewAssignment(String aProfessor,String aType, String aTitle, String aDate, String aAdditionalInfo) {
        Assignment assignment = new Assignment(aProfessor,aType,aTitle,aDate,aAdditionalInfo);
        mDatabase.child("assignments").child(aProfessor).child(aTitle).setValue(assignment);
    }

    public void getName(final FirebaseUser specificUser){
        final DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(specificUser.getUid())){
                        userName = ds.child("firstName").getValue() + " " + ds.child("lastName").getValue();
                        System.out.println(userName);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
