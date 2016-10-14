package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.maaaarckyo.donagoandroid.R;


public class SignupLoginActivity extends Activity
{
    Button btnSignup;
    Button btnLoginSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        btnSignup = (Button)findViewById(R.id.btnSignup);
        btnLoginSignup = (Button)findViewById(R.id.btnLoginSignup);


        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignupLoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLoginSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignupLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
