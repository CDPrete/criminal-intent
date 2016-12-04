package com.bignerdranch.android.criminalintent.model.util;

import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.Suspect;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntity;
import com.bignerdranch.android.criminalintent.model.entity.SuspectEntity;

/**
 * @author Cosimo Damiano Prete
 * @since 04/12/2016
 */

public class DtoToEntityConverter {

    public static CrimeEntity toCrimeEntity(Crime crime) {
        CrimeEntity crimeEntity = null;
        if(crime != null) {
            crimeEntity = new CrimeEntity();
            crimeEntity.setSolved(crime.isSolved());
            crimeEntity.setTitle(crime.getTitle());
            crimeEntity.setUuid(crime.getId().toString());
            crimeEntity.setDate(crime.getDate());
            crimeEntity.setSuspect(toSuspectEntity(crime.getSuspect()));
        }
        return crimeEntity;
    }

    public static SuspectEntity toSuspectEntity(Suspect suspect) {
        SuspectEntity entity = null;
        if(suspect != null) {
            entity = new SuspectEntity();
            entity.setPhoneNumber(suspect.getNumber());
            entity.setSuspect(suspect.getName());
        }

        return entity;
    }
}
