package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.maaaarckyo.donagoandroid.R;
import com.example.maaaarckyo.donagoandroid.Activities.PublicUserActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PrivateUserActivity extends Activity
{
    Button btnFinalSignup, btnBirthdate;

    EditText etBirthday;
    EditText etWeight;
    EditText etHeight;

    String txtBirthdate;

    Date birthdate;

    int numHeight, numWeight;


    DatePickerDialog birthdayDialog;
    SimpleDateFormat dateFormat;

//    public static final String UserPRIVATE = "MyPrefs";
//
//    private SharedPreferences privUserPREF;
//    private SharedPreferences.Editor privPREFEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_user);

        btnFinalSignup = (Button)findViewById(R.id.btnFinalSignup);

        etBirthday = (EditText)findViewById(R.id.etBirthday);
        etHeight = (EditText)findViewById(R.id.etHeight);
        etWeight = (EditText)findViewById(R.id.etWeight);

        btnBirthdate = (Button)findViewById(R.id.btnBirthdate);

        etBirthday.setInputType(InputType.TYPE_NULL);
        etBirthday.requestFocus();

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        setDateTimeField();


        btnFinalSignup.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtBirthdate = etBirthday.getText().toString();
//                txtHeight = etHeight.getText().toString();
//                txtWeight = etWeight.getText().toString();

                try
                {
                    birthdate = dateFormat.parse(txtBirthdate);
                }
                catch(Exception e)
                {

                }


                numHeight = Integer.parseInt(etHeight.getText().toString());
                numWeight = Integer.parseInt(etWeight.getText().toString());


                ParseUser user = ParseUser.getCurrentUser();
                user.put("birthdate", birthdate);
                user.put("heightInCentimeters",numHeight);
                user.put("weightInKilogram", numWeight);
                user.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if(e == null)
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Ny data indsat!!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Der gik noget galt: " + e.getMessage() ,
                                    Toast.LENGTH_LONG).show();
                            Log.e("ParseException", e.getMessage());
                            finish();
                        }

                        Intent intent = new Intent(PrivateUserActivity.this,StepCountActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });
            }

        });

        btnBirthdate.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == btnBirthdate)
                {
                    birthdayDialog.show();
                }
            }
        });
    }
    private void setDateTimeField()
    {


        Calendar newCalendar = Calendar.getInstance();
        birthdayDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etBirthday.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
