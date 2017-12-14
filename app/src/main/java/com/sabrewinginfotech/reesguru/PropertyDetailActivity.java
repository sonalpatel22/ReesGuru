package com.sabrewinginfotech.reesguru;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sabrewinginfotech.reesguru.adapter.AdapterSummary;
import com.sabrewinginfotech.reesguru.adapter.SimilarPropertyAdapter;
import com.sabrewinginfotech.reesguru.api.model.AgentModel;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.api.model.PropertyDetailModel;
import com.sabrewinginfotech.reesguru.api.model.PropertyModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PropertyDetailActivity extends BaseActivity {

    AdapterSummary adapterSummary;
    RecyclerView recyclerView;
    ViewPager viewpager;
    SimilarPropertyAdapter similarPropertyAdapter;
    String Pid;
    RobotoTextView txt_address, txt_property_id, txt_price, txt_property_detail;
    ImageView property_image, img_location;

    LinearLayout table_1, table_2, table_3;
    LayoutInflater linf;

    private Call lastCall;
    private Handler handler;
    private PropertyDetailModel detailModel;
    public PropertyModel propertyModel;
    String strLatitude;
    String strLongitude;
    Toolbar toolbar;
    JSONArray imagearray;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        getpropertydata();
        initView();
        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyDetailActivity.this, Map_AActivity.class);
                intent.putExtra("Latlang", "" + strLatitude + "," + strLongitude);
                startActivity(intent);
            }
        });
        property_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PropertyDetailActivity.this, AGallery_Activity.class);
                Log.e("image",""+imagearray);
                intent.putExtra("images", "" + imagearray);
                startActivity(intent);
            }
        });
    }

    public void getpropertydata() {
        try {
            Intent i = getIntent();
            Pid = i.getStringExtra("pid");
        } catch (Exception e) {

        }

    }

    private void initView() {
        table_1 = (LinearLayout) findViewById(R.id.table_1);
        table_2 = (LinearLayout) findViewById(R.id.table_2);
        table_3 = (LinearLayout) findViewById(R.id.table_3);

        txt_address = (RobotoTextView) findViewById(R.id.txt_address);
        txt_property_id = (RobotoTextView) findViewById(R.id.text_property_id);
        txt_price = (RobotoTextView) findViewById(R.id.txt_price);
        txt_property_detail = (RobotoTextView) findViewById(R.id.txt_property_detail);

        property_image = (ImageView) findViewById(R.id.property_image);
        img_location = (ImageView) findViewById(R.id.img_location);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // adapterSummary = new AdapterSummary(this);
        // recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setAdapter(adapterSummary);

//        viewpager.setAdapter(new SimilarPropertyAdapter(this,));

        actionGetAllPropertiesForFeatureDetails();
    }


    private void actionGetAllPropertiesForFeatureDetails() {

        handler = new Handler(Looper.getMainLooper());
        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("PropertyID", "" + Pid)
                .addEncoded("GroupID", "0").build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(this, UrlConstant.API_GET_ALL_PROPERTIES_FOR_FEATURE_DETAILS_URL, requestBody, mResponseHelper);
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
                    //mGroups.clear();
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DATA);
                    detailModel = new PropertyDetailModel();
                    detailModel.fromJson(dataObject.getJSONObject(0));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // addMarkers();
                                preperUi();
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


    private void preperUi() {
        try {
            txt_address.setText(detailModel.getTitle() + ", " + detailModel.getKeyLandmark());
            txt_property_id.setText("" + detailModel.getPropertyID());
            txt_price.setText("$ " + detailModel.getUsdSalePrice());
            txt_property_detail.setText(detailModel.getDescription());

            adapterSummary = new AdapterSummary(this, detailModel.getSummery());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapterSummary);

            Picasso.with(property_image.getContext()).load(UrlConstant.APPLICATION_URL + detailModel.getPropertyImage()).fit().centerCrop().placeholder(R.color.colorAccent).into(property_image);
            imagearray = new JSONArray();
            imagearray.put(""+detailModel.getPropertyImage());
            imagearray.put(""+detailModel.getPropertyImage());

            strLatitude = String.valueOf(detailModel.getLatitude());
            strLongitude = String.valueOf(detailModel.getLongitude());
//            Log.e("latlang",""+latEiffelTower+","+lngEiffelTower);
//
//            String strLatitude ="21.19365498864821";
//            String strLongitude ="72.8217601776123";

            String mapUrl = UrlConstant.StaticMapUrl;
            mapUrl = mapUrl + Float.parseFloat(strLatitude) + "," + Float.parseFloat(strLongitude) + "&center=" + Float.parseFloat(strLatitude) + "," + Float.parseFloat(strLongitude);
//            mapUrl = mapUrl + "21.19365498864821,72.8217601776123&center=21.19365498864821,72.8217601776123";
            Picasso.with(img_location.getContext()).load(mapUrl).fit().centerCrop().placeholder(R.color.colorAccent).into(img_location);

            String[] homefetures = detailModel.getHomeFeaturesListKeyName().split(",");
            String[] societyfetures = detailModel.getSocietyFeaturesListKeyName().split(",");
            String[] otherfetures = detailModel.getOtherFeaturesListKeyName().split(",");


            //FrameLayout.LayoutParams layoutParams;
            //layoutParams = new FrameLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

            linf = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            linf = LayoutInflater.from(this);

           /* for (int i = 1; i < NoOfTimes; i++) {

                final View v = linf.inflate(R.layout.item, null);
                //rr.addView(v);
            }*/

            for (String name : homefetures) {
                // TableRow tbrow = new TableRow(this);
                // TextView t1v = new TextView(this);
                // t1v.setLayoutParams(layoutParams);
                // t1v.setText("" + name);
                // tbrow.addView(t1v);
                // table_1.addView(tbrow);
                final TextView v = (TextView) linf.inflate(R.layout.item, null);
                v.setText(name);
                table_1.addView(v);

            }
            for (String name : societyfetures) {
                // TableRow tbrow = new TableRow(this);
                //    TextView t1v = new TextView(this);
                //   t1v.setLayoutParams(layoutParams);
                //  t1v.setText("" + name);
                //  tbrow.addView(t1v);
                final TextView v = (TextView) linf.inflate(R.layout.item, null);
                v.setText(name);
                table_2.addView(v);
                // table_2.addView(tbrow);
            }
            for (String name : otherfetures) {
                final TextView v = (TextView) linf.inflate(R.layout.item, null);
                v.setText(name);
                table_3.addView(v);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void createjsonstring() {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < 10; i++) {
                JSONObject obj = new JSONObject();
                obj.put("image", "" + i);
                array.put(obj);
            }
            jsonObject.put("MyArray", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
