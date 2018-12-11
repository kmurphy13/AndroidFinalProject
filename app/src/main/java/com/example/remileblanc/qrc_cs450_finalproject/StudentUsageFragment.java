package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentUsageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentUsageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentUsageFragment extends Fragment {
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

    private ScrollView studentUsageScrollView;
    private TextView studentUsageData;
    private TextView totalVisits;



    FirebaseUser currentUser;
    private String userName;

    public StudentUsageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentUsageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentUsageFragment newInstance(String param1, String param2) {
        StudentUsageFragment fragment = new StudentUsageFragment();
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

        final View rootView = inflater.inflate(R.layout.fragment_student_usage, container, false);

        studentUsageScrollView = rootView.findViewById(R.id.studentUsageScrollView);
        studentUsageData = rootView.findViewById(R.id.studentUsageData);
        totalVisits = rootView.findViewById(R.id.totalVisits);

        currentUser = mAuth.getCurrentUser();
        getName(currentUser);



        DatabaseReference checkIns = database.getReference("checkIns");
        checkIns.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 1;
                for(DataSnapshot course : dataSnapshot.getChildren()){
                    for(DataSnapshot time : course.getChildren()){
                        if((time.child("professor").getValue()).equals(userName)){
                            String specificDate = time.getKey().substring(0,10);
                            String specificTime = time.getKey().substring(11,16);
                            String data ="- "+time.child("name").getValue()+" came to the QRC on "+specificDate+" at "+specificTime+" for "+time.child("course").getValue();
                            studentUsageData.append(data);
                            studentUsageData.append("\n\n");
                            count ++;

                        }
                    }
                }
                totalVisits.setText("Total Visits for your Students: "+(count-1));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        return rootView;
    }

    public void getName(final FirebaseUser specificUser){
        final DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(specificUser.getUid())){
                        userName = ds.child("firstName").getValue() + " " + ds.child("lastName").getValue();
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
