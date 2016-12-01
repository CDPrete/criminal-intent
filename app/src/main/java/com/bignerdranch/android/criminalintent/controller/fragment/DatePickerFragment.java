package com.bignerdranch.android.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.DatePicker;

import com.bignerdranch.android.criminalintent.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Cosimo Damiano Prete
 * @since 01/12/2016
 */

public class DatePickerFragment extends DialogFragment {
    private static final String CRIME_DATE_ARG = "crime_date";

    private static final String EXTRA_CRIME_DATE =
                                    DatePickerFragment.class.getPackage().getName() + ".crime_date";

    public static DatePickerFragment newInstance(Date crimeDate) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_DATE_ARG, crimeDate);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final DatePicker datePicker =
                (DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        if(args != null && args.containsKey(CRIME_DATE_ARG)) {
            Date crimeDate = (Date) args.getSerializable(CRIME_DATE_ARG);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(crimeDate);

            datePicker.init(
                    calendar.get(
                            Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            null);
        }
        return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.date_picker_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth();
                            int day = datePicker.getDayOfMonth();
                            sendResult(Activity.RESULT_OK, new GregorianCalendar(year, month, day).getTime());
                        }
                    })
                    .setView(datePicker)
                    .create();
    }

    public static Date getExtraCrimeDate(Intent data) {
        Date crimeDate = null;
        if(data != null) {
            Serializable serializable = data.getSerializableExtra(EXTRA_CRIME_DATE);
            if(serializable != null) {
                crimeDate = (Date) serializable;
            }
        }
        return crimeDate;
    }

    private void sendResult(int resultCode, Date crimeDate) {
        Fragment targetFragment = getTargetFragment();
        if(targetFragment != null) {
            Intent resultData = new Intent();
            resultData.putExtra(EXTRA_CRIME_DATE, crimeDate);
            targetFragment.onActivityResult(getTargetRequestCode(), resultCode, resultData);
        }
    }
}
