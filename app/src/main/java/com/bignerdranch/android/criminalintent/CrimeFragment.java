package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.database.Crime;
import com.bignerdranch.android.criminalintent.database.CrimeLab;

import java.io.File;
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
    private final static String DIALOG_TIME = "Dialogtime";
    private final static String DIALOG_PHOTO = "DialogPhoto";
    public final static int REQUEST_DATE = 0;
    public final static int REQUEST_TIME = 1;
    public final static int REQUEST_CONTACT = 2;
    public final static int REQUEST_PHOTO = 3;
    public final static int REQUEST_PHOTO_DIALOG = 4;



    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoview;
    private File mPhotoFile;



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
        setHasOptionsMenu(true);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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
            public void afterTextChanged(Editable s)
            {
                setCrimeChangedResult(mCrime.getId());
            }
        });


        mDateButton = (Button) view.findViewById(R.id.crime_date);
        //mDateButton.setText(mCrime.getDate().toString());
        updateDate();
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


        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        //mTimeButton.setText(mCrime.convertTimeForHuman());
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
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


        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getCrimeReport())
                    .setSubject(getString(R.string.crime_report_subject))
                    .setChooserTitle(getString(R.string.send_report))
                    .createChooserIntent();
                startActivity(intent);

                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);*/
            }
        });


        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        // for testing if the suspect button get disabled
        // this line prevents any contacts applications from matching the intent above
        //pickContact.addCategory(Intent.CATEGORY_HOME);

        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });


        if (mCrime.getSuspect() != null)
        {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            mSuspectButton.setEnabled(false);
        }


        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoview = (ImageView) view.findViewById(R.id.crime_photo);
        mPhotoview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mPhotoFile != null && mPhotoFile.exists())
                {
                    //Toast.makeText(getActivity(),R.string.photo_get_pressed,Toast.LENGTH_SHORT).show();
                    FragmentManager manager = getFragmentManager();
                    PhotoViewerFragment dialog = PhotoViewerFragment.newInstance(mPhotoFile);
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_PHOTO_DIALOG);
                    dialog.show(manager, DIALOG_PHOTO);
                }
            }
        });
        updatePhotoView();

        return view;
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
            updateDate();
        }
        else if (requestCode == REQUEST_TIME)
        {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.TIME_EXTRA);
            mCrime.setTime(time);
            updateTime();
        }
        else if (requestCode == REQUEST_CONTACT & data != null)
        {
            Uri contactUri = data.getData();

            // Specify which fields you want your query to return values form
            String[] queryFields = new String[]{ ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID };

            // perform your query - the contactUri is like a "where" clause
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try
            {
                // double-check that you actually got results
                if (c.getCount() == 0)
                {
                    return;
                }

                // pull out the first column of the first row of data - that is you suspect's name
                c.moveToFirst();
                String suspect = c.getString(0);
                /*String contactId = c.getString(1);

                String[] queryFieldsPhone = new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER };
                Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFieldsPhone, null, null, null);
                cursor.moveToFirst();
                String phoneNumber = cursor.getString(0);*/

                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
            finally
            {
                c.close();
            }


        } else if (requestCode == REQUEST_PHOTO)
        {
            updatePhotoView();
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_delete_crime:
                Activity activity = getActivity();
                CrimeLab.get(activity).remove(mCrime);
                activity.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onPause()
    {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }



    private void updateDate()
    {
        String readableDate = mCrime.convertDateForHuman();
        mDateButton.setText(readableDate);
    }



    private void updateTime()
    {
        String readableTime = mCrime.convertTimeForHuman();
        mTimeButton.setText(readableTime);
    }



    private String getCrimeReport()
    {
        String  solvedString = null;

        if(mCrime.isSolved())
        {
            solvedString = getString(R.string.crime_report_solved);
        }
        else
        {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null)
        {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else
        {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTime(), dateString, solvedString, suspect);
        return report;
    }



    private void updatePhotoView()
    {
        if (mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoview.setImageDrawable(null);
        }
        else
        {
            Bitmap bitmap = PictureUtils.getScaleBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoview.setImageBitmap(bitmap);
        }
    }
}
