package com.example.mad_project.ui.faculty_access;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.ui.home.post_obj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.google.firebase.database.FirebaseDatabase;

import org.xml.sax.ErrorHandler;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link register_candi_form.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link register_candi_form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class register_candi_form extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button select,Register;
    TextView notification;
    Uri pdfUri;

    FirebaseStorage storage;
    FirebaseDatabase database;
    private  final String[] year = {"Select Year","FE", "SY", "TY","Btech"};
    private  final String[] department = {"Select Department","FE","SCET", "SMEC", "CHEMICAL","ETC"};
    private  final String[] block = {"Select Block","A", "B", "C","D","E","F","G","H"};
    TextView full_name,prn_no,email_id,dob,position;
    Spinner candidate_year,candidate_dept,candidate_block;
    Spinner spinner_dept,spinner_year,spinner_block;
    DatabaseReference reff;
    public String candi_id,name,prn,email,dob1,dept,candi_year,candi_block,post_position;




    public register_candi_form() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment register_candi_form.
     */
    // TODO: Rename and change types and number of parameters
    public static register_candi_form newInstance(String param1, String param2) {
        register_candi_form fragment = new register_candi_form();
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
        View view=inflater.inflate(R.layout.fragment_register_candi_form, container, false);
         spinner_dept = view.findViewById(R.id.Spinner1);
         spinner_year = view.findViewById(R.id.Spinner2);
         spinner_block = view.findViewById(R.id.Spinner3);


        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,year);
        ArrayAdapter<String> adapter_dept = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,department);
        ArrayAdapter<String> adapter_block = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,block);

        adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter_year);

        adapter_dept.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dept.setAdapter(adapter_dept);

        adapter_block.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_block.setAdapter(adapter_block);

        storage=FirebaseStorage.getInstance();
        database= FirebaseDatabase.getInstance();

        select=view.findViewById(R.id.select);
        Register=view.findViewById(R.id.Register);
        notification=view.findViewById(R.id.notification);

        full_name=view.findViewById(R.id.editText);
        prn_no=view.findViewById(R.id.editText3);
        email_id=view.findViewById(R.id.editText4);
        dob=view.findViewById(R.id.editText5);

        reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println(count);
                int new_count=count+1;
                candi_id="candidate_"+new_count;
                if (dataSnapshot.hasChild(candi_id)){
                    int new_count1=count+2;
                    candi_id="candidate_"+new_count1;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission( getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();
                }
                else
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
        });

        Register=(Button) view.findViewById(R.id.Register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_candi();

            }
        });


        return view;
    }

    public void register_candi(){
        name=full_name.getText().toString();
        email=email_id.getText().toString();
        prn=prn_no.getText().toString();
        dob1=dob.getText().toString();
        dept=spinner_dept.getSelectedItem().toString();
        candi_year=spinner_year.getSelectedItem().toString();
        candi_block=spinner_block.getSelectedItem().toString();
        post_position=getArguments().getString("post_id_upadte");


        if (name.length()==0 || email.length()==0 || prn.length()==0 || dob1.length()==0 || dept.length()==0 || candi_year.length()==0 || candi_block.length()==0){
            full_name.setBackgroundResource(R.drawable.border_alert);
            email_id.setBackgroundResource(R.drawable.border_alert);
            prn_no.setBackgroundResource(R.drawable.border_alert);
            dob.setBackgroundResource(R.drawable.border_alert);
            spinner_dept.setBackgroundResource(R.drawable.border_alert);
            spinner_year.setBackgroundResource(R.drawable.border_alert);
            spinner_block.setBackgroundResource(R.drawable.border_alert);
            Toast.makeText(getContext(), "All Fields is required!"+post_position, Toast.LENGTH_LONG).show();

        }
        else{
            try {
                System.out.println("id----------->"+candi_id);
                register_candi_obj new_obj = new register_candi_obj(name,prn,email,dob1,dept,candi_year,candi_block,candi_id,post_position);
                FirebaseDatabase.getInstance().getReference().child("mad_project").child("register_candidate_election")
                        .child(candi_id).setValue(new_obj);
                String text_subject="Candidate Registration ";
                String text_content="Thank You! You are Register for "+post_position+" Post Successufully! Further Instruction will send you " +
                        "to your email id about the Interview process.";
                JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(),email,text_subject,text_content);
                javaMailAPI.execute();
                Toast.makeText(getContext(),"Candidate Register Sucessfully",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){

            }
            finally {
            }


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else{
            Toast.makeText(getContext(),"please provide permission..",Toast.LENGTH_SHORT).show();

        }

    }

    private void selectPdf() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null)
        {
            pdfUri=data.getData();
        }
        else {
            Toast.makeText(getContext(),"Please select the  file",Toast.LENGTH_SHORT).show();
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
