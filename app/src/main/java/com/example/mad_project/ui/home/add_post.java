package com.example.mad_project.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import  com.example.mad_project.ui.home.post_obj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mad_project.ui.home.HomeFragment.post_id;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link add_post.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link add_post#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_post extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Spinner spinner;
    private static final String[] paths = {"Select Position","CR", "President", "WIse President"};
    private String [] month_name= {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    TextView title,content;
    CheckBox register_button;
    Spinner candidate_position;
    DatabaseReference add_post_reff;
  // String post_id;
    int count_obj=3;
    post_obj new_post_obj;
    SharedPreferences sharedPreferences;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public add_post() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static add_post newInstance(String param1, String param2) {
        add_post fragment = new add_post();
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
        View v=inflater.inflate(R.layout.fragment_add_post, container, false);
        spinner = (Spinner)v.findViewById(R.id.spinner_drop_down);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        sharedPreferences=getContext().getSharedPreferences("user_detail", MODE_PRIVATE);
        System.out.println("bbbbbbbbbbbbbbb-->"+sharedPreferences.getString("user_id",""));

        //spinner.setOnItemSelectedListener(this);
        title=v.findViewById(R.id.post_title);
        content=v.findViewById(R.id.post_Content);
        candidate_position=v.findViewById(R.id.spinner_drop_down);
        register_button=v.findViewById(R.id.checkBox);


        Button make_annou=v.findViewById(R.id.button);
        make_annou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_data();
            }
        });

        return v;
    }


    public void post_data(){
        String date1;
        String time;
        String post_title=title.getText().toString();
        String post_content=content.getText().toString();
        boolean isChecked = ((CheckBox)register_button).isChecked();
        String position = candidate_position.getSelectedItem().toString();
        String user_id=sharedPreferences.getString("user_id","");
        String user_name=sharedPreferences.getString("user_name","");
        String user_access=sharedPreferences.getString("user_access","");
        String user_dept=sharedPreferences.getString("user_dept","");
        String register_button_text;
        if (isChecked){
            register_button_text="true";
        }
        else {
            register_button_text="false";
        }


        if (post_title.length()==0 && post_content.length()==0){
            title.setBackgroundResource(R.drawable.border_alert);
            content.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "Field is required!", Toast.LENGTH_LONG).show();

        }
        else if (post_title.length()==0){
            title.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "Title is required!", Toast.LENGTH_LONG).show();


        }
        else if (post_content.length()==0){
            content.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "post Discription is required!", Toast.LENGTH_LONG).show();
        }
        else{
            Calendar cc = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            int year = cc.get(Calendar.YEAR);
            int month = cc.get(Calendar.MONTH);
            int mDay = cc.get(Calendar.DAY_OF_MONTH);
            Date date = new Date();
            String strDate = formatter.format(date);

            date1=mDay+" "+month_name[month]+","+year;
            time =strDate.split(" ")[1];

            add_post_reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("announcement_post");
            System.out.print("------------------------------- **********************  ");
            System.out.println(post_id);
            new_post_obj=new post_obj(post_id,post_title,post_content,register_button_text,user_id,date1,time,position,user_name,user_access,user_dept);
            add_post_reff.child(post_id).setValue(new_post_obj);
            Toast.makeText(getContext(), "Your Announcement created Successfully!", Toast.LENGTH_LONG).show();
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
