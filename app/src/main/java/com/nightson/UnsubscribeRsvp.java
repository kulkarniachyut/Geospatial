package com.nightson;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/15/16.
 */
public class UnsubscribeRsvp extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/subscribe";
    private Map<String, String> params;
    private Map<String, String> headers;


    public UnsubscribeRsvp(String id , String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super( Method.DELETE, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id", id);
        headers = new HashMap<>();
        headers.put("x-auth-token",token);

    }
    @Override
    public Map<String, String> getParams()
    {
        return params;
    }
    @Override
    public Map<String, String> getHeaders()
    {
        return headers;
    }


}
