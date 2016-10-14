package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.Models.Cause;
import com.example.maaaarckyo.donagoandroid.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.FileInputStream;
import java.util.List;


public class CauseInformationActivity extends Activity
{
    TextView causeDescription;
    TextView donatedMoney;

    ImageView causeHeader;

    Button supportCauseBtn;

    Cause clickedCause;

    public static final String UserPUBLIC = "MyPrefs";

    private SharedPreferences publicUserPREF;
    private SharedPreferences.Editor PREFEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cause_information);

        publicUserPREF = getSharedPreferences(UserPUBLIC,MODE_PRIVATE);
        PREFEditor = publicUserPREF.edit();

        clickedCause = getIntent().getParcelableExtra("causeObject");

        causeDescription = (TextView)findViewById(R.id.causeDescription);
        donatedMoney = (TextView)findViewById(R.id.donatedMoney);

        causeHeader = (ImageView)findViewById(R.id.causeImage);

        supportCauseBtn = (Button)findViewById(R.id.supportCauseBtn);

        final ParseObject pubUsr = (ParseObject)ParseUser.getCurrentUser().get("publicUserProfile");


        checkCauseSupported(pubUsr);

        supportCauseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ParseObject parseCause = ParseObject.createWithoutData("Cause", clickedCause.getObjectId());

                pubUsr.put("currentCause", parseCause);
                pubUsr.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        Toast.makeText(CauseInformationActivity.this,
                                       "Du støtter nu sagen!",
                                       Toast.LENGTH_SHORT).show();
                        supportCauseBtn.setClickable(false);
                    }
                });
            }
        });


        causeDescription.setText(clickedCause.getDescription());
        donatedMoney.setText(clickedCause.getDonatedAmount().toString() + " DKK");

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try
        {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            causeHeader.setImageBitmap(bmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setCurrentTabByTag("aboutTab");
        tabHost.setup();

        TabHost.TabSpec aboutTab = tabHost.newTabSpec("aboutTab");
        aboutTab.setIndicator("Om");
        aboutTab.setContent(R.id.aboutTab);
        tabHost.addTab(aboutTab);

        TabHost.TabSpec donationTab = tabHost.newTabSpec("donationsTab");
        donationTab.setIndicator("Donationer");
        donationTab.setContent(R.id.donationsTab);
        tabHost.addTab(donationTab);

        tabHost.setCurrentTab(0);



    }

    public void checkCauseSupported(ParseObject pubUsr)
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("PublicUserProfile");


        query.getInBackground(pubUsr.getObjectId(), new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseObject, ParseException e)
            {
                ParseObject parseCause2 = parseObject.getParseObject("currentCause");
                try
                {
                    parseCause2.fetchIfNeeded();
                }
                catch(ParseException g)
                {
                    g.printStackTrace();
                }

                if (parseCause2.getObjectId().equals(clickedCause.getObjectId()))
                {
                    supportCauseBtn.setEnabled(false);
                }
                else
                {
                    supportCauseBtn.setEnabled(true);
                }
            }
        });

    }


}
