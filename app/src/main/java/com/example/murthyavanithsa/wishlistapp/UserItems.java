package com.example.murthyavanithsa.wishlistapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class SavedUserItems{
    int item_id;
    String item_name;
    public SavedUserItems(String item_name){
        this.item_name = item_name;
    }
}
class UserItemsResponse{
    String status;
    String message;
    SavedUserItems[] usersaveditems;

}

public class UserItems extends AppCompatActivity {
    ArrayList<String> itemsArrayList;
    ListView listView;
    ArrayAdapter arrayAdapter;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_todo_list);
        Bundle bundle = getIntent().getExtras();
        listView = (ListView) findViewById(R.id.listView2);
        //itemsArrayList = new ArrayList<SavedUserItems>();
        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
//        UserItemsResponse.SavedUserItems savedUserItems;
        arrayAdapter = new ArrayAdapter(UserItems.this,android.R.layout.simple_list_item_1,itemsArrayList);

        String token = bundle.getString("token");
        Log.i("token in user items", token);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token",token)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.2.12/index.php/basket")
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
                Log.i("User response",jsonResponse);
                Gson gson = new GsonBuilder().create();
                UserItemsResponse userItemsResponse = gson.fromJson(jsonResponse, UserItemsResponse.class);
                for (SavedUserItems saveItem:userItemsResponse.usersaveditems){
                    itemsArrayList.add(saveItem.item_name);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(arrayAdapter);
                    }
                });

                Log.i("User status ", userItemsResponse.status);
                Log.i("User message", userItemsResponse.message);


            }
        });

    }

}
