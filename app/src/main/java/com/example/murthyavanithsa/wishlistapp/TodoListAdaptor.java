package com.example.murthyavanithsa.wishlistapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
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
    String status;
    Handler handler;
    SharedPreferences sharedPreferences;
    ArrayList<User_tasks> itemsArrayList;
    Button completebtn;
    Button deletebtn;
    String deletebtntxt;
    String buttontext;
    String token;
    public TodoListAdaptor(Context context,ArrayList<User_tasks> itemsArrayList){
        super(context,0,itemsArrayList);
        this.itemsArrayList = itemsArrayList;
    }
    public View getView(int position, View convertView,ViewGroup parent){
        final User_tasks task1 = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_main, parent, false);
        final TextView textViewtask = (TextView) convertView.findViewById(R.id.task);
        final TextView textViewstatus = (TextView) convertView.findViewById(R.id.status);
        textViewtask.setText(task1.task);
        textViewstatus.setText(task1.status);
        final OkHttpClient client = new OkHttpClient();
        sharedPreferences = getContext().getSharedPreferences("WishListAppSettings", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "token is missing");
        completebtn = (Button) convertView.findViewById(R.id.completebutton);
        buttontext = completebtn.getText().toString();
        handler = new Handler(Looper.getMainLooper());
        final Urlendpoints urlendpoints = new Urlendpoints();
        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Complete button", buttontext);
                if (buttontext.equals("complete")) {
                    status = "c";
                    Log.i("status todolistadapter", status);
                    Log.i("taskidin todolist",Integer.toString(task1.task_id));
                    RequestBody formBody = new FormBody.Builder()
                            .add("token", token)
                            .add("taskid", Integer.toString(task1.task_id))
                            .add("status", status)
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
                        public void onResponse(Call call, final Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                            String jsonResponse = response.body().string();
                            Log.i("complete item response", jsonResponse);
                            Log.i("status after response", task1.status);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textViewstatus.setText(status);
                                }
                            });
                            // Gson gson = new GsonBuilder().create();
                        }
                    });
                }
            }
        });
        deletebtn = (Button) convertView.findViewById(R.id.deletebutton);
        deletebtntxt = deletebtn.getText().toString();
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("delete button", deletebtntxt);
                if (deletebtntxt.equals("delete")) {
                    Log.i("taskidin deletebtn",Integer.toString(task1.task_id));
                    RequestBody formBody = new FormBody.Builder()
                            .add("token", token)
                            .add("taskid", Integer.toString(task1.task_id))
                            .build();
                    Request request = new Request.Builder()
                            .url(urlendpoints.getdeleteurl())
                            .post(formBody)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                            String jsonResponse = response.body().string();
                            Log.i("Delete item response", jsonResponse);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    itemsArrayList.remove(task1);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            }
        });
        return convertView;
    }
}
