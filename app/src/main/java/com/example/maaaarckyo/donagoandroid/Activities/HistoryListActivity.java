package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.maaaarckyo.donagoandroid.Models.ActivityDay;
import com.example.maaaarckyo.donagoandroid.R;

import java.util.ArrayList;


public class HistoryListActivity extends Activity
{

    private Bundle historyBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_history_list);

        historyBundle = getIntent().getExtras();

        new FillHistoryList().execute();

    }

    private class FillHistoryList extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            ArrayList<ActivityDay> arrayList = historyBundle.getParcelableArrayList("histList");
            ArrayList<String> stringArrayList = new ArrayList<>();


            for (ActivityDay item : arrayList)
            {
                stringArrayList.add(item.getStartTime() + " " + item.getStepValue());
            }

            ListView histList = (ListView)findViewById(R.id.histList);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HistoryListActivity.this, android.R.layout.simple_list_item_1, stringArrayList);

            histList.setAdapter(arrayAdapter);
            return null;
        }


    }
}
