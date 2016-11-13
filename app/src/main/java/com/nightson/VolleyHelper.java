package com.nightson;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyHelper
{
    private Context appContext = null;
    private RequestQueue requestQueue = null;
    private static VolleyHelper instance = null;

    private VolleyHelper(Context context)
    {
        this.appContext = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.instance = this;
    }

    public static synchronized VolleyHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new VolleyHelper(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        return this.requestQueue;
    }

    public void add(Request req)
    {
        requestQueue.add(req);
    }
}
