package durga.wishlist.murthyavanithsa.app;

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
//public class CompletedTasks extends UserTasks {
//    SharedPreferences wishListAppSettings;
//    String usertoken;
//    TodoListAdaptor todoListAdaptor;
//    private Handler mHandler;
//    ArrayList<User_tasks> itemsArrayList;
//    ListView listView;

//    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.completedtask, null, false);
//        drawerLayout.addView(contentView,0);
////        super.replaceContentLayout(R.layout.completedtask, R.id.content_frame);
//        wishListAppSettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
//        usertoken = wishListAppSettings.getString("token", "token is missing");
//        Log.i("userTokencompletedtask", usertoken);
//        listView = (ListView) findViewById(R.id.completetasklistview);
//        itemsArrayList = new ArrayList<User_tasks>();
//        mHandler = new Handler(Looper.getMainLooper());
//        todoListAdaptor = new TodoListAdaptor(this,itemsArrayList);
//        Urlendpoints urlendpoints = new Urlendpoints();
//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = new FormBody.Builder()
//                .add("token", usertoken)
//                .add("status","completed")
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
//                            Toast.makeText(CompletedTasks.this, userTask.message, Toast.LENGTH_SHORT).show();
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
//
//}
//class UserTaskResponse {
//    String status;
//    String message;
//    User_tasks[] tasks;
//
//}
//class User_tasks_response{
//    int task_id;
//    String task;
//    String status;
//    public User_tasks_response(int task_id,String task,String status){
//        this.task_id = task_id;
//        this.task = task;
//        this.status = status;
//    }
//}
public class CompletedTasks extends Fragment {
    SharedPreferences wishListAppSettings;
    String usertoken;
    //    TodoListAdaptor todoListAdaptor;
    CompletedTaskAdapter completedTaskAdapter;
    private Handler mHandler;
    ArrayList<User_tasks> itemsArrayList;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.completedtask, container, false);
        listView = (ListView) rootView.findViewById(R.id.completetasklistview);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        wishListAppSettings = getActivity().getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        usertoken = wishListAppSettings.getString("token", "token is missing");
        Log.i("userTokencompletedtask", usertoken);
        itemsArrayList = new ArrayList<User_tasks>();
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"font/Roboto-Light.ttf");
//        itemsArrayList = new ArrayList<String>();
        mHandler = new Handler(Looper.getMainLooper());
//        UserItemsResponse.SavedUserItems savedUserItems;
//        todoListAdaptor = new TodoListAdaptor(getContext(), itemsArrayList);
        completedTaskAdapter = new CompletedTaskAdapter(getContext(),itemsArrayList,typeface);
        Urlendpoints urlendpoints = new Urlendpoints();
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", usertoken)
                .add("status","completed")
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
                        listView.setAdapter(completedTaskAdapter);
                        completedTaskAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
