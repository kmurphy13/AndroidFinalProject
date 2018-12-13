package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
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
    private String date = "";
    private String title;
    private String additionalInfoString;
    private String assignmentType;
    private String userName;
    private String course;

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
        final Spinner courseSpinner = rootView.findViewById(R.id.courseSpinner);
        final EditText assignmentTitle = rootView.findViewById(R.id.assignmentTitle);
        final EditText additionalInformation = rootView.findViewById(R.id.additionalInformation);
        CalendarView calendarView = rootView.findViewById(R.id.profCalenderView);
        Button submitAssignment = rootView.findViewById(R.id.submitAssignmentButton);

        String[] assignments = new String[]{"--","Exam","Quiz","Problem Set","Project","HW","Other"};
        String[] courses = new String[]{"--","Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2",
                "Multivariable Calculus","Vector Calculus","Differential Equations", "Linear Algebra", "Mathematical Problem Solving",
                "Bridge to Higher Math", "Symbolic Logic","Real Analysis", "Complex Analysis","Ring Theory", "Group Theory", "Graph Theory",
                "Geometry","Financial Mathematics","History of Mathematics","Probability", "Mathematical Methods of Physics", "Number Theory",
                "Topology","Theory of Computation","Applied Statistics","Statistical Computing", "Applied Regression Analysis",
                "Statistical Methods of Data Collection","Topics in Statistical Learning", "Mathematical Statistics","Econometrics",
                "Time Series Analysis","Time Series Analysis","Intro to CS", "Techniques of Computer Science","Computer Organization",
                "Data Structures","Computer Networking", "Web Programming","Software Engineering","Database Systems","Algorithm Analysis",
                "Programming Analysis", "Operating Systems","Artificial Intelligence","Theory of Computation","Intro to Economics",
                "Microeconomics", "Macroeconomics", "Quantitative Methods","Financial Accounting","Intro to Physics","Intro to Bio",
                "Intro to Chem","Intro to Psych","Intro to Philosophy", "Reasoning"};

        ArrayAdapter<String> assignmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, assignments);
        assignmentSpinner.setAdapter(assignmentAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, courses);
        courseSpinner.setAdapter(courseAdapter);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int d) {
                year = y;
                day = d;
                month = m+1;
                date = month+"-"+day+"-"+year;
            }
        });

        submitAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = courseSpinner.getSelectedItem().toString();
                assignmentType = assignmentSpinner.getSelectedItem().toString();
                title = assignmentTitle.getText().toString();
                additionalInfoString = additionalInformation.getText().toString();
                if(date.equals("")){
                    Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    month = month+1;
                    day = c.get(Calendar.DAY_OF_MONTH);
                    date = month+"-"+day+"-"+year;
                }
                if(course.equals("--") || assignmentType.equals("--") || title.equals("") || additionalInfoString.equals("")){
                    Toast.makeText(getContext(), "Please enter all the required fields.", Toast.LENGTH_LONG).show();
                } else {
                    writeNewAssignment(course, userName, assignmentType, title, date, additionalInfoString);
                    Toast.makeText(getContext(), "You have successfully posted your "+ title+", that is due on "+date, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), ProfessorActivity.class);
                    getActivity().startActivity(intent);
                }


            }
        });



        return rootView;
    }

    public void writeNewAssignment(String aCourse, String aProfessor,String aType, String aTitle, String aDate, String aAdditionalInfo) {
        Assignment assignment = new Assignment(aCourse, aProfessor,aType,aTitle,aDate,aAdditionalInfo);
        mDatabase.child("assignments").child(aCourse).child(aProfessor).child(aTitle).setValue(assignment);
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
