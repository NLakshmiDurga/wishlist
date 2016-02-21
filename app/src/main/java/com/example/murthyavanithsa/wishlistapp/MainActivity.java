package com.example.murthyavanithsa.wishlistapp;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Headers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


class LoginResponse{
        String status;
        String message;
        String token;
}

public class MainActivity extends AppCompatActivity {
    EditText editemailtext;
    EditText editpasswordtext;
    String LOG_LABEL="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        final OkHttpClient client = new OkHttpClient();
        editemailtext = (EditText) findViewById(R.id.emailid);
        editpasswordtext = (EditText) findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.submitbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Log.i(LOG_LABEL,"email:"+editemailtext.getText().toString());
                        Log.i(LOG_LABEL,"email:"+editpasswordtext.getText().toString());

                        RequestBody formBody = new FormBody.Builder()
                                .add("emailid", editemailtext.getText().toString())
                                .add("password", editpasswordtext.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url("http://192.168.2.12/index.php/users/login")
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
                                Log.i("Response", jsonResponse);
                                Gson gson = new GsonBuilder().create();
                                LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
                                String token = loginResponse.token;
                                Log.i("Status", loginResponse.status);
                                Log.i("Message", loginResponse.message);
                                Log.i("token", loginResponse.token);
                                // first parameter is the context, second is the class of the activity to launch
                                Intent intent = new Intent(MainActivity.this, UserItems.class);
                                intent.putExtra("token", token);
                                startActivity(intent);
//                                finish();
                            }
                        });
                    }
                });

    }




}
