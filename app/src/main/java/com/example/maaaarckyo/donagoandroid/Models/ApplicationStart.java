package com.example.maaaarckyo.donagoandroid.Models;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;



public class ApplicationStart extends Application
{
    @Override
    public  void onCreate()
    {
        super.onCreate();

       // Parse.enableLocalDatastore(this);
        Parse.initialize(this, "8lsw8PMZDlCAlHGY0O1QMfzhvqaT3zsqgfPavl8K", "mlouoSXOgifqmi3YNQufGiDxlkQ2j3s0NTIJOB49");

        ParseUser.enableAutomaticUser();
        ParseACL defACL = new ParseACL();

        defACL.setPublicReadAccess(true);
        defACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defACL, true);
    }
}




