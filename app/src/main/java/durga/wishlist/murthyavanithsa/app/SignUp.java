package durga.wishlist.murthyavanithsa.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wishlist.murthyavanithsa.app.R;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
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
    String existemailid;
}
public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener {
    private EditText editnametext, editemailtext, editpasswordtext;
    private String username,emailid,password,type;
    private SharedPreferences wishlistappsettings;
    private Typeface face;
    private static final String TAG = "SignUpActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private final Urlendpoints urlendpoints = new Urlendpoints();
    private final OkHttpClient client = new OkHttpClient();
    private Handler handler;
    private String LOG_LABEL="SignUp";
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.sign_up);
        face = Typeface.createFromAsset(getAssets(), "font/Roboto-Light.ttf");
//        textView = new TextView(getApplicationContext());
//        ActionBar actionBar = getSupportActionBar();
//        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        textView.setLayoutParams(layoutparams);
//        textView.setText("WishList");
//        textView.setTextSize(20);
        //To set font created a directory called assets in that font directory is created.
//        textView.setTypeface(face);
//        textView.setTextColor(getResources().getColor(R.color.textColorPrimary));
//        textView.setGravity(Gravity.CENTER);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(textView);
        textView = (TextView) findViewById(R.id.listtv);
        textView.setTypeface(face);
        textView = (TextView) findViewById(R.id.signuptv);
        textView.setTypeface(face);
        handler = new Handler(Looper.getMainLooper());
        editnametext = (EditText) findViewById(R.id.username);
        editemailtext = (EditText) findViewById(R.id.emailid);
        editpasswordtext = (EditText) findViewById(R.id.password);
        editnametext.setTypeface(face);
        editemailtext.setTypeface(face);
        editpasswordtext.setTypeface(face);
        TextView textViewtosignin = (TextView) findViewById(R.id.textviewtosignin);
        textViewtosignin.setTypeface(face);
        textViewtosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                existingUser();
            }
        });
        Button button = (Button) findViewById(R.id.signupbutton);
        button.setTypeface(face);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editnametext.getText().toString();
                emailid = editemailtext.getText().toString();
                password = editpasswordtext.getText().toString();
                type = "wishlistlogin";
                Log.i(LOG_LABEL, "username:" + username);
                Log.i(LOG_LABEL, "email:" + emailid);
                Log.i(LOG_LABEL, "password:" + password);
                Log.i(LOG_LABEL, "type:" + type);
                if(Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
                    // e-mail is valid
                    if (username.isEmpty() || emailid.isEmpty() || password.isEmpty()){
                        Toast.makeText(SignUp.this, "Please give valid inputs (username,emailid,password)", Toast.LENGTH_LONG).show();
                        editnametext.getText().clear();
                        editemailtext.getText().clear();
                        editpasswordtext.getText().clear();
                        editnametext.requestFocus();
                    }
                    else{
                        userSignUp(username, emailid, password, type);
                    }
                }
                else {
                    // e-mail is invalid
                    editemailtext.requestFocus();
                    Toast.makeText(SignUp.this, "Please enter correct email address", Toast.LENGTH_LONG).show();
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
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_up_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        setGooglePlusButtonText(signInButton,"Signup with Google");
//        signInButton.setColorScheme(getResources().getColor(R.color.colorAccent));
        findViewById(R.id.sign_up_button).setOnClickListener(this);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            username = acct.getDisplayName();
            emailid = acct.getEmail();
//            String id = acct.getIdToken();
            password = "";
            type = "googlelogin";
            Log.i("name",username);
            Log.i("email",emailid);
            userSignUp(username, emailid, password, type);
//            Log.i("id",id);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG).show();
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

    private void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTypeface(face);
                tv.setTextSize(20);
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                signIn();
                break;
        }
    }

    public void userSignUp(final String username, final String emailid, String password, String type){
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("emailid", emailid)
                .add("password", password)
                .add("type", type)
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
                Log.i("signup Response", jsonResponse);
                Gson gson = new GsonBuilder().serializeNulls().create();
                final SignUpResponse signUpResponse = gson.fromJson(jsonResponse, SignUpResponse.class);
                Log.i(LOG_LABEL,"signupresponse" + signUpResponse.toString());
                Log.i(LOG_LABEL,"status" +signUpResponse.status);
                Log.i(LOG_LABEL,"message" +signUpResponse.message);
                if (signUpResponse.status.equals("True")) {
                    wishlistappsettings = getSharedPreferences("WishListAppSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = wishlistappsettings.edit();
                    editor.putString("token", signUpResponse.user_token);
                    editor.putString("name",username);
                    editor.putString("emilid",emailid);
                    editor.apply();
                    newUser();
                } else {
                    SignUp.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, signUpResponse.message, Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });
    }
    public void newUser(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignUp.this, UserTasks.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void existingUser(){
        Intent intent = new Intent(SignUp.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}