/*package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity
{



    private final static String CRIME_ID_EXTRA = "com.bignerdranch.android.criminalintent.crime_id";



    @Override
    protected Fragment createFragment()
    {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID_EXTRA);
        return CrimeFragment.newInstance(crimeId);
    }



    public static Intent newIntent(Context packContext, UUID crimeId)
    {
        Intent intent = new Intent(packContext, CrimeActivity.class);
        intent.putExtra(CRIME_ID_EXTRA, crimeId);
        return intent;
    }
}
*/