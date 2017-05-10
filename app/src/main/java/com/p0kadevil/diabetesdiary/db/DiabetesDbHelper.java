package com.p0kadevil.diabetesdiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract.DiaryEntry;


public class DiabetesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DiabetesDb.db";
    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiabetesDbContract.DiaryEntry.TABLE_NAME + " (" +
                    DiaryEntry._ID + " INTEGER PRIMARY KEY," +
                    DiaryEntry.COLUMN_NAME_DATE + " TIMESTAMP," +
                    DiaryEntry.COLUMN_NAME_BLOOD + " FLOAT," +
                    DiaryEntry.COLUMN_NAME_BE + " INTEGER," +
                    DiaryEntry.COLUMN_NAME_INSULIN + " INTEGER)";

    public DiabetesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
