package com.kotula.nikolai.trainingpeakscodetest.fragments;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kotula.nikolai.trainingpeakscodetest.data.PeakHeartRate;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakFragment.OnListFragmentInteractionListener;
import com.kotula.nikolai.trainingpeakscodetest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PeakHeartRate} entry and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PeakHeartRateRecyclerViewAdapter extends RecyclerView.Adapter<PeakHeartRateRecyclerViewAdapter.ViewHolder> {

    private final List<PeakHeartRate> mValues;
    private final OnListFragmentInteractionListener<PeakHeartRate> mListener;

    PeakHeartRateRecyclerViewAdapter(List<PeakHeartRate> items, OnListFragmentInteractionListener<PeakHeartRate> listener) {
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
        // Populate the values from the POJO.
        // NOTE That this assumes a non-null POJO instance, and it is up to the DATA SOURCE to
        //      ensure the list of POJOs contains no null references.
        holder.peakHeartRate = mValues.get(position);
        holder.intervalView.setText(buildIntervalString(mValues.get(position).getInterval()));
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

        // Alternate row colors for easier viewing:
        if ((position % 2) == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F0F0FF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    @NonNull
    private String buildIntervalString(int interval) {
        // Populate relevant values first:
        int minutes = interval / 60;
        int hours = minutes / 60;
        int seconds = interval % 60;


        if (Math.abs(hours) > 0) {
            // If we have hours, deal with that case first:
            if ((seconds == 0) && (minutes == 0))
                return String.format(Locale.getDefault(), "%d hr", hours);
            else
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else if (Math.abs(minutes) > 0) {
            // We don't have hours, but we have minutes:
            if (seconds == 0)
                return String.format(Locale.getDefault(), "%d min", minutes);
            else
                return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        } else {
            // Only seconds:
            return String.format(Locale.getDefault(), "%d sec", seconds);
        }
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
        public PeakHeartRate peakHeartRate;

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
