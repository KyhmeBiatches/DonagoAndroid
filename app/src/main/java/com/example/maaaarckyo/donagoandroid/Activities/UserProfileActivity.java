package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.example.maaaarckyo.donagoandroid.Activities.PrivateUserActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UserProfileActivity extends Activity
{
    String txtFname, txtLname;
    String txtBirthday;
    EditText etViewFname, etViewLname;
    EditText etViewHeight, etViewWeight, etViewBirthday;

    int numWeight, numHeight;

    Date birthdate;

    Button btnBTMenu, btnViewBirthday;
    private ParseObject viewUser;

    DatePickerDialog birthdayDialog;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        etViewFname = (EditText)findViewById(R.id.etViewFname);
        etViewLname = (EditText)findViewById(R.id.etViewLname);
        etViewHeight = (EditText)findViewById(R.id.etViewHeight);
        etViewWeight = (EditText)findViewById(R.id.etViewWeight);
        etViewBirthday = (EditText)findViewById(R.id.etViewBirthday);

        btnBTMenu = (Button)findViewById(R.id.btnBTMenu);
        btnViewBirthday = (Button)findViewById(R.id.btnViewBirthday);


        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        setDateTimeField();

        final ParseUser user = ParseUser.getCurrentUser();

        final ParseObject userId  = (ParseObject)ParseUser.getCurrentUser().get("publicUserProfile");

        viewUser = new ParseObject("PublicUserProfile");
        try
        {
            int weight = user.getInt("weightInKilogram");
            int height = user.getInt("heightInCentimeters");
            Date birthday = user.getDate("birthdate");
            String birthdate = dateFormat.format(birthday);
            viewUser = ParseQuery.getQuery("PublicUserProfile").get(userId.getObjectId());
            String fName = viewUser.getString("firstname");
            String lName = viewUser.getString("lastname");

            etViewFname.setText(fName);
            etViewLname.setText(lName);
            etViewWeight.setText(String.valueOf(weight));
            etViewHeight.setText(String.valueOf(height));
            etViewBirthday.setText(birthdate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }




        btnBTMenu.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View v)
            {

                ParseQuery<ParseObject>query1 = ParseQuery.getQuery("User");
                query1.getInBackground(user.getObjectId(), new GetCallback<ParseObject>()
                {
                    @Override
                    public void done(ParseObject parseObject, ParseException e)
                    {
                        numWeight = Integer.parseInt(etViewWeight.getText().toString().trim());
                        numHeight = Integer.parseInt(etViewHeight.getText().toString().trim());
//                        txtBirthday = etViewBirthday.getText().toString().trim();
                        try
                        {
                            birthdate = dateFormat.parse(etViewBirthday.getText().toString().trim());
                        }
                        catch (java.text.ParseException e1)
                        {

                            e1.printStackTrace();
                        }

                        if(numWeight != 0)
                        {
                            user.put("weightInKilogram",numWeight);
                        }

                        if(numHeight != 0)
                        {
                            user.put("heightInCentimeters",numHeight);
                        }

                        if(birthdate != null)
                        {
                            user.put("birthday",birthdate);
                        }

                        user.saveInBackground();
                    }
                });


                ParseQuery<ParseObject>query = ParseQuery.getQuery("PublicUserProfile");
                query.getInBackground(userId.getObjectId(), new GetCallback<ParseObject>()
                {
                    @Override
                    public void done(ParseObject parseObject, ParseException e)
                    {

                        txtFname = etViewFname.getText().toString().trim();
                        txtLname = etViewLname.getText().toString().trim();

                        if(txtFname.length() != 0)
                        {
                            viewUser.put("firstname",txtFname);
                        }
                        if(txtLname.length() != 0)
                        {
                            viewUser.put("lastname",txtLname);
                        }


//                        user.saveInBackground();
                        viewUser.saveInBackground(new SaveCallback()
                        {
                            @Override
                            public void done(ParseException e)
                            {
                                if (e == null)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Profiloplysninger opdateret!",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Der gik noget galt:" + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e("ParseException", e.getMessage());
                                    finish();
                                }
                            }
                        });
                                Intent intent = new Intent(UserProfileActivity.this, MenuActivity.class);
                                startActivity(intent);
                                finish();
                    }
                });


            }


        });


    }

    private void setDateTimeField()
    {
        btnViewBirthday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == btnViewBirthday)
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
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth + 1);
                etViewBirthday.setText(dateFormat.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)-1);
    }
}
