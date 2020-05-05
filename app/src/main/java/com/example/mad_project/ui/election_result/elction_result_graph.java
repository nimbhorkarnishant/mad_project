package com.example.mad_project.ui.election_result;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mad_project.R;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link elction_result_graph.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link elction_result_graph#newInstance} factory method to
 * create an instance of this fragment.
 */
public class elction_result_graph extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BarChart barChart;
    String year_candi,block_candi,dept_candi,pos_candi;
    DatabaseReference reff;
    ArrayList<elected_candi_obj> candidate_elected_list;
    ArrayList<String> name_of_elected_candi=new ArrayList<String>();
    String voting_status;


    private OnFragmentInteractionListener mListener;

    public elction_result_graph() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment elction_result_graph.
     */
    // TODO: Rename and change types and number of parameters
    public static elction_result_graph newInstance(String param1, String param2) {
        elction_result_graph fragment = new elction_result_graph();
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
        View view =inflater.inflate(R.layout.fragment_elction_result_graph, container, false);

        barChart =view.findViewById(R.id.barChart);

        year_candi = getArguments().getString("year");
        dept_candi =  getArguments().getString("dept");
        block_candi = getArguments().getString("block");
        pos_candi =   getArguments().getString("post");
        if (year_candi.equals("Select Year") || dept_candi.equals("Select Department")){
            Toast.makeText(getContext(), "This Fields are required!", Toast.LENGTH_LONG).show();
        }
        else{
            reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election");
            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    candidate_elected_list=new ArrayList<elected_candi_obj>();
                    int count = (int) dataSnapshot.getChildrenCount();
                    System.out.println("------------------------------- database -----------------------------");
                    System.out.println(count);
                    for (DataSnapshot ds1:dataSnapshot.getChildren()){
                        String year_candi_test=ds1.child("candi_year").getValue().toString();
                        String dept_candi_test=ds1.child("candi_dept").getValue().toString();
                        String block_candi_test=ds1.child("candi_block").getValue().toString();
                        String pos_candi_test=ds1.child("candi_pos").getValue().toString();
                        String selection_status=ds1.child("selected_for_live_vote").getValue().toString();
                        System.out.println(selection_status);
                        System.out.println(pos_candi_test);
                        if (pos_candi.equals("CR")){
                            if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi)
                                    && block_candi_test.equals(block_candi) && pos_candi_test.equals(pos_candi)){
                                if (selection_status.equals("Yes")){
                                    System.out.println(pos_candi);
                                    String candidate_id=ds1.child("candidate_id").getValue().toString();
                                    String candidate_name=ds1.child("candi_name").getValue().toString();
                                    candidate_elected_list.add(new elected_candi_obj(candidate_id,candidate_name,0));
                                    System.out.println(candidate_elected_list);

                                }
                                else { }

                            }
                            else { }

                        }
                        else{
                            if ( dept_candi_test.equals(dept_candi) && pos_candi_test.equals(pos_candi)){

                                if (selection_status.equals("Yes")){
                                    String candidate_id=ds1.child("candidate_id").getValue().toString();
                                    String candidate_name=ds1.child("candidate_name").getValue().toString();
                                    candidate_elected_list.add(new elected_candi_obj(candidate_id,candidate_name,0));
                                }
                                else { }
                            }
                            else {

                            }

                        }

                    }
                    online_election_data();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }



        return view;
    }

    public void online_election_data(){
        DatabaseReference new_reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("online_voting");
        new_reff.addValueEventListener(new ValueEventListener() {
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
                    voting_status=ds1.child("voting_status").getValue().toString();
                    if (pos_candi.equals("CR")){
                        if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi)
                                && block_candi_test.equals(block_candi) && pos_candi_test.equals(pos_candi)){
                            for (int i=0;i<candidate_elected_list.size();i++){
                                Integer vote_count=ds1.child(candidate_elected_list.get(i).candidate_id).getValue(Integer.class);
                                candidate_elected_list.get(i).candidate_vote_count=vote_count;
                            }


                        }
                        else {}

                    }
                    else{
                        if ( dept_candi_test.equals(dept_candi) && pos_candi_test.equals(pos_candi)){
                            for (int i=0;i<candidate_elected_list.size();i++){
                                Integer vote_count=ds1.child(candidate_elected_list.get(i).candidate_id).getValue(Integer.class);
                                candidate_elected_list.get(i).candidate_vote_count=vote_count;
                            }

                        }
                        else {

                        }

                    }

                }
                bar_graph_display();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public void bar_graph_display(){
        BarDataSet barDataSet = new BarDataSet(getData(), "Candidates");
        barDataSet.setBarBorderWidth(0.9f);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // final String[] months = new String[]{"Sandesh", "Abhi", "Nishant", "Aditya", "Neha", "Satish"};
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(name_of_elected_candi);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(5000, 5000);
        barChart.invalidate();
    }
    private ArrayList getData(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> perc_vote_max=new ArrayList<>();
        int total_vote=0;
        System.out.println("count-->"+candidate_elected_list.size());
        for (int i=0;i<candidate_elected_list.size();i++){
            total_vote+=candidate_elected_list.get(i).candidate_vote_count;

        }
        for (int i=0;i<candidate_elected_list.size();i++){
            System.out.println("total votes-->"+total_vote);
            System.out.println(candidate_elected_list.get(i).candidate_vote_count);
            name_of_elected_candi.add(candidate_elected_list.get(i).candidate_name);
            int candi_vote=candidate_elected_list.get(i).candidate_vote_count;
            int prec_vote=(candi_vote*100)/total_vote;
            System.out.println(prec_vote);
            perc_vote_max.add(prec_vote);
            entries.add(new BarEntry(i, prec_vote));
        }
        if (perc_vote_max.size()==0){
            Toast.makeText(getContext(), "Sorry no election happend in this class or department!", Toast.LENGTH_LONG).show();
        }
        else {
            int max = Collections.max(perc_vote_max);
            int index=perc_vote_max.indexOf(max);
            String winner_name=name_of_elected_candi.get(index);
            if (voting_status.equals("off")){
                Toast.makeText(getContext(), winner_name+" is become a new "+pos_candi+" for your class!", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getContext(), winner_name+" is in lead !", Toast.LENGTH_LONG).show();

            }
        }

//        entries.add(new BarEntry(1, 80));
//        entries.add(new BarEntry(2, 60));
//        entries.add(new BarEntry(3, 50));
//        entries.add(new BarEntry(4, 70));
//        entries.add(new BarEntry(5, 60));
        return entries;
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
