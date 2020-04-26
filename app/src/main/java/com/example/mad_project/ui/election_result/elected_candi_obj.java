package com.example.mad_project.ui.election_result;

public class elected_candi_obj {
    public String candidate_id,candidate_name;
    public Integer candidate_vote_count;
    public elected_candi_obj(String candidate_id,String candidate_name,Integer candidate_vote_count ) {
        this.candidate_id=candidate_id;
        this.candidate_name=candidate_name;
        this.candidate_vote_count=candidate_vote_count;
    }
}
