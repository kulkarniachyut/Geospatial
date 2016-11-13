package com.nightson;

import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/12/16.
 */

public class PasswordChangeRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/updatepassword";
    private Map<String, String> params;
    private Map<String, String> headers;


    public PasswordChangeRequest(String oldpass , String newpass, String token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super( Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("old_password", oldpass);
        params.put("new_password", newpass);
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
