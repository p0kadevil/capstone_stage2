package com.p0kadevil.diabetesdiary.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.monxalo.android.widget.SectionCursorAdapter;
import com.p0kadevil.diabetesdiary.R;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract.DiaryEntry;


public class DiaryFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PLATFORM = 1;

    private static final String PROVIDER_NAME = "com.p0kadevil.diabetesdiary.DiabetesDiaryContentProvider.entries";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/entries");

    private EntrySectionAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new EntrySectionAdapter(getActivity(), null);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().
                setTitle(getString(R.string.diary));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DiabetesDbContract.DiaryEntry._ID,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_DATE,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_BLOOD,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_BE,
                DiabetesDbContract.DiaryEntry.COLUMN_NAME_INSULIN
        };

        return new CursorLoader(getActivity(),
                CONTENT_URI,
                projection,
                null,
                null,
                DiaryEntry.COLUMN_NAME_DATE + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
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
