package com.nightson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nightson.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener

{


    FloatingActionButton changePassword;
    FloatingActionButton updatePicure;
    FloatingActionButton addEvent;
    private static int RESULT_LOAD_IMAGE = 1;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);
        changePassword = (FloatingActionButton) myView.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);

        updatePicure = (FloatingActionButton) myView.findViewById(R.id.uploadPicture);
        updatePicure.setOnClickListener(this);

//        addEvent = (FloatingActionButton) myView.findViewById(R.id.addEvents);
//        addEvent.setOnClickListener(this);

        return myView;


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changePassword:
                Intent registerIntent = new Intent(getActivity(),
                        PasswordChange.class);
                startActivity(registerIntent);
                
//                edttxt_projectname.sesdfsdtText("Test Submit!#@%!#%");

                break;
            case R.id.uploadPicture:
                Intent registerIntent2 = new Intent(getActivity(),
                        UploadImage.class);
                startActivity(registerIntent2);


                break;
            case R.id.addEvents:

//                edttxt_projectname.setText("Test Submit!#@%!#%");

                break;
            default:
                break;
        }

        // implements your things
    }





}
