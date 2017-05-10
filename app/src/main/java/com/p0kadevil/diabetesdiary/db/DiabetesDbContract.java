package com.p0kadevil.diabetesdiary.db;

import android.provider.BaseColumns;


public class DiabetesDbContract {

    public static class DiaryEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_BLOOD = "blood";
        public static final String COLUMN_NAME_BE = "be";
        public static final String COLUMN_NAME_INSULIN = "insulin";
    }
}
