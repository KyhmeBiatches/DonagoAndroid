package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends Activity
{
    Button btnLogin;

    EditText etEmailLogin;
    EditText etPasswordLogin;

    String txtEmail;
    String txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin  = (Button)findViewById(R.id.btnLogin);

        etEmailLogin = (EditText)findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText)findViewById(R.id.etPasswordLogin);


        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtEmail = etEmailLogin.getText().toString();
                txtPassword = etPasswordLogin.getText().toString();

                ParseUser.logInInBackground(txtEmail, txtPassword, new LogInCallback()
                {
                    @Override
                    public void done(ParseUser parseUser, ParseException e)
                    {
                        if (parseUser != null)
                        {
                            Intent intent = new Intent(LoginActivity.this, StepCountActivity.class);
                            startActivity(intent);
//                            Toast.makeText(getApplicationContext(),
//                                    "Hej " + txtEmail + ", du er nu logget ind!",
//                                    Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Bruger findes ikke - opret bruger!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                            startActivity(intent);


                        }
                    }
                });
            }
        });
    }
}