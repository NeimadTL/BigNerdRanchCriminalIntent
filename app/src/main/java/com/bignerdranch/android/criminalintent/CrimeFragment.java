package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Neimad on 02/03/2017.
 */

public class CrimeFragment extends Fragment
{

    private final static String ARG_CRIME_ID = "crime_id";
    private final static String CRIME_ID_CHANGED_EXTRA = "com.bignerdranch.android.geoquiz.crime_id_changed";
    private final static String DIALOG_DATE = "DialogDate";
    public final static int REQUEST_DATE = 0;



    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;



    public static CrimeFragment newInstance(UUID crimeId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return  fragment;
    }


    public static UUID wasCrimeChanged(Intent result)
    {
        return (UUID) result.getSerializableExtra(CRIME_ID_CHANGED_EXTRA);
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        mDateButton = (Button) view.findViewById(R.id.crime_date);
        //mDateButton.setText(convertToHumanDate());
        mDateButton.setText(mCrime.getDate().toString());

        mDateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mCrime.setSolved(isChecked);
            }
        });


        setCrimeChangedResult(mCrime.getId());

        return view;
    }


    private String convertToHumanDate()
    {
        DateFormat format = new DateFormat();
        return format.format("EEEE, MMMM dd, yyyy", mCrime.getDate()).toString();
    }



    private void setCrimeChangedResult(UUID crimeId)
    {
        Intent data = new Intent();
        data.putExtra(CRIME_ID_CHANGED_EXTRA, crimeId);
        getActivity().setResult(Activity.RESULT_OK, data);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }

        if (requestCode == REQUEST_DATE)
        {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE_EXTRA);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
    }
}