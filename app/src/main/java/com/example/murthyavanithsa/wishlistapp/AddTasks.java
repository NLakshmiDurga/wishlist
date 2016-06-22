package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by durga on 1/3/16.
 */
class AddTaskResponse
{
    String status;
}
public class AddTasks extends AppCompatActivity {
    EditText editText;
    static String usertoken;
    private Handler mHandler;
    SharedPreferences wishListAppSettings;
    AutoCompleteTextView textView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        mHandler = new Handler(Looper.getMainLooper());
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.app_name);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text_material_light));
//        String[] COUNTRIES = new String[] {
//                "Belgium", "France", "Italy", "Germany", "Spain"
//        };
        editText = (EditText) findViewById(R.id.addtask);
//        textView = (AutoCompleteTextView) findViewById(R.id.addtask);
//        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
//        textView.setAdapter(arrayAdapter);
        Button button = (Button) findViewById(R.id.submitbutton);
        final Urlendpoints urlendpoints = new Urlendpoints();
        final OkHttpClient client = new OkHttpClient();
        SharedPreferences sharedPreferences = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "token is missing");
//        final TextView textView = (TextView) findViewById(R.id.textView);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                RequestBody formBody = new FormBody.Builder()
//                        .add("search",s.toString())
//                        .build();
//                Request request = new Request.Builder()
//                        .url(urlendpoints.searchitems())
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
//                        final String jsonResponse = response.body().string();
//                        Log.i("User  search result", jsonResponse);
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                textView.setText(jsonResponse);
//                            }
//                        });
//
//                        // Gson gson = new GsonBuilder().create();
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tasktoadd = editText.getText().toString();
                Log.i("Text:", tasktoadd);
                Log.i("User token in add items", token);
//                Toast.makeText(AddTasks.this, "Clicked green Floating Action Button", Toast.LENGTH_SHORT).show();
                RequestBody formBody = new FormBody.Builder()
                        .add("token", token)
                        .add("task", tasktoadd)
                        .build();
                Request request = new Request.Builder()
                        .url(urlendpoints.getaddtasksurl())
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
                        Log.i("User add item response", jsonResponse);
                        Gson gson = new GsonBuilder().create();
                        final AddTaskResponse addTaskResponse = gson.fromJson(jsonResponse,AddTaskResponse.class);
                        if (addTaskResponse.status.equals("True"))
                        {
                            Intent addTasksIntent = new Intent(AddTasks.this, UserTasks.class);
                            finish();
                            startActivity(addTasksIntent);
                        }
//                        else {
//                            Toast.makeText(getBaseContext(), "Add your tasks", Toast.LENGTH_LONG).show();
//                        }
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
        Log.i("userToken addtask pause", usertoken);
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
        // your code.
        Intent usertask = new Intent(AddTasks.this, UserTasks.class);
        usertask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(usertask);
        finish();
    }
}