package com.nightson;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.zip.Inflater;

import static com.nightson.R.id.container;

/**
 * Created by achi on 11/14/16.
 */

public class OwnedAdapter extends BaseAdapter {


    ArrayList<OwnedEventObject> mOwnedEventObjects;
    Context mContext;

    public OwnedAdapter(Context mContext, ArrayList<OwnedEventObject> mOwnedObjectList) {
            this.mOwnedEventObjects = mOwnedObjectList;
            this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mOwnedEventObjects.size();
    }

    @Override
    public OwnedEventObject getItem(int position) {
        return mOwnedEventObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.owned_event_item,null);
        final OwnedEventObject mObject = getItem(position);
        TextView ownedName = (TextView)view.findViewById(R.id.ownedname);
        ownedName.setText("Event Name : " + mObject.getmName());
        TextView startTime = (TextView)view.findViewById(R.id.starttime);
        startTime.setText("Start Time : " + String.valueOf(mObject.getmStartDate()));
        TextView endTime = (TextView)view.findViewById(R.id.endtime);
        endTime.setText("End Time : " + String.valueOf(mObject.getmEndTime()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup(inflater,mObject);
            }
        });
        return view;
    }

    public void updateResults(ArrayList<OwnedEventObject> results) {
        mOwnedEventObjects = results;
        //Triggers the list update
        notifyDataSetChanged();
    }


    private void callPopup(LayoutInflater layoutInflater, OwnedEventObject mObject) {


        View popupView = layoutInflater.inflate(R.layout.event_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER_VERTICAL, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView mname = (TextView) popupView.findViewById(R.id.ownedname1);
        TextView mstart = (TextView) popupView.findViewById(R.id.starttime1);
        TextView mend = (TextView) popupView.findViewById(R.id.endtime1);
        TextView mlat = (TextView) popupView.findViewById(R.id.latitude);
        TextView mlong = (TextView) popupView.findViewById(R.id.longitude);

        mname.setText("Event Name : " + mObject.getmName());
        mstart.setText("Start Time : " + String.valueOf(mObject.getmStartDate()));
        mend.setText("End Time : " + String.valueOf(mObject.getmEndTime()));
        mlat.setText("Latitude : " + mObject.getMlatitude());
        mlong.setText("Longitude : " + mObject.getMlongitude());


        ((Button) popupView.findViewById(R.id.done))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        popupWindow.dismiss();
                    }
                });

    }


}
