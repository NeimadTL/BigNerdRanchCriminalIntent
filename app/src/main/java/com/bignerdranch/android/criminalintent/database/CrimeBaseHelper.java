package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by Neimad on 11/03/2017.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper
{



    private final static int VERSION = 1;
    private final static String DATABASE_NAME = "crimesBase.db";



    public CrimeBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + "," +
                CrimeTable.Cols.TITLE + "," +
                CrimeTable.Cols.DATE + "," +
                CrimeTable.Cols.TIME + "," +
                CrimeTable.Cols.SOLVED + "," +
                CrimeTable.Cols.SUSPECT + ")"
        );
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
