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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckInItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckInItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private OnFragmentInteractionListener mListener;
    List<String> items = new ArrayList<String>();

    public CheckInItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckInItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInItemFragment newInstance(String param1, String param2) {
        CheckInItemFragment fragment = new CheckInItemFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_check_in_item, container, false);

        final Spinner checkInItemSpinner = rootView.findViewById(R.id.checkInItemSpinner);
        Button submitCheckInItem = rootView.findViewById(R.id.submitCheckInButton);


        DatabaseReference checkedOutItems = database.getReference("checkedOutItems");
        checkedOutItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    for(DataSnapshot id: item.getChildren()){
                        String anID = id.child("ID").getValue().toString();
                        String anItem = id.child("item").getValue().toString();
                        String anAdditionalInfo = id.child("additionalInfo").getValue().toString();
                        String checkedOutItem = anID +", "+ anItem+", "+anAdditionalInfo;
                        addItem(checkedOutItem);
                    }
                }
                checkItemsEmpty();
                ArrayAdapter<String> assignmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);
                checkInItemSpinner.setAdapter(assignmentAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitCheckInItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItemSpinner = checkInItemSpinner.getSelectedItem().toString();
                List<String> infoList = Arrays.asList(selectedItemSpinner.split(","));
                final String selectedId = infoList.get(0).replaceAll("\\s+","");
                final String selectedItem = infoList.get(1).replaceAll("\\s+","");
                final String selectedInfo = infoList.get(2).substring(1);
                System.out.println(selectedId + selectedInfo +selectedItem);
                DatabaseReference checkedOutItems = database.getReference("checkedOutItems");
                checkedOutItems.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            for(DataSnapshot id: item.getChildren()){
                                System.out.println(id.child("ID").getValue().toString()+selectedId);
                                System.out.println(id.child("item").getValue().toString()+selectedItem);
                                System.out.println(id.child("additionalInfo").getValue().toString()+selectedInfo);

                                if(id.child("ID").getValue().toString().equals(selectedId)&&id.child("item").getValue().toString().equals(selectedItem)&&id.child("additionalInfo").getValue().toString().equals(selectedInfo)){
                                    id.getRef().removeValue();
                                    Toast.makeText(getContext(), "You have successfully checked in this item.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), MentorActivity.class);
                                    getActivity().startActivity(intent);
                                }
                            }
                        }
                        checkItemsEmpty();
                        ArrayAdapter<String> assignmentAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);
                        checkInItemSpinner.setAdapter(assignmentAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



        return rootView;
    }

    private void addItem(String assignment){
        items.add(assignment);
    }

    private void checkItemsEmpty(){
        if(items.isEmpty()){
            items.add("There are no items that are checked out.");
        }
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
