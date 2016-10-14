package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class CreateUserActivity extends Activity
{
    EditText etEmail;
    EditText etPassword;

    String txtEmail;
    String txtPassword;

    Button btnCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);

        btnCreate = (Button)findViewById(R.id.btnCreate);

        final ParseObject publicUserProfile = new ParseObject("PublicUserProfile");

        btnCreate.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtEmail = etEmail.getText().toString();
                txtPassword = etPassword.getText().toString();

                boolean fillAll = false;
                String returntxt = "Udfyld:";

                if(txtEmail.trim().equals(""))
                {
                    returntxt = returntxt + " email";
                    fillAll = true;
                }

                //PASSWORDCHECK!!!!!!!!!!!!!!!!!!!!!!!!!!!<------------------------------!!!
//                if(!PassCheck(txtPassword.trim()).equals("Gyldigt kodeord")) //&& txtPassword.matches("^(?=.*\\d)(?=.*[a-zA-Z]).{4,10}$"))
//                {
//                    if (returntxt.equals("Udfyld:"))
//                    {
//                        returntxt = PassCheck(txtPassword.trim());
//                        fillAll = true;
//                    }
//                    else
//                    {
//                        returntxt = returntxt + " & " + PassCheck(txtPassword);
//                    }
//                }

                if(fillAll)
                {
                    Toast.makeText(getApplicationContext(),
                            returntxt,
                            Toast.LENGTH_LONG).show();
                }

                else
                {
                    final ParseUser user = new ParseUser();
                    user.setUsername(txtEmail);
                    user.setEmail(txtEmail);
                    user.setPassword(txtPassword);
                    user.signUpInBackground(new SignUpCallback()
                    {
                        @Override
                        public void done(ParseException e)
                        {
                            if (e == null)
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Du er nu blevet oprettet med brugernavnet: " + txtEmail,
                                        Toast.LENGTH_LONG).show();
                                finish();

                                publicUserProfile.saveInBackground(new SaveCallback()
                                {
                                    @Override
                                    public void done(ParseException e)
                                    {
                                        user.put("publicUserProfile", publicUserProfile);
                                        user.saveInBackground(new SaveCallback()
                                        {
                                            @Override
                                            public void done(ParseException e)
                                            {
//                                                ParseObject pu = (ParseObject) user.get("publicUserProfile");
//                                                ParseObject uid = ParseUser.getCurrentUser();
                                                Intent intent = new Intent(CreateUserActivity.this, PublicUserActivity.class);
//                                                Bundle bundle = new Bundle();
//                                                bundle.putString("publicUserProfile", pu.getObjectId());
//                                                bundle.putString("user",uid.getObjectId());
//                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Der gik noget galt",
                                        Toast.LENGTH_LONG).show();
                                Log.e("ParseException", e.getMessage());
                                finish();
                            }
                        }
                    });

                }

            }
        });
    }

    public static String PassCheck(String txtPassword)
    {
        String result = "Gyldigt kodeord";
        int length = 0;
        int numCount = 0;
        int capCount = 0;

        //NYYYYYYYYYYYYYYYYYYYYYYYT!!!
        for(int i = 0; i<txtPassword.length(); i++)
        {
            if ((txtPassword.charAt(i) >= 47 && txtPassword.charAt(i) <= 58) || (txtPassword.charAt(i) >= 64 && txtPassword.charAt(i) <= 91) ||
                    (txtPassword.charAt(i) >= 97 && txtPassword.charAt(i) <= 122))
            {

            }
            else
            {
                result = "Kodeord indeholder ugyldige tegn!*";
            }
            //counts the number of numbers
            if ((txtPassword.charAt(i) > 47 && txtPassword.charAt(i) < 58))
            {
                numCount++;
            }
            if ((txtPassword.charAt(i) > 64 && txtPassword.charAt(i) < 91))
            {
                capCount++;
            }
            length = (i + 1);
        }//for loop ends

        if(numCount < 1)
        {
            result = "Mindst ét tal i kodeord!";
        }
        if(capCount < 1)
        {
            result = "Mindst ét stort bogstav i kodeord!";
        }
        if(length < 8)
        {
            result = "Kodeordet skal være mindst 8 tegn!";
        }
        return result;
    }
}