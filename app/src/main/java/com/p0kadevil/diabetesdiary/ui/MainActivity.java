package com.p0kadevil.diabetesdiary.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.p0kadevil.diabetesdiary.R;


public class MainActivity extends AppCompatActivity {

    public static final String PREF_BE_FACTOR = "be_factor_pref";
    public static final String ANALYTICS_APP_OPEN = "APP_OPEN";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);

        if(getSupportFragmentManager().getFragments() == null ||
                getSupportFragmentManager().getFragments().size() == 0)
        {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, newFragment);
            transaction.commit();
        }
    }
}
