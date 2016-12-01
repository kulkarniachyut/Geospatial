package com.nightson;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.zip.Inflater;

import static com.nightson.R.id.container;
import static com.nightson.R.id.start;

/**
 * Created by achi on 11/14/16.
 */

public class OwnedAdapter extends BaseAdapter
{

    String newName;

    ArrayList<OwnedEventObject> mOwnedEventObjects;
    Context mContext;
    boolean isOwned;

    public OwnedAdapter(Context mContext,
            ArrayList<OwnedEventObject> mOwnedObjectList, boolean isOwned)
    {
        this.mOwnedEventObjects = mOwnedObjectList;
        this.mContext = mContext;
        this.isOwned = isOwned;
    }

    @Override
    public int getCount()
    {
        return mOwnedEventObjects.size();
    }

    @Override
    public OwnedEventObject getItem(int position)
    {
        return mOwnedEventObjects.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.owned_event_item, null);
        final OwnedEventObject mObject = getItem(position);
        TextView ownedName = (TextView) view.findViewById(R.id.ownedname);
        ownedName.setText("Event Name : " + mObject.getmName());
        TextView startTime = (TextView) view.findViewById(R.id.starttime);
        startTime.setText(
                "Start Time : " + String.valueOf(mObject.getmStartDate()));
        TextView endTime = (TextView) view.findViewById(R.id.endtime);
        endTime.setText("End Time : " + String.valueOf(mObject.getmEndTime()));

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                callPopup(inflater, mObject, position);
            }
        });
        return view;
    }

    public void updateResults(ArrayList<OwnedEventObject> results)
    {
        mOwnedEventObjects = results;
        //Triggers the list update
        notifyDataSetChanged();

    }

    public void updateEvent(String name, String starttime, String endtime,
            String latitude, String longitude, String id)
    {

        DateFormat format = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
        Date date = new Date();
        long startTime = 0;
        long endTime = 0;
        Timestamp TimeStampStart;
        Timestamp TimeStampend;
        Log.d(starttime, "start");
        Log.d(endtime, "end");
        try
        {

            date = format.parse(starttime.trim());
            startTime = (date.getTime());
            startTime /= 1000;
            Log.d(String.valueOf(startTime), "start");
            date = format.parse(endtime.trim());
            endTime = date.getTime();
            endTime /= 1000;
            Log.d(String.valueOf(endTime), "end");

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        SharedPreferences preferences = mContext
                .getSharedPreferences(Constants.PREF_FILE_NAME, 0);
        final String token = preferences.getString("x-auth-token", "None");

        Response.Listener<String> responseListener = new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                Log.d("response", response);
                Context context = mContext;
                CharSequence text = "RSvp events Tab !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                //                toast.show();
                //
                //                JSONObject respobj;
                //                ArrayList<OwnedEventObject> mArrayList = new ArrayList<>();
                //                Log.d("json" , String.valueOf(response.getClass()));
                //                try {
                //                    JSONArray jarray = new JSONArray(String.valueOf(response));
                ////                    Log.d("jsonarray" , String.valueOf(jarray.getJSONObject(0)));
                //                    if(jarray.length() >0 ) {
                //                        for (int i=0;i<jarray.length();i++){
                //                            JSONObject obj = (jarray.getJSONObject(i));
                //                            String id = obj.getString("id");
                //                            String name = obj.getString("name");
                //                            String etime = obj.getString("end_time");
                //                            String stime = obj.getString("start_time");
                //                            String location1 = obj.getString("location");
                //                            Log.d("location" ,location1);
                //
                ////                        JSONObject loc = location1.getJSONObject(0);
                ////                        String cordinatess = loc.getString("coordinates");
                //                            String[] parts = location1.split("\\[");
                //                            String location = parts[1];
                //                            String[] newloc = location.split(",");
                //                            String latitude = newloc[0];
                //                            String[] newlong = newloc[1].split("\\]");
                //                            String longitude = newlong[0];
                //
                //
                //
                //                            Log.d("JSON string", longitude);
                //                            Log.d("JSON string", latitude);
                //                            Log.d("JSON class", String.valueOf(latitude.getClass()));
                //
                ////                        String lat = loc.getString("name");
                ////                        String lon = obj.getString("name");
                //
                //                            Date StartTime = new Date(Long.parseLong(stime) * 1000);
                //                            Date endTime = new Date(Long.parseLong(etime) * 1000);
                //                            mArrayList.add(new OwnedEventObject(StartTime, endTime , name , latitude,longitude,id));
                //                        }
                //
                //                    }
                //                    else {
                //                        String name = "No events Rsvp'd";
                //                        Date endTime = new Date();
                //                        Date StartTime = new Date();
                //                        String latitude = "110";
                //                        String longitude = "-118";
                //
                //                        mArrayList.add(new OwnedEventObject(StartTime, endTime , name , latitude,longitude,"1"));
                //                    }
                //
                //                }
                //                catch (JSONException e) {
                //                    e.printStackTrace();
                //                }

                //
                //                ListView mListView = (ListView) rootView.findViewById(R.id.ownedlistview);
                //
                ////                        mArrayList.add(new OwnedEventObject(new Date(),new Date(),"Achyut","1","2"));
                //                OwnedAdapter kAdapter = new OwnedAdapter(getContext(),mArrayList,false);
                //                mListView.setAdapter(kAdapter);

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

        Log.d("Token", token);
        Log.d("lat", latitude);
        Log.d("long", longitude);
        Log.d("id", id);

        UpdateEventsRequest updateEventsRequest = new UpdateEventsRequest(name,
                String.valueOf(startTime), String.valueOf(endTime), latitude,
                longitude, id, token, responseListener, ErrorresponseListener);
        VolleyHelper.getInstance(mContext);
        VolleyHelper vh = VolleyHelper.getInstance(mContext);
        vh.getRequestQueue().add(updateEventsRequest);

    }

    private void callPopup(LayoutInflater layoutInflater,
            final OwnedEventObject mObject, final int position)
    {

        if (isOwned)
        {
            View popupView = layoutInflater
                    .inflate(R.layout.event_popup1, null);
            final PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER_VERTICAL,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final EditText mname = (EditText) popupView
                    .findViewById(R.id.ownedname1);
            final EditText mstart = (EditText) popupView
                    .findViewById(R.id.starttime1);
            final EditText mend = (EditText) popupView
                    .findViewById(R.id.endtime1);
            final EditText mlat = (EditText) popupView
                    .findViewById(R.id.latitude);
            final EditText mlong = (EditText) popupView
                    .findViewById(R.id.longitude);

            mname.setText("Event Name : " + mObject.getmName());
            mstart.setText(
                    "Start Time : " + String.valueOf(mObject.getmStartDate()));
            mend.setText("End Time : " + String.valueOf(mObject.getmEndTime()));
            mlat.setText("Latitude : " + mObject.getMlatitude());
            mlong.setText("Longitude : " + mObject.getMlongitude());

            ((Button) popupView.findViewById(R.id.done))
                    .setOnClickListener(new View.OnClickListener()
                    {

                        public void onClick(View arg0)
                        {

                            popupWindow.dismiss();
                        }
                    });

            ((Button) popupView.findViewById(R.id.Edit))
                    .setOnClickListener(new View.OnClickListener()
                    {

                        public void onClick(View arg0)
                        {

                            //                         popupWindow.dismiss();

                            DateFormat format = new SimpleDateFormat(
                                    "EEE MMM dd hh:mm:ss z yyyy");
                            Date date = new Date();
                            Date date1 = new Date();

                            try
                            {
                                date = format.parse(mstart.getText().toString()
                                        .split("e :")[1].trim());
                                date1 = format.parse(mend.getText().toString()
                                        .split("e :")[1].trim());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            mOwnedEventObjects.get(position).mName = mname
                                    .getText().toString().split(":")[1];
                            mOwnedEventObjects.get(position).mStartDate = date;
                            mOwnedEventObjects.get(position).mEndTime = date1;
                            mOwnedEventObjects.get(position).mlatitude = mlat
                                    .getText().toString();
                            mOwnedEventObjects.get(position).mlongitude = mlong
                                    .getText().toString();

                            updateEvent(
                                    mname.getText().toString().split(":")[1],
                                    mstart.getText().toString().split("e :")[1],
                                    mend.getText().toString().split("e :")[1],
                                    mlat.getText().toString().split(":")[1],
                                    mlong.getText().toString().split(":")[1],
                                    mObject.getmEventId());

                            popupWindow.dismiss();

                        }
                    });
        }
        else
        {
            View popupView = layoutInflater.inflate(R.layout.event_popup, null);
            final PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.showAtLocation(popupView, Gravity.CENTER_VERTICAL,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            TextView mname = (TextView) popupView.findViewById(R.id.ownedname1);
            TextView mstart = (TextView) popupView
                    .findViewById(R.id.starttime1);
            TextView mend = (TextView) popupView.findViewById(R.id.endtime1);
            TextView mlat = (TextView) popupView.findViewById(R.id.latitude);
            TextView mlong = (TextView) popupView.findViewById(R.id.longitude);

            mname.setText("Event Name : " + mObject.getmName());
            mstart.setText(
                    "Start Time : " + String.valueOf(mObject.getmStartDate()));
            mend.setText("End Time : " + String.valueOf(mObject.getmEndTime()));
            mlat.setText("Latitude : " + mObject.getMlatitude());
            mlong.setText("Longitude : " + mObject.getMlongitude());

            ((Button) popupView.findViewById(R.id.done))
                    .setOnClickListener(new View.OnClickListener()
                    {

                        public void onClick(View arg0)
                        {

                            popupWindow.dismiss();
                        }
                    });

        }

    }

}
