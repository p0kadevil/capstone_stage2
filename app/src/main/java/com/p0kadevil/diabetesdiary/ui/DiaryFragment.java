package com.p0kadevil.diabetesdiary.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.monxalo.android.widget.SectionCursorAdapter;
import com.p0kadevil.diabetesdiary.R;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract.DiaryEntry;
import com.p0kadevil.diabetesdiary.db.DiabetesDbHelper;


public class DiaryFragment extends ListFragment {

    private static final int PLATFORM = 1;

    private Cursor cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestCursor();
        setListAdapter(new EntrySectionAdapter(getActivity(), cursor));
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().
                setTitle(getString(R.string.diary));
    }

    private void requestCursor()
    {
        String[] projection = {
                DiaryEntry._ID,
                DiaryEntry.COLUMN_NAME_DATE,
                DiaryEntry.COLUMN_NAME_BLOOD,
                DiaryEntry.COLUMN_NAME_BE,
                DiaryEntry.COLUMN_NAME_INSULIN
        };

        DiabetesDbHelper dbHelper = new DiabetesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor = db.query(
                DiaryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                DiaryEntry.COLUMN_NAME_DATE + " ASC"
        );
    }

    private class EntrySectionAdapter extends SectionCursorAdapter{

        public EntrySectionAdapter(Context context, Cursor c) {
            super(context, c, android.R.layout.preference_category, PLATFORM);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view;

            String text = getString(R.string.blood_value) + ": " +
                    cursor.getString(cursor.getColumnIndex(DiaryEntry.COLUMN_NAME_BLOOD)) + ", " +
                    getString(R.string.be) + ": " +
                    cursor.getString(cursor.getColumnIndex(DiaryEntry.COLUMN_NAME_BE)) + ", " +
                    getString(R.string.insulin) + ": " +
                    cursor.getString(cursor.getColumnIndex(DiaryEntry.COLUMN_NAME_INSULIN));

            textView.setText(text);
        }
    }
}
