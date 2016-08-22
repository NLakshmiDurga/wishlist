package com.example.murthyavanithsa.wishlistapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by durga on 21/2/16.
 */
public class TodoListAdaptor extends ArrayAdapter<User_tasks> {
    ArrayList<User_tasks> itemsArrayList;
    Handler handler;
    String token;
    SharedPreferences sharedPreferences;
    public TodoListAdaptor(Context context,ArrayList<User_tasks> itemsArrayList){
        super(context,0,itemsArrayList);
        this.itemsArrayList = itemsArrayList;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        final User_tasks task1 = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.swipe_layout, parent, false);
        final TextView textView = (TextView) convertView.findViewById(R.id.task);
        textView.setText(task1.task);
        handler = new Handler(Looper.getMainLooper());
        final Urlendpoints urlendpoints = new Urlendpoints();
        final OkHttpClient client = new OkHttpClient();
        sharedPreferences = getContext().getSharedPreferences("WishListAppSettings", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "token is missing");
        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, convertView.findViewById(R.id.bottomwrapper1));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, convertView.findViewById(R.id.bottomwrapper));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(getContext(), "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        final int taskposition = getPosition(task1);
        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("taskidin deletebtn", Integer.toString(task1.task_id));
                Toast.makeText(getContext(), "clicked on delete", Toast.LENGTH_SHORT).show();
                itemsArrayList.remove(taskposition);
                notifyDataSetChanged();

//                if (Integer.toString(taskposition)!=null){
//                    deleteArrayList.add(taskposition);
//                    Log.i("deletetaskid",deleteArrayList.toString());
//                    Log.i("itemsarraylist",Integer.toString(itemsArrayList.size()));
//                    Log.i("position of task",Integer.toString(taskposition));
//                    itemsArrayList.remove(taskposition);
//                    notifyDataSetChanged();
//                }
//                RequestBody formBody = new FormBody.Builder()
//                        .add("token", token)
//                        .add("taskid", Integer.toString(task1.task_id))
//                        .build();
//                Request request = new Request.Builder()
//                        .url(urlendpoints.getdeleteurl())
//                        .post(formBody)
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//                        if (!response.isSuccessful()) {
//                            throw new IOException("Unexpected code " + response);
//                        }
//                        String jsonResponse = response.body().string();
//                        Log.i("Delete item response", jsonResponse);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                itemsArrayList.remove(getPosition(task1));
//                                notifyDataSetChanged();
//                            }
//                        });
//                    }
//                });
            }
        });
        convertView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "clicked on done", Toast.LENGTH_SHORT).show();
//                if (Integer.toString(taskposition)!=null){
//                    doneArrayList.add(taskposition);
//                    Log.i("done taskid",doneArrayList.toString());
//                    Log.i("itemsarraylist",Integer.toString(itemsArrayList.size()));
//                    Log.i("position of task",Integer.toString(taskposition));
//
//
//                }
                itemsArrayList.remove(taskposition);
                notifyDataSetChanged();
                RequestBody formBody = new FormBody.Builder()
                        .add("token", token)
                        .add("taskid", Integer.toString(task1.task_id))
                        .add("status", "completed")
                        .build();
                Request request = new Request.Builder()
                        .url(urlendpoints.getupdateurl())
                        .post(formBody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("IO Exception" + response);
                        }
                        String jsonResponse = response.body().string();
                        Log.i("Done item response", jsonResponse);
                        Gson gson = new GsonBuilder().create();
                    }
                });
            }
        });
        return convertView;
    }
}