package com.bignerdranch.android.criminalintent.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;

/**
 * @author Cosimo Damiano Prete
 * @since 30/10/2016
 */

public class DateUtils {
    private static final String TAG = DateUtils.class.getName();

    private static final String DEFAULT_FORMAT = "EEEE, MMM dd, yyyy";
    private static final String DATE_NOT_SET_MESSAGE = "Crime date is not set.";

    public static CharSequence format(Date dateToFormat, String pattern) {
        if(dateToFormat == null) {
            Log.d(TAG, "The date passed is null -> using the default message '" + DATE_NOT_SET_MESSAGE + "'.");
            return DATE_NOT_SET_MESSAGE;
        } else if(pattern == null || pattern.isEmpty()) {
            Log.d(TAG, "The pattern passed is null or empty -> returning the date as timestamp.");
            return dateToFormat.toString();
        }
        return DateFormat.format(pattern, dateToFormat);
    }

    public static CharSequence format(Date dateToFormat) {
        return format(dateToFormat, DEFAULT_FORMAT);
    }
}
