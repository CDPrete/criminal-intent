package com.bignerdranch.android.criminalintent.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.util.PictureUtils;

/**
 * @author pec
 * @since 05/12/2016
 */

public class PhotoShowerFragment extends DialogFragment {

    private static final String PHOTO_FILE_ARG = "photo_file";

    public static PhotoShowerFragment newInstance(String photoFile) {
        Bundle args = new Bundle();
        args.putString(PHOTO_FILE_ARG, photoFile);

        PhotoShowerFragment fragment = new PhotoShowerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        Activity parent = getActivity();

        ImageView view =
                (ImageView) LayoutInflater.from(parent).inflate(R.layout.dialog_photo_view, null);

        if(args != null && args.containsKey(PHOTO_FILE_ARG)) {
            String photoFile = args.getString(PHOTO_FILE_ARG);
            view.setImageBitmap(PictureUtils.getScaledBitmap(photoFile, parent));
        }

        return new AlertDialog.Builder(parent)
                .setTitle(R.string.crime_picture_description)
                .setPositiveButton(android.R.string.ok, null)
                .setView(view)
                .create();
    }
}
