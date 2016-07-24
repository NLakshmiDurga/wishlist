package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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


//class SavedUserItems{
//    int item_id;
//    String item_name;
//    String status;
//    public SavedUserItems(String status,String item_name){
//        this.status = status;
//        this.item_name = item_name;
//    }
//}
//class UserItemsResponse{
//    String status;
//    String message;
//    SavedUserItems[] usersaveditems;
//
//}
//class UserTask {
//    String status;
//    String message;
//    User_tasks[] tasks;
//
//}
//class User_tasks{
//    int task_id;
//    String task;
//    String status;
//    public User_tasks(int task_id,String task,String status){
//        this.task_id = task_id;
//        this.task = task;
//        this.status = status;
//    }
//}

public class UserTasks extends AppCompatActivity{
    ArrayList<User_tasks> itemsArrayList;
    ListView listView;
    //    ArrayAdapter arrayAdapter;
    TodoListAdaptor todoListAdaptor;
    private Handler mHandler;
    ActionBarDrawerToggle mDrawerToggle;
    static String usertoken;
    private static final String TAG = "UserTasks";
    SharedPreferences wishListAppSettings;
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useritems_list);
        TextView textView = new TextView(getApplicationContext());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_drawer);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutparams);
        textView.setText("WishList");
        textView.setTextSize(20);
        //To set font created a directory called assets in that font directory is created.
        Typeface face= Typeface.createFromAsset(getAssets(), "font/Roboto-Medium.ttf");
        textView.setTypeface(face);
        textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
        textView.setGravity(Gravity.CENTER);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textView);
        String[] items = {"Pending","Completed","Logout"};
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerlist = (ListView) findViewById(R.id.drawer_list);
//        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.textColorPrimary));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        drawerlist.setAdapter(adapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    R.string.drawer_open, R.string.drawer_close) {
            /* Called when drawer is closed */
            public void onDrawerClosed(View view) {
                //Put your code here
                super.onDrawerClosed(view);
                actionBar.show();
            }
            /* Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //Put your code here
                super.onDrawerOpened(drawerView);
                actionBar.hide();
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.showContextMenu();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("WishList");
//        toolbar.setTitleTextColor(Color.red(Color.RED));
//        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipelayout);
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottomwrapper1));
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,findViewById(R.id.
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
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Pending Tasks"));
//        tabLayout.addTab(tabLayout.newTab().setText("Completed Tasks"));
//        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this,R.color.textColorPrimary));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("userToken", usertoken);
        listView = (ListView) findViewById(listView);
        itemsArrayList = new ArrayList<User_tasks>();
//        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
//        UserItemsResponse.SavedUserItems savedUserItems;

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
                else{
                    UserTasks.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserTasks.this, userTask.message, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(todoListAdaptor);
                        todoListAdaptor.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onPause(){
        super.onPause();
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
//        Log.i("userToken in pause", usertoken);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem){
//        switch (menuItem.getItemId()){
//            case R.id.logout:
//                Intent intent = new Intent(this,Logout.class);
//                startActivity(intent);
//                break;
//            case R.id.item_settings:
//                Toast.makeText(getBaseContext(), "Clicked on settings", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.deletetask:
//                Toast.makeText(getBaseContext(), "Clicked on Delete tasks option", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.refresh:
//                Intent refreshintent = getIntent();
//                finish();
//                startActivity(refreshintent);
//                break;
//            default:
//                return super.onOptionsItemSelected(menuItem);
//        }
//        return true;
//    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
//        Log.i("Token resume addtask", usertoken);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
