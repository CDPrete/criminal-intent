package com.bignerdranch.android.criminalintent.model;

import android.app.Application;

import com.bignerdranch.android.criminalintent.App;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntity;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntityDao;
import com.bignerdranch.android.criminalintent.model.entity.DaoSession;
import com.bignerdranch.android.criminalintent.model.entity.SuspectEntity;
import com.bignerdranch.android.criminalintent.model.entity.SuspectEntityDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.criminalintent.model.util.DtoToEntityConverter.toCrimeEntity;
import static com.bignerdranch.android.criminalintent.model.util.EntityToDtoConverter.toCrime;

/**
 * @author Cosimo Damiano Prete
 * @since 31/10/2016
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private CrimeEntityDao mCrimeDao;
    private SuspectEntityDao mSuspectDao;

    private CrimeLab(Application context) {
        DaoSession daoSession = ((App) context).getDaoSession();
        mCrimeDao = daoSession.getCrimeEntityDao();
        mSuspectDao = daoSession.getSuspectEntityDao();
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

        updateSuspect(crimeEntity.getSuspect(), crime.getSuspect());

        mCrimeDao.update(crimeEntity);
    }

    private void updateSuspect(SuspectEntity oldSuspect, Suspect newSuspect) {
        if(oldSuspect != null && newSuspect != null) {
            oldSuspect.setPhoneNumber(newSuspect.getNumber());
            oldSuspect.setSuspect(newSuspect.getName());

            mSuspectDao.update(oldSuspect);
        }
    }

    private CrimeEntity getCrimeEntity(UUID crimeId) {
        return mCrimeDao
                .queryBuilder()
                .where(CrimeEntityDao.Properties.Uuid.eq(crimeId.toString()))
                .uniqueOrThrow();
    }/*

    private Cursor fromBeginning() {
        return mCrimeDao
                .queryBuilder()
                .buildCursor()
                .query();
    }*/
}
