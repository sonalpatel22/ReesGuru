package com.sabrewinginfotech.reesguru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sabrewinginfotech.reesguru.adapter.AdapterAgents;
import com.sabrewinginfotech.reesguru.adapter.AdapterProprty;
import com.sabrewinginfotech.reesguru.adapter.ListingAdapter;
import com.sabrewinginfotech.reesguru.adapter.SimilarPropertyAdapter;
import com.sabrewinginfotech.reesguru.api.model.AgentDetailModel;
import com.sabrewinginfotech.reesguru.api.model.AgentModel;
import com.sabrewinginfotech.reesguru.api.model.PropertyModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;
import com.sabrewinginfotech.reesguru.helper.TintDrawableHelper;
import com.sabrewinginfotech.reesguru.view.CircleImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AgentDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView headerCoverImage;
    private CircleImageView userProfilePhoto;
    private RelativeLayout profileLayout;
    private TextView userProfileName;
    private TextView userProfileShortBio;
    private TextView tvrate;
    private CardView cardAgentinfo;
    private LinearLayout llcontactinfo;
    private TextView tvEmail;
    private TextView tvNumber;
    private CardView cardCompanyinfo;
    private RelativeLayout rlcompanyinfo;
    private TextView tvCompanydetaillabel;
    private TextView tvcomapnyname;
    private TextView tvcomapnycontact;
    private CardView cardPropertyinfo;
    private RelativeLayout propertyinfo;
    private TextView tvPropertylabel;
    private TextView tvPropertyname;
    private TextView tvPropertyaddress;
    private TextView tvPdec;
    private ImageView imageView;
    private ImageView ivlist;
    private RecyclerView recyclerView;
    private CircleImageView compimageview;
    ListingAdapter listingAdapter;
    String tag = "AgentDetailActivity";
    int flag = 0;
    ViewPager viewpager;
    ArrayList<PropertyModel> list = new ArrayList<>();


    private void findViews() {
        tvtitle = (RobotoTextView) findViewById(R.id.tvtoolbartitle);
        headerCoverImage = (ImageView) findViewById(R.id.header_cover_image);
        userProfilePhoto = (CircleImageView) findViewById(R.id.user_profile_photo);
        userProfileName = (TextView) findViewById(R.id.user_profile_name);
        userProfileShortBio = (TextView) findViewById(R.id.user_profile_short_bio);
        tvrate = (TextView) findViewById(R.id.tvrate);
        cardAgentinfo = (CardView) findViewById(R.id.card_agentinfo);
        llcontactinfo = (LinearLayout) findViewById(R.id.llcontactinfo);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        cardCompanyinfo = (CardView) findViewById(R.id.card_companyinfo);
        rlcompanyinfo = (RelativeLayout) findViewById(R.id.rlcompanyinfo);
        tvCompanydetaillabel = (TextView) findViewById(R.id.tvCompanydetaillabel);
        tvcomapnyname = (TextView) findViewById(R.id.tvcomapnyname);
        tvcomapnycontact = (TextView) findViewById(R.id.tvcomapnycontact);
        compimageview = (CircleImageView) findViewById(R.id.ivclogo);
        ivlist = (ImageView) findViewById(R.id.ivlist);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(AgentDetailActivity.this);
        recyclerView.setLayoutManager(manager);
        ivlist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivlist:
                if (flag == 0) {
                    listingAdapter = new ListingAdapter(this, list, tag);
                    recyclerView.setAdapter(listingAdapter);
                    viewpager.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    ivlist.setImageResource(R.drawable.ic_indeterminate_check_box_black_24dp);
                    flag = 1;
                } else if (flag == 1) {
                    recyclerView.setVisibility(View.GONE);
                    viewpager.setVisibility(View.VISIBLE);
                    ivlist.setImageResource(R.drawable.ic_playlist_play_black_24dp);
                    flag = 0;
                }

                break;
        }
    }

    private Call lastCall;
    private Handler handler;
    public String id;
    public String aimage;
    public String aname;
    public String aemail;
    public String aphone;
    public String cname;
    public String cemail;
    public String caddress;
    public String cmobile;
    public String clandline;
    public String compImage;
    PropertyModel propertyModel;
    RobotoTextView tvtitle;

    Toolbar toolbar;
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agent_detail_2);
        findViews();

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setNavigationIcon(TintDrawableHelper.getTintedResource(this, android.support.design.R.drawable.abc_ic_ab_back_material, R.color.colorWhite));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent i = getIntent();
        try {
            tvtitle.setText(i.getStringExtra("agentname"));
            id = i.getStringExtra("agentid");
            cname = i.getStringExtra("C_name");
            cemail = i.getStringExtra("Email");
            caddress = i.getStringExtra("Address");
            cmobile = i.getStringExtra("agentModel");
            aname = i.getStringExtra("agentname");
            aemail = i.getStringExtra("agentemail");
            aphone = i.getStringExtra("agentphone");
            aimage = i.getStringExtra("Image");
            clandline = i.getStringExtra("Landline");
            compImage = i.getStringExtra("compImage");

            Picasso.with(userProfilePhoto.getContext()).load(UrlConstant.APPLICATION_URL + aimage).fit().placeholder(R.color.colorAccent).into(userProfilePhoto);
            Picasso.with(compimageview.getContext()).load(UrlConstant.APPLICATION_URL + compImage).fit().placeholder(R.color.colorAccent).into(compimageview);
            userProfileName.setText(aname);
            userProfileShortBio.setText(cname);
            tvcomapnyname.setText(cname);
            tvcomapnycontact.setText(cemail);
//                                txtstar.setText("");
            tvEmail.setText(aemail);
            tvNumber.setText(aphone);
        } catch (Exception e) {
            return;
        }
        getdata();
    }
    public void getdata() {

        handler = new Handler(Looper.getMainLooper());
        RequestBody requestBody = new FormBody.Builder().addEncoded("currentPage", "0").addEncoded("AgentID", "30").build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(AgentDetailActivity.this, UrlConstant.API_GET_AGENTS_DETAIL_URL, requestBody, mResponseHelper);
    }
    private MyHttpClientHelper.ProcessResponseHelper mResponseHelper = new MyHttpClientHelper.ProcessResponseHelper() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onResponse(JSONObject object) {
            try {
                int code = object.getInt(ApiConstant.JSON_KEY_CODE);

                if (code == 1) {
                    Log.e("obj", "" + object);                    //mGroups.clear();
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DATA);

                    for (int i = 0; i < dataObject.length(); i++) {
                        propertyModel = new PropertyModel();
                        propertyModel.fromJson(dataObject.getJSONObject(i));
                        Log.e("pid", "" + propertyModel.getUsdMonthlyRent());
                        list.add(propertyModel);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                viewpager.setAdapter(new SimilarPropertyAdapter(AgentDetailActivity.this, list));
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
