package com.example.mad_project.ui.live_voting;

import android.os.Parcelable;

import java.io.Serializable;

public class selected_candi_obj implements Serializable {
    String candi_name,candi_prn_no,candi_email,candi_dob,candi_dept,candi_year,candi_block,candidate_id,candi_pos;
    public selected_candi_obj(String candi_name,String candi_prn_no,String candi_email,
                              String candi_dob,String candi_dept,String candi_year,String candi_block,String candidate_id,String candi_pos) {
        this.candi_name=candi_name;
        this.candi_prn_no=candi_prn_no;
        this.candi_email=candi_email;
        this.candi_dob=candi_dob;
        this.candi_dept=candi_dept;
        this.candi_year=candi_year;
        this.candi_block=candi_block;
        this.candidate_id=candidate_id;
        this.candi_pos=candi_pos;
    }
}
