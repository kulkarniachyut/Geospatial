package com.example.achi.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

/**
 * Created by achi on 11/5/16.
 */

public class LogoActivity extends AppCompatActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        SharedPreferences sharedPref = this
                .getPreferences(Context.MODE_PRIVATE);
        String token = sharedPref.getString("XauthToken", null);
        if (token != null)
        {

        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

}
