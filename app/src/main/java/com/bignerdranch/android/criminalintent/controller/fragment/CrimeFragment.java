package com.bignerdranch.android.criminalintent.controller.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 30/10/2016
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_TAG = "crime_date_picker";
    private static final String TIME_PICKER_TAG = "crime_time_picker";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckbox;

    private Crime mCrime;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrime = CrimeLab.getInstance(getActivity()).getCrime((UUID)
                getArguments().getSerializable(ARG_CRIME_ID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, group, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(getFragmentManager(), DATE_PICKER_TAG);
            }
        });
        updateDate();

        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(getFragmentManager(), TIME_PICKER_TAG);
            }
        });
        updateTime();

        mSolvedCheckbox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                mCrime.setSolved(checked);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Calendar calendar = createCalendar(mCrime.getDate());

            if(requestCode == REQUEST_DATE) {
                Calendar crimeDateCalendar = createCalendar(DatePickerFragment.getExtraCrimeDate(data));

                calendar.set(Calendar.YEAR, crimeDateCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, crimeDateCalendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, crimeDateCalendar.get(Calendar.DAY_OF_MONTH));

                updateDateModel(calendar);
                updateDate();
            } else if(requestCode == REQUEST_TIME) {
                Calendar crimeTimeCalendar = createCalendar(TimePickerFragment.getExtraCrimeTime(data));

                calendar.set(Calendar.HOUR_OF_DAY, crimeTimeCalendar.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, crimeTimeCalendar.get(Calendar.MINUTE));

                updateDateModel(calendar);
                updateTime();
            }
        }
    }

    private void updateDateModel(Calendar calendar) {
        mCrime.setDate(calendar.getTime());
    }

    private void updateDate() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mDateButton.setText(format.format(mCrime.getDate()));
    }

    private void updateTime() {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeButton.setText(format.format(mCrime.getDate()));
    }

    private Calendar createCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
