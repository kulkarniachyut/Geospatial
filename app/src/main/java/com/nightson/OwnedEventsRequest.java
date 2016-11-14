package com.nightson;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/13/16.
 */

public class OwnedEventsRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/eventsownedbyuser";
    private Map<String, String> headers;


    public OwnedEventsRequest( String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super( Request.Method.GET, REGISTER_REQUEST_URL, listener, null);

        headers = new HashMap<>();
        headers.put("x-auth-token",token);

    }

    @Override
    public Map<String, String> getHeaders()
    {
        return headers;
    }

}
