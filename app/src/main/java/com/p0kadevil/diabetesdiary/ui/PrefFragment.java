package com.p0kadevil.diabetesdiary.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.p0kadevil.diabetesdiary.R;


public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fragment_prefs);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getView() != null)
        {
            getView().setBackgroundColor(Color.WHITE);
            getView().setClickable(true);
        }
    }
}
