package com.p0kadevil.diabetesdiary.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.p0kadevil.diabetesdiary.R;


public class HomeFragment extends Fragment {

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        view.findViewById(R.id.btn_newEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new NewEntryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        view.findViewById(R.id.btn_diary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new DiaryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        view.findViewById(R.id.fab_prefs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrefFragment newFragment = new PrefFragment();
                android.app.FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity)getActivity()).getSupportActionBar().
                    setTitle(getString(R.string.app_name));
    }
}
