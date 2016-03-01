package com.example.murthyavanithsa.wishlistapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

/**
 * Created by durga on 21/2/16.
 */
public class TodoListAdaptor extends ArrayAdapter<User_tasks> {
    public TodoListAdaptor(Context context,ArrayList<User_tasks> itemsArrayList){
        super(context,0,itemsArrayList);
    }
    public View getView(int position, View convertView,ViewGroup parent){
        User_tasks task1 = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main, parent, false);
        TextView textViewtask = (TextView) convertView.findViewById(R.id.task);
        TextView textViewstatus = (TextView) convertView.findViewById(R.id.status);
        textViewtask.setText(task1.task);
        textViewstatus.setText(task1.status);
        return convertView;
    }
}
