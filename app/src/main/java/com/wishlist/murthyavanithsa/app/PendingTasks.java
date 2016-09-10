package com.wishlist.murthyavanithsa.app;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by durga on 5/7/16.
 */

//public class PendingTasks extends AppCompatActivity {
//    SharedPreferences wishListAppSettings;
//    String usertoken;
//    TodoListAdaptor todoListAdaptor;
//    private Handler mHandler;
//    ArrayList<User_tasks> itemsArrayList;
//    ListView listView;
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pendingtask);
//        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
//        usertoken = wishListAppSettings.getString("token", "token is missing");
//        Log.i("userTokencompletedtask", usertoken);
//        itemsArrayList = new ArrayList<User_tasks>();
//        mHandler = new Handler(Looper.getMainLooper());
//        listView = (ListView) findViewById(R.id.pendingtasklistview);
//        todoListAdaptor = new TodoListAdaptor(this, itemsArrayList);
//        Urlendpoints urlendpoints = new Urlendpoints();
//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("token", usertoken)
//                .add("status","pending")
//                .build();
//        Request request = new Request.Builder()
//                .url(urlendpoints.getusertasksurl())
//                .post(formBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("Error",e.toString());
//                e.printStackTrace();
//            }
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    throw new IOException("Unexpected code " + response);
//
//                }
//                String jsonResponse = response.body().string();
//                Log.i("User response", jsonResponse);
//                Gson gson = new GsonBuilder().create();
//                final UserTask userTask = gson.fromJson(jsonResponse,UserTask.class);
//                if(userTask.status.equals("True")){
//                    for (User_tasks user_tasks : userTask.tasks) {
//                        itemsArrayList.add(user_tasks);
//                    }
//                }
//                else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(PendingTasks.this, userTask.message, Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        listView.setAdapter(todoListAdaptor);
//                    }
//                });
//            }
//        });
//    }
//}
public class PendingTasks extends Fragment {
    SharedPreferences wishListAppSettings;
    String usertoken;
    TodoListAdaptor todoListAdaptor;
    private Handler mHandler;
    ArrayList<User_tasks> itemsArrayList;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pendingtask, container, false);
        listView = (ListView) rootView.findViewById(R.id.pendingtasklistview);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        wishListAppSettings = getActivity().getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"font/Roboto-Light.ttf");
        Log.i("userTokencompletedtask", usertoken);
        itemsArrayList = new ArrayList<User_tasks>();
        mHandler = new Handler(Looper.getMainLooper());
        todoListAdaptor = new TodoListAdaptor(getContext(), itemsArrayList, typeface);
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
                final UserTask userTask = gson.fromJson(jsonResponse,UserTask.class);
                if(userTask.status.equals("True")){
                    for (User_tasks user_tasks : userTask.tasks) {
                        itemsArrayList.add(user_tasks);
                    }
                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), userTask.message, Toast.LENGTH_SHORT).show();

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
}