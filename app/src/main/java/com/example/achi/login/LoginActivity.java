package com.example.achi.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {


    private void getAuthToken(String email, String password){
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("Working API");
                Log.d("Working API", "3");
                Log.d("response", response);

            }
        };

        Response.ErrorListener ErrorresponseListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse( VolleyError error) {
                Log.d("Error" , String.valueOf(error));

            }

        };



        LoginRequest registerRequest = new LoginRequest(email, password, responseListener);
        VolleyHelper.getInstance(getApplicationContext());
        VolleyHelper vh1 = VolleyHelper.getInstance(getApplicationContext());
        vh1.getRequestQueue().add(registerRequest);


    }

    private void dataProcess() {

        Intent I = getIntent();
        if(I.getStringExtra("email") != null) {
            String password = I.getStringExtra("password");
            String email = I.getStringExtra("email");
            getAuthToken(email,password);


        }
        else {
            final EditText emailText = (EditText) findViewById(R.id.etEmail);
            final EditText pass = (EditText) findViewById(R.id.etPassword);

            final Button loginBtn = (Button) findViewById(R.id.btnLogin);

            final TextView registerHereTV = (TextView) findViewById(R.id.tvRegisterhere);

            registerHereTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerIntent = new Intent(LoginActivity.this , RegisterActivity.class);
                    LoginActivity.this.startActivity(registerIntent);

                }
            });

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = emailText.getText().toString();
                    final String password = pass.getText().toString();
                    getAuthToken(email,password);


                }
            });

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.dataProcess();

    }
    protected void onResume(Bundle savedInstanceState) {
        this.dataProcess();
    }







}