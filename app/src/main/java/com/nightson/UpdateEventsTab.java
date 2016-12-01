package com.nightson;

/**
 * Created by achi on 11/13/16.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UpdateEventsTab extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater
                .inflate(R.layout.update_events_tab, container, false);
        return rootView;
    }
}
