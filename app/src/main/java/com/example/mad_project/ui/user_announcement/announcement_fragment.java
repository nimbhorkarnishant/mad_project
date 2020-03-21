package com.example.mad_project.ui.user_announcement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mad_project.R;
import com.example.mad_project.ui.home.add_post;
import com.example.mad_project.ui.home.post_adapter;
import com.example.mad_project.ui.home.post_obj;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link announcement_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link announcement_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class announcement_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private announcement_post_adapter adapter;
    ArrayList<post_obj> post_data_announce;
    DatabaseReference reff;
    String post_id_delete;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public announcement_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment announcement_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static announcement_fragment newInstance(String param1, String param2) {
        announcement_fragment fragment = new announcement_fragment();
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.text_home);
        textView.setText("Your Announcements");
        post_data_announce=new ArrayList<>();
        FloatingActionButton add_post_buuton=root.findViewById(R.id.add_post);
        add_post_buuton.setVisibility(View.GONE);

        final ListView listView=root.findViewById(R.id.list_of_post);
        adapter=new announcement_post_adapter(getContext(),post_data_announce);
        listView.setAdapter(adapter);

        reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("announcement_post");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                System.out.println(count);

                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    if (ds1.child("user_id").getValue().toString().equals("1")){
                        post_obj obj=new post_obj(ds1.child("post_id").getValue().toString(),ds1.child("post_title").getValue().toString(),ds1.child("post_content").getValue().toString(),
                                ds1.child("register_button").getValue().toString(),ds1.child("user_id").getValue().toString(),
                                ds1.child("post_date").getValue().toString(),ds1.child("post_time").getValue().toString(),
                                ds1.child("post_candidatepost").getValue().toString());
                        post_data_announce.add(obj);
                    }
                    else {

                    }
                }
                if (post_data_announce.size()==0){
                    Toast.makeText(getContext(), "Sorry we didn't Found any Announcment you Made!", Toast.LENGTH_LONG).show();
                }
                else{
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        registerForContextMenu(listView);
        return root;

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.popup_memu,menu);

    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.post_update:
                System.out.println("position--->"+" "+menuInfo.position);
                String post_id_upadte=post_data_announce.get(menuInfo.position).post_id;
                System.out.println(post_id_upadte);
                Bundle bundle=new Bundle();
                bundle.putString("post_id_upadte",post_id_upadte );
                bundle.putString("post_id_title", post_data_announce.get(menuInfo.position).post_title);
                bundle.putString("post_id_content",post_data_announce.get(menuInfo.position).post_content );
                bundle.putString("post_id_candi_post",post_data_announce.get(menuInfo.position).post_candidatepost );
                bundle.putString("post_id_register_button",post_data_announce.get(menuInfo.position).register_button );


                final Fragment frag=new update_post();
                frag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, frag);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;

            case R.id.post_delete:
                System.out.println("position--->"+" "+menuInfo.position);
                 post_id_delete=post_data_announce.get(menuInfo.position).post_id;
                System.out.println(post_id_delete);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseDatabase.getInstance().getReference().child("mad_project").child("announcement_post").child(post_id_delete).removeValue();
                                Toast.makeText(getContext(), "Your Announcement Deleted Successfully!", Toast.LENGTH_LONG).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Do your No progress
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            default:
                return super.onContextItemSelected(item);
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
