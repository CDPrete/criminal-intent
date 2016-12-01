package com.bignerdranch.android.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author pec
 * @since 01/12/2016
 */

public class TimePickerFragment extends DialogFragment {
    private static final String CRIME_TIME_ARG = "crime_time";
    private static final String EXTRA_CRIME_TIME =
                                    TimePickerFragment.class.getPackage().getName() + ".crime_time";

    public static TimePickerFragment newInstance(Date crimeDate) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_TIME_ARG, crimeDate);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final TimePicker timePicker =
                (TimePicker) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        Bundle args = getArguments();
        if(args != null && args.containsKey(CRIME_TIME_ARG)) {
            Date crimeTime = (Date) args.getSerializable(CRIME_TIME_ARG);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(crimeTime);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
            } else {
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            }
        }

        return new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.time_picker_title)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int hour;
                                        int minute;

                                        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                            hour = timePicker.getCurrentHour();
                                            minute = timePicker.getCurrentMinute();
                                        } else {
                                            hour = timePicker.getHour();
                                            minute = timePicker.getMinute();
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                                        calendar.set(Calendar.MINUTE, minute);
                                        sendResult(Activity.RESULT_OK, calendar.getTime());
                                    }
                                })
                                .setView(timePicker)
                                .create();
    }

    public static Date getExtraCrimeTime(Intent data) {
        Date crimeDate = null;
        if(data != null) {
            Serializable serializable = data.getSerializableExtra(EXTRA_CRIME_TIME);
            if(serializable != null) {
                crimeDate = (Date) serializable;
            }
        }
        return crimeDate;
    }

    private void sendResult(int resultCode, Date crimeTime) {
        Fragment targetFragment = getTargetFragment();
        if(targetFragment != null) {
            Intent data = new Intent();
            data.putExtra(EXTRA_CRIME_TIME, crimeTime);
            targetFragment.onActivityResult(getTargetRequestCode(), resultCode, data);
        }
    }
}
