package com.bignerdranch.android.criminalintent.controller.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;
import com.bignerdranch.android.criminalintent.model.Suspect;

import java.util.UUID;

/**
 * @author Cosimo Damiano Prete
 * @since 30/10/2016
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_TAG = "crime_date_picker";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;

    private static final int REQUEST_READ_CONTACT_PERMISSION = 0;

    private Button mDateButton;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallSuspectButton;

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

        mCrime = CrimeLab.getInstance(getActivity().getApplication()).getCrime((UUID)
                getArguments().getSerializable(ARG_CRIME_ID));
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

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) view.findViewById(R.id.choose_crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mReportButton = (Button) view.findViewById(R.id.report_crime);
        mReportButton.setEnabled(isSuspectSet());
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_suspect))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
            }
        });

        mCallSuspectButton = (Button) view.findViewById(R.id.call_suspect);
        mCallSuspectButton.setEnabled(isSuspectSet());
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + mCrime.getSuspect().getNumber()));
                startActivity(call);
            }
        });
        if(mCrime.getSuspect() != null) {
            String suspectName = mCrime.getSuspect().getName().trim();
            if(!suspectName.isEmpty()) {
                mSuspectButton.setText(suspectName);
            }
        }
        if(getActivity().getPackageManager().resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        return view;
    }

    private boolean isSuspectSet() {
        boolean isSet = false;
        Suspect suspect = mCrime.getSuspect();
        if(suspect != null) {
            String phone = suspect.getNumber();
            isSet = phone != null && !phone.trim().isEmpty();
        }
        return isSet;
    }

    private void requestReadContactsPermission() {
        ActivityCompat
                .requestPermissions(
                        getActivity(),
                        new String[] {Manifest.permission.READ_CONTACTS},
                        REQUEST_READ_CONTACT_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACT_PERMISSION:
                if(grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showGrantPermissionMessage();
                }
        }
    }

    private void showGrantPermissionMessage() {
        Toast.makeText(getActivity(), R.string.grant_permission, Toast.LENGTH_SHORT).show();
    }

    private boolean isReadContactsPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity().getApplication()).updateCrime(mCrime);
        boolean isSuspectSet = isSuspectSet();
        mReportButton.setEnabled(isSuspectSet);
        mCallSuspectButton.setEnabled(isSuspectSet);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DATE:
                    mCrime.setDate(DatePickerFragment.getExtraCrimeDate(data));
                    updateDate();
                    break;
                case REQUEST_CONTACT:
                    if(data != null) {
                        Uri contactUri = data.getData();
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        String[] queryFields =
                                {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
                        Cursor cursor = contentResolver.query(contactUri, queryFields, null, null, null);
                        try {
                            if(cursor.getCount() > 0) {
                                cursor.moveToFirst();

                                Suspect suspect = new Suspect();
                                suspect.setName(cursor.getString(1));

                                do {
                                    if (isReadContactsPermissionGranted()) {
                                        String[] projectionFields = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                                        String selection = ContactsContract.CommonDataKinds.Phone._ID + "=?";
                                        String[] selectionValues = {cursor.getString(0)};
                                        Cursor phoneCursor =
                                                contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionFields, selection, selectionValues, null);

                                        try {
                                            if (phoneCursor.getCount() > 0) {
                                                phoneCursor.moveToFirst();
                                                suspect.setNumber(phoneCursor.getString(0));
                                            }
                                        } finally {
                                            phoneCursor.close();
                                        }
                                    } else {
                                        showGrantPermissionMessage();
                                        requestReadContactsPermission();
                                    }
                                }while (!isReadContactsPermissionGranted());

                                    mCrime.setSuspect(suspect);
                                    mSuspectButton.setText(mCrime.getSuspect().getName());
                            }
                        } finally {
                            cursor.close();
                        }
                    }
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = getString(mCrime.isSolved() ? R.string.crime_report_solved : R.string.crime_report_unsolved);

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        Suspect suspect = mCrime.getSuspect();
        String suspectString;
        if(suspect == null || suspect.getName().trim().isEmpty()) {
            suspectString = getString(R.string.crime_report_no_suspect);
        } else {
            suspectString = getString(R.string.crime_report_suspect, suspect.getName());
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspectString);
    }
}
