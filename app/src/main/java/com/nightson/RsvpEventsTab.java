package com.nightson;

/**
 * Created by achi on 11/13/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class RsvpEventsTab extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.rsvp_events_tab, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREF_FILE_NAME,0);
        final String token = preferences.getString("x-auth-token","None");
        Response.Listener<String> responseListener = new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                System.out.println("Working API");
                Log.d("Working API", "3");
                Log.d("response", response);
                Context context = getContext();
                CharSequence text = "RSvp events Tab !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                JSONObject respobj;
                ArrayList<OwnedEventObject> mArrayList = new ArrayList<>();
                Log.d("json" , String.valueOf(response.getClass()));
                try {
                    JSONArray jarray = new JSONArray(String.valueOf(response));
//                    Log.d("jsonarray" , String.valueOf(jarray.getJSONObject(0)));
                    if(jarray.length() >0 ) {
                        for (int i=0;i<jarray.length();i++){
                            JSONObject obj = (jarray.getJSONObject(i));
                            String id = obj.getString("id");
                            String name = obj.getString("name");
                            String etime = obj.getString("end_time");
                            String stime = obj.getString("start_time");
                            String location1 = obj.getString("location");
                            Log.d("location" ,location1);

//                        JSONObject loc = location1.getJSONObject(0);
//                        String cordinatess = loc.getString("coordinates");
                            String[] parts = location1.split("\\[");
                            String location = parts[1];
                            String[] newloc = location.split(",");
                            String latitude = newloc[0];
                            String[] newlong = newloc[1].split("\\]");
                            String longitude = newlong[0];



                            Log.d("JSON string", longitude);
                            Log.d("JSON string", latitude);
                            Log.d("JSON class", String.valueOf(latitude.getClass()));

//                        String lat = loc.getString("name");
//                        String lon = obj.getString("name");

                            Date StartTime = new Date(Long.parseLong(stime) * 1000);
                            Date endTime = new Date(Long.parseLong(etime) * 1000);
                            mArrayList.add(new OwnedEventObject(StartTime, endTime , name , latitude,longitude,id));
                    }

                    }
                    else {
                        String name = "No events Rsvp'd";
                        Date endTime = new Date();
                        Date StartTime = new Date();
                        String latitude = "110";
                        String longitude = "-118";

                        mArrayList.add(new OwnedEventObject(StartTime, endTime , name , latitude,longitude,"1"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ListView mListView = (ListView) rootView.findViewById(R.id.ownedlistview);

//                        mArrayList.add(new OwnedEventObject(new Date(),new Date(),"Achyut","1","2"));
                OwnedAdapter kAdapter = new OwnedAdapter(getContext(),mArrayList,false);
                mListView.setAdapter(kAdapter);

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
        RsvpEventsRequest rsvpEventsRequest = new RsvpEventsRequest(
                token, responseListener,ErrorresponseListener );
        VolleyHelper.getInstance(getContext());
        VolleyHelper vh = VolleyHelper
                .getInstance(getContext());
        vh.getRequestQueue().add(rsvpEventsRequest);




        return rootView;
    }

}


