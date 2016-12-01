package com.nightson;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nightson.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class RegisterActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = LoginActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        Log.d("reaches here !", "maybe");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fname = (EditText) findViewById(R.id.etFirstName);
        final EditText lname = (EditText) findViewById(R.id.etLastName);
        final EditText emailText = (EditText) findViewById(R.id.etEmail);
        final EditText pass = (EditText) findViewById(R.id.etPassword);
        final EditText phone = (EditText) findViewById(R.id.etPhoneNumber);
        Log.d("reaches here 2!", "maybe2");
        Button registerBtn = (Button) findViewById(R.id.btnRegister);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        Log.d("location", String.valueOf(location));
        Log.d("location", "?");

        registerBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Log.d("working", "1");

                final String first_name = fname.getText().toString();
                final String last_name = lname.getText().toString();
                final String email = emailText.getText().toString();
                final String password = pass.getText().toString();
                final String phone_number = phone.getText().toString();
                Log.d("working", "2");
                //                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.d("try 2 location", String.valueOf(location));
                Log.d("location", "?");

                Response.Listener<String> responseListener = new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response)
                    {
                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        RegisterActivity.this.startActivity(intent);
                        System.out.println("Working API");
                        Log.d("Working API", "3");
                        Log.d("response", response);

                    }

                };

                Response.ErrorListener ErrorresponseListener = new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        Intent intent = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        Log.d("Error", String.valueOf(error));

                    }

                };

                RegisterRequest registerRequest = new RegisterRequest(
                        first_name, last_name, email, password, phone_number,
                        responseListener, ErrorresponseListener);
                VolleyHelper.getInstance(getApplicationContext());
                VolleyHelper vh = VolleyHelper
                        .getInstance(getApplicationContext());
                vh.getRequestQueue().add(registerRequest);

            }
        });

    }

    private void handleNewLocation(Location location)
    {
        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        Log.d("Longitude", String.valueOf(currentLongitude));
        Log.d("Latitude", String.valueOf(currentLatitude));
    }

    @Override
    public void onConnected(
            @Nullable
                    Bundle bundle)
    {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (location == null)
        {
            Log.i(TAG, "idk wtf is the problem !");
            // Blank for a moment...
        }
        else
        {
            Log.i(TAG, "Handling the locations.");
            handleNewLocation(location);

        }
        ;

    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG, "Location services suspended. Please reconnect.");

    }

    @Override
    public void onConnectionFailed(
            @NonNull
                    ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Log.i(TAG, "Location services connection failed with code "
                    + connectionResult.getErrorCode());
        }

    }
}