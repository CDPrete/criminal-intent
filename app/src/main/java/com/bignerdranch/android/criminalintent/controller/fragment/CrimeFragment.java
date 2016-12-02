package com.bignerdranch.android.criminalintent.controller.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;

import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 30/10/2016
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_TAG = "crime_date_picker";
    private static final String CRIME_CANCELLATION_CONFIRMATION_TAG = "confirm_crime_cancellation";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CRIME_CANCELLATION = 1;

    private Button mDateButton;

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

        CrimeLab lab = CrimeLab.getInstance(getActivity().getApplication());
        mCrime = lab.getCrime((UUID)
                getArguments().getSerializable(ARG_CRIME_ID));

        setHasOptionsMenu(!lab.getCrimes().isEmpty());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, group, false);

        EditText mTitleField = (EditText) view.findViewById(R.id.crime_title);
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

        CheckBox mSolvedCheckbox = (CheckBox) view.findViewById(R.id.crime_solved);
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
    public void onPause() {
        super.onPause();

        if(mCrime != null) {
            CrimeLab.getInstance(getActivity().getApplication()).updateCrime(mCrime);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime_menu_item:
                ConfirmationDialogFragment confirmation = ConfirmationDialogFragment
                        .newInstance(
                                R.string.delete_crime,
                                R.string.delete_crime_confirmation);
                confirmation.setTargetFragment(CrimeFragment.this, REQUEST_CRIME_CANCELLATION);
                confirmation.show(getFragmentManager(), CRIME_CANCELLATION_CONFIRMATION_TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DATE:
                    mCrime.setDate(DatePickerFragment.getExtraCrimeDate(data));
                    updateDate();
                    break;
                case REQUEST_CRIME_CANCELLATION:
                    Activity parent = getActivity();
                    CrimeLab.getInstance(parent.getApplication()).removeCrime(mCrime);
                    mCrime = null;
                    parent.finish();
                    break;
            }

        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
