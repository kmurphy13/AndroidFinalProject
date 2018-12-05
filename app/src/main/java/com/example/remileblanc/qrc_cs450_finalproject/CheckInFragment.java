package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
 * {@link CheckInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CheckInFragment.OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String userName;

    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);

        // Get user's name
        getName(mAuth.getCurrentUser());

        // Get Spinners
        final Spinner classSpinner = rootView.findViewById(R.id.classSpinner);
        final Spinner professorSpinner = rootView.findViewById(R.id.professorSpinner);

        // Get Button
        Button submitButton = rootView.findViewById(R.id.checkInSubmitButton);

        //create a list of items for the spinner.
        String[] professors = new String[]{"--","Maegan Bos", "Jessica Chapman","Dan Cryster","Jim Defranza","Dante Giarusso","Robert Haney","Ed Harcourt",
                                        "Natasha Komarov","Choong-Soo Lee","Patti Frazer Lock","Robin Lock","Daniel Look","Duncan Melville",
                                        "Ivan Ramler","Michael Schuckers","Lisa Torrey","Other"};
        String[] classes = new String[]{"--","Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2", "Multivariable Calculus",
                                            "Vector Calculus","Differential Equations", "Linear Algebra", "Mathematical Problem Solving","Bridge to Higher Math",
                                            "Symbolic Logic","Real Analysis", "Complex Analysis","Ring Theory", "Group Theory", "Graph Theory",
                                            "Geometry","Financial Mathematics","History of Mathematics","Probability", "Mathematical Methods of Physics",
                                            "Number Theory","Topology","Theory of Computation","Applied Statistics","Statistical Computing",
                                            "Applied Regression Analysis","Statistical Methods of Data Collection","Topics in Statistical Learning",
                                            "Mathematical Statistics","Econometrics","Time Series Analysis","Time Series Analysis","Intro to CS",
                                            "Techniques of Computer Science","Computer Organization","Data Structures","Computer Networking",
                                            "Web Programming","Software Engineering","Database Systems","Algorithm Analysis","Programming Analysis",
                                            "Operating Systems","Artificial Intelligence","Theory of Computation","Intro to Economics","Microeconomics", "Macroeconomics",
                                            "Quantitative Methods","Financial Accounting","Intro to Physics","Intro to Bio","Intro to Chem","Intro to Psych","Intro to Philosophy",
                                            "Reasoning","Other"};


        ArrayAdapter<String> professorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, professors);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,classes);

        professorSpinner.setAdapter(professorAdapter);
        classSpinner.setAdapter(classAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedProfessor = professorSpinner.getSelectedItem().toString();
                String selectedClass = classSpinner.getSelectedItem().toString();
                String objective = ((CheckInActivity) getActivity()).getObjectiveType();
                System.out.println(selectedClass);
                System.out.println(selectedProfessor);
                writeNewCheckIn(userName,selectedProfessor,selectedClass,objective);

                Toast.makeText(getContext(), "You have successfully checked in to the QRC! Thank you.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), StudentActivity.class);
                getActivity().startActivity(intent);


            }
        });


        return rootView;
    }

    public void writeNewCheckIn(String aUserName,String aProfessor, String aCourse, String anObjective) {
        QRCCheckIn checkIn = new QRCCheckIn(aUserName,aProfessor,aCourse,anObjective);
        mDatabase.child("checkIns").child(aCourse).child(Calendar.getInstance().getTime().toString()).setValue(checkIn);
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
