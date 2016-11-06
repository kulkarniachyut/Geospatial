package com.example.achi.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//
        final EditText fname = (EditText) findViewById(R.id.etFirstName);
        final EditText lname = (EditText) findViewById(R.id.etLastName);
        final EditText emailText = (EditText) findViewById(R.id.etEmail);
        final EditText pass = (EditText) findViewById(R.id.etPassword);
        final EditText phone = (EditText) findViewById(R.id.etPhoneNumber);

        Button registerBtn = (Button) findViewById(R.id.btnRegister);

//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
////               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        final Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        final double longitude = location.getLongitude();
//        final double latitude = location.getLatitude();
//        Date currentTime = new Date();
//        final String epochTime = (currentTime.getTime())+"";
//        final String Slongitude = longitude+"";
//        final String Slatitude = latitude+"";



        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("working" , "1");
                final String first_name = fname.getText().toString();
                final String last_name = lname.getText().toString();
                final String email = emailText.getText().toString();
                final String password = pass.getText().toString();
                final String phone_number = phone.getText().toString();
                Log.d("working" , "2");


                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse( String response) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        RegisterActivity.this.startActivity(intent);
                        System.out.println("Working API");
                        Log.d("Working API" , "3");
                        Log.d("response" , response);


                    }

                };

                Response.ErrorListener ErrorresponseListener = new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse( VolleyError error) {

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        Log.d("Error" , String.valueOf(error));

                    }

                };

                RegisterRequest registerRequest = new RegisterRequest(first_name, last_name, email,password, phone_number,responseListener);
                VolleyHelper.getInstance(getApplicationContext());
                VolleyHelper vh = VolleyHelper.getInstance(getApplicationContext());
                vh.getRequestQueue().add(registerRequest);


            }
        });
    }

}