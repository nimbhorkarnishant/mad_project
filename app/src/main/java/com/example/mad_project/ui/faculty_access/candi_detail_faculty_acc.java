package com.example.mad_project.ui.faculty_access;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.user_announcement.update_post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link candi_detail_faculty_acc.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link candi_detail_faculty_acc#newInstance} factory method to
 * create an instance of this fragment.
 */
public class candi_detail_faculty_acc extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<register_candi_obj> register_candi_list_new;
    private OnFragmentInteractionListener mListener;
    TextView candi_name_text,candi_prn_no_text,candi_email,candi_dob,candi_year,candi_dept,candi_block,candi_pos,candi_id_tv;
    Button button_update_candi;
    String candi_id_detail;
    String candi_email_id,candi_pos_email,year_test,dept_test,block_test,position_test;
    String voting_id;
    Boolean flag=false;
    int count_vot_obj;
    Button see_resume;
    public candi_detail_faculty_acc() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment candi_detail_faculty_acc.
     */
    // TODO: Rename and change types and number of parameters
    public static candi_detail_faculty_acc newInstance(String param1, String param2) {
        candi_detail_faculty_acc fragment = new candi_detail_faculty_acc();
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
        View v =inflater.inflate(R.layout.fragment_candi_detail_faculty_acc, container, false);
        register_candi_list_new=new ArrayList<>();
        candi_id_detail=getArguments().getString("candidate_id_det");
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
        see_resume=v.findViewById(R.id.resume_see_button);

        DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println("------------------------------- database -----------------------------");
                System.out.println(count);
                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    String id_candi_test=ds1.child("candidate_id").getValue().toString();
                    if (id_candi_test.equals(candi_id_detail)){
                        System.out.println("--- hello---");
                        register_candi_list_new.add(new register_candi_obj(ds1.child("candi_name").getValue().toString(),ds1.child("candi_prn_no").getValue().toString(),ds1.child("candi_email").getValue().toString(),
                               ds1.child("candi_dob").getValue().toString(),ds1.child("candi_dept").getValue().toString(),ds1.child("candi_year").getValue().toString(),
                                ds1.child("candi_block").getValue().toString(),ds1.child("candidate_id").getValue().toString(),ds1.child("candi_pos").getValue().toString(),ds1.child("resume_link").getValue().toString()));
                        break;

                    }
                    else {register_candi_list_new.clear();}

                }
                display_candi_data();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        registerForContextMenu(button_update_candi);
        return v;
    }
    public void  display_candi_data(){
        try {
            candi_name_text.setText("Candidate Name: "+register_candi_list_new.get(0).candi_name);
            candi_prn_no_text.setText("Candidate PRN NO:  "+register_candi_list_new.get(0).candi_prn_no);
            candi_email.setText(register_candi_list_new.get(0).candi_email);
            candi_dob.setText(register_candi_list_new.get(0).candi_dob);
            candi_year.setText(register_candi_list_new.get(0).candi_year);
            candi_dept.setText(register_candi_list_new.get(0).candi_dept);
            candi_block.setText(register_candi_list_new.get(0).candi_block);
            candi_pos.setText(register_candi_list_new.get(0).candi_pos);
            candi_id_tv.setText(register_candi_list_new.get(0).candidate_id);
            candi_email_id=register_candi_list_new.get(0).candi_email;
            candi_pos_email=register_candi_list_new.get(0).candi_pos;

            year_test=register_candi_list_new.get(0).candi_year;
            block_test=register_candi_list_new.get(0).candi_block;
            dept_test=register_candi_list_new.get(0).candi_dept;
            position_test=register_candi_list_new.get(0).candi_pos;

            see_resume.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent =new Intent();
                    intent.setType(Intent.ACTION_VIEW_LOCUS);
                    Uri link=Uri.parse(register_candi_list_new.get(0).resume_link);
                    System.out.println("here-- link "+link);
                    intent.setData(link);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){

        }
        finally {

        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.candi_update_menu,menu);

    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.accept_candi:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                try {
                                    FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election").
                                            child(candi_id_detail).child("selected_for_live_vote").setValue("Yes");
                                    adding_to_live_voting();
                                }
                                catch (Exception e){

                                }
                                finally {

                                }

                            break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Are you sure to accept? Once you accept he is candidate for election").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;

            case R.id.reject_candi:
               // System.out.println("position--->"+" "+menuInfo.position);
                DialogInterface.OnClickListener dialogClickListener1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String text_subject="Candidate Interview Result";
                                String text_content="By considering your Profile and your interview,u your Are not Selectd for live voting for" + candi_pos_email+ " Election ";
                                JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),candi_email_id,text_subject,text_content);
                                javaMailAPI.execute();
                               FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election").
                                        child(candi_id_detail).removeValue();
                                Toast.makeText(getContext(), "He is no more Candidate for election!", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab1 = new AlertDialog.Builder(getContext());
                ab1.setMessage("Are you sure to Reject? Once you Reject he is no more candidate for election").setPositiveButton("Yes", dialogClickListener1)
                        .setNegativeButton("No", dialogClickListener1).show();
                return true;

            case R.id.give_update_candi:
                //Send Mail
                Bundle bundle=new Bundle();
                bundle.putString("candidate_email_id", candi_email_id);
                final Fragment frag=new update_candi_via_email();
                frag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, frag);
                transaction.addToBackStack("frag_update_candi");
                transaction.commit();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void adding_to_live_voting(){
        final DatabaseReference reff_vote= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");
        final DatabaseReference reff_vote_finalize= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");

        reff_vote.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count_vot_obj = (int) dataSnapshot.getChildrenCount();
                System.out.println(count_vot_obj);
                System.out.println("heyyhhhhhhhhhhhhhhhhhhhhhh");
                try {
                    for (DataSnapshot ds1:dataSnapshot.getChildren()){
                        String year_candi_test=ds1.child("year").getValue().toString();
                        String dept_candi_test=ds1.child("dept").getValue().toString();
                        String block_candi_test=ds1.child("block").getValue().toString();
                        String pos_candi_test=ds1.child("position").getValue().toString();
                        if (position_test.equals("CR")){
                            if (year_test.equals(year_candi_test) && dept_test.equals(dept_candi_test) && block_test.equals(block_candi_test)
                                    && position_test.equals(pos_candi_test))
                            {      System.out.println("ya i got");
                                if (ds1.child(candi_id_detail).exists()){
                                    Toast.makeText(getContext(), "Candidate is Already added For live Vote!", Toast.LENGTH_LONG).show();
                                    flag=true;
                                    break;

                                }
                                else {
                                    reff_vote.child(ds1.getKey()).child(candi_id_detail).setValue(0);
                                    flag=true;
                                    String text_subject="Candidate Interview Result";
                                    String text_content="By considering your Profile and your interview,We are happy to say you, your Are Selectd for live voting for "+ candi_pos_email+ " Election ";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),candi_email_id,text_subject,text_content);
                                    javaMailAPI.execute();
                                    Toast.makeText(getContext(), "Candidate is Eligible For live Vote!", Toast.LENGTH_LONG).show();
                                    break;

                                }
                            }
                            else{
                                System.out.println("its CR");
                                flag=false;

                            }
                        }
                        else{
                            if (year_test.equals(year_candi_test) && dept_test.equals(dept_candi_test) && position_test.equals(pos_candi_test))
                            {      System.out.println("ya i got 2");
                                if (ds1.child(candi_id_detail).exists()){
                                    Toast.makeText(getContext(), "Candidate is Already added For live Vote!", Toast.LENGTH_LONG).show();
                                    flag=true;
                                    break;

                                }
                                else {
                                    reff_vote.child(ds1.getKey()).child(candi_id_detail).setValue(0);
                                    flag=true;
                                    String text_subject="Candidate Interview Result";
                                    String text_content="By considering your Profile and your interview,We are happy to say you, your Are Selectd for live voting for "+ candi_pos_email+ " Election ";
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),candi_email_id,text_subject,text_content);
                                    javaMailAPI.execute();
                                    Toast.makeText(getContext(), "Candidate is Eligible For live Vote!", Toast.LENGTH_LONG).show();
                                    break;

                                }
                            }
                            else{
                                System.out.println("its other");
                                flag=false;
                            }
                        }


                    }
                }
                catch(Exception e) {
                    //  Block of code to handle errors
                }
                finally {

                }
                if (!flag){
                    int new_count=count_vot_obj+1;
                    voting_id="voting_"+new_count;
                    System.out.println("Its Online voting-->"+count_vot_obj);
                    if (dataSnapshot.hasChild(voting_id)){
                        int new_count1=count_vot_obj+2;
                        voting_id="voting_"+new_count1;
                    }
                    System.out.println(voting_id);
                    reff_vote_finalize.child(voting_id).child("year").setValue(year_test);
                    reff_vote_finalize.child(voting_id).child("dept").setValue(dept_test);
                    reff_vote_finalize.child(voting_id).child("block").setValue(block_test);
                    reff_vote_finalize.child(voting_id).child("position").setValue(position_test);
                    reff_vote_finalize.child(voting_id).child("voting_status").setValue("off");
                    reff_vote_finalize.child(voting_id).child(candi_id_detail).setValue(0);
                    String text_subject="Candidate Interview Result";
                    String text_content="By considering your Profile and your interview,We are happy to say you, your Are Selectd for live voting for "+ candi_pos_email+ " Election ";
                    JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),candi_email_id,text_subject,text_content);
                    javaMailAPI.execute();
                }
                else {}
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
