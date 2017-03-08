package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Neimad on 07/03/2017.
 */

public class TimePickerFragment extends DialogFragment
{


    private final static String ARG_TIME = "time";
    public final static String TIME_EXTRA = "com.bignerdranch.android.criminalintent.time";
    private final static String TAG = "TimePickerFragment";



    private TimePicker mTimePicker;



    public static TimePickerFragment newInstance(Date time)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Date time = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
        else
        {
            Log.e(TAG, " hours and minutes not set...");
        }


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title) // TODO: issue with that on landscape mode
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        int hour = 0;
                        int minute = 0;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                           hour = mTimePicker.getHour();
                           minute  = mTimePicker.getMinute();
                        }
                        else
                        {
                            Log.e(TAG, " haven't got hours and minutes...");
                        }
                        Date time = new GregorianCalendar(0,0,0,hour,minute).getTime();
                        sendResult(Activity.RESULT_OK, time);


                    }
                })
                .create();
    }



    private void sendResult(int resultCode, Date date)
    {
        if (getTargetFragment() == null)
        {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(TIME_EXTRA, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
