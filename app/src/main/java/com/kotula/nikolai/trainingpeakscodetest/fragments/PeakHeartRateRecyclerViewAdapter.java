package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakHeartRateFragment.OnListFragmentInteractionListener;
import com.kotula.nikolai.trainingpeakscodetest.fragments.dummy.DummyContent.DummyItem;
import com.kotula.nikolai.trainingpeakscodetest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PeakHeartRateRecyclerViewAdapter extends RecyclerView.Adapter<PeakHeartRateRecyclerViewAdapter.ViewHolder> {

    private final List<PeakHeartRate> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PeakHeartRateRecyclerViewAdapter(List<PeakHeartRate> items, OnListFragmentInteractionListener listener) {
        if (items != null) {
            mValues = items;
        } else {
            mValues = new ArrayList<PeakHeartRate>();
        }

        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_peakheartrate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.peakHeartRate = mValues.get(position);
        holder.intervalView.setText(String.valueOf(mValues.get(position).getInterval()));
        holder.valueView.setText(String.valueOf(mValues.get(position).getValue()));
        holder.unitView.setText(R.string.lbl_bpm);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.peakHeartRate);
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
        public final TextView intervalView;
        public final TextView valueView;
        public final TextView unitView;
        public PeakHeartRate peakHeartRate;

        public ViewHolder(View view) {
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
