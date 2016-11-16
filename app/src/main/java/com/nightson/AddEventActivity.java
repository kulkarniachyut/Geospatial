package com.nightson;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.fragment;


public class AddEventActivity extends AppCompatActivity implements CreateEventFragment.onButtonClickListener, AddressFragment.onBackButtonClickListener,CreateEventFragment.onSubmitClickListener {

Fragment event_fragment, map_fragment;
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        FragmentManager fm = getSupportFragmentManager();

        event_fragment = fm.findFragmentById(R.id.Fragment2);
        if (event_fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            event_fragment =new CreateEventFragment();
            ft.add(R.id.frag1,event_fragment,"Fragment2");
            ft.commit();

        }



    }

    @Override
    public void onbutton(int number) {
        Log.d("toast ","sindhu");
        if (number == 1)
        {
            FragmentManager fm = getSupportFragmentManager();
            map_fragment = fm.findFragmentById(R.id.Fragment1);
            if (map_fragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                map_fragment =new AddressFragment();
                ft.add(R.id.frag1,map_fragment,"Fragment1");
                ft.hide(event_fragment);
                ft.commit();
            }

        }
    }

    @Override
    public void onmapclick(Double currlat, Double currlng) {
        Log.d("toastsindhu ",currlat.toString());
            String latlng = currlat.toString()+":"+currlng.toString();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(map_fragment);
            ft.show(event_fragment);
            //event_fragment.setAddrtext(latlng);
            ((EditText)event_fragment.getView().findViewById(R.id.Eventaddr)).setText(latlng);
            ft.commit();



    }

    @Override
    public void onsubmit(String name, String latlng, String start, String end) {
        String[] splitString = latlng.split("\\:");
        String lat = splitString[0];
        String lng = splitString[1];
        String url = "http://vswamy.net:8888/events?name="+name+"&latitude=" + lat + "&longitude="
                        + lng + "&start_time="+start+"&end_time="+end;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT,
                url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                // the response is already constructed as a JSONObject!
                Log.d(" responsesindhu ", response.toString());
                Log.d("response sindhu", response.toString());
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-auth-token",
                        "$2b$12$freZc8ldmGXka/5O40YDcuZ8uqQ.WwzVaDsFB0imjX3BaTqV0AlTC");  // hard coded.. change it
                return headers;
            }
        };
        VolleyHelper.getInstance(this.getBaseContext())
                .add(jsonRequest);
        this.finish();
    }
}
