package com.nightson;

import java.util.Date;

/**
 * Created by achi on 11/14/16.
 */

public class OwnedEventObject {

    Date mStartDate;

    Date mEndTime;

    String mName;
    String mlatitude;
    String mlongitude;

    String mEventId;


    public OwnedEventObject(Date mStartDate, Date mEndTime, String mName , String mlatitude, String mlongitude, String mEventId) {
        this.mStartDate = mStartDate;
        this.mEndTime = mEndTime;
        this.mName = mName;
        this.mlatitude = mlatitude;

        this.mlongitude = mlongitude;
        this.mEventId = mEventId;
    }


    public String getmEventId() {
        return mEventId;
    }


    public String getMlatitude() {
        return mlatitude;
    }

    public String getMlongitude() {
        return mlongitude;
    }

    public Date getmStartDate() {
        return mStartDate;
    }

    public Date getmEndTime() {
        return mEndTime;
    }

    public String getmName() {
        return mName;
    }
}
