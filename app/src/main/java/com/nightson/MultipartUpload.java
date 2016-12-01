package com.nightson;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.HttpEntity;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by achi on 11/13/16.
 */

public class MultipartUpload extends Request<String>
{

    //    private MultipartEntity entity = new MultipartEntity();

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private static final String REGISTER_REQUEST_URL = "http://vswamy.net:8888/upload";
    private static final String FILE_PART_NAME = "file_name";
    private static final String STRING_PART_NAME = "text";
    //    HttpEntity httpentity;

    private String mStringPart;
    private Map<String, String> params;
    private final Response.Listener<String> mListener;
    private File mFilePart;
    private Map<String, String> headers;

    public MultipartUpload(File file, String token,
            Response.Listener<String> listener,
            Response.ErrorListener errorlistener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, errorlistener);

        mListener = listener;
        mFilePart = file;
        mStringPart = token;
        //        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        buildMultipartEntity();
        httpentity = entity.build();
        //        entity = new MultipartEntityBuilder(HttpMultipartMode.BROWSER_COMPATIBLE);

    }

    private void buildMultipartEntity()
    {
        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
        try
        {
            entity.addPart(STRING_PART_NAME, new StringBody(""));
        }
        catch (UnsupportedEncodingException e)
        {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType()
    {
        return httpentity.getContentType().getValue();
        //        return entity
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            httpentity = entity.build();
            httpentity.writeTo(bos);
            //            entity.writeTo(bos);
            //            httpentity.httpentitywriteTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap()))
        {
            headers = new HashMap<String, String>();
        }

        headers.put("Content-Type", "multipart/form-data");
        headers.put("x-auth-token", mStringPart);

        return headers;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }

    //
    //    public static interface MultipartProgressListener {
    //        void transferred(long transfered, int progress);
    //    }

}
