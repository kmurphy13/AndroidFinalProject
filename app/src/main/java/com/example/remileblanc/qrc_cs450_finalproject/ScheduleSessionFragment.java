package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ScheduleSessionFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private String dayOfWeek;
    private String shift;
    private String course;
    private String professor;
    private ArrayList<String> mentors;
    private ArrayList<String> qualMentors;
    private int year;
    private int month;
    private int dayOfMonth;
    private boolean avail = false;


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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_schedule_session, container, false);

        final Spinner classSpinner = rootView.findViewById(R.id.classSpinner);
        final Spinner professorSpinner = rootView.findViewById(R.id.professorSpinner);
        final CalendarView calender = rootView.findViewById(R.id.calenderView);
        final TimePicker timePicker = rootView.findViewById(R.id.timePicker);
        final Button submitButton = rootView.findViewById(R.id.submitButton);




        //create a list of items for the spinner.
        String[] professors = new String[]{"--","Maegan Bos", "Jessica Chapman","Dan Cryster","Jim Defranza","Dante Giarusso","Robert Haney","Ed Harcourt", "Natasha Komarov","Choong-Soo Lee","Patti Frazer Lock","Robin Lock","Daniel Look","Duncan Melville", "Ivan Ramler","Michael Schuckers","Lisa Torrey","Other"};
        String[] classes = new String[]{"--","Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2",
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


        calender.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int dofm) {
                year = y;
                month = m;
                dayOfMonth = dofm;
                getDayOfWeek(year, month, dayOfMonth);

            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                shift = getShift(hourOfDay);

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef0 = database.getReference("Schedule");
                DatabaseReference myRef1 = database.getReference("mentorClasses");


                course = classSpinner.getSelectedItem().toString();
                professor = professorSpinner.getSelectedItem().toString();
                mentors = new ArrayList<>();
                qualMentors = new ArrayList<>();


                int hourOfDay = timePicker.getHour();
                shift = getShift(hourOfDay);
                getDayOfWeek(year, month, dayOfMonth);


                if (shift.equals("closed")) {
                    Toast.makeText(getContext(), "The QRC is closed during your selected time.", Toast.LENGTH_LONG).show();
                } else if (dayOfWeek.equals("Saturday")) {
                    Toast.makeText(getContext(), "The QRC is closed on Saturdays.", Toast.LENGTH_LONG).show();

                } else {
                    // This is the get mentors the work during the selected shift
                    myRef0.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals(dayOfWeek)) {
                                    for (DataSnapshot dataSnap : ds.getChildren()) {
                                        if (dataSnap.getKey().equals(shift)) {
                                            for (DataSnapshot dataSnapshot1 : dataSnap.getChildren()) {
                                                String mentor = dataSnapshot1.getValue(String.class);
                                                mentors.add(mentor);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            System.out.println("Submit cancelled for some reason");
                        }

                    });

                    myRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals(course)) {
                                    for (DataSnapshot dataSnap : ds.getChildren()) {
                                        if (mentors.contains(dataSnap.getValue(String.class))) {
                                            String qualMentor = dataSnap.getValue(String.class);
                                            qualMentors.add(qualMentor);
                                        }
                                    }
                                }
                            }
                            if (mentors.size() > 0 && qualMentors.size() == 0) {
                                Toast.makeText(getContext(), "There are no mentors who have taken this course working during the time you selected.", Toast.LENGTH_LONG).show();
                                avail = false;
                            }
                            if (!qualMentors.isEmpty()){
                                avail = true;
                            }
                            getQualMentors();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(avail) {
                        Intent intent = new Intent(getActivity(), SelectMentorActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
            }

        });

       return rootView;

    }

    public String getShift(int hour){
        String shift = "init";
        switch (hour) {
            case 0:
                shift =  "closed";
                break;
            case 1:
                shift = "closed";
                break;
            case 2:
                shift = "closed";
                break;
            case 3:
                shift = "closed";
                break;
            case 4:
                shift = "closed";
                break;
            case 5:
                shift = "closed";
                break;
            case 6:
                shift = "closed";
                break;
            case 7:
                shift = "closed";
                break;
            case 8:
                shift = "closed";
                break;
            case 9:
                shift = "closed";
                break;
            case 10:
                shift = "10-11am";
                break;
            case 11:
                shift = "11-12";
                break;
            case 12:
                shift = "12-1";
                break;
            case 13:
                shift = "1-2";
                break;
            case 14:
                shift = "2-3";
                break;
            case 15:
                shift = "3-4";
                break;
            case 16:
                shift = "4-5";
                break;
            case 17:
                shift = "5-6";
                break;
            case 18:
                shift = "6-7";
                break;
            case 19:
                shift = "7-8";
                break;
            case 20:
                shift = "8-9";
                break;
            case 21:
                shift = "9-10";
                break;
            case 22:
                shift = "10-11pm";
                break;
            case 23:
                shift = "closed";
                break;
        }
        return shift;
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

    public void getQualMentors() {
        //Put the value
        SelectMentorFragment smf = new SelectMentorFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("Qual Mentors", qualMentors);
        smf.setArguments(args);

        //Inflate the fragment
        getFragmentManager().beginTransaction().add(R.id.fragment_schedule_session, smf).commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
