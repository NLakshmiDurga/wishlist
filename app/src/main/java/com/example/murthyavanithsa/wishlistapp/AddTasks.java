package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by durga on 24/2/16.
 */
public class AddTasks extends AppCompatActivity {
    EditText editText;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);
        Button button = (Button) findViewById(R.id.submitbutton);
        editText = (EditText) findViewById(R.id.addtask);
        final OkHttpClient client = new OkHttpClient();
        SharedPreferences sharedPreferences = getSharedPreferences("WishListAppSettings",MODE_PRIVATE);
        final String token = sharedPreferences.getString("token","token is missing");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.i("Text:", editText.getText().toString());
            Log.i("User token in add items", token);
            Toast.makeText(AddTasks.this, "Clicked green Floating Action Button", Toast.LENGTH_SHORT).show();
            RequestBody formBody = new FormBody.Builder()
                    .add("token", token)
                    .add("task",editText.getText().toString())
                    .build();
            Request request = new Request.Builder()
                    .url("http://192.168.2.12/index.php/usertasks/add_user_task")
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
//                Gson gson = new GsonBuilder().create();
                }
            });
            Intent addTasksIntent = new Intent(AddTasks.this,UserTasks.class);
            startActivity(addTasksIntent);
            }
        });
    }
}
