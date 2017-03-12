package com.bignerdranch.android.criminalintent.database;

/**
 * Created by Neimad on 11/03/2017.
 */

public class CrimeDbSchema
{



    public static final class CrimeTable
    {
        public final static String NAME = "crimes";

        public final static class Cols
        {
            public final static String UUID = "uuid";
            public final static String TITLE = "title";
            public final static String DATE = "date";
            public final static String TIME = "time";
            public final static String SOLVED = "solved";
        }
    }
}
