package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by Neimad on 03/03/2017.
 */

public class CrimeListFragment extends Fragment
{



    private final static int REQUEST_CODE_ITEM_CHANGED = 0;
    private final String SAVED_SUBTITLE_VISIBLE = "subtitle";



    private RecyclerView mCrimeReccyclerView;
    private CrimeAdapter mAdapter;
    private UUID mCrimeId;
    private boolean mSubtitleVisible;
    private TextView emptyListTextView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeReccyclerView = (RecyclerView) view.findViewById(R.id.crime_recyler_view);
        mCrimeReccyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*if (CrimeLab.get(getActivity()).getCrimes().size() == 0)
        {
            View emptyView = inflater.inflate(R.layout.fragment_crime_list_empty, container, false);
            emptyListTextView = (TextView) emptyView.findViewById(R.id.empty_text_view);
            emptyListTextView.setText("There no crimes");
            return view;
        }*/


        if (savedInstanceState != null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();


        return view;
    }



    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }



    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }



    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        //int count = crimes.size();

        if (mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeReccyclerView.setAdapter(mAdapter);
        }
        else
        {

            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();

            /* if one day this app has list of all the crime in memory
            if (mCrimeId == null)
            {
                // refresh the entire list when crime has been added or deleted
                mAdapter.notifyDataSetChanged();
            }
            else
            {
                // refresh only the crime that has been modified
                Crime crime = crimeLab.getCrime(mCrimeId);
                int position = crimeLab.getCrimes().indexOf(crime);
                mAdapter.notifyItemChanged(position);
            }
            */


        }

        updateSubtitle();

        /*if (count == 0)
        {
            mCrimeReccyclerView.setVisibility(View.GONE);
        }
        else
        {
            mCrimeReccyclerView.setVisibility(View.VISIBLE);
        }*/
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private CheckBox mSolvedCheckbox;

        public CrimeHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.list_item_crime_time_text_view);
            mSolvedCheckbox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.convertDateForHuman());
            mTimeTextView.setText(mCrime.convertTimeForHuman());
            mSolvedCheckbox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v)
        {
            //Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivityForResult(intent, REQUEST_CODE_ITEM_CHANGED);
        }
    }



    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position)
        {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes = crimes;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        if (requestCode == REQUEST_CODE_ITEM_CHANGED)
        {
            if(data == null)
            {
                return;
            }

            mCrimeId = CrimeFragment.wasCrimeChanged(data);
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount,crimeCount);
        if (!mSubtitleVisible)
        {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
