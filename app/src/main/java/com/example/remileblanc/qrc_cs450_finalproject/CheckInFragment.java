package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CheckInFragment.OnFragmentInteractionListener mListener;

    public CheckInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);

        //Get Spinners
        Spinner classSpinner = rootView.findViewById(R.id.classSpinner);
        Spinner professorSpinner = rootView.findViewById(R.id.professorSpinner);

        //create a list of items for the spinner.
        String[] professors = new String[]{"Maegan Bos", "Jessica Chapman","Dan Cryster","Jim Defranza","Dante Giarusso","Robert Haney","Ed Harcourt",
                                        "Natasha Komarov","Choong-Soo Lee","Patti Frazer Lock","Robin Lock","Daniel Look","Duncan Melville",
                                        "Ivan Ramler","Michael Schuckers","Lisa Torrey","Other"};
        String[] classes = new String[]{"Concepts of Mathematics", "Mathematics and Art","PreCalculus","Calculus 1", "Calculus 2", "Multivariable Calculus",
                                            "Vector Calculus","Differential Equations", "Linear Algebra", "Mathematical Problem Solving","Bridge to Higher Math",
                                            "Symbolic Logic","Real Analysis", "Complex Analysis","Ring Theory", "Group Theory", "Graph Theory",
                                            "Geometry","Financial Mathematics","History of Mathematics","Probability", "Mathematical Methods of Physics",
                                            "Number Theory","Topology","Theory of Computation","Applied Statistics","Statistical Computing",
                                            "Applied Regression Analysis","Statistical Methods of Data Collection","Topics in Statistical Learning",
                                            "Mathematical Statistics","Econometrics","Time Series Analysis","Time Series Analysis","Intro to CS",
                                            "Techniques of Computer Science","Computer Organization","Data Structures","Computer Networking",
                                            "Web Programming","Software Engineering","Database Systems","Algorithm Analysis","Programming Analysis",
                                            "Operating Systems","Artificial Intelligence","Theory of Computation","Intro to Economics","Microeconomics", "Macroeconomics",
                                            "Quantitative Methods","Financial Accounting","Intro to Physics","Intro to Bio","Intro to Chem","Intro to Psych","Intro to Philosophy",
                                            "Reasoning"};


        ArrayAdapter<String> professorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, professors);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,classes);

        professorSpinner.setAdapter(professorAdapter);
        classSpinner.setAdapter(classAdapter);

        


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
