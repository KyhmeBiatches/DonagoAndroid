package com.example.maaaarckyo.donagoandroid.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class NGO implements Parcelable
{
    private Bitmap image;
    private List<Cause> causeList;
    private String objectId;
    private boolean active;
    private String name;
    private String description;
    private String logoUrl;

    public NGO(){ }

    public NGO(Parcel in) {
        objectId = in.readString();
        active = in.readByte() != 0;
        name = in.readString();
        description = in.readString();
        logoUrl = in.readString();
        in.readList(causeList, getClass().getClassLoader());

    }


    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public List<Cause> getCauseList()
    {
        return causeList;
    }

    public void setCauseList(List<Cause> causeList)
    {
        this.causeList = causeList;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }


    // Parcelable implement


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(objectId);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(logoUrl);
        dest.writeList(causeList);
    }

    public static final Creator<NGO> CREATOR = new Creator<NGO>()
    {
        public NGO createFromParcel(Parcel in)
        {
            return new NGO(in);
        }
        public NGO[] newArray(int size)
        {
            return new NGO[size];
        }
    };


    @Override
    public String toString()
    {
        return this.getName();
    }
}
