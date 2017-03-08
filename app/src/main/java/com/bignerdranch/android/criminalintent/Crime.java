package com.bignerdranch.android.criminalintent;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Neimad on 02/03/2017.
 */

public class Crime
{



    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Date mTime;
    private boolean mSolved;




    public Crime()
    {
        mId = UUID.randomUUID();
        mDate = new Date();
        mTime = new Date();
    }



    public UUID getId()
    {
        return mId;
    }



    public void setId(UUID id)
    {
        mId = id;
    }



    public String getTitle()
    {
        return mTitle;
    }



    public void setTitle(String title)
    {
        mTitle = title;
    }


    public Date getDate()
    {
        return mDate;
    }



    public void setDate(Date date)
    {
        mDate = date;
    }



    public boolean isSolved()
    {
        return mSolved;
    }



    public void setSolved(boolean solved)
    {
        mSolved = solved;
    }



    public Date getTime()
    {
        return mTime;
    }



    public void setTime(Date time)
    {
        mTime = time;
    }



    public String convertDateForHuman()
    {
        DateFormat format = new DateFormat();
        return format.format("EEEE, MMMM dd, yyyy", mDate).toString();
    }



    public String convertTimeForHuman()
    {
        DateFormat format = new DateFormat();
        return format.format("HH:mm", mTime).toString();
    }
}
