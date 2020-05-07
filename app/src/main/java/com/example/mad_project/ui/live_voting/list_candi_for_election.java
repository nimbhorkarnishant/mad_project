package com.example.mad_project.ui.live_voting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.faculty_access.register_candi_faculty_access;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link list_candi_for_election.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link list_candi_for_election#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list_candi_for_election extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button button_show_candi;
    TextView tv_mesg;
    private selected_candi_adpter adpater;
    ArrayList<selected_candi_obj> register_candi_list_selected;
    String year_candi,block_candi,dept_candi,pos_candi;
    DatabaseReference reff;
    boolean flag;
    boolean st=false;
    String parent,vote_status;

    private OnFragmentInteractionListener mListener;

    public list_candi_for_election() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list_candi_for_election.
     */
    // TODO: Rename and change types and number of parameters
    public static list_candi_for_election newInstance(String param1, String param2) {
        list_candi_for_election fragment = new list_candi_for_election();
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
        View view= inflater.inflate(R.layout.fragment_list_candi_for_election, container, false);
        start_election();
        register_candi_list_selected=new ArrayList<>();

         year_candi = getArguments().getString("year");
         dept_candi =  getArguments().getString("dept");
         block_candi = getArguments().getString("block");
         pos_candi =   getArguments().getString("post");

        button_show_candi=view.findViewById(R.id.button_show_candi);
        tv_mesg=view.findViewById(R.id.election_status_text);
        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("user_detail",MODE_PRIVATE);
        String user_access=sharedPreferences.getString("user_access","");


        final ListView listView=view.findViewById(R.id.list_of_candi);
        adpater=new selected_candi_adpter(getContext(),register_candi_list_selected);
        listView.setAdapter(adpater);
        show_candidate();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected=register_candi_list_selected.get(position).candidate_id;
                Fragment fragment=new register_candi_faculty_access();
                Bundle bundle=new Bundle();
                bundle.putString("candidate_id_det",selected );
                bundle.putSerializable("candi_obj",register_candi_list_selected.get(position)); ; // Key, value

                final Fragment frag=new candidate_info_election();
                Fragment f1=getParentFragmentManager().findFragmentById(R.id.nav_host_fragment);
                frag.setArguments(bundle);
                FragmentTransaction transaction =getParentFragmentManager().beginTransaction();
                transaction.add(R.id.nav_host_fragment, frag);
                transaction.addToBackStack(null);
                transaction.remove(f1);
                transaction.commit();
                //Toast.makeText(getContext(), "Show detail!", Toast.LENGTH_LONG).show();
            }
        });


        if (!user_access.equals("faculty")){
            button_show_candi.setVisibility(View.GONE);
        }
        else {}
        button_show_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                toggle_button();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Are you sure to Change voting Status?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        return view;
    }

    public void start_election(){
        System.out.println("------------------- cooming to start election ---------------- ");
        reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");
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
                    if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi)
                            && block_candi_test.equals(block_candi) && pos_candi_test.equals(pos_candi)){
                        flag=true;
                        vote_status=ds1.child("voting_status").getValue().toString();
                        parent=ds1.getKey();
                        if (vote_status.equals("on")){
                            button_show_candi.setText("Stop Live Voting");
                            tv_mesg.setText("Live Voting has been Started for This Class Go and Vote For Your Candidate");
                            tv_mesg.setTextColor(Color.GREEN);
                        }
                        else{
                            button_show_candi.setText("Start Live Voting");
                            tv_mesg.setText("Live Voting is OFF FOR This Class");
                            tv_mesg.setTextColor(Color.RED);

                        }
                        break;
                    }
                    else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
    public void toggle_button(){
        System.out.println("------------------- cooming to start election ---------------- ");
        reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");
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
                    if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi)
                            && block_candi_test.equals(block_candi) && pos_candi_test.equals(pos_candi)){
                        flag=true;
                        vote_status=ds1.child("voting_status").getValue().toString();
                        parent=ds1.getKey();
                        break;
                    }
                    else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        if (flag){
            if (vote_status.equals("off")){
                reff.child(parent).child("voting_status").setValue("on");
                button_show_candi.setText("Stop Live Voting");
                tv_mesg.setText("Live Voting has been Started for This Class Go and Vote For Your Candidate");
                tv_mesg.setTextColor(Color.GREEN);
            }
            else{
                reff.child(parent).child("voting_status").setValue("off");
                button_show_candi.setText("Start Live Voting");
                tv_mesg.setText("Live Voting is OFF FOR This Class");
                tv_mesg.setTextColor(Color.RED);

            }
        }
        else {}

    }
    public void show_candidate(){
        System.out.println("----------------- this is show candid ------------------------------");

        DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println(count);
                System.out.println("heyyhhhhhhhhhhhhhhhhhhhhhh");
                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    try {
                        String year_candi_test=ds1.child("candi_year").getValue().toString();
                        String dept_candi_test=ds1.child("candi_dept").getValue().toString();
                        String block_candi_test=ds1.child("candi_block").getValue().toString();
                        String pos_candi_test=ds1.child("candi_pos").getValue().toString();
                        String selected_as_candi= ds1.child("selected_for_live_vote").getValue().toString();
                        System.out.println("kkkk"+"  "+selected_as_candi);
                        if (pos_candi.equals("CR")){
                            if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi) && block_candi_test.equals(block_candi) &&
                                    pos_candi_test.equals(pos_candi) && selected_as_candi.equals("Yes"))
                            {
                                register_candi_list_selected.add(new selected_candi_obj(ds1.child("candi_name").getValue().toString(),ds1.child("candi_prn_no").getValue().toString(),ds1.child("candi_email").getValue().toString(),
                                        ds1.child("candi_dob").getValue().toString(),ds1.child("candi_dept").getValue().toString(),ds1.child("candi_year").getValue().toString(),
                                        ds1.child("candi_block").getValue().toString(),ds1.child("candidate_id").getValue().toString(),ds1.child("candi_pos").getValue().toString(),ds1.child("resume_link").getValue().toString()));
                            }
                        }
                        else {
                            if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi) &&
                                    pos_candi_test.equals(pos_candi) && selected_as_candi.equals("Yes"))
                            {
                                register_candi_list_selected.add(new selected_candi_obj(ds1.child("candi_name").getValue().toString(),ds1.child("candi_prn_no").getValue().toString(),ds1.child("candi_email").getValue().toString(),
                                        ds1.child("candi_dob").getValue().toString(),ds1.child("candi_dept").getValue().toString(),ds1.child("candi_year").getValue().toString(),
                                        ds1.child("candi_block").getValue().toString(),ds1.child("candidate_id").getValue().toString(),ds1.child("candi_pos").getValue().toString(),ds1.child("resume_link").getValue().toString()));
                            }

                        }
                    }
                    catch (Exception e){

                    }
                    finally {

                    }



                }
                if (register_candi_list_selected.size()==0){
                     Toast.makeText(getContext(), "No one is Selected yet in this section!", Toast.LENGTH_LONG).show();

                }
                else{
                    adpater.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
