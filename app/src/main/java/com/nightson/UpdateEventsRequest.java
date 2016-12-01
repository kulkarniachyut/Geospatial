package com.nightson;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/13/16.
 */

public class UpdateEventsRequest extends StringRequest
{

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/events";
    private Map<String, String> headers;
    private Map<String, String> params;

    public UpdateEventsRequest(String name, String starttime, String endtime,
            String latitude, String longitude, String id, String token,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        headers = new HashMap<>();
        headers.put("x-auth-token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("start_time", starttime);
        params.put("end_time", endtime);

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
