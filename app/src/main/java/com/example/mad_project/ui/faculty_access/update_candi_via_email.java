package com.example.mad_project.ui.faculty_access;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link update_candi_via_email.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link update_candi_via_email#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_candi_via_email extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView subject_email,messge_email;
    Button send_email;
    String sender_email_id;

    private OnFragmentInteractionListener mListener;

    public update_candi_via_email() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_candi_via_email.
     */
    // TODO: Rename and change types and number of parameters
    public static update_candi_via_email newInstance(String param1, String param2) {
        update_candi_via_email fragment = new update_candi_via_email();
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
        View v=inflater.inflate(R.layout.fragment_update_candi_via_email, container, false);
        subject_email=v.findViewById(R.id.email_subject);
        messge_email=v.findViewById(R.id.email_Content);
        send_email=v.findViewById(R.id.button_email);
        sender_email_id=getArguments().getString("candidate_email_id");


        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_email_data();
            }
        });


        return v;
    }
    public  void send_email_data(){
        String text_subject=subject_email.getText().toString();
        String text_content=messge_email.getText().toString();

        if (text_subject.length()==0 && text_content.length()==0){
            subject_email.setBackgroundResource(R.drawable.border_alert);
            messge_email.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "Field is required!", Toast.LENGTH_LONG).show();

        }
        else if (text_subject.length()==0){
            subject_email.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "Title is required!", Toast.LENGTH_LONG).show();


        }
        else if (text_content.length()==0){
            messge_email.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "post Discription is required!", Toast.LENGTH_LONG).show();
        }
        else {
            JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),sender_email_id,text_subject,text_content);
            javaMailAPI.execute();

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
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
