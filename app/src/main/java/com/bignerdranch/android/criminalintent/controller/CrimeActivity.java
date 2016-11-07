package com.bignerdranch.android.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.controller.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_POSITION =
            CrimeActivity.class.getPackage().toString() + ".crime_position";

    @Override
    protected Fragment createFragment() {
        return CrimeFragment.newInstance(getIntent().getIntExtra(EXTRA_CRIME_POSITION, 0));
    }

    public static Intent newIntent(Context packageContext, int crimePosition) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_POSITION, crimePosition);
        return intent;
    }
}
