package com.sabrewinginfotech.reesguru.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sabrewinginfotech.reesguru.R;


public class MyMatchesFragment extends Fragment {

    private MatchPagerAdapter matchPagerAdapter;
    private ViewPager viewPager;

    public MyMatchesFragment() {
        // Required empty public constructor
    }
    public static MyMatchesFragment newInstance() {
        MyMatchesFragment fragment = new MyMatchesFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_matches, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        matchPagerAdapter = new MatchPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(matchPagerAdapter);
    }
    private class MatchPagerAdapter extends FragmentPagerAdapter {

        private int baseId = 0;

        public MatchPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            return MatchFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }


    }

}
