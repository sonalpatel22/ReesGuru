package com.sabrewinginfotech.reesguru.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sabrewinginfotech.reesguru.MainActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;

public class SuppliersFragment extends Fragment {

    public SuppliersFragment() {
        // Required empty public constructor
    }

    public static SuppliersFragment newInstance(String param1, String param2) {
        SuppliersFragment fragment = new SuppliersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UrlConstant.fragmenttag = 5;
        MainActivity.ivfilter.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_suppliers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
