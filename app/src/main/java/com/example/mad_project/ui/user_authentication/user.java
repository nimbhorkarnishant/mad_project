package com.example.mad_project.ui.user_authentication;

public class user {
    public String user_id;
    public String full_name;
    public String prn_no;
    public String user_access;
    public String user_year;
    public String user_dept;
    public String user_block;

    public user(String user_id,String full_name,String prn_no,String user_access,String user_year,String user_dept,String user_block) {
        this.user_id=user_id;
        this.full_name=full_name;
        this.prn_no=prn_no;
        this.user_access=user_access;
        this.user_year=user_year;
        this.user_dept=user_dept;
        this.user_block=user_block;

    }
}
