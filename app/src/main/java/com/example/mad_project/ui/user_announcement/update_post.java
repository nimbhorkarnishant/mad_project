package com.example.mad_project.ui.user_announcement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.home.post_obj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link update_post.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link update_post#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_post extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinner;
    private static final String[] paths = {"Select Position","CR", "President", "WIse President"};
    private String [] month_name= {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    TextView title,content;
    CheckBox register_button;
    Spinner candidate_position;
    DatabaseReference add_post_reff;
    String post_id_update;
    // String post_id;
    int count_obj=3;
    post_obj new_post_obj;

    private OnFragmentInteractionListener mListener;

    public update_post() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_post.
     */
    // TODO: Rename and change types and number of parameters
    public static update_post newInstance(String param1, String param2) {
        update_post fragment = new update_post();
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
        View v=inflater.inflate(R.layout.fragment_update_post, container, false);
        System.out.println("count--------->"+getParentFragmentManager().getBackStackEntryCount());
        spinner = (Spinner)v.findViewById(R.id.spinner_drop_down);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //spinner.setOnItemSelectedListener(this);
        title=v.findViewById(R.id.post_title);
        content=v.findViewById(R.id.post_Content);
        candidate_position=v.findViewById(R.id.spinner_drop_down);
        register_button=v.findViewById(R.id.checkBox);

        post_id_update=getArguments().getString("post_id_upadte");

        System.out.println(post_id_update);
        title.setText(getArguments().getString("post_id_title"));
        content.setText(getArguments().getString("post_id_content"));

        paths[0]=getArguments().getString("post_id_candi_post");

        if (getArguments().getString("post_id_register_button").equals("true")){
            register_button.setChecked(true);

        }
        else{
            register_button.setChecked(false);

        }

        Button make_annou=v.findViewById(R.id.button);
        make_annou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_post_data();
            }
        });
        return v;
    }

    public void update_post_data(){
        String date1;
        String time;
        String post_title=title.getText().toString();
        String post_content=content.getText().toString();
        boolean isChecked = ((CheckBox)register_button).isChecked();
        String position = candidate_position.getSelectedItem().toString();
        String user_id="1";
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

           System.out.print("------------------------------- **********************  ");
           System.out.println(post_id_update);
            add_post_reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("announcement_post");

            new_post_obj=new post_obj(post_id_update,post_title,post_content,register_button_text,user_id,date1,time,position);
            add_post_reff.child(post_id_update).setValue(new_post_obj);
            Toast.makeText(getContext(), "Your Announcement Updated Successfully!", Toast.LENGTH_LONG).show();
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
