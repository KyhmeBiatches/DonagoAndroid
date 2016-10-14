package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.maaaarckyo.donagoandroid.R;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class MainActivity extends Activity
{
    //Button btnSignup;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()))
        {
            //If - send to SignupLogin
            Intent intent = new Intent(MainActivity.this, SignupLoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            //If not - get current ParseUser
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null)
            {
                Intent intent = new Intent(MainActivity.this,/*StepCountActivity.class*/SignupLoginActivity.class /*Login.class*/);
                startActivity(intent);
                finish();
            }
           /* else
            {

                Intent intent = new Intent(this, SignupLogin.class);
                startActivity(intent);
                finish();
            }*/
        }
    }
}

