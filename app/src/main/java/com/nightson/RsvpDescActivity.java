package com.nightson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class RsvpDescActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rsvp_desc_avtivity);
        String name = getIntent().getStringExtra("name");
        String start = getIntent().getStringExtra("starttime");
        String end = getIntent().getStringExtra("endtime");
        TextView nameText = (TextView) findViewById(R.id.name);
        nameText.setText(name);
        TextView startText = (TextView) findViewById(R.id.starttime);
        startText.setText(start);
        TextView endText = (TextView) findViewById(R.id.endtime);
        endText.setText(end);
        CheckBox check = (CheckBox) findViewById(R.id.rsvp);
        boolean ischecked = (getIntent().getIntExtra("rsvp" , 0) == 1);
        final String id = getIntent().getStringExtra("id");

        check.setChecked(ischecked);
        if(check.isChecked()) {

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE_NAME,0);
                    final String token = preferences.getString("x-auth-token","None");
                    Response.Listener<String> responseListener = new Response.Listener<String>()
                    {

                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("response", response);
                            Context context = getApplicationContext();
                            CharSequence text = "Password Updated succesfully !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            CheckBox check = (CheckBox) findViewById(R.id.rsvp);
                            check.setChecked(false);

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
                    Log.d("Token" , token);
                    UnsubscribeRsvp rsvpEventsRequest = new UnsubscribeRsvp(id,
                            token, responseListener,ErrorresponseListener );
                    VolleyHelper.getInstance(getApplicationContext());
                    VolleyHelper vh = VolleyHelper
                            .getInstance(getApplicationContext());
                    vh.getRequestQueue().add(rsvpEventsRequest);




                }
            });

        }
        else {

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE_NAME,0);
                    final String token = preferences.getString("x-auth-token","None");

                    Response.Listener<String> responseListener = new Response.Listener<String>()
                    {

                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("response", response);
                            Context context = getApplicationContext();
                            CharSequence text = "Password Updated succesfully !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            CheckBox check = (CheckBox) findViewById(R.id.rsvp);
                            check.setChecked(true);
//                            check.setChecked(1);

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
                    Log.d("Token" , token);
                    UpdateRsvp rsvpEventsRequest = new UpdateRsvp(id,
                            token, responseListener,ErrorresponseListener );
                    VolleyHelper.getInstance(getApplicationContext());
                    VolleyHelper vh = VolleyHelper
                            .getInstance(getApplicationContext());
                    vh.getRequestQueue().add(rsvpEventsRequest);



                }
            });

        }





    }
}
