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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PostEventFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private String contactPersonString;
    private String contactInfoString;
    private String titleString;
    private String locationString;
    private String date = "";
    private String time = "";
    private String additionalInfoString;
    private String dayOfWeek;
    private String userType;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public PostEventFragment() {
        // Required empty public constructor
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
        final View rootView = inflater.inflate(R.layout.fragment_post_event, container, false);

        final EditText contactPerson = rootView.findViewById(R.id.contactInfo);
        final EditText contactInfo = rootView.findViewById(R.id.contactPerson);
        final EditText title = rootView.findViewById(R.id.title);
        final EditText location = rootView.findViewById(R.id.location);
        final CalendarView calendarView = rootView.findViewById(R.id.eventCalenderView);
        final TimePicker timePicker = rootView.findViewById(R.id.timePicker);
        final EditText additionalInfo = rootView.findViewById(R.id.additionalInfo);
        final Button submitButton = rootView.findViewById(R.id.submitButton);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        getDayOfWeek(year, month, dayOfMonth);
        createDate(dayOfWeek, month, dayOfMonth, year);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        time = dateFormat.format(cal);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int y, int m, int dofm) {
                getDayOfWeek(y, m, dofm);
                createDate(dayOfWeek, m, dofm, y);

            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                if(minute<10){
                    String m = String.valueOf(minute);
                    m = "0"+m;
                    time = hourOfDay+":"+m;
                } else {
                    time = hourOfDay+":"+minute;
                }

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactPersonString = contactPerson.getText().toString();
                contactInfoString = contactInfo.getText().toString();
                titleString = title.getText().toString();
                locationString = location.getText().toString();
                additionalInfoString = additionalInfo.getText().toString();

                if(contactPersonString.equals("") || contactInfoString.equals("") || titleString.equals("") || locationString.equals("") ||  time.equals("") || date.equals("")){
                    Toast.makeText(getContext(), "Please enter all required fields.", Toast.LENGTH_LONG).show();
                } else {

                    final DatabaseReference users = database.getReference("users");
                    final FirebaseUser specificUser = mAuth.getCurrentUser();
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                if(ds.getKey().equals(specificUser.getUid())){
                                    userType = ds.child("userType").getValue().toString();
                                }
                            }
                            writeNewEvent(contactPersonString, contactInfoString, titleString, locationString, date, time, additionalInfoString);
                            Toast.makeText(getContext(), "You have successfully created an event.", Toast.LENGTH_LONG).show();
                            switch (userType){
                                case "Student":
                                    Intent intent1 = new Intent(getActivity(), StudentActivity.class);
                                    getActivity().startActivity(intent1);
                                    break;
                                case "Mentor":
                                    Intent intent2 = new Intent(getActivity(), MentorActivity.class);
                                    getActivity().startActivity(intent2);
                                    break;
                                case "Professor":
                                    Intent intent3 = new Intent(getActivity(), ProfessorActivity.class);
                                    getActivity().startActivity(intent3);
                                    break;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return rootView;
    }

    public void writeNewEvent(String cp, String ci, String aTitle, String l, String d, String aTime, String ai) {
        Event event = new Event(cp, ci, aTitle, l, d, aTime, ai);
        mDatabase.child("Events").child(aTitle).setValue(event);
    }

    public void createDate(String dofw, int month, int dofm, int year){
        month = month+1;
        String m = String.valueOf(month);
        String y = String.valueOf(year);
        if(dofm<10){
            String dom = String.valueOf(dofm);
            dom = "0"+dom;
            date = dayOfWeek+", "+ m+"-"+dom+"-"+y;

        } else {
            date = dayOfWeek + ", " + m+ "-" + dofm + "-" + y;
        }
    }

    public void getDayOfWeek(int year, int month, int dayOfMonth){
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
