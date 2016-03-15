package com.example.murthyavanithsa.wishlistapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.IOException;
import java.util.Arrays;
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
    String LOG_LABEL = "MainActivity";
    Handler handler;
    SharedPreferences wishlistappsettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);
        final OkHttpClient client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
        editemailtext = (EditText) findViewById(R.id.emailid);
        editpasswordtext = (EditText) findViewById(R.id.password);
        Button loginbutton = (Button) findViewById(R.id.loginButton);
        Button signupbutton = (Button) findViewById(R.id.signup);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_LABEL, "email:" + editemailtext.getText().toString());
                Log.i(LOG_LABEL, "password:" + editpasswordtext.getText().toString());
                Urlendpoints urlendpoints = new Urlendpoints();
                Log.i("login url end point", urlendpoints.getloginurl());
                RequestBody formBody = new FormBody.Builder()
                        .add("emailid", editemailtext.getText().toString())
                        .add("password", editpasswordtext.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(urlendpoints.getloginurl())
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
                        final LoginResponse loginResponse = gson.fromJson(jsonResponse, LoginResponse.class);
                        Log.i("Status", loginResponse.status);
                        Log.i("Message", loginResponse.message);
                        Log.i("token", loginResponse.token);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                wishlistappsettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
                                SharedPreferences.Editor editor = wishlistappsettings.edit();
                                editor.putString("token", loginResponse.token);
                                editor.apply();
                                String token = wishlistappsettings.getString("token", "token is missing");
                                if (loginResponse.token.equals(token))
                                {
                                    Toast.makeText(getBaseContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, UserTasks.class);
                                    finish();
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getBaseContext(), "login correctly", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainActivity.this, SignUp.class);
                startActivity(signup);
            }
        });
    }
}
