package com.example.mad_project.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mad_project.R;
import com.example.mad_project.ui.faculty_access.register_candi_form;
import com.example.mad_project.ui.user_authentication.user;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.mad_project.ui.home.post_obj;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

import static com.example.mad_project.ui.user_authentication.register_user_activity.TAG;


public class HomeFragment extends Fragment {
    private post_adapter adapter;
    private post_adapter adapter_user;
    DatabaseReference reff;
    ArrayList<post_obj> post_data;
    static String post_id;
    ArrayList<post_obj> post_data_sort;

    int count;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = root.findViewById(R.id.text_home);
        textView.setText("Announcements");
        post_data=new ArrayList<>();
        post_data_sort=new ArrayList<>();
        ListView listView=root.findViewById(R.id.list_of_post);
        adapter=new post_adapter(getContext(),post_data,getParentFragmentManager());
        listView.setAdapter(adapter);

        reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("announcement_post");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                System.out.println(count);
                System.out.println("heyyhhhhhhhhhhhhhhhhhhhhhh");
                for (DataSnapshot ds1:dataSnapshot.getChildren()){
                    post_obj obj=new post_obj(ds1.child("post_id").getValue().toString(),ds1.child("post_title").getValue().toString(),ds1.child("post_content").getValue().toString(),
                            ds1.child("register_button").getValue().toString(),ds1.child("user_id").getValue().toString(),
                            ds1.child("post_date").getValue().toString(),ds1.child("post_time").getValue().toString(),
                            ds1.child("post_candidatepost").getValue().toString(),ds1.child("user_name").getValue().toString(),
                            ds1.child("user_access").getValue().toString(),ds1.child("user_dept").getValue().toString());
                    //post_data.add(obj);
                    post_data_sort.add(obj);

                }
                int new_count=count+1;
                post_id="post_"+new_count;
                if (dataSnapshot.hasChild(post_id)){
                    int new_count1=count+2;
                    post_id="post_"+new_count1;
                }
                for (int j = post_data_sort.size()-1;j>=0;j--){
                    post_data.add(post_data_sort.get(j));
                }
                adapter.notifyDataSetChanged();
               // System.out.println("dta afetr use-->"+user_detail_post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

           final Fragment frag=new add_post();
            FloatingActionButton add_post_buuton=root.findViewById(R.id.add_post);
            add_post_buuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, frag);
                transaction.addToBackStack("frag_add_post");
                transaction.commit();

            }
        });
        return root;

    }



}