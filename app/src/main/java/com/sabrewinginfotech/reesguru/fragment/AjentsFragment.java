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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sabrewinginfotech.reesguru.MainActivity;
import com.sabrewinginfotech.reesguru.Progressbar.AVLoadingIndicatorView;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.adapter.AdapterAgents;
import com.sabrewinginfotech.reesguru.adapter.ListingAdapter;
import com.sabrewinginfotech.reesguru.api.model.AgentModel;
import com.sabrewinginfotech.reesguru.api.model.PropertyDetailModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AjentsFragment extends Fragment {

    AdapterAgents adapterAgents;
    protected RecyclerView recyclerView;
    private Call lastCall;
    private Handler handler;
    AgentModel agentModel;
    ArrayList<AgentModel> listallagent = new ArrayList<>();
    LinearLayout linearLayout;
    AVLoadingIndicatorView avLoadingIndicatorView;

    public AjentsFragment() {
        // Required empty public constructor
    }

    public static AjentsFragment newInstance() {
        AjentsFragment fragment = new AjentsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getdata();
        UrlConstant.fragmenttag = 2;
        MainActivity.ivfilter.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_ajents, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll1);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avLoadingIndicatorView.setIndicator("BallSpinFadeLoaderIndicator");
        avLoadingIndicatorView.setIndicatorColor(R.color.colorPrimary);
        avLoadingIndicatorView.startAnimation();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);


    }

    public void getdata() {
        handler = new Handler(Looper.getMainLooper());
        RequestBody requestBody = new FormBody.Builder().addEncoded("currentPage", "0").build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(getActivity(), UrlConstant.API_GET_All_AGENTS_DETAIL_URL, requestBody, mResponseHelper);
    }

    private MyHttpClientHelper.ProcessResponseHelper mResponseHelper = new MyHttpClientHelper.ProcessResponseHelper() {
        @Override
        public void onRequest() {

        }

        @Override
        public void onResponse(JSONObject object) {
            avLoadingIndicatorView.show();
            try {
                int code = object.getInt(ApiConstant.JSON_KEY_CODE);

                if (code == 1) {

                    //mGroups.clear();
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DATA);
                    listallagent.clear();
                    for (int i = 0; i < dataObject.length(); i++) {
                        agentModel = new AgentModel();
                        agentModel.fromJson(dataObject.getJSONObject(i));
                        listallagent.add(agentModel);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                adapterAgents = new AdapterAgents(getActivity(), listallagent, avLoadingIndicatorView);
                                recyclerView.setAdapter(adapterAgents);
                                avLoadingIndicatorView.hide();
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
