package com.bignerdranch.android.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.controller.CrimeActivity;
import com.bignerdranch.android.criminalintent.model.Crime;
import com.bignerdranch.android.criminalintent.model.CrimeLab;

import java.util.List;

/**
 * @author Cosimo Damiano Prete
 * @since 31/10/2016
 */

public class CrimeListFragment extends Fragment {
    private static final int NO_CRIME_SELECTED = -1;
    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, group, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CRIME) {
            updateUI(CrimeFragment.getCrimePositionFromData(data, NO_CRIME_SELECTED));
        }
    }

    private void updateUI() {
        updateUI(NO_CRIME_SELECTED);
    }

    private void updateUI(int crimePosition) {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            if(crimePosition != NO_CRIME_SELECTED) {
                mCrimeAdapter.notifyItemChanged(crimePosition);
                crimePosition = NO_CRIME_SELECTED;
            } else {
                mCrimeAdapter.notifyDataSetChanged();
            }
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCrimeTitleTextView;
        private TextView mCrimeDateTextView;
        private CheckBox mCrimeSolvedCheckBox;

        private Crime mCrime;
        private int mCrimePosition;

        public CrimeHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mCrimeTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mCrimeDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mCrimeSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(Crime crime, int crimePosition) {
            mCrime = crime;
            mCrimePosition = crimePosition;

            mCrimeTitleTextView.setText(mCrime.getTitle());
            mCrimeDateTextView.setText(mCrime.getDate().toString());
            mCrimeSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view) {
            startActivityForResult(
                    CrimeActivity.newIntent(getActivity(), mCrimePosition),
                    REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
