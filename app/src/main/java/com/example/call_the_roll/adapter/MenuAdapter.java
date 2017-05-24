package com.example.call_the_roll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.call_the_roll.R;
import com.example.call_the_roll.database.Menu;
import com.example.call_the_roll.database.MenuMember;

import java.util.List;

/**
 * Created by 廖智涌 on 2017/5/21.
 */

public class MenuAdapter extends ArrayAdapter<Menu> {

    private int resourceId;

    public MenuAdapter(Context context, int textViewResourceId, List<Menu> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Menu menu = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name = (TextView) view.findViewById(R.id.item);
        name.setText(menu.getMenuName());
        return view;
    }

}
