package com.example.mad_project.ui.live_voting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.faculty_access.JavaMailAPI;
import com.example.mad_project.ui.faculty_access.register_candi_obj;
import com.example.mad_project.ui.home.post_obj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link candidate_info_election.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link candidate_info_election#newInstance} factory method to
 * create an instance of this fragment.
 */
public class candidate_info_election extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView candi_name_text,candi_prn_no_text,candi_email,candi_dob,candi_year,candi_dept,candi_block,candi_pos,candi_id_tv;
    Button button_update_candi;
    String candi_id_detail;
    String candi_email_id,candi_pos_email;
    ArrayList<selected_candi_obj> register_candi_list_new;
    selected_candi_obj object_candi;
    String parent_key;
    int candidate_vote_count;
    boolean flag_ex;



    private OnFragmentInteractionListener mListener;

    public candidate_info_election() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment candidate_info_election.
     */
    // TODO: Rename and change types and number of parameters
    public static candidate_info_election newInstance(String param1, String param2) {
        candidate_info_election fragment = new candidate_info_election();
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
        View v=inflater.inflate(R.layout.fragment_candidate_info_election, container, false);
        register_candi_list_new=new ArrayList<>();
     //   candi_id_detail=getArguments().getString("candidate_id_det");
        System.out.println(candi_id_detail);

        button_update_candi=v.findViewById(R.id.button_update_candi);
        candi_name_text=v.findViewById(R.id.candi_name_text);
        candi_prn_no_text=v.findViewById(R.id.candi_prn_no_text);
        candi_email=v.findViewById(R.id.candi_email);
        candi_dob=v.findViewById(R.id.candi_dob);
        candi_year=v.findViewById(R.id.candi_year);
        candi_dept=v.findViewById(R.id.candi_dept);
        candi_block=v.findViewById(R.id.candi_block);
        candi_pos=v.findViewById(R.id.candi_pos);
        candi_id_tv=v.findViewById(R.id.candi_id);

        object_candi=(selected_candi_obj)getArguments().getSerializable("candi_obj");

        DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println("------------------------------- database -----------------------------");
                System.out.println(count);
                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    String year_candi_test=ds1.child("year").getValue().toString();
                    String dept_candi_test=ds1.child("dept").getValue().toString();
                    String block_candi_test=ds1.child("block").getValue().toString();
                    String pos_candi_test=ds1.child("position").getValue().toString();
                    if (object_candi.candi_pos.equals("CR")){
                        if (year_candi_test.equals(object_candi.candi_year) && dept_candi_test.equals(object_candi.candi_dept)
                                && block_candi_test.equals(object_candi.candi_block)){
                            parent_key =ds1.getKey();
                            candidate_vote_count=ds1.child(object_candi.candidate_id).getValue(Integer.class);
                            if (ds1.child("voting_status").getValue().toString().equals("off")){
                                button_update_candi.setVisibility(View.GONE);
                            }
                            else{}
                        }
                        else {}
                    }
                    else {
                        if (year_candi_test.equals(object_candi.candi_year) && dept_candi_test.equals(object_candi.candi_dept)){
                            if (ds1.child("voting_status").getValue().toString().equals("off")){
                                button_update_candi.setVisibility(View.GONE);
                            }
                            else{}
                        }
                        else {}

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        display_candi_data();
        button_update_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                vote_for_candidate();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Are you sure to Vote This Candidate? ").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        return v;
    }
    public void  display_candi_data(){

        candi_name_text.setText("Candidate Name: "+object_candi.candi_name);
        candi_prn_no_text.setText("Candidate PRN NO:  "+object_candi.candi_prn_no);
        candi_email.setText(object_candi.candi_email);
        candi_dob.setText(object_candi.candi_dob);
        candi_year.setText(object_candi.candi_year);
        candi_dept.setText(object_candi.candi_dept);
        candi_block.setText(object_candi.candi_block);
        candi_pos.setText(object_candi.candi_pos);
        candi_id_tv.setText(object_candi.candidate_id);
        candi_email_id=object_candi.candi_email;
        candi_pos_email=object_candi.candi_pos;
    }

    public void vote_for_candidate(){
        String user_block="C";
        String user_dept="SCET";
        String user_year="TY";
        System.out.println("heyyhhhhhhhhhhhhhhhhhhhhhh");
        System.out.println(parent_key);
        DatabaseReference vote= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting").child(parent_key).child("users");

        if (object_candi.candi_pos.equals("CR")){
            if (object_candi.candi_block.equals(user_block) && object_candi.candi_dept.equals(user_dept) && object_candi.candi_year.equals(user_year)){
                System.out.println("dksklbklbklnlktbn");
                vote.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = (int) dataSnapshot.getChildrenCount();
                        System.out.println(count);
                        for (DataSnapshot ds1:dataSnapshot.getChildren()){
                           // System.out.println("------------------------------- database -----------------------------");
                            System.out.println(ds1.getValue().toString());
                            if (ds1.getValue().toString().equals("user_5")){
                                flag_ex=true;
                            }
                            else {
                                flag_ex=false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
        else {
            System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkk");
            if (object_candi.candi_block.equals(user_block) && object_candi.candi_dept.equals(user_dept) && object_candi.candi_year.equals(user_year)){
                System.out.println("dksklbklbklnlktbn");
                vote.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = (int) dataSnapshot.getChildrenCount();
                        System.out.println(count);
                        for (DataSnapshot ds1:dataSnapshot.getChildren()){
                            // System.out.println("------------------------------- database -----------------------------");
                            System.out.println(ds1.getValue().toString());
                            if (ds1.getValue().toString().equals("user_5")){
                                flag_ex=true;
                            }
                            else {
                                flag_ex=false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

        }
        if (!flag_ex){
            System.out.println(candidate_vote_count);
            FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting")
                    .child(parent_key).child(object_candi.candidate_id).setValue(candidate_vote_count+1);
            FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting")
                    .child(parent_key).child("users").child("user_5").setValue("user_5");
            Toast.makeText(getContext(), "Your Vote is Consider!", Toast.LENGTH_LONG).show();


        }
        else {
            Toast.makeText(getContext(),"You Already Voted !!",Toast.LENGTH_LONG).show();
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
