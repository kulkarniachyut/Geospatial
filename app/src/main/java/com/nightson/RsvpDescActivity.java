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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class RsvpDescActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rsvp_desc_avtivity);
        String name = getIntent().getStringExtra("name");
        String start = getIntent().getStringExtra("starttime");
        String end = getIntent().getStringExtra("endtime");
        Date startDate = new Date(Long.parseLong(start) * 1000);
        Date endDate = new Date(Long.parseLong(end) * 1000);
        String url = getIntent().getStringExtra("url");
        Log.d("photo url " , url);

        TextView nameText = (TextView) findViewById(R.id.name);
        nameText.setText(name);

        ImageView imageview = (ImageView) findViewById(R.id.eventImage);
        Picasso.with(getApplicationContext()).load(url).into(imageview);

        EditText startdate = (EditText) findViewById(R.id.startDate);
        EditText enddate = (EditText) findViewById(R.id.endDate);
        startdate.setText("Start Date : " + startDate);
        enddate.setText("End Date : " + endDate);

        CheckBox check = (CheckBox) findViewById(R.id.rsvp);
        boolean ischecked = (getIntent().getIntExtra("rsvp" , 0) == 1);
        final String id = getIntent().getStringExtra("id");

        check.setChecked(ischecked);


            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE_NAME,0);
                    final String token = preferences.getString("x-auth-token","None");
                    Log.d("ischecked", String.valueOf(isChecked));
                    if(isChecked) {
                        Response.Listener<String> responseListener = new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d("response", response);
                                Context context = getApplicationContext();
                                CharSequence text = "unRSVP'd";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                CheckBox check = (CheckBox) findViewById(R.id.rsvp);
//                                check.setChecked(false);

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
                        Log.d("id" , id);
                        UpdateRsvp rsvpEventsRequest = new UpdateRsvp(id,
                                token, responseListener,ErrorresponseListener );
                        VolleyHelper.getInstance(getApplicationContext());
                        VolleyHelper vh = VolleyHelper
                                .getInstance(getApplicationContext());
                        vh.getRequestQueue().add(rsvpEventsRequest);
                    }
                  else {
                        Response.Listener<String> responseListener1 = new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.d("response", response);
                            }
                        };

                        Response.ErrorListener ErrorresponseListener1 = new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                Log.d("Error", String.valueOf(error));
                            }

                        };
                        Log.d("Token" , token);
                        Log.d("id" , id);
                        UnsubscribeRsvp rsvpEventsRequest1 = new UnsubscribeRsvp(id,
                                token, responseListener1, ErrorresponseListener1 );
                        VolleyHelper.getInstance(getApplicationContext());
                        VolleyHelper vh = VolleyHelper
                                .getInstance(getApplicationContext());
                        vh.getRequestQueue().add(rsvpEventsRequest1);

                    }




                }
            });






    }
}
