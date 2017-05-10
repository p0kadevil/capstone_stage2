package com.p0kadevil.diabetesdiary.ui;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.p0kadevil.diabetesdiary.R;
import com.p0kadevil.diabetesdiary.db.DiabetesDbContract.DiaryEntry;
import com.p0kadevil.diabetesdiary.db.DiabetesDbHelper;
import com.p0kadevil.diabetesdiary.util.Util;
import java.util.Date;


public class NewEntryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_new_entry, container, false);

        ((TextView) view.findViewById(R.id.tv_currentDate)).setText(
                Util.getFormattedDateString(new Date(), true) + " " + getString(R.string.oclock)
        );

        ((EditText)view.findViewById(R.id.et_bloodValue)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recalculateRecommendation();
            }
        });

        ((EditText)view.findViewById(R.id.et_be)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recalculateRecommendation();
            }
        });

        ((EditText)view.findViewById(R.id.et_insulin)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                recalculateRecommendation();
            }
        });

        view.findViewById(R.id.fab_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateForm())
                {
                    String bloodValue = ((EditText) getView().findViewById(R.id.et_bloodValue)).
                            getText().toString().trim();

                    String beValue = ((EditText) getView().findViewById(R.id.et_be)).
                            getText().toString().trim();

                    String insulinValue = ((EditText) getView().findViewById(R.id.et_insulin)).
                            getText().toString().trim();

                    writeEntryToDb(Float.valueOf(bloodValue),
                            Integer.valueOf(beValue), Integer.valueOf(insulinValue));
                }
            }
        });

        ((TextView)view.findViewById(R.id.tv_recommendation)).setText(getString(R.string.insulin_recommendation) + " " + "0");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().
                setTitle(getString(R.string.new_entry));
    }

    private void recalculateRecommendation()
    {
        String bloodValue = ((EditText) getView().findViewById(R.id.et_bloodValue)).
                getText().toString().trim();

        String beValue = ((EditText) getView().findViewById(R.id.et_be)).
                getText().toString().trim();

        if(bloodValue.length() > 0 && beValue.length() > 0)
        {
            try
            {
                float bloodValueF = Float.valueOf(bloodValue);
                int beValueI = Integer.valueOf(beValue);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                float beFactor = Float.valueOf(sharedPrefs.getString(MainActivity.PREF_BE_FACTOR, "1"));

                int recommendation = Math.round(beValueI * beFactor + ((bloodValueF - 80) / 30));
                ((TextView) getView().findViewById(R.id.tv_recommendation)).
                        setText(getString(R.string.insulin_recommendation) + " " + recommendation);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                ((TextView) getView().findViewById(R.id.tv_recommendation)).
                        setText(getString(R.string.insulin_recommendation) + " " + "0");
            }
        }
        else
        {
            ((TextView) getView().findViewById(R.id.tv_recommendation)).
                    setText(getString(R.string.insulin_recommendation) + " " + "0");
        }
    }

    private void writeEntryToDb(float blood, int be, int insulin)
    {
        DiabetesDbHelper dbHelper = new DiabetesDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DiaryEntry.COLUMN_NAME_DATE, Util.getDateStringSql(new Date()));
        values.put(DiaryEntry.COLUMN_NAME_BLOOD, blood);
        values.put(DiaryEntry.COLUMN_NAME_BE, be);
        values.put(DiaryEntry.COLUMN_NAME_INSULIN, insulin);
        db.insert(DiaryEntry.TABLE_NAME, null, values);

        Toast.makeText(getActivity(), getString(R.string.insert_entry_success),
                Toast.LENGTH_LONG).show();

        getActivity().getSupportFragmentManager().popBackStack();
    }

    private boolean validateForm()
    {
        String bloodValue = ((EditText) getView().findViewById(R.id.et_bloodValue)).
                getText().toString().trim();

        String beValue = ((EditText) getView().findViewById(R.id.et_be)).
                getText().toString().trim();

        String insulinValue = ((EditText) getView().findViewById(R.id.et_insulin)).
                getText().toString().trim();

        if(bloodValue.length() == 0)
        {
            Toast.makeText(getActivity(), getString(R.string.error_blood_value),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(beValue.length() == 0 ||
                !Util.isInteger(beValue, true))
        {
            Toast.makeText(getActivity(), getString(R.string.error_be), Toast.LENGTH_LONG).show();
            return false;
        }

        if(insulinValue.length() == 0 ||
                !Util.isInteger(insulinValue, true))
        {
            Toast.makeText(getActivity(), getString(R.string.error_insulin), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
