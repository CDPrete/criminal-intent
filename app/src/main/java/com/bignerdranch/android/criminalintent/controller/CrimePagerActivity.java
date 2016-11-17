package com.bignerdranch.android.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.controller.fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 17/11/2016
 */

public class CrimePagerActivity extends FragmentActivity {
    private static final String EXTRA_CRIME_ID =
                                    CrimePagerActivity.class.getPackage().getName() + ".crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mCrimes = CrimeLab.getInstance(this).getCrimes();
        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mCrimes.get(position).getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager.setCurrentItem(getCurrentCrimeIndex(crimeId));
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    private int getCurrentCrimeIndex(UUID crimeId) {
        for(int i = 0; i < mCrimes.size(); i++) {
            if(mCrimes.get(i).getId().equals(crimeId)) {
                return i;
            }
        }
        return 0;
    }
}
