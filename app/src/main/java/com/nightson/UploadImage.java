package com.nightson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class UploadImage extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private Button browseBtn;
    private Button uploadBtn;
    private Bitmap bitmap;
    private final int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://vswamy.net:8888/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image2);
        imageView = (ImageView) findViewById(R.id.viewImage);
        browseBtn = (Button) findViewById(R.id.browseBtn);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        browseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public File getFileImage(Bitmap bmp)  {
        File f = new File(getCacheDir(), "upload_image");
        try {
            f.createNewFile();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(imageBytes);
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    private void uploadImage()  {
        //Showing the progress dialog
        SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE_NAME,0);
        final String token = preferences.getString("x-auth-token","None");
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //Disimissing the progress dialog
                loading.dismiss();
                Log.d("response", s);
                //Showing toast message of the response
                Toast.makeText(UploadImage.this, s , Toast.LENGTH_LONG).show();
            }
        };

        Response.ErrorListener ErrorresponseListener = new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {

//                            Intent intent = new Intent(PasswordChange.this,
//                            MapActivity.class);
//                            PasswordChange.this.startActivity(intent);
                Log.d("Error", String.valueOf(error));

            }

        };
//
//{
//            @Override
//            protected Map<String,File> () throws AuthFailureError {
//                //Converting Bitmap to String
////                String image = getStringImage(bitmap);
//                try {
                    File file = getFileImage(bitmap);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //Creating parameters
//                Map<String,File> params = new Hashtable<String, File>();
//                //Adding parameters
//                params.put("file_name", file);
//                //returning parameters
//                return params;
//            }

//            @Override
//            public Map<String, String> getHeaders()
//
//            {
//                Map<String,String> headers = new Hashtable<String, String>();
//                headers.put("x-auth-token",token);
//                return headers;
//            }
        MultipartUpload UploadRequest = new MultipartUpload(
                file , token, responseListener,ErrorresponseListener );

        VolleyHelper.getInstance(getApplicationContext());
        VolleyHelper vh = VolleyHelper
                .getInstance(getApplicationContext());
        vh.getRequestQueue().add(UploadRequest);


    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    @Override
    public void onClick(View v) {
        if(v == browseBtn){
            showFileChooser();
        }

        if(v == uploadBtn){
            uploadImage();
        }
    }
}
