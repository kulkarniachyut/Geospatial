package com.nightson;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/15/16.
 */
public class UpdateRsvp extends StringRequest
{

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/subscribe";
    private Map<String, String> params;
    private Map<String, String> headers;

    public UpdateRsvp(String id, String token,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("event_id", id);

        headers = new HashMap<>();
        headers.put("x-auth-token", token);

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
