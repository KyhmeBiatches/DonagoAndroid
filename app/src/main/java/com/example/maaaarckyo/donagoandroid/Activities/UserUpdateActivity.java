package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class UserUpdateActivity extends Activity
{
    Button btnUpdateU;

    EditText etUpdateFN;
    EditText etUpdateLN;
    EditText etUpdateH;
    EditText etUpdateW;
    EditText etUpdateB;

    String txtUpdateFN;
    String txtUpdateLN;
    String txtUpdateH;
    String txtUpdateW;
    String txtUpdateB;

    DatePickerDialog birthdayDialog;
    SimpleDateFormat dateFormat;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);

        btnUpdateU = (Button)findViewById(R.id.btnUpdateU);

        etUpdateFN = (EditText)findViewById(R.id.etUpdateFN);
        etUpdateLN = (EditText)findViewById(R.id.etUpdateLN);
        etUpdateH = (EditText)findViewById(R.id.etUpdateH);
        etUpdateW = (EditText)findViewById(R.id.etUpdateH);
        etUpdateB = (EditText)findViewById(R.id.etUpdateB);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        setDateTimeField();


        btnUpdateU.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtUpdateFN = etUpdateFN.getText().toString();
                txtUpdateLN = etUpdateLN.getText().toString();
                txtUpdateH = etUpdateH.getText().toString();
                txtUpdateW = etUpdateW.getText().toString();
                txtUpdateB = etUpdateB.getText().toString();

                boolean changed = false;
                String returntxt = "er opdateret!";


                ParseUser user = ParseUser.getCurrentUser();
                if(txtUpdateFN.trim().length()>0)
                {
                    user.put("firstname", txtUpdateFN);

                    returntxt = "Fornavn " + returntxt;
                    changed = true;
                }
                if(txtUpdateLN.trim().length()>0)
                {
                    user.put("lastname", txtUpdateLN);
                    returntxt = "Efternavn " + returntxt;
                    changed = true;
                }
                if(txtUpdateH.trim().length()>0)
                {
                    user.put("heightInCentimeters", txtUpdateH);
                    returntxt = "Højde " + returntxt;
                    changed = true;
                }
                if(txtUpdateW.trim().length()>0)
                {
                    user.put("weightInKiloGram", txtUpdateW);
                    returntxt = "Vægt " + returntxt;
                    changed = true;
                }
                if(txtUpdateB.trim().length()>0)
                {
                    user.put("birthday", txtUpdateB);
                    returntxt = "Fødselsdag " + returntxt;
                    changed = true;
                }
                if(changed)
                {
                    Toast.makeText(getApplicationContext(),
                            returntxt,
                            Toast.LENGTH_LONG).show();
                }

                user.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if (e == null)
                        {
//                            Toast.makeText(getApplicationContext(),
//                                    "Ny data indsat!!",
//                                    Toast.LENGTH_LONG).show();
//                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Der gik noget galt",
                                    Toast.LENGTH_LONG).show();
                            Log.e("ParseException", e.getMessage());
                            finish();

                        }
                        Intent intent = new Intent(UserUpdateActivity.this,MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }
    private void setDateTimeField()
    {
        etUpdateB.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == etUpdateB)
                {
                    birthdayDialog.show();
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        birthdayDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                monthOfYear = 1;
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etUpdateB.setText(dateFormat.format(newDate.getTime()));

                //Calculate age and make a toast with it
                LocalDate birthdate = new LocalDate(year,monthOfYear,dayOfMonth);
                LocalDate now = new LocalDate();
                Period period = new Period(birthdate, now, PeriodType.yearMonthDay());
//                System.out.println(period.getDays());
//                System.out.println(period.getMonths());
                System.out.println(period.getYears());

                Toast.makeText(getApplicationContext(),
                        "Din alder er: " + period.getYears(),
                        Toast.LENGTH_SHORT).show();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH) + 1, newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}
