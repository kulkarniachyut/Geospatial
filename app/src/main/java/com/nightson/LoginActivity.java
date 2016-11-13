package com.nightson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{

    private void getAuthToken(String email, String password)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {   JSONObject jsonObj = null ;
                try {
                   jsonObj= new JSONObject(response);
                    SharedPreferences.Editor e = getSharedPreferences(Constants.PREF_FILE_NAME,0).edit();
                    e.putString("x-auth-token" ,jsonObj.getString("token"));
                    e.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Working API", "3");
                Log.d("response", response);
                Intent registerIntent = new Intent(LoginActivity.this,
                        MapActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        };

        Response.ErrorListener ErrorresponseListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Error", String.valueOf(error));

            }

        };
        LoginRequest registerRequest = new LoginRequest(email, password,
                responseListener, ErrorresponseListener);
        VolleyHelper.getInstance(getApplicationContext());
        VolleyHelper vh1 = VolleyHelper.getInstance(getApplicationContext());
        vh1.getRequestQueue().add(registerRequest);
    }

    private void dataProcess()
    {

        Intent I = getIntent();
        if (I.getStringExtra("email") != null)
        {
            String password = I.getStringExtra("password");
            String email = I.getStringExtra("email");
            getAuthToken(email, password);

        }
        else
        {
            final EditText emailText = (EditText) findViewById(R.id.etEmail);
            final EditText pass = (EditText) findViewById(R.id.etPassword);

            final Button loginBtn = (Button) findViewById(R.id.btnLogin);

            final TextView registerHereTV = (TextView) findViewById(
                    R.id.tvRegisterhere);

            registerHereTV.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent registerIntent = new Intent(LoginActivity.this,
                            RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);

                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String email = emailText.getText().toString();
                    final String password = pass.getText().toString();
                    getAuthToken(email, password);
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.dataProcess();

    }

    protected void onResume(Bundle savedInstanceState)
    {
        super.onResume();
        this.dataProcess();
    }

}