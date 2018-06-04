package com.kotula.nikolai.trainingpeakscodetest.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.kotula.nikolai.trainingpeakscodetest.R;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakFragment;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakHeartRateFragment;
import com.kotula.nikolai.trainingpeakscodetest.fragments.PeakSpeedFragment;

public class WorkoutExplorer extends AppCompatActivity
                             implements PeakFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private String mWorkoutTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_explorer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mWorkoutTag = getIntent().getStringExtra(WorkoutSubmission.WORKOUT_TAG);
    }

    @Override
    public void onListFragmentInteraction(Object item) {
        // For now, do nothing.
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets an instance of the {@link Fragment} at the specified Tab index.
         * <p/>
         * This method MUST handle exactly the same number of cases as the number returned by getCount().
         * @param position The Tab index to get the {@link Fragment} for.
         * @return The {@link Fragment} at the specified index.
         */
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return PeakHeartRateFragment.newInstance(mWorkoutTag);
            } else if (position == 1) {
                return PeakSpeedFragment.newInstance(mWorkoutTag);
            }

            // We should NEVER get here.  The cases above MUST cover all of the positions.
            return null;
        }

        /**
         * Returns the number of tabs in this Tab View.
         * <p/>
         * This number MUST match the number of items that can be returned from getItem().
         * @return The number of tabs in this Tab View.
         */
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
