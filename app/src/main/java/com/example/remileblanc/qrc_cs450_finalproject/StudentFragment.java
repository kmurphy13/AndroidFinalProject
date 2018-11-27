package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class StudentFragment extends Fragment {


    private StudentFragment.OnFragmentInteractionListener mListener;



    public StudentFragment() {
        // Required empty public constructor
    }



    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_student, container, false);
        Button b_scheduleSession = rootView.findViewById(R.id.ScheduleASessionButton);
        Button b_checkIn = rootView.findViewById(R.id.CheckInToQRCButton);
        Button b_writeEvaluation = rootView.findViewById(R.id.WriteEvaluationButton);
        Button b_studyCorner = rootView.findViewById(R.id.StudyCornerButton);

        b_scheduleSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleSessionActivity.class);
                getActivity().startActivity(intent);
            }
        });

        b_checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        b_writeEvaluation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.stlawu.edu/pqrc/form/evaluation"));
                startActivity(browserIntent);

            }
        });

        b_studyCorner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudyCornerActivity.class);
                getActivity().startActivity(intent);
            }
        });


        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentFragment.OnFragmentInteractionListener) {
            mListener = (StudentFragment.OnFragmentInteractionListener) context;
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





}
