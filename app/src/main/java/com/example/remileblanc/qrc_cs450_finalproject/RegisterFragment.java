package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.CheckBox;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button registerButton;

    private EditText newFirstName;
    private EditText newLastName;
    private EditText newEmail;
    private EditText newPassword;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String userType;


    public RegisterFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        final CheckBox student = rootView.findViewById(R.id.checkbox_student);
        final CheckBox mentor = rootView.findViewById(R.id.checkbox_mentor);
        final CheckBox professor = rootView.findViewById(R.id.checkbox_professor);


        registerButton = rootView.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                newFirstName = rootView.findViewById(R.id.newFirstName);
                firstName = newFirstName.getText().toString();
                newLastName = rootView.findViewById(R.id.newLastName);
                lastName = newLastName.getText().toString();

                newEmail = rootView.findViewById(R.id.newEmail);
                email = newEmail.getText().toString();

                newPassword = rootView.findViewById(R.id.newPassword);
                password = newPassword.getText().toString();

                userType = ((RegisterActivity) getActivity()).getUserType();


                if(email == null || password == null || firstName == null|| lastName == null || userType == null){
                    Toast.makeText(getContext(), "Please enter all required fields", Toast.LENGTH_LONG).show();
                }
                else {
                    createAccount(email, password);
                }

            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (student.isChecked()) {
                    // the user is a student
                    student.setChecked(true);
                    mentor.setChecked(false);
                    professor.setChecked(false);
                } else {
                    student.setChecked(false);
                }
            }

        });

        mentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mentor.isChecked()) {
                    // the user is a student
                    student.setChecked(false);
                    mentor.setChecked(true);
                    professor.setChecked(false);
                } else {
                    mentor.setChecked(false);
                }
            }
        });


        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (professor.isChecked()) {
                    // the user is a student
                    student.setChecked(false);
                    mentor.setChecked(false);
                    professor.setChecked(true);
                } else {
                    professor.setChecked(false);
                }
            }

        });

        return rootView;

    }

    public void writeNewUser(String aUserID, String aFirstName, String aLastName, String aEmail, String aUserType) {
        User user = new User(aUserID,aEmail, aFirstName, aLastName, aUserType);
        mDatabase.child("users").child(aUserID).setValue(user);

    }

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

    public void createAccount(String aEmail, String aPassword) {

        mAuth.createUserWithEmailAndPassword(aEmail, aPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    userType = ((RegisterActivity) getActivity()).getUserType();
                    writeNewUser(user.getUid(), firstName, lastName, email, userType);
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getContext(), "Cannont create new account.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateUI(FirebaseUser user) {

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
}
