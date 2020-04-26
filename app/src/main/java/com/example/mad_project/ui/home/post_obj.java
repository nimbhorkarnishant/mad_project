package com.example.mad_project.ui.home;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class post_obj  {
    public  String post_id;
    public String post_title;
    public String post_content;
    public String register_button;
    public String user_id;
    public String post_date;
    public String post_time;
    public String post_candidatepost;




    public post_obj(String post_id,String post_title, String post_content, String register_button, String user_id,String post_date,String post_time,String post_candidatepost) {
        this.post_id=post_id;
        this.post_title=post_title;
        this.post_content=post_content;
        this.register_button=register_button;
        this.user_id=user_id;
        this.post_date=post_date;
        this.post_time=post_time;
        this.post_candidatepost=post_candidatepost;

    }

}
