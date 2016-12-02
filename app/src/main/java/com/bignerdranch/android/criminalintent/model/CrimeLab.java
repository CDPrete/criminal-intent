package com.bignerdranch.android.criminalintent.model;

import android.app.Application;

import com.bignerdranch.android.criminalintent.App;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntity;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntityDao;
import com.bignerdranch.android.criminalintent.model.entity.DaoSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 31/10/2016
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private CrimeEntityDao mCrimeDao;

    private CrimeLab(Application context) {
        DaoSession daoSession = ((App) context).getDaoSession();
        mCrimeDao = daoSession.getCrimeEntityDao();
    }

    public static CrimeLab getInstance(Application context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<CrimeEntity> crimeEntities = mCrimeDao.loadAll();
        List<Crime> crimes = new ArrayList<>();
        for(CrimeEntity crimeEntity : crimeEntities) {
            crimes.add(toCrime(crimeEntity));
        }
        return crimes;
    }

    public Crime getCrime(UUID crimeId) {
        return toCrime(getCrimeEntity(crimeId));
    }

    public void addCrime(Crime crime) {
        mCrimeDao.insert(toCrimeEntity(crime));
    }

    public void updateCrime(Crime crime) {
        CrimeEntity crimeEntity = getCrimeEntity(crime.getId());
        crimeEntity.setDate(crime.getDate());
        crimeEntity.setTitle(crime.getTitle());
        crimeEntity.setSolved(crime.isSolved());

        mCrimeDao.update(crimeEntity);

    }

    private CrimeEntity getCrimeEntity(UUID crimeId) {
        return mCrimeDao
                .queryBuilder()
                .where(CrimeEntityDao.Properties.Uuid.eq(crimeId.toString()))
                .uniqueOrThrow();
    }

    private Crime toCrime(CrimeEntity crimeEntity) {
        Crime crime = null;
        if(crimeEntity != null) {
            crime = new Crime();
            crime.setDate(crimeEntity.getDate());
            crime.setTitle(crimeEntity.getTitle());
            crime.setId(UUID.fromString(crimeEntity.getUuid()));
            crime.setSolved(crimeEntity.getSolved());
        }
        return crime;
    }

    private CrimeEntity toCrimeEntity(Crime crime) {
        CrimeEntity crimeEntity = null;
        if(crime != null) {
            crimeEntity = new CrimeEntity();
            crimeEntity.setSolved(crime.isSolved());
            crimeEntity.setTitle(crime.getTitle());
            crimeEntity.setUuid(crime.getId().toString());
            crimeEntity.setDate(crime.getDate());
        }
        return crimeEntity;
    }

    public void removeCrime(Crime crime) {
        mCrimeDao.delete(getCrimeEntity(crime.getId()));
    }
}
