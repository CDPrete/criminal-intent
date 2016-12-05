package com.bignerdranch.android.criminalintent.controller.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;
import com.bignerdranch.android.criminalintent.util.PictureUtils;

import java.io.File;
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
    private static final int REQUEST_PHOTO = 2;

    private Button mDateButton;
    private Button mSuspectButton;

    private ImageView mPhotoView;

    private File mPhotoFile;

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
        mCrime = lab.getCrime((UUID) getArguments().getSerializable(ARG_CRIME_ID));
        mPhotoFile = lab.getPhotoFile(mCrime);
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

        Button mReportButton = (Button) view.findViewById(R.id.report_crime);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                startActivity(Intent.createChooser(i, getString(R.string.send_report)));
            }
        });

        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final ImageButton mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.criminalintent", mPhotoFile);
                Log.i("ContentURI", contentUri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                startActivityForResult(takePictureIntent, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);
        updatePhotoView();

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) view.findViewById(R.id.choose_crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if(mCrime.getSuspect() != null && !mCrime.getSuspect().isEmpty()) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        if(!areAppsToHandleIntentPresent(pickContact)) {
            mSuspectButton.setEnabled(false);
        }
        if(!areAppsToHandleIntentPresent(takePictureIntent) || mPhotoFile == null) {
            mPhotoButton.setEnabled(false);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity().getApplication()).updateCrime(mCrime);
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
                        String[] queryFields = {ContactsContract.Contacts.DISPLAY_NAME};
                        Cursor cursor = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
                        try {
                            if(cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                mCrime.setSuspect(cursor.getString(0));
                                mSuspectButton.setText(mCrime.getSuspect());
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    break;
                case REQUEST_PHOTO:
                    updatePhotoView();
                    break;
            }
        }
    }

    private boolean areAppsToHandleIntentPresent(Intent intent) {
        return getActivity()
                .getPackageManager()
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = getString(mCrime.isSolved() ? R.string.crime_report_solved : R.string.crime_report_unsolved);

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null || suspect.trim().isEmpty()) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
             suspect = getString(R.string.crime_report_suspect, suspect);
        }

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
    }

    private void updatePhotoView() {
        if(mPhotoFile == null && !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            mPhotoView.setImageBitmap(
                    PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity()));
        }
    }
}
