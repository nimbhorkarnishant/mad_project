package com.example.mad_project.ui.faculty_access;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.home.post_adapter;
import com.example.mad_project.ui.home.post_obj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link register_candi_faculty_access.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link register_candi_faculty_access#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_candi_faculty_access extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<register_candi_obj> register_candi_list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  final String[] year = {"Select Year","FE", "SY", "TY","Btech"};
    private  final String[] department = {"Select Department","FE","SCET", "SMEC", "CHEMICAL","ETC"};
    private  final String[] block = {"Select Block","A", "B", "C","D","E","F","G","H"};
    private  final String[] position = {"Select Position","CR", "President", "WIse President"};
    private register_candidate_adpter adpater;
    Spinner spinner_year,spinner_dept,spinner_block,spinner_pos;
    Button button_show_candi;



    private OnFragmentInteractionListener mListener;

    public register_candi_faculty_access() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment register_candi_faculty_access.
     */
    // TODO: Rename and change types and number of parameters
    public static register_candi_faculty_access newInstance(String param1, String param2) {
        register_candi_faculty_access fragment = new register_candi_faculty_access();
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
        View view=inflater.inflate(R.layout.fragment_register_candi_faculty_access, container, false);
        register_candi_list=new ArrayList<>();
         spinner_year = (Spinner)view.findViewById(R.id.drop_down_year);
         spinner_dept = (Spinner)view.findViewById(R.id.drop_down_dept);
         spinner_block = (Spinner)view.findViewById(R.id.drop_down_block);
         spinner_pos = (Spinner)view.findViewById(R.id.drop_down_position);

        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,year);
        ArrayAdapter<String> adapter_dept = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,department);
        ArrayAdapter<String> adapter_block = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,block);
        ArrayAdapter<String> adapter_post = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,position);

        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);

        adapter_dept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dept.setAdapter(adapter_dept);

        adapter_block.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_block.setAdapter(adapter_block);

        adapter_post.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pos.setAdapter(adapter_post);
        // Inflate the layout for this fragment
        ListView listView=view.findViewById(R.id.list_of_candi);
        adpater=new register_candidate_adpter(getContext(),register_candi_list);
        listView.setAdapter(adpater);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getContext(), "Show detail!", Toast.LENGTH_LONG).show();
            }
        });


        button_show_candi=view.findViewById(R.id.button_show_candi);
        button_show_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_candidate();
            }
        });
        return view;
    }

    public void show_candidate(){
        final String  year_candi = spinner_year.getSelectedItem().toString();
        final String dept_candi = spinner_dept.getSelectedItem().toString();
        final String block_candi = spinner_block.getSelectedItem().toString();
        final String pos_candi = spinner_pos.getSelectedItem().toString();

        DatabaseReference reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println(count);
                System.out.println("heyyhhhhhhhhhhhhhhhhhhhhhh");
                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    String year_candi_test=ds1.child("candi_year").getValue().toString();
                    String dept_candi_test=ds1.child("candi_dept").getValue().toString();
                    String block_candi_test=ds1.child("candi_block").getValue().toString();
                    String pos_candi_test=ds1.child("candi_pos").getValue().toString();
                    if (year_candi_test.equals(year_candi) && dept_candi_test.equals(dept_candi) && block_candi_test.equals(block_candi) &&
                            pos_candi_test.equals(pos_candi))
                    {
                        register_candi_list.add(new register_candi_obj(ds1.child("candi_name").getValue().toString(),ds1.child("candi_prn_no").getValue().toString(),ds1.child("candi_email").getValue().toString(),
                                ds1.child("candi_dob").getValue().toString(),ds1.child("candi_dept").getValue().toString(),ds1.child("candi_year").getValue().toString(),
                                ds1.child("candi_block").getValue().toString(),ds1.child("candidate_id").getValue().toString(),ds1.child("candi_pos").getValue().toString()));
                    }
                    else {
                        Toast.makeText(getContext(), "No one is register yet in this section!", Toast.LENGTH_LONG).show();

                    }

                }
                adpater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getId() == R.id.drop_down_year)
        {
                       Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        }
        else if(parent.getId() == R.id.drop_down_dept)
        {
                       Toast.makeText(getContext(), "bye", Toast.LENGTH_SHORT).show();
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
