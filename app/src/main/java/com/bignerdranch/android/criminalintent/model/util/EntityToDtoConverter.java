package com.bignerdranch.android.criminalintent.model.util;

import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.Suspect;
import com.bignerdranch.android.criminalintent.model.entity.CrimeEntity;
import com.bignerdranch.android.criminalintent.model.entity.SuspectEntity;

import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 04/12/2016
 */

public class EntityToDtoConverter {
    public static Crime toCrime(CrimeEntity crimeEntity) {
        Crime crime = null;
        if(crimeEntity != null) {
            crime = new Crime();
            crime.setDate(crimeEntity.getDate());
            crime.setTitle(crimeEntity.getTitle());
            crime.setId(UUID.fromString(crimeEntity.getUuid()));
            crime.setSolved(crimeEntity.getSolved());
            crime.setSuspect(toSuspect(crimeEntity.getSuspect()));
        }
        return crime;
    }

    public static Suspect toSuspect(SuspectEntity suspectEntity) {
        Suspect suspect = null;
        if(suspectEntity != null) {
            suspect = new Suspect();
            suspect.setNumber(suspectEntity.getPhoneNumber());
            suspect.setName(suspectEntity.getSuspect());
        }

        return suspect;
    }
}
