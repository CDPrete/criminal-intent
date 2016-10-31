package com.bignerdranch.android.criminalintent.controller;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.controller.fragment.CrimeListFragment;

/**
 * @author Cosimo Damiano Prete
 * @since 31/10/2016
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
