package com.p0kadevil.diabetesdiary.widget;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract;
import com.p0kadevil.diabetesdiary.db.DiabetesDbHelper;


public class DiabetesDiaryContentProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "com.p0kadevil.diabetesdiary.DiabetesDiaryContentProvider.entries";
    private static final int ENTRIES = 1;

    private static final UriMatcher uriMatcher = getUriMatcher();

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "entries", ENTRIES);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        if (uriMatcher.match(uri) == ENTRIES) {

            return DiabetesDbContract.getAllEntries(getContext());
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri))
        {
            case ENTRIES:
                return "vnd.android.cursor.item/vnd." + PROVIDER_NAME;
            default:
                return "";
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
