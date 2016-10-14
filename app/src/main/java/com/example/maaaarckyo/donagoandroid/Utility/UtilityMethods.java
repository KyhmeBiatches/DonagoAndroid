package com.example.maaaarckyo.donagoandroid.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.maaaarckyo.donagoandroid.Models.Cause;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UtilityMethods
{

    // Converts a url to a bitmap
    public static Bitmap ConvertUrlToBitmap(String url)
    {
        try
        {
            URL newUrl = new URL(url);
            return BitmapFactory.decodeStream(newUrl.openConnection().getInputStream());
        }
        catch(Exception f)
        {
            f.printStackTrace();
        }

        return null;
    }

    // Gets the amount of supporters for the cause
    public static int GetSupportersCountForCause(Cause cause)
    {

        ParseObject compareCause = ParseObject.createWithoutData("Cause", cause.getObjectId());

        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("PublicUserProfile");
            query.whereEqualTo("cause",
                               compareCause);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1 ));
            return query.find().size();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    // Gets the total amount of money donated to the selected cause
    public static Number GetDonatedMoneyForCause(Cause cause)
    {
        ParseObject compareCause = ParseObject.createWithoutData("Cause", cause.getObjectId());
        int donatedMoney = 0;
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Donation");
            query.whereEqualTo("cause", compareCause);
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.setMaxCacheAge(TimeUnit.HOURS.toMillis(1));
            List<ParseObject> donationList = query.find();


            for (ParseObject item : donationList)
            {
                Number temp = item.getNumber("donationAmount");
                donatedMoney += temp.intValue();
            }

            return donatedMoney;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return donatedMoney;
    }


}
