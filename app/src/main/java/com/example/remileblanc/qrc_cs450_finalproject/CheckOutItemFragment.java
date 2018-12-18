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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckOutItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckOutItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOutItemFragment extends Fragment {
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

    public CheckOutItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckOutItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckOutItemFragment newInstance(String param1, String param2) {
        CheckOutItemFragment fragment = new CheckOutItemFragment();
        Bundle args = new Bundle();
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
        View rootView = inflater.inflate(R.layout.fragment_check_out_item, container, false);
        final Spinner checkOutItemSpinner = rootView.findViewById(R.id.checkOutItemSpinner);
        Button submitCheckOut = rootView.findViewById(R.id.submitCheckOutButton);
        final EditText additionalInfo = rootView.findViewById(R.id.checkOutItemInformation);
        final EditText SLUid = rootView.findViewById(R.id.checkOutItemSLUID);

        String[] items = new String[]{"--","Calculator","Textbook"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        checkOutItemSpinner.setAdapter(itemAdapter);

        submitCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = SLUid.getText().toString();
                String additionalInformation = additionalInfo.getText().toString();
                String item = checkOutItemSpinner.getSelectedItem().toString();
                checkOutItem(item, additionalInformation,ID);
                Toast.makeText(getContext(), "You have successfully checked out this item.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MentorActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return rootView;
    }

    public void checkOutItem(String anItem, String anAdditionalInformation, String anID) {
        CheckOutItem checkOut = new CheckOutItem(anItem, anAdditionalInformation, anID);
        mDatabase.child("checkedOutItems").child(anItem).child(anID).setValue(checkOut);
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
