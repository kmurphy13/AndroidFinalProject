package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    // Buttons
    private Button signInButton;
    private Button createAccountButton;
    private EditText enterEmail;
    private EditText enterPassword;

    private String stringEmail;
    private String stringPassword;
    private String stringUserType;

    private FirebaseUser aUser;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        createAccountButton = rootView.findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        View signInButton = rootView.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterEmail = rootView.findViewById(R.id.enterEmail);
                stringEmail = enterEmail.getText().toString();


                enterPassword = rootView.findViewById(R.id.enterPassword);
                stringPassword = enterPassword.getText().toString();
                if(stringEmail == null || stringEmail.equals("")){
                    Toast.makeText(getContext(), "Please enter your email.",Toast.LENGTH_SHORT).show();
                } else if (stringPassword == null || stringPassword.equals("")){
                    Toast.makeText(getContext(), "Please enter your password.",Toast.LENGTH_SHORT).show();
                } else {
                    signIn(stringEmail, stringPassword);
                }
            }
        });




        return rootView;
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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            aUser = mAuth.getCurrentUser();
                            System.out.println(aUser);
                            Log.d(TAG, "signInWithEmail:success");
                            updateUI(aUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            aUser=null;
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void updateUI(final FirebaseUser specificUser) {

        DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(specificUser.getUid())){
                        stringUserType = ds.child("userType").getValue(String.class);
                        openUserFragment(stringUserType);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void openUserFragment(String userType){
        switch (userType) {
            case "Student":
                Intent intent0 = new Intent(getActivity(), StudentActivity.class);
                getActivity().startActivity(intent0);
                break;
            case "Mentor":
                Intent intent1 = new Intent(getActivity(), MentorActivity.class);
                getActivity().startActivity(intent1);
                break;
            case "Professor":
                Intent intent2 = new Intent(getActivity(), ProfessorActivity.class);
                getActivity().startActivity(intent2);
                break;
        }

    }

    @Override
    public void onDestroy() {
        aUser = null;
        stringUserType = null;
        super.onDestroy();
    }
}
