package com.sabrewinginfotech.reesguru;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.fragment.SearchListingFragment;
import com.sabrewinginfotech.reesguru.helper.LogHelper;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;
import com.sabrewinginfotech.reesguru.helper.TintDrawableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PropertyFilterActivity extends BaseActivity {

    // protected ViewPager viewPager;
    //PagerTabStrip tabLayout;
    //private TabLayout tabLayout;

    private Call lastCall;
    private Handler handler;

    private List<NearestPropertiesModel> propertiesModels = new ArrayList<>();

    Toolbar toolbar;

    RobotoTextView txt_min_price, txt_max_price;
    CrystalRangeSeekbar rangeSeekbarPrice;

    RadioGroup rdg_property_search_for;
    RadioButton radio_for_sale, radio_for_rent, radio_project;

    RadioGroup rdg_property_type;
    RadioButton radio_Commercial, radio_resident;

    RadioGroup rdg_bed_roomes;
    RadioButton radio_any, radio_1, radio_2, radio_3, radio_4;

    RadioGroup rdg_bathrooms;
    RadioButton radio_any_bath, radio_1_bath, radio_2_bath, radio_3_bath, radio_4_bath;

    RadioGroup rdg_possession;
    RadioButton radio_under_construction, radio_ready, radio_both;

    RadioGroup rdg_transaction_type;
    RadioButton radio_new, radio_resale, radio_both_transaction_type;

    EditText edt_search_for_property;

    Spinner spinner_price_unit, spinner_cover_unit, spinner_plot_unit;
    EditText min_cover_area, max_cover_area, min_plot_area, max_plot_area;
    Button search_button;

    String property_search_for = "1";
    String possession = "0";
    String transaction_type = "0";
    String bathRooms = "0";
    String bedRooms = "0";
    String property_type = "0";
    String CurrencyUnit = "US Dollar (USD)";
    String CoverAreaUnit = "Sqfeet";
    String PlotAreaUnit = "Sqfeet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter_for_sale);
        initView();
    }

    private void initView() {

        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        handler = new Handler(Looper.getMainLooper());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(TintDrawableHelper.getTintedResource(this, android.support.design.R.drawable.abc_ic_ab_back_material, R.color.colorWhite));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //txt_min_cover_area = (RobotoTextView) findViewById(R.id.txt_min_cover_area);
        // txt_max_cover_area = (RobotoTextView) findViewById(R.id.txt_max_cover_area);
        txt_min_price = (RobotoTextView) findViewById(R.id.txt_min_price);
        txt_max_price = (RobotoTextView) findViewById(R.id.txt_max_price);

        edt_search_for_property = (EditText) findViewById(R.id.edt_search_for_property);
        min_cover_area = (EditText) findViewById(R.id.min_cover_area);
        max_cover_area = (EditText) findViewById(R.id.max_cover_area);
        min_plot_area = (EditText) findViewById(R.id.min_plot_area);
        max_plot_area = (EditText) findViewById(R.id.max_plot_area);

        spinner_price_unit = (Spinner) findViewById(R.id.spinner_price_unit);
        spinner_cover_unit = (Spinner) findViewById(R.id.spinner_cover_unit);
        spinner_plot_unit = (Spinner) findViewById(R.id.spinner_plot_unit);

        rangeSeekbarPrice = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbarPrice);

        rdg_property_search_for = (RadioGroup) findViewById(R.id.rdg_property_search_for);
        radio_for_sale = (RadioButton) findViewById(R.id.radio_for_sale);
        radio_for_sale.setTag("1");
        radio_for_rent = (RadioButton) findViewById(R.id.radio_for_rent);
        radio_for_rent.setTag("2");
        radio_project = (RadioButton) findViewById(R.id.radio_project);
        radio_project.setTag("3");
        radio_for_sale.setChecked(true);

        rdg_property_type = (RadioGroup) findViewById(R.id.rdg_property_type);
        radio_Commercial = (RadioButton) findViewById(R.id.radio_Commercial);
        radio_Commercial.setTag("4");
        radio_resident = (RadioButton) findViewById(R.id.radio_resident);
        radio_resident.setTag("3");

        rdg_bed_roomes = (RadioGroup) findViewById(R.id.rdg_bed_roomes);
        radio_any = (RadioButton) findViewById(R.id.radio_any);
        radio_any.setTag("0");
        radio_1 = (RadioButton) findViewById(R.id.radio_1);
        radio_1.setTag("1");
        radio_2 = (RadioButton) findViewById(R.id.radio_2);
        radio_2.setTag("2");
        radio_3 = (RadioButton) findViewById(R.id.radio_3);
        radio_3.setTag("3");
        radio_4 = (RadioButton) findViewById(R.id.radio_4);
        radio_4.setTag("4");
        radio_any.setChecked(true);

        rdg_bathrooms = (RadioGroup) findViewById(R.id.rdg_bathrooms);
        radio_any_bath = (RadioButton) findViewById(R.id.radio_any_bath);
        radio_any_bath.setTag("0");
        radio_1_bath = (RadioButton) findViewById(R.id.radio_1_bath);
        radio_1_bath.setTag("1");
        radio_2_bath = (RadioButton) findViewById(R.id.radio_2_bath);
        radio_2_bath.setTag("2");
        radio_3_bath = (RadioButton) findViewById(R.id.radio_3_bath);
        radio_3_bath.setTag("3");
        radio_4_bath = (RadioButton) findViewById(R.id.radio_4_bath);
        radio_4_bath.setTag("4");
        radio_any_bath.setChecked(true);

        rdg_possession = (RadioGroup) findViewById(R.id.rdg_possession);
        radio_under_construction = (RadioButton) findViewById(R.id.radio_under_construction);
        radio_under_construction.setTag("1");
        radio_ready = (RadioButton) findViewById(R.id.radio_ready);
        radio_ready.setTag("2");
        radio_both = (RadioButton) findViewById(R.id.radio_both);
        radio_both.setTag("3");

        rdg_transaction_type = (RadioGroup) findViewById(R.id.rdg_transaction_type);
        radio_new = (RadioButton) findViewById(R.id.radio_new);
        radio_new.setTag("11");
        radio_resale = (RadioButton) findViewById(R.id.radio_resale);
        radio_resale.setTag("12");
        radio_both_transaction_type = (RadioButton) findViewById(R.id.radio_both_transaction_type);
        radio_both_transaction_type.setTag("13");

        search_button = (Button) findViewById(R.id.search_button);


        rangeSeekbarPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                txt_min_price.setText(String.valueOf(minValue));
                txt_max_price.setText(String.valueOf(maxValue));
            }
        });

        rangeSeekbarPrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        rdg_property_search_for.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                property_search_for = (String) radioButton.getTag();
            }
        });

        rdg_possession.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                possession = (String) radioButton.getTag();
            }
        });

        rdg_transaction_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                transaction_type = (String) radioButton.getTag();
            }
        });

        rdg_bathrooms.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                bathRooms = (String) radioButton.getTag();
            }
        });

        rdg_bed_roomes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                bedRooms = (String) radioButton.getTag();
            }
        });

        rdg_property_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                LogHelper.d("Selected property type", (String) radioButton.getTag());
                property_type = (String) radioButton.getTag();
            }
        });

        spinner_price_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.d("Selected property type", parent.getItemAtPosition(position).toString());
                CurrencyUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_cover_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.d("Selected property type", parent.getItemAtPosition(position).toString());
                CoverAreaUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_plot_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogHelper.d("Selected property type", parent.getItemAtPosition(position).toString());
                PlotAreaUnit = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionGetNearestProperties();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void actionGetNearestProperties() {
        if (CurrencyUnit.length() > 3) {
            CurrencyUnit = CurrencyUnit.substring(CurrencyUnit.indexOf("(") + 1, CurrencyUnit.indexOf(")"));
        }
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = this.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = this.getResources().getConfiguration().locale;
        }
        String locales = locale.getCountry();
        LogHelper.d("Country code", locales);

