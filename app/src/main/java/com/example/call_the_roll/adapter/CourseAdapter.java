package com.example.call_the_roll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.call_the_roll.R;
import com.example.call_the_roll.database.Course;
import com.example.call_the_roll.database.MenuMember;

import java.util.List;

/**
 * Created by 廖智涌 on 2017/5/21.
 */

public class CourseAdapter extends ArrayAdapter<Course> {

    private int resourceId;

    public CourseAdapter(Context context, int textViewResourceId, List<Course> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Course course = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name = (TextView) view.findViewById(R.id.item);
        name.setText(course.getCourseName());
        return view;
    }

}
