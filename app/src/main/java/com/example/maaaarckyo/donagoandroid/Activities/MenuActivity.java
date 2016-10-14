package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.example.maaaarckyo.donagoandroid.Models.ActivityDay;
import com.example.maaaarckyo.donagoandroid.R;

import java.util.ArrayList;


public class MenuActivity extends Activity
{
    Button btnUActivity;
    Button btnNGO;
    Button btnSocial;
    Button btnUHistory;
    Button btnURank;
    Button btnSettings;
    Button btnUProfile;
    Button btnMyDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnUActivity = (Button)findViewById(R.id.btnUActivity);
        btnNGO = (Button)findViewById(R.id.btnNGO);
        btnSocial = (Button)findViewById(R.id.btnSocial);
        btnUHistory = (Button)findViewById(R.id.btnUHistory);
        btnURank = (Button)findViewById(R.id.btnURank);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        btnUProfile = (Button)findViewById(R.id.btnUProfile);
        btnMyDonation = (Button)findViewById(R.id.btnMyDonation);





        btnSettings.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnNGO.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuActivity.this,NgoListActivity.class);
                startActivity(intent);
            }
        });

        btnUHistory.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle historyBundle = getIntent().getExtras();
                ArrayList<ActivityDay> activityDays = historyBundle.getParcelableArrayList("histList");

                Intent intent = new Intent(MenuActivity.this,HistoryListActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("histList",
                                              activityDays);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnUProfile.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MenuActivity.this,UserProfileActivity.class);
                startActivity(intent);

            }
        });



    }
}