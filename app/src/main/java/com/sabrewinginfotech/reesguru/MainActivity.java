package com.sabrewinginfotech.reesguru;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoRadioButton;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.events.SearchEvent;
import com.sabrewinginfotech.reesguru.fragment.AjentsFragment;
import com.sabrewinginfotech.reesguru.fragment.ListingFragment;
import com.sabrewinginfotech.reesguru.fragment.MyMatchesFragment;
import com.sabrewinginfotech.reesguru.fragment.NavigationDrawerFragment;
import com.sabrewinginfotech.reesguru.fragment.NearByFragment;
import com.sabrewinginfotech.reesguru.fragment.SuppliersFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener, RadioButton.OnCheckedChangeListener {
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static String searchlocation;
    RobotoRadioButton radio_dashboard_post;
    RobotoRadioButton radio_dashboard_agent;
    RobotoRadioButton radio_dashboard_near_by;
    RobotoRadioButton radio_dashboard_matches;
    RobotoRadioButton radio_dashboard_supplier;
    public EditText txtSearch;
    RadioGroup dashbord_tab;
    FrameLayout layout_title;
    RobotoTextView txt_title;

    private NearByFragment nearByFragment;
    private ListingFragment listingFragment;
    private AjentsFragment ajentsFragment;
    private MyMatchesFragment myMatchesFragment;
    private SuppliersFragment suppliersFragment;

    MenuItem item_search;
    Toolbar searchtollbar;
    Menu search_menu;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private ImageView imageView;
    public static ImageView ivfilter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSearch();
        init();
        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        ivfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PropertyFilterActivity.class));
                Toast.makeText(MainActivity.this, "filter clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initSearch() {
        searchtollbar = (Toolbar) findViewById(R.id.search_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        setSearchtollbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                View mainView = findViewById(R.id.main_content);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawerLayout.bringChildToFront(drawerView);
                mDrawerLayout.requestLayout();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private void init() {

        txt_title = (RobotoTextView) findViewById(R.id.txt_title);
        radio_dashboard_post = (RobotoRadioButton) findViewById(R.id.radio_dashboard_listing);
        radio_dashboard_agent = (RobotoRadioButton) findViewById(R.id.radio_dashboard_agent);
        radio_dashboard_near_by = (RobotoRadioButton) findViewById(R.id.radio_dashboard_near_by);
        radio_dashboard_matches = (RobotoRadioButton) findViewById(R.id.radio_dashboard_matches);
        radio_dashboard_supplier = (RobotoRadioButton) findViewById(R.id.radio_dashboard_supplier);

        dashbord_tab = (RadioGroup) findViewById(R.id.dashbord_tab);
        layout_title = (FrameLayout) findViewById(R.id.layout_title);

        radio_dashboard_post.setOnCheckedChangeListener(this);
        radio_dashboard_agent.setOnCheckedChangeListener(this);
        radio_dashboard_near_by.setOnCheckedChangeListener(this);
        radio_dashboard_matches.setOnCheckedChangeListener(this);
        radio_dashboard_supplier.setOnCheckedChangeListener(this);
        ivfilter = (ImageView) findViewById(R.id.ivfilter);
        prepareUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        if (UrlConstant.fragmenttag == 1) {
            item.setVisible(false);
            ivfilter.setVisibility(View.VISIBLE);
        } else {
            item.setVisible(true);
            ivfilter.setVisibility(View.GONE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Log.e("fragmenttag", "" + UrlConstant.fragmenttag);
                if (UrlConstant.fragmenttag == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.search_toolbar, 1, true, true);
                    else
                        searchtollbar.setVisibility(View.VISIBLE);
                } else if (UrlConstant.fragmenttag == 3) {
                    try {
                        Intent intent = null;
                        intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MainActivity.this);
                        startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }

//                  item_search.expandActionView();

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.radio_dashboard_listing:

                if (isChecked) {
                    if (listingFragment == null) {
                        listingFragment = new ListingFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, listingFragment).commit();
                    radio_dashboard_agent.setChecked(false);
                    radio_dashboard_near_by.setChecked(false);
                    radio_dashboard_matches.setChecked(false);
                    radio_dashboard_supplier.setChecked(false);
                    txt_title.setText("Listing");
                }

                break;

            case R.id.radio_dashboard_agent:
                if (isChecked) {

                    if (ajentsFragment == null) {
                        ajentsFragment = new AjentsFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, ajentsFragment).commit();

                    radio_dashboard_post.setChecked(false);
                    radio_dashboard_near_by.setChecked(false);
                    radio_dashboard_matches.setChecked(false);
                    radio_dashboard_supplier.setChecked(false);
                    txt_title.setText("Agents");
                }
                break;

            case R.id.radio_dashboard_near_by:
                if (isChecked) {

                    if (nearByFragment == null) {
                        nearByFragment = new NearByFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, nearByFragment).commit();
                    radio_dashboard_post.setChecked(false);
                    radio_dashboard_agent.setChecked(false);
                    radio_dashboard_matches.setChecked(false);
                    radio_dashboard_supplier.setChecked(false);
                    txt_title.setText("ReEs Guru");
                }
                break;

            case R.id.radio_dashboard_matches:
                if (isChecked) {

                    if (myMatchesFragment == null) {
                        myMatchesFragment = new MyMatchesFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myMatchesFragment).commit();
                    radio_dashboard_post.setChecked(false);
                    radio_dashboard_agent.setChecked(false);
                    radio_dashboard_near_by.setChecked(false);
                    radio_dashboard_supplier.setChecked(false);
                    txt_title.setText("My Matches");
                }
                break;

            case R.id.radio_dashboard_supplier:
                if (isChecked) {
                    if (suppliersFragment == null) {
                        suppliersFragment = new SuppliersFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, suppliersFragment).commit();
                    radio_dashboard_post.setChecked(false);
                    radio_dashboard_agent.setChecked(false);
                    radio_dashboard_near_by.setChecked(false);
                    radio_dashboard_matches.setChecked(false);
                    txt_title.setText("Supplier");
                }
                break;
        }
    }

    private void prepareUi() {
        try {
            if (nearByFragment == null) {
                nearByFragment = new NearByFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, nearByFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSearchtollbar() {
        searchtollbar = (Toolbar) findViewById(R.id.search_toolbar);
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu = searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.search_toolbar, 1, true, false);
                    else
                        searchtollbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.search_toolbar, 1, true, false);
                    } else
                        searchtollbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });
            initSearchView();
        }
    }

    public void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard
        searchView.setSubmitButtonEnabled(false);
        // Change search close button image
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        //closeButton.setImageResource(R.drawable.ic_close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_src_text);
                et.setText("");
                searchView.setQuery("", false);
            }
        });

        // set hint and the text colors
        txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));

        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    searchlocation = txtSearch.getText().toString();

                    return true;
                }
                return false;
            }
        });

        // set the cursor
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchtollbar.getVisibility() == View.VISIBLE)
                    callSearch(newText);
                return false;
            }

            public void callSearch(String query) {

            }
        });
    }

    public void closeSearchView() {
        try {
            SearchView searchView = (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();
            searchView.setQuery("", false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                circleReveal(R.id.search_toolbar, 1, true, false);
            else
                searchtollbar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);
        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = myView.getHeight() / 2;

        Animator anim;
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        anim.setDuration((long) 220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });
        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);
        // start the animation
        anim.start();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    public void drawMapView(Boolean flage) {
        if (flage) {
            dashbord_tab.setVisibility(View.GONE);
            layout_title.setVisibility(View.GONE);
        } else {
            dashbord_tab.setVisibility(View.VISIBLE);
            layout_title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                String latlang = (place.getLatLng().latitude) + "," + (place.getLatLng().longitude);
                EventBus.getDefault().post(new SearchEvent(SearchEvent.ACTION_SEARCH, latlang));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("status", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}



