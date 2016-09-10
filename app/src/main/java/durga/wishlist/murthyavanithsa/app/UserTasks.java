package durga.wishlist.murthyavanithsa.app;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
class UserTask {
    String status;
    String message;
    User_tasks[] tasks;

}
class User_tasks {
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
    private ArrayList<User_tasks> itemsArrayList;
    private ListView listView,drawerlist;
    //    ArrayAdapter arrayAdapter;
    private TodoListAdaptor todoListAdaptor;
    private Handler mHandler;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private static String usertoken;
    private DrawerLayout drawerLayout;
    private static final String TAG = "UserTasks";
    private SharedPreferences wishListAppSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.useritems_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        String userName = wishListAppSettings.getString("name", "name is missing");
        Log.i("UserTask username", userName);
        TextView textView = new TextView(getApplicationContext());
        final ActionBar actionBar = getSupportActionBar();
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutparams);
        textView.setText("WishList");
        textView.setTextSize(20);
        //To set font created a directory called assets in that font directory is created.
        Typeface face = Typeface.createFromAsset(getAssets(), "font/Roboto-Medium.ttf");
        textView.setTypeface(face);
        textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
//        textView.setGravity(Gravity.CENTER);
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textView);
//        String[] items = getResources().getStringArray(R.array.drawerlistitems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        nvDrawer.setBackgroundColor(getResources().getColor(R.color.navdrawerbackground));
        nvDrawer.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorSecondary)));
        Log.i("UserTasks","setupDrawerContentMethod");
//        drawerlist = (ListView) findViewById(R.id.drawer_list);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.navdrawerbackground));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        nvDrawer.addHeaderView();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, items);
//        drawerlist.setAdapter(adapter);
        mDrawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(mDrawerToggle);
        face = Typeface.createFromAsset(getAssets(), "font/Roboto-Light.ttf");
//      Setup drawer view
//        drawerLayout.setDrawerListener(mDrawerToggle);

        View header = nvDrawer.inflateHeaderView(R.layout.navigation_drawer_header);
        TextView headerTextView = (TextView) header.findViewById(R.id.header_text_view);
        headerTextView.setText(userName);
        headerTextView.setTypeface(face);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.showContextMenu();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("WishList");
//        toolbar.setTitleTextColor(Color.red(Color.RED));
//        SwipeLayout swipeLayout = (SwipeLayout) findViewById(R.id.swipelayout);
//        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottomwrapper1));
//        swipeLayout.addDrag(SwipeLayout.DragEdge.Right,findViewById(R.id.

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
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("userTokenusertasks", usertoken);
        listView = (ListView) findViewById(R.id.listView);
        itemsArrayList = new ArrayList<User_tasks>();
//        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
        todoListAdaptor = new TodoListAdaptor(this,itemsArrayList, face);
//        UserItemsResponse.SavedUserItems savedUserItems;
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
        Urlendpoints urlendpoints = new Urlendpoints();
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", usertoken)
                .add("status","pending")
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
//                        System.out.println(itemsArrayList);
                    }
                }
                else{
                    UserTasks.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserTasks.this, userTask.message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(todoListAdaptor);;
//                        todoListAdaptor.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.pending:
                Toast.makeText(UserTasks.this,"pending",Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                fragmentClass = PendingTasks.class;
                break;
            case R.id.completed:
                Toast.makeText(UserTasks.this,"completed",Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                fragmentClass = CompletedTasks.class;
                break;
            case R.id.logout:
//                Intent logoutIntent = new Intent(UserTasks.this,Logout.class);
//                startActivity(logoutIntent);
                fragmentClass = Logout.class;
                break;
            default:
                listView.setVisibility(View.GONE);
                fragmentClass = PendingTasks.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
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
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onPause(){
        super.onPause();
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
