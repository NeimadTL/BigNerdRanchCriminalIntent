package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Neimad on 14/03/2017.
 */

public class PhotoViewerFragment extends DialogFragment
{


    private final static String ARG_PHOTO_FILE = "photoFile";


    private ImageView mPhotoImageView;


    public static PhotoViewerFragment newInstance(File photoFile)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE, photoFile);

        PhotoViewerFragment fragment = new PhotoViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        File photoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mPhotoImageView = (ImageView) view.findViewById(R.id.crime_photo_dialog);

        Bitmap bitmap = PictureUtils.getScaleBitmap(photoFile.getPath(), getActivity());
        mPhotoImageView.setImageBitmap(bitmap);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.photo_viewer_photo)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}