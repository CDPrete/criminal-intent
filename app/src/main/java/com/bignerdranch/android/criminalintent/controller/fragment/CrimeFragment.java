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

/**
 * @author Cosimo Damiano Prete
 * @since 30/10/2016
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_POSITION = "crime_position";
    private static final String EXTRA_CRIME_POSITION =
                                    CrimeFragment.class.getPackage().toString() + ".crime_position";

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    private Crime mCrime;

    public static CrimeFragment newInstance(int crimePosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_CRIME_POSITION, crimePosition);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int crimePosition = getArguments().getInt(ARG_CRIME_POSITION);
        mCrime = CrimeLab.getInstance(getActivity()).getCrimes().get(crimePosition);

        sendResult(crimePosition);
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
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

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

    public static int getCrimePositionFromData(Intent data, int defaultValue) {
        return data.getIntExtra(EXTRA_CRIME_POSITION, 0);
    }

    private void sendResult(int crimePosition) {
        Intent i = new Intent();
        i.putExtra(EXTRA_CRIME_POSITION, crimePosition);
        getActivity().setResult(Activity.RESULT_OK, i);
    }
}
