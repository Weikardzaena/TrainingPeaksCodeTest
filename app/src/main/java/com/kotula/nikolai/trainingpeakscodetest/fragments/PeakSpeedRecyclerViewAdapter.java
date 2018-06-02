package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakSpeed;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakSpeedFragment.OnListFragmentInteractionListener;
import com.kotula.nikolai.trainingpeakscodetest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PeakSpeed} entry and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PeakSpeedRecyclerViewAdapter extends RecyclerView.Adapter<PeakSpeedRecyclerViewAdapter.ViewHolder> {

    private final List<PeakSpeed> mValues;
    private final OnListFragmentInteractionListener mListener;

    PeakSpeedRecyclerViewAdapter(List<PeakSpeed> items, OnListFragmentInteractionListener listener) {
        if (items != null) {
            mValues = items;
        } else {
            mValues = new ArrayList<PeakSpeed>();
        }
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_peakspeed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // Populate the values from the POJO.
        // NOTE That this assumes a non-null POJO instance, and it is up to the DATA SOURCE to
        //      ensure the list of POJOs contains no null references.
        holder.peakSpeed = mValues.get(position);
        holder.intervalView.setText(String.valueOf(mValues.get(position).getInterval()));
        holder.valueView.setText(String.format(Locale.getDefault(), "%.2f", mValues.get(position).getValue()));
        holder.unitView.setText(R.string.lbl_min_per_mile);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.peakSpeed);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        final TextView intervalView;
        final TextView valueView;
        final TextView unitView;
        public PeakSpeed peakSpeed;

        ViewHolder(View view) {
            super(view);
            this.view = view;
            intervalView = view.findViewById(R.id.interval_peak);
            valueView = view.findViewById(R.id.value_peak);
            unitView = view.findViewById(R.id.unit_peak);
        }

        @Override
        public String toString() {
            return String.format("%s '%s %s %s'",
                    super.toString(),
                    intervalView.getText().toString(),
                    valueView.getText().toString(),
                    unitView.getText().toString());
        }
    }
}
