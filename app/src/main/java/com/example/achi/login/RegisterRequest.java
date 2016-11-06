package com.example.achi.login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 10/21/16.
 */

public class RegisterRequest extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/signup" ;
    private Map<String,String> params;

    public RegisterRequest(String fname, String lname, String email,  String password, String phone, Response.Listener<String> listener ){
        super(Method.PUT,REGISTER_REQUEST_URL , listener , null);
        params = new HashMap<>();
        params.put("first_name",fname);
        params.put("last_name",lname);
        params.put("email",email);
        params.put("password",password);
        params.put("photo_url","hahaha ");
        params.put("latitude","31");
        params.put("longitude","46");
        params.put("location_recorded_at","1477098182");
        params.put("phone",phone);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}