package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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

class LoginResponse{
        String status;
        String message;
        String token;
}

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener  {
    EditText editemailtext;
    EditText editpasswordtext;
    Handler handler;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    String emailid,password;
    SharedPreferences wishlistappsettings;
    final OkHttpClient client = new OkHttpClient();
    Urlendpoints urlendpoints = new Urlendpoints();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        TextView signuptextview= (TextView) findViewById(R.id.textviewsignup);
        signuptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(SignInActivity.this,SignUp.class);
                startActivity(signupIntent);
                finish();
            }
        });
        handler = new Handler(Looper.getMainLooper());
        editemailtext = (EditText) findViewById(R.id.emailid);
        editpasswordtext = (EditText) findViewById(R.id.password);
        Button loginbutton = (Button) findViewById(R.id.loginButton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailid = editemailtext.getText().toString();
                password = editpasswordtext.getText().toString();
                Log.i(TAG, "email:" + emailid);
                Log.i(TAG, "password:" + password);
                if(Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
                    // e-mail is valid
                    if (emailid.isEmpty() || password.isEmpty()){
                        Toast.makeText(SignInActivity.this, "Please give valid inputs (emailid,password)", Toast.LENGTH_LONG).show();
                    }
                    else{
                        userSignIn(emailid, password);
                    }
                }
                else {
                    // e-mail is invalid
                    Toast.makeText(SignInActivity.this, "Please enter correct email address", Toast.LENGTH_LONG).show();
                }
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            emailid = acct.getEmail();
            password = "";
            Log.i("email",emailid);
            userSignIn(emailid,password);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getBaseContext(), "Not logged in", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void signIn() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    public void userSignIn(String emailid,String password){
        RequestBody formBody = new FormBody.Builder()
                .add("emailid", emailid)
                .add("password", password)
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wishlistappsettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
                        SharedPreferences.Editor editor = wishlistappsettings.edit();
                        editor.putString("token", loginResponse.token);
                        editor.apply();
                        String token = wishlistappsettings.getString("token", "token is missing");
                        if (loginResponse.token.equals(token)) {
                            Toast.makeText(getBaseContext(), "Successfully logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignInActivity.this, UserTasks.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "login correctly", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}