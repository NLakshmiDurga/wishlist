package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.murthyavanithsa.signinwithgoogle.MainActivity;
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
 * Created by durga on 4/3/16.
 */
class SignUpResponse{
    String status;
    String message;
    String user_token;
}
public class SignUp extends AppCompatActivity {
    EditText editnametext;
    EditText editemailtext;
    EditText editpasswordtext;
    SharedPreferences wishlistappsettings;
    Handler handler;
    String LOG_LABEL="SignUp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        final OkHttpClient client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
        editnametext = (EditText) findViewById(R.id.username);
        editemailtext = (EditText) findViewById(R.id.signupemailid);
        editpasswordtext = (EditText) findViewById(R.id.signuppassword);
        Button button = (Button) findViewById(R.id.signupbutton);
        final Urlendpoints urlendpoints = new Urlendpoints();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_LABEL, "username:" + editnametext.getText().toString());
                Log.i(LOG_LABEL, "email:" + editemailtext.getText().toString());
                Log.i(LOG_LABEL, "password:" + editpasswordtext.getText().toString());
                RequestBody formBody = new FormBody.Builder()
                        .add("username",editnametext.getText().toString())
                        .add("emailid", editemailtext.getText().toString())
                        .add("password", editpasswordtext.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(urlendpoints.getsignup())
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
                        SignUpResponse signUpResponse = gson.fromJson(jsonResponse, SignUpResponse.class);
                        wishlistappsettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = wishlistappsettings.edit();
                        editor.putString("token", signUpResponse.user_token);
                        editor.apply();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "Signed up successfully. Please login to continue", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignUp.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}