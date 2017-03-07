package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
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



    private RecyclerView mCrimeReccyclerView;
    private CrimeAdapter mAdapter;
    private UUID mCrimeId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeReccyclerView = (RecyclerView) view.findViewById(R.id.crime_recyler_view);
        mCrimeReccyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }



    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }



    private void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeReccyclerView.setAdapter(mAdapter);
        }
        else
        {
            //mAdapter.notifyDataSetChanged();
            Crime crime = crimeLab.getCrime(mCrimeId);
            int position = crimeLab.getCrimes().indexOf(crime);
            mAdapter.notifyItemChanged(position);
        }
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckbox;

        public CrimeHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckbox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.convertDateForHuman());
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
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK) {
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
}
