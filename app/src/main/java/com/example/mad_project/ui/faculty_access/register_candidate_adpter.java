package com.example.mad_project.ui.faculty_access;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mad_project.R;
import com.example.mad_project.ui.home.post_obj;

import java.util.ArrayList;

public class register_candidate_adpter extends BaseAdapter {
    private Context context;
    private ArrayList<register_candi_obj> list_data_candi;
    public register_candidate_adpter(Context context, ArrayList list_data_candi) {
        this.context=context;
        this.list_data_candi=list_data_candi;
    }


    @Override
    public int getCount() {
        return list_data_candi.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data_candi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.customize_view_for_register_candidate,null);
        TextView candi_name=view.findViewById(R.id.candi_name);
        candi_name.setText(list_data_candi.get(position).candi_name);
        return view;
    }
}
