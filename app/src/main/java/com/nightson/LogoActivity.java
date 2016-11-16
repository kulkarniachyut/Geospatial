package com.nightson;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;


public class LogoActivity extends AppCompatActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        SharedPreferences sharedPref = this
                .getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString("x-auth-Token", null);
        if (token != null)
        {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
//        finish();
    }

}
