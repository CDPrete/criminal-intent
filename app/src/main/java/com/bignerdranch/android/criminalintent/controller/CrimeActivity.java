package com.bignerdranch.android.criminalintent.controller;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.controller.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
