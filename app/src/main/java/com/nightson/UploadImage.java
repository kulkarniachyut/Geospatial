package com.nightson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.nightson.R.id.imageView;

public class UploadImage extends AppCompatActivity {
    private ImageView imageView;
    private final int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image2);
        imageView = (ImageView) findViewById(R.id.viewImage);

        Button pickImage = (Button) findViewById(R.id.upload);
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

    }

    protected void next() {
        Intent registerIntent = new Intent(UploadImage.this,
                MapActivity.class);
        startActivity(registerIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
            super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

            switch(requestCode) {
                case SELECT_PHOTO:
                    if(resultCode == RESULT_OK){
                        try {
                            final Uri imageUri = imageReturnedIntent.getData();
                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            imageView.setImageBitmap(selectedImage);
                            next();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
            }
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
