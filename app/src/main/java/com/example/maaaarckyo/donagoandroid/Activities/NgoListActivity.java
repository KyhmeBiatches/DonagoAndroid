package com.example.maaaarckyo.donagoandroid.Activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.maaaarckyo.donagoandroid.Adapters.NgoListAdapter;
import com.example.maaaarckyo.donagoandroid.Models.NGO;
import com.example.maaaarckyo.donagoandroid.Utility.UtilityMethods;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NgoListActivity extends ListActivity
{
    private ArrayList<NGO> ngoArrayList = new ArrayList<>();
    private ListView ngoList;
    private static boolean queryPinned = false;

    private NGO clickedNgo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        new QueryNgoWebList().execute();

    }

    private class QueryNgoWebList extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(Void... params)
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("NGO");
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.whereEqualTo("active",
                               true);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            query.findInBackground(new FindCallback<ParseObject>()
            {
                @Override
                public void done(List<ParseObject> results, ParseException e)
                {
                    for (ParseObject a : results)
                    {

                        NGO newNgo = new NGO();
                        newNgo.setObjectId(a.getObjectId());
                        newNgo.setActive(a.getBoolean("active"));
                        newNgo.setName(a.getString("name"));
                        newNgo.setDescription(a.getString("ngoDescription"));
                        newNgo.setLogoUrl(a.getString("ngoLogoImageUrl"));

                        try
                        {
                            newNgo.setImage(UtilityMethods.ConvertUrlToBitmap(a.getString("ngoImageUrl")));
                        }
                        catch (Exception f)
                        {
                            f.printStackTrace();
                        }

                        ngoArrayList.add(newNgo);

                    }
                    NgoListAdapter adapter = new NgoListAdapter(NgoListActivity.this,
                                                                ngoArrayList);
                    setListAdapter(adapter);

                }
            });

            return null;
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        clickedNgo = (NGO)getListAdapter().getItem(position);


        try
        {
            //Write file
            String filename = "bitmap.png";
            Bitmap bmp = clickedNgo.getImage();
            // Compress and save the attached image in order to get it in the next activity
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG,
                         100,
                         stream);

            //Cleanup
            stream.close();

            //Pop intent
            Intent intent = new Intent(NgoListActivity.this, NgoInformationActivity.class);
            intent.putExtra("image", filename);
            intent.putExtra("clickedItem", clickedNgo);

            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

}
