package com.example.remileblanc.qrc_cs450_finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudyCornerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudyCornerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyCornerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String dayOfWeek;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean avail = false;
    private boolean here = false;

    private String userName;

    Button findAssignment;

    Spinner classSpinner;
    Spinner professorSpinner;
    Spinner assignmentSpinner;

    String[] professors;
    String[] classes;

    List<String> assignments = new ArrayList<String>();

    private int hour;
    private int min;

    public StudyCornerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyCornerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyCornerFragment newInstance(String param1, String param2) {
        StudyCornerFragment fragment = new StudyCornerFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_study_corner, container, false);

        getName(mAuth.getCurrentUser());

        classSpinner = rootView.findViewById(R.id.studyCoursesSpinner);
        professorSpinner = rootView.findViewById(R.id.studyProfessorsSpinner);
        assignmentSpinner = rootView.findViewById(R.id.studyAssignmentsSpinner);
        CalendarView calendar = rootView.findViewById(R.id.calenderStudyCorner);
        TimePicker timePicker = rootView.findViewById(R.id.timePickerStudyCorner);
        Button submitButton = rootView.findViewById(R.id.submitStudyCornerButton);
        findAssignment = rootView.findViewById(R.id.findPostedAssignmentButton);





        professors = new String[]{"--","Maegan Bos", "Jessica Chapman","Dan Cryster","Jim Defranza","Dante Giarusso","Robert Haney","Ed Harcourt", "Natasha Komarov","Choong-Soo Lee","Patti Frazer Lock","Robin Lock","Daniel Look","Duncan Melville", "Ivan Ramler","Michael Schuckers","Lisa Torrey","Other"};
        classes = new String[]{"--","Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2",
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

        ArrayAdapter<String> professorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, professors);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,classes);

        professorSpinner.setAdapter(professorAdapter);
        classSpinner.setAdapter(classAdapter);

        findAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                assignments.clear();

                final String selectedProfessor = professorSpinner.getSelectedItem().toString();
                final String selectedClass = classSpinner.getSelectedItem().toString();

                DatabaseReference assignmentsReference = database.getReference("assignments");
                assignmentsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot course : dataSnapshot.getChildren()){
                            for(DataSnapshot professor: course.getChildren()){
                                for(DataSnapshot title : professor.getChildren()){
                                    if(selectedProfessor.equals(title.child("professor").getValue().toString())&&selectedClass.equals(title.child("course").getValue().toString())){
                                        addAssignment(title.child("title").getValue().toString());
                                        System.out.println(title.child("title").getValue().toString());
                                    }
                                }

                            }
                        }
                        System.out.println(assignments);
                        checkAssignmentsEmpty();
                        ArrayAdapter<String> assignmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,assignments);
                        assignmentSpinner.setAdapter(assignmentAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int dofm) {
                year = y;
                month = m;
                dayOfMonth = dofm;
                getDayOfWeek(year, month, dayOfMonth);
                here = true;

            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String chosenProfessor = professorSpinner.getSelectedItem().toString();
                        String chosenClass = classSpinner.getSelectedItem().toString();
                        String chosenAssignment = assignmentSpinner.getSelectedItem().toString();
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(chosenAssignment.equals("There are no asssignments posted for this professor and this class.")){
                                    Toast.makeText(getContext(), "You must choose a valid assignment.", Toast.LENGTH_LONG).show();
                                }else{
                                    writeNewSubmission(chosenProfessor,chosenClass,chosenAssignment,null,null,true);
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                if(chosenAssignment.equals("There are no asssignments posted for this professor and this class.")){
                                    Toast.makeText(getContext(), "You must choose a valid assignment.", Toast.LENGTH_LONG).show();
                                }else{
                                    writeNewSubmission(chosenProfessor,chosenClass,chosenAssignment,null,null,false);
                                    Intent intent = new Intent(getActivity(), StudentActivity.class);
                                    getActivity().startActivity(intent);
                                }
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Would you like to share when you are studying with other students? If you choose Yes, other students will be able to see when you are " +
                        "studying and you can see when they are studying. If you choose No, other students will not be able to see when you are studying and you will" +
                        "not be able to see when they are studying.").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return rootView;
    }

    public void writeNewSubmission(String aProfessor,String aCourse, String anAssignment, String aTime, String aDate, boolean aShareWithOthers) {
        StudyCornerSubmission submission = new StudyCornerSubmission(userName, aProfessor, aCourse, anAssignment, aTime, aDate,aShareWithOthers);
        mDatabase.child("studySubmissions").child(userName).child(anAssignment).setValue(submission);
    }

    private void addAssignment(String assignment){
        assignments.add(assignment);
    }

    private void checkAssignmentsEmpty(){
        if(assignments.isEmpty()){
            assignments.add("There are no asssignments posted for this professor and this class.");
        }
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

    public void getDayOfWeek(int year, int month, int dayOfMonth){
        month = month+1;
        // First convert to Date. This is one of the many ways.
        String dateString = String.format("%d-%d-%d", year, month, dayOfMonth);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Then get the day of week from the Date based on specific locale.
        dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
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
