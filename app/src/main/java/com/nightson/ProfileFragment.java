package com.nightson;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nightson.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener
{
    FloatingActionButton changePassword;
    FloatingActionButton updatePicure;
    FloatingActionButton addEvent;

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

        addEvent = (FloatingActionButton) myView.findViewById(R.id.addEvents);
        addEvent.setOnClickListener(this);

        return myView;


        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.changePassword:
                
//                edttxt_projectname.sesdfsdtText("Test Submit!#@%!#%");

                break;
            case R.id.uploadPicture:

//                edttxt_projectname.setText("Test Submit!#@%!#%");

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