//        RequestBody requestBody = new FormBody.Builder()
//                .addEncoded("location", edt_search_for_property.getText().toString().trim())
//                .addEncoded("propertyFor", property_search_for)
//                .addEncoded("propertyType", property_type)
//                .addEncoded("bathroom", bathRooms)
//                .addEncoded("bedroom", bedRooms)
//                .addEncoded("minprice", txt_min_price.getText().toString().length() > 0 ? txt_min_price.getText().toString() : "0")
//                .addEncoded("maxprice", txt_max_price.getText().toString().length() > 0 ? txt_max_price.getText().toString() : "0")
//                .addEncoded("mincoverarea", min_cover_area.getText().toString().length() > 0 ? min_cover_area.getText().toString() : "0")
//                .addEncoded("maxcoverarea", max_cover_area.getText().toString().length() > 0 ? max_cover_area.getText().toString() : "0")
//                .addEncoded("minplotarea", min_plot_area.getText().toString().length() > 0 ? min_plot_area.getText().toString() : "0")
//                .addEncoded("maxplotarea", max_plot_area.getText().toString().length() > 0 ? max_plot_area.getText().toString() : "0")
//                .addEncoded("Possession", possession)
//                .addEncoded("TransactionType", transaction_type)
//                .addEncoded("orderby", "UsdMonthlyRent")
//                .addEncoded("orderto", "desc")
//                .addEncoded("distance", "0")
//                .addEncoded("CurrencyUnit", CurrencyUnit)
//                .addEncoded("CoverAreaUnit", CoverAreaUnit)
//                .addEncoded("PlotAreaUnit", PlotAreaUnit)
//                //.addEncoded("center_latitude", "72.8311")
//                // .addEncoded("center_longitude", "72.8311")
//                .build();

        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("location","")
                .addEncoded("propertyFor", "2")
                .addEncoded("propertyType", "0")
                .addEncoded("bathroom", "0")
                .addEncoded("bedroom", "0")
                .addEncoded("minprice", "10000")
                .addEncoded("maxprice", "100000000")
                .addEncoded("mincoverarea", "0")
                .addEncoded("maxcoverarea", "0")
                .addEncoded("minplotarea", "0")
                .addEncoded("maxplotarea", "0")
                .addEncoded("TransactionType", "0")
                .addEncoded("Possession", "0")
                .addEncoded("CurrencyUnit", "AED")
                .addEncoded("CoverAreaUnit", "SqMeter")
                .addEncoded("PlotAreaUnit", "SqMeter")
                .addEncoded("orderby", "UsdMonthlyRent")
                .addEncoded("orderto", "desc")
                .addEncoded("distance", "0")
                .addEncoded("center_latitude", "0")
                .addEncoded("center_longitude", "0")
                .build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(this, UrlConstant.API_GET_SEARCH_BY_SALE_RENT_PROPERTY_URL, requestBody, mSearchPropertyResponseHelper);
    }

    private MyHttpClientHelper.ProcessResponseHelper mSearchPropertyResponseHelper = new MyHttpClientHelper.ProcessResponseHelper() {
        @Override
        public void onRequest() {
        }

        @Override
        public void onResponse(JSONObject object) {
            try {
                int code = object.getInt(ApiConstant.JSON_KEY_CODE);
                if (code == 1) {
                    //mGroups.clear();
                    propertiesModels = new ArrayList<>();
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DETAIL_DATA);
                    Log.e("properties", "" + dataObject);
                    for (int i = 0; i < dataObject.length(); i++) {
                        JSONObject property = dataObject.getJSONObject(i);
                        NearestPropertiesModel propertiesModel = new NearestPropertiesModel();
                        propertiesModel.fromJson(property);
                        propertiesModels.add(propertiesModel);
                    }
                    Log.e("properties", "" + propertiesModels);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (propertiesModels.size() > 0)

                                    SearchListingFragment.newInstance(propertiesModels).show(getSupportFragmentManager(), "SHARE-VOICE");
                                //getSupportFragmentManager().beginTransaction().replace(R.id.container, SearchListingFragment.newInstance(propertiesModels)).commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {

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
            LogHelper.d("Tag", "Something Want Wrong");
        }
    };


}
