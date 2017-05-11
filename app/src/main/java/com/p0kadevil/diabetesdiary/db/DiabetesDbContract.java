package com.p0kadevil.diabetesdiary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;


public class DiabetesDbContract {

    public static Cursor getAllEntries(Context context)
    {
        String[] projection = {
                DiabetesDbContract.DiaryEntry._ID,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_DATE,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_BLOOD,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_BE,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_INSULIN
        };

        DiabetesDbHelper dbHelper = new DiabetesDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(
                DiabetesDbContract.DiaryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_DATE + " ASC"
        );
    }

    public static class DiaryEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_BLOOD = "blood";
        public static final String COLUMN_NAME_BE = "be";
        public static final String COLUMN_NAME_INSULIN = "insulin";
    }
}
