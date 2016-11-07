package com.example.achi.login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 10/21/16.
 */

public class LoginRequest extends StringRequest
{

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/login";
    private Map<String, String> params;

    public LoginRequest(String email, String password,
            Response.Listener<String> listener,
            Response.ErrorListener ErrorresponseListener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }
}