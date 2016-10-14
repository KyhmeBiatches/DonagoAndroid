package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maaaarckyo.donagoandroid.R;


public class SettingsActivity extends Activity
{
    Button btnUUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnUUpdate = (Button)findViewById(R.id.btnUUpdate);

        btnUUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingsActivity.this,UserUpdateActivity.class);
                startActivity(intent);
            }
        });


    }
}
