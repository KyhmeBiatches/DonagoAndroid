package com.example.maaaarckyo.donagoandroid.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class ActivityDay extends Object implements Parcelable
{
    public String Name;
    public String StepValue;
    public String StartTime;
    public String EndTime;

    public ActivityDay()
    {

    }

    public ActivityDay(String name, String stepValue, String startTime, String endTime) {
        Name = name;
        StepValue = stepValue;
        StartTime = startTime;
        EndTime = endTime;
    }

    public ActivityDay(Parcel in) {
        Name = in.readString();
        StepValue = in.readString();
        StartTime = in.readString();
        EndTime = in.readString();
    }

    public String getName() {
        return Name;
    }

    public String getStepValue() {
        return StepValue;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setStepValue(String stepValue) {
        StepValue = stepValue;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(StepValue);
        dest.writeString(StartTime);
        dest.writeString(EndTime);
    }

    public static final Creator<ActivityDay> CREATOR = new Creator<ActivityDay>()
    {
        public ActivityDay createFromParcel(Parcel in)
        {
            return new ActivityDay(in);
        }
        public ActivityDay[] newArray(int size)
        {
            return new ActivityDay[size];
        }
    };
}
