package com.example.call_the_roll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.call_the_roll.R;
import com.example.call_the_roll.database.MenuMember;

import java.util.List;

/**
 * Created by 廖智涌 on 2017/5/19.
 */

public class MemberAdapter extends ArrayAdapter<MenuMember> {

    private int resourceId;

    public MemberAdapter(Context context,int textViewResourceId,List<MenuMember> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MenuMember member = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView id = (TextView) view.findViewById(R.id.id_item);
        TextView name = (TextView) view.findViewById(R.id.name_item);
        id.setText(member.getMemberID());
        name.setText(member.getMemberName());
        return view;
    }

}
