package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ScheduleSessionFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private String dayOfWeek;

    public ScheduleSessionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ScheduleSessionFragment newInstance(String param1, String param2) {
        ScheduleSessionFragment fragment = new ScheduleSessionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_schedule_session, container, false);

        final Spinner classSpinner = rootView.findViewById(R.id.classSpinner);
        final Spinner professorSpinner = rootView.findViewById(R.id.professorSpinner);
        final CalendarView calender = rootView.findViewById(R.id.calenderView);


        calender.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


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

                System.out.println(year +","+ month+","+dayOfMonth);
                System.out.println(dayOfWeek);

                // Write a message to the database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Schedule");

                // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //String value = dataSnapshot.getValue(String.class);
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            if(ds.getKey().equals(dayOfWeek)){
                                for(DataSnapshot dataSnap: ds.getChildren()) {
                                    if(dataSnap.getKey().equals("10-11pm")){
                                        for(DataSnapshot dataSnapshot1: dataSnap.getChildren()){
                                            String mentor = dataSnapshot1.getValue(String.class);
                                            System.out.println(mentor);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        System.out.println("here");
                    }
                });
            }
        });




        //create a list of items for the spinner.
        String[] professors = new String[]{"Maegan Bos", "Jessica Chapman","Dan Cryster","Jim Defranza","Dante Giarusso","Robert Haney","Ed Harcourt", "Natasha Komarov","Choong-Soo Lee","Patti Frazer Lock","Robin Lock","Daniel Look","Duncan Melville", "Ivan Ramler","Michael Schuckers","Lisa Torrey","Other"};
        String[] classes = new String[]{"Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2",
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





        return rootView;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
