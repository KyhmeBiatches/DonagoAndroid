package com.example.maaaarckyo.donagoandroid.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

public class Cause implements Parcelable
{
    private String objectId;
    private boolean active;
    private String description;
    private String name;
    private Bitmap image;
    private int supporters;
    private Number donatedAmount;


    public Cause()
    {
    }

    public Number getDonatedAmount()
    {
        return donatedAmount;
    }

    public void setDonatedAmount(Number donatedAmount)
    {
        this.donatedAmount = donatedAmount;
    }

    public int getSupporters()
    {
        return supporters;
    }

    public void setSupporters(int supporters)
    {
        this.supporters = supporters;
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public Cause(Parcel in) {
        objectId = in.readString();
        active = in.readByte() != 0;
        name = in.readString();
        description = in.readString();
        donatedAmount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(objectId);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(donatedAmount.doubleValue());
    }

    public static final Parcelable.Creator<Cause> CREATOR = new Parcelable.Creator<Cause>()
    {
        public Cause createFromParcel(Parcel in)
        {
            return new Cause(in);
        }
        public Cause[] newArray(int size)
        {
            return new Cause[size];
        }
    };
}
