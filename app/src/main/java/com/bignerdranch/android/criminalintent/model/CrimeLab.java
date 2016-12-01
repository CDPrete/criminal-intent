package com.bignerdranch.android.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 31/10/2016
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
    }

    public static CrimeLab getInstance(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID crimeId) {
        Crime crime = null;
        int i = 0;
        while(i < mCrimes.size() && crime == null) {
            Crime c = mCrimes.get(i);
            if(c.getId().equals(crimeId)) {
                crime = c;
            } else {
                i++;
            }
        }
        return crime;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }
}
