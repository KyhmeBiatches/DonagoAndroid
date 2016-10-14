package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.maaaarckyo.donagoandroid.Models.ActivityDay;
import com.example.maaaarckyo.donagoandroid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class TotalStepsActivity extends Activity {

    private static final int REQUEST_OAUTH = 1;

    private GoogleApiClient _googleApiClient;

    public static final String TAG = "BasicHistoryApi";
    private static final String DATE_FORMAT = "EEE - dd.MM.yyyy HH:mm:ss";
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    private TextView _steps;

    private ArrayList<ActivityDay> activityDays = new ArrayList<ActivityDay>();
    private String stepsToday = "";
    private int totalSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_total_steps);

        _steps = (TextView)findViewById(R.id.textView2);
        Button button = (Button)findViewById(R.id.button);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v)
            {
                Intent stepCount = new Intent(TotalStepsActivity.this, StepCountActivity.class);
                startActivity(stepCount);
            }
        });

        Button historyButton = (Button)findViewById(R.id.historyButton);

        historyButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent history = new Intent(TotalStepsActivity.this, HistoryListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("histList",
                                                  activityDays);
                    history.putExtras(bundle);

                TotalStepsActivity.this.startActivity(history);
            }
        });

        Button ngoList = (Button)findViewById(R.id.button2);

        ngoList.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                TotalStepsActivity.this.startActivity(new Intent(TotalStepsActivity.this, NgoListActivity.class
                ));
            }
        });

        buildFitnessClient();
        new QueryFitnessDataForPreviousWeek().execute();

    }

    public void buildFitnessClient()
    {
        _googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                .addScope(Fitness.SCOPE_BODY_READ_WRITE)
                .addConnectionCallbacks(
                        new ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle bundle)
                            {
                                Log.i(TAG, "Connected!");

                            }

                            @Override
                            public void onConnectionSuspended(int i)
                            {
                                if (i == ConnectionCallbacks.CAUSE_NETWORK_LOST)
                                {
                                    Log.i(TAG, "Connection lost: Cause: Network Lost");
                                }
                                else if (i == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED)
                                {
                                    Log.i(TAG, "Connection lost: Reason: Service Disconnected");
                                }
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                            TotalStepsActivity.this, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        Log.i(TAG, "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(TotalStepsActivity.this,
                                                REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG,
                                                "Exception while starting resolution activity", e);
                                    }
                                }
                            }
                        }
                )
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect to the Fitness API
        Log.i(TAG, "Connecting...");
        _googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (_googleApiClient.isConnected()) {
            _googleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!_googleApiClient.isConnecting() && !_googleApiClient.isConnected()) {
                    _googleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    private class QueryFitnessDataForPreviousWeek extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
           

            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();

            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(_googleApiClient, readRequest).await(1, TimeUnit.MINUTES);

            printData(dataReadResult);

            return null;
        }
    }


    private DataReadRequest queryFitnessData() {


        Calendar cal = Calendar.getInstance();
        Date now = new Date();

        cal.setTime(now);
        // Sets EndTime to midnight
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long endTime = cal.getTimeInMillis();
        // Sets start time to Midnight 1 week ago
        cal.add(Calendar.DAY_OF_YEAR, -7);
        long startTime = cal.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        // Requests the fitness data for the previous week
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    private void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    private void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                String steps = dp.getValue(field).toString();
                int intSteps = Integer.parseInt(steps);
                stepsToday = dp.getValue(field).toString();
                totalSteps += intSteps;

                ActivityDay activityDay = new ActivityDay(dp.getDataType().getName(),
                        dp.getValue(field).toString(),
                        dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)),
                        dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                );

                activityDays.add(activityDay);
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _steps.setText(String.valueOf(totalSteps));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
