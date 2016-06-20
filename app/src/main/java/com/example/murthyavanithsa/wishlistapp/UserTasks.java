package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
class UserTask {
    String status;
    String message;
    User_tasks[] tasks;

}
class User_tasks{
    int task_id;
    String task;
    String status;
    public User_tasks(int task_id,String task,String status){
        this.task_id = task_id;
        this.task = task;
        this.status = status;
    }
}

public class UserTasks extends AppCompatActivity{
    ArrayList<User_tasks> itemsArrayList;
    ListView listView;
    //    ArrayAdapter arrayAdapter;
    TodoListAdaptor todoListAdaptor;
    private Handler mHandler;
    static String usertoken;
    private static final String TAG = "UserTasks";
    SharedPreferences wishListAppSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_items);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.showContextMenu();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("WishList");
//        toolbar.setTitleTextColor(Color.red(Color.RED));
//        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipelayout);
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottomwrapper1));
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,findViewById(R.id.bottomwrapper));
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.jumpDrawablesToCurrentState();
        floatingActionButton.show();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userTasksIntent = new Intent(UserTasks.this, AddTasks.class);
                startActivity(userTasksIntent);
                finish();
            }
        });
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("userToken", usertoken);
        listView = (ListView) findViewById(R.id.listView);
        itemsArrayList = new ArrayList<User_tasks>();
//        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
//        UserItemsResponse.SavedUserItems savedUserItems;
        todoListAdaptor = new TodoListAdaptor(this, itemsArrayList);
        Urlendpoints urlendpoints = new Urlendpoints();
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", usertoken)
                .build();
        Request request = new Request.Builder()
                .url(urlendpoints.getusertasksurl())
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error",e.toString());
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);

                }
                String jsonResponse = response.body().string();
                Log.i("User response", jsonResponse);
                Gson gson = new GsonBuilder().create();
//                UserItemsResponse userItemsResponse = gson.fromJson(jsonResponse, UserItemsResponse.class);
//                for (SavedUserItems saveItem:userItemsResponse.usersaveditems){
//                    itemsArrayList.add(saveItem);
//                }
                final UserTask userTask = gson.fromJson(jsonResponse, UserTask.class);
                if(userTask.status.equals("True")){
                    for (User_tasks user_tasks : userTask.tasks) {
                        itemsArrayList.add(user_tasks);
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(todoListAdaptor);
                    }
                });
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("userToken in pause", usertoken);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(this,Logout.class);
                startActivity(intent);
                break;
            case R.id.item_settings:
                Toast.makeText(getBaseContext(), "Clicked on settings", Toast.LENGTH_LONG).show();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("Token resume addtask", usertoken);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
