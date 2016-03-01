package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
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
    String status;
    public SavedUserItems(String status,String item_name){
        this.status = status;
        this.item_name = item_name;
    }
}
class UserItemsResponse{
    String status;
    String message;
    SavedUserItems[] usersaveditems;

}
class UserTask{
    String status;
    String message;
    User_tasks[] tasks;
}
class User_tasks{
    String task;
    String status;
    public User_tasks(String task,String status){
        this.task = task;
        this.status = status;
    }
}

public class UserTasks extends AppCompatActivity {
    ArrayList<User_tasks> itemsArrayList;
    ListView listView;
//    ArrayAdapter arrayAdapter;
    TodoListAdaptor todoListAdaptor;
    private Handler mHandler;
    SharedPreferences wishListAppSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_todo_list);
//        Bundle bundle = getIntent().getExtras();
//        String token = bundle.getString("token");
//
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.jumpDrawablesToCurrentState();
        floatingActionButton.show();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userTasksIntent = new Intent(UserTasks.this,AddTasks.class);
                startActivity(userTasksIntent);
            }
        });
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        String usertoken = wishListAppSettings.getString("token","token is missing");
        Log.i("userToken", usertoken);
        listView = (ListView) findViewById(R.id.listView2);
        itemsArrayList = new ArrayList<User_tasks>();
//        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
//        UserItemsResponse.SavedUserItems savedUserItems;
        todoListAdaptor = new TodoListAdaptor(this,itemsArrayList);

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", usertoken)
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.2.12/index.php/usertasks/get_user_tasks")
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
//                UserItemsResponse userItemsResponse = gson.fromJson(jsonResponse, UserItemsResponse.class);
//                for (SavedUserItems saveItem:userItemsResponse.usersaveditems){
//                    itemsArrayList.add(saveItem);
//                }
                UserTask userTask = gson.fromJson(jsonResponse,UserTask.class);
                for (User_tasks user_tasks:userTask.tasks){
                    itemsArrayList.add(user_tasks);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                    listView.setAdapter(todoListAdaptor);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ((SwipeLayout)(listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
                        }
                    });
                    listView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Log.e("ListView", "OnTouch");
                            return false;
                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            return true;
                        }
                    });
                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            Log.e("ListView", "onScrollStateChanged");
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        }
                    });

                    listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("ListView", "onItemSelected:" + position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.e("ListView", "onNothingSelected:");
                        }
                    });

                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
