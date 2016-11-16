package com.nightson;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //CreateEventFragment.//OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateEventFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventFragment extends Fragment {
    FloatingActionButton mapbtn,sdatebtn,stimebtn,edatebtn,etimebtn,submitbt;
    EditText startDate, startTime,endDate,endTime,addrtext,nametext;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Activity activity;
    private onButtonClickListener listener;
    private onSubmitClickListener listener1;


    public CreateEventFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FloatingActionButton mapbtn,sdatebtn,stimebtn,edatebtn,etimebtn;

        View view = inflater.inflate(R.layout.fragment_create_event, container, false);

        mapbtn=(FloatingActionButton) view.findViewById(R.id.addrbtn);
        sdatebtn=(FloatingActionButton) view.findViewById(R.id.startDatebtn);
        stimebtn=(FloatingActionButton) view.findViewById(R.id.startTimebtn);
        edatebtn=(FloatingActionButton) view.findViewById(R.id.endDatebtn);
        etimebtn=(FloatingActionButton) view.findViewById(R.id.endTimebtn);
        submitbt=(FloatingActionButton) view.findViewById(R.id.submitbtn);

        startDate=(EditText) view.findViewById(R.id.startdate);
        startTime=(EditText) view.findViewById(R.id.starttime);
        endDate=(EditText) view.findViewById(R.id.enddate);
        endTime=(EditText) view.findViewById(R.id.endtime);
        addrtext=(EditText) view.findViewById(R.id.Eventaddr);
        nametext=(EditText) view.findViewById(R.id.EventName);

        submitbt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date start_date=null, end_date=null;
                String name = nametext.getText().toString();
                String latlng = addrtext.getText().toString();
                String startdate = startDate.getText().toString();
                String starttime = startTime.getText().toString();
                String enddate = endDate.getText().toString();
                String endtime = endTime.getText().toString();
                String combined_start = startdate+" "+starttime;
                String combined_end = enddate+" "+endtime;
                DateFormat dF = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {
                    start_date = dF.parse(combined_start);
                }catch(ParseException e)
                {
                    e.printStackTrace();
                }

                try {
                    end_date = dF.parse(combined_end);
                }catch(ParseException e)
                {
                    e.printStackTrace();
                }

                String start_time = Double.toString(start_date.getTime()/1000);
                String end_time = Double.toString(end_date.getTime()/1000);

                listener1.onsubmit(name,latlng,start_time,end_time);


            }
        });
        sdatebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDate(startDate);

            }
        });

        stimebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTime(startTime);

            }
        });

        edatebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDate(endDate);

            }
        });

        etimebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTime(endTime);

            }
        });

        mapbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onbutton(1);

            }
        });

        return view;


    }
    /*public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        mapbtn.setOnClickListener();


    }*/
    @Override
    public void onAttach(Activity activity)
    {


         this.activity= activity;
        super.onAttach(activity);
        if (activity instanceof onButtonClickListener) {
            listener = (onButtonClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
        if (activity instanceof onSubmitClickListener) {
            listener1 = (onSubmitClickListener) activity;
        }
    }

    public void setAddrtext(String value){
        addrtext.setText(value);
    }
    @Override
    public void onViewCreated(View view,
                              @Nullable
                                      Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //        MapFragment mFragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map2);
        //        mFragment.getMapAsync(this);
    }

    public interface onButtonClickListener{
        public void onbutton(int position);
    }

    public interface onSubmitClickListener{
        public void onsubmit(String name,String latlng, String start, String end);
    }

    private void showTime(final EditText displaytext)
    {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        displaytext.setText(hourOfDay + ":" + minute);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    private void showDate(final EditText displaytext)
    {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        displaytext.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }


}
