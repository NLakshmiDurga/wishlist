package com.example.murthyavanithsa.wishlistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by durga on 8/3/16.
 */
public class Logout extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_items);
        sharedPreferences = getSharedPreferences("WishListAppSettings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("user-token");
        editor.apply();
        Intent intent = new Intent(Logout.this,SignInActivity.class);
        startActivity(intent);
    }

}
