package com.nightson;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class PasswordChange extends AppCompatActivity {



    private void dataProcess() {

        final Button submit = (Button) findViewById(R.id.passChangeSubmitBtn);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences preferences = getSharedPreferences(Constants.PREF_FILE_NAME,0);
                final String token = preferences.getString("x-auth-token","None");

                final EditText pass1 = (EditText) findViewById(R.id.changePass);
                final EditText pass2 = (EditText) findViewById(R.id.changepass2);
                final EditText oldpass = (EditText) findViewById(R.id.oldPass);

                    Response.Listener<String> responseListener = new Response.Listener<String>()
                    {

                        @Override
                        public void onResponse(String response)
                        {
                            System.out.println("Working API");
                            Log.d("Working API", "3");
                            Log.d("response", response);
                            Context context = getApplicationContext();
                            CharSequence text = "Password Updated succesfully !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            Intent intent = new Intent(PasswordChange.this,
                            MapActivity.class);
                            PasswordChange.this.startActivity(intent);

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


                    Log.d("token ", token);
                    Log.d("old password" , oldpass.getText().toString());
                    Log.d("new password" , (pass1.getText().toString()));

                    PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(
                            oldpass.getText().toString(), (pass1.getText().toString()) , token, responseListener,ErrorresponseListener );
                    VolleyHelper.getInstance(getApplicationContext());
                    VolleyHelper vh = VolleyHelper
                            .getInstance(getApplicationContext());
                    vh.getRequestQueue().add(passwordChangeRequest);

                }

        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        this.dataProcess();


    }
}
