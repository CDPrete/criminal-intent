package com.bignerdranch.android.criminalintent.model;


/**
 * @author Cosimo Damiano Prete
 * @since 04/12/2016
 */

public class Suspect {
    private String number;
    private String name;

    public Suspect() {}

    public Suspect(String number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
