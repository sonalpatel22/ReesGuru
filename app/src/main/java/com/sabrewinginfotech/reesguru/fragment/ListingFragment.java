package com.sabrewinginfotech.reesguru.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sabrewinginfotech.reesguru.MainActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.adapter.ListingAdapter;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;


public class ListingFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected ListingAdapter listingAdapter;
    private ImageView ivfilter;
    private Call lastCall;
    private Handler handler;
    private List<NearestPropertiesModel> propertiesModels = new ArrayList<>();
    public ListingFragment() {
        // Required empty public constructor
    }
    public static ListingFragment newInstance(String param1, String param2) {
        ListingFragment fragment = new ListingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UrlConstant.fragmenttag = 1;
        MainActivity.ivfilter.setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handler = new Handler(Looper.getMainLooper());
        initView(view);
    }

    private void initView(View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        listingAdapter = new ListingAdapter(getActivity(), propertiesModels);
        recyclerView.setAdapter(listingAdapter);

        actionGetPropertiesListing();
    }

    private void actionGetPropertiesListing() {

        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("latitude", "21.1702")
                .addEncoded("longitude", "72.8311").build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(getActivity(), UrlConstant.API_GET_NEAREST_PROPERTIES_URL, requestBody, mPropertiesListingResponseHelper);
    }

    private MyHttpClientHelper.ProcessResponseHelper mPropertiesListingResponseHelper = new MyHttpClientHelper.ProcessResponseHelper() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onResponse(JSONObject object) {
            try {
                int code = object.getInt(ApiConstant.JSON_KEY_CODE);

                if (code == 1) {
                    //mGroups.clear();
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DATA);
                    propertiesModels = new ArrayList<>();
                    Log.e("propertylist", "" + dataObject.getJSONObject(3));
                    for (int i = 0; i < dataObject.length(); i++) {
                        JSONObject property = dataObject.getJSONObject(i);
                        NearestPropertiesModel propertiesModel = new NearestPropertiesModel();
                        propertiesModel.fromJson(property);
                        propertiesModels.add(propertiesModel);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listingAdapter = new ListingAdapter(getActivity(), propertiesModels);
                                recyclerView.setAdapter(listingAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
        }

        @Override
        public void onFail() throws Exception {

        }
    };
}
