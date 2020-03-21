package com.example.mad_project.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mad_project.R;

import java.util.ArrayList;

public class post_adapter extends BaseAdapter {
    private Context context;
    private ArrayList<post_obj> list_data;
    public post_adapter(Context context, ArrayList list_data) {
        this.context=context;
        this.list_data=list_data;
    }


    @Override
    public int getCount() {
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.post_costumize_view,null);
        TextView post_title=view.findViewById(R.id.post_title);
        TextView post_content=view.findViewById(R.id.post_Content);
        TextView user_name=view.findViewById(R.id.user_name);
        TextView user_post=view.findViewById(R.id.user_post);
        TextView post_date=view.findViewById(R.id.post_date);
        TextView post_time=view.findViewById(R.id.post_time);
        TextView register_button_text_bool=view.findViewById(R.id.register_button_text_bool);

        Button register_button=view.findViewById(R.id.register_button);

        post_title.setText(list_data.get(position).post_title);
        post_content.setText(list_data.get(position).post_content);
        post_date.setText(list_data.get(position).post_date);
        post_time.setText(list_data.get(position).post_time);
        if (!list_data.get(position).register_button.equals("true")){
            register_button.setVisibility(View.GONE);
            register_button_text_bool.setVisibility(View.GONE);
        }
        else{
            register_button_text_bool.setVisibility(View.GONE);

            register_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });;
        }
        //user_name.setText(list_data.get(position).post_title);
        user_name.setText("Rahul jain");
        user_post.setText("HOD of SCET");

        return view;
    }
}
