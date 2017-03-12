package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Neimad on 06/03/2017.
 */

public class CrimePagerActivity extends AppCompatActivity
{


    private final static String CRIME_ID_EXTRA = "com.bignerdranch.android.criminalintent.crime_id";


    private ViewPager mViewPager;
    private List<Crime> mCrimes;



    public static Intent newIntent(Context packContext, UUID crimeId)
    {
        Intent intent = new Intent(packContext, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID_EXTRA, crimeId);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID_EXTRA);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager)
        {
            @Override
            public Fragment getItem(int position)
            {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount()
            {
                return mCrimes.size();
            }
        });


        for (int i=0; i<mCrimes.size(); i++)
        {
            if (mCrimes.get(i).getId().equals(crimeId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
