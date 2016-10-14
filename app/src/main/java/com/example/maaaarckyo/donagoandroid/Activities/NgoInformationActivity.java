package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maaaarckyo.donagoandroid.Adapters.CauseListAdapter;
import com.example.maaaarckyo.donagoandroid.Models.Cause;
import com.example.maaaarckyo.donagoandroid.Models.NGO;
import com.example.maaaarckyo.donagoandroid.R;
import com.example.maaaarckyo.donagoandroid.Utility.UtilityMethods;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NgoInformationActivity extends Activity
{
    private List<Cause> causeList = new ArrayList<>();

    private ImageView iwHeader;

    private TextView twNgoDescription;

    private LinearLayout causeListLayout;

    private NGO clickedNgo;
    private Cause clickedCause = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_information);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        iwHeader = (ImageView)findViewById(R.id.causeImage);
        causeListLayout = (LinearLayout)findViewById(R.id.causeList);
        twNgoDescription = (TextView)findViewById(R.id.twNgoDescription);

        clickedNgo = getIntent().getParcelableExtra("clickedItem");

        twNgoDescription.setText(clickedNgo.getDescription());

        // Get the saved image path and decode it into an image
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try
        {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            iwHeader.setImageBitmap(bmp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        new AddCausesToNgo().execute();


    }

    // Starts a new thread in order to add Causes to the selected NGO
    private class AddCausesToNgo extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {

            ParseObject parseNgo = ParseObject.createWithoutData("NGO", clickedNgo.getObjectId());

            // Queries the causes attached to the selected NGO with a cache limit of max 1 Hour
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Cause");
            query.whereEqualTo("ngo", parseNgo);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    for (ParseObject cause : results)
                    {
                        Cause newCause = new Cause();
                        newCause.setObjectId(cause.getObjectId());
                        newCause.setActive(cause.getBoolean("active"));
                        newCause.setDescription(cause.getString("causeDescription"));
                        newCause.setName(cause.getString("name"));
                        newCause.setSupporters(UtilityMethods.GetSupportersCountForCause(newCause));
                        newCause.setDonatedAmount(UtilityMethods.GetDonatedMoneyForCause(newCause));

                        try
                        {
                            newCause.setImage(UtilityMethods.ConvertUrlToBitmap(cause.getString("causeImageUrl")));
                        }
                        catch (Exception f)
                        {
                            f.printStackTrace();
                        }

                        causeList.add(newCause);
                    }
                    clickedNgo.setCauseList(causeList);
                    final CauseListAdapter adapter = new CauseListAdapter(NgoInformationActivity.this,
                                                                causeList);
                    final int adapterCount = adapter.getCount();

                    // Add an onClick for each cause as it is loaded.
                    for (int i = 0; i < adapterCount; i++)
                    {
                        View item = adapter.getView(i, null, null);
                        item.setClickable(true);
                        // Save the object to the item tag of the current cause view
                        item.setTag(causeList.get(i));
                        item.setOnClickListener(new View.OnClickListener()
                        {
                            // Is cast whenever a cause is clicked
                            public void onClick(View v)
                            {
                                Toast.makeText(v.getContext(),
                                               "Det er rigtigt!",
                                               Toast.LENGTH_SHORT).show();
                                // Retrieve the object from the tag
                                clickedCause = (Cause) v.getTag();

                                try
                                {
                                    //Write file
                                    String filename = "bitmap.png";
                                    Bitmap bmp = clickedCause.getImage();
                                    FileOutputStream stream = NgoInformationActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
                                    bmp.compress(Bitmap.CompressFormat.PNG,
                                                 100,
                                                 stream);
                                    //Cleanup
                                    stream.close();
                                    //Pop intent
                                    Intent intent = new Intent(NgoInformationActivity.this, CauseInformationActivity.class);
                                    intent.putExtra("causeObject", clickedCause);
                                    intent.putExtra("image", filename);

                                    startActivity(intent);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        });

                        causeListLayout.addView(item);
                    }
                }
            });
            return null;
        }
    }
}
