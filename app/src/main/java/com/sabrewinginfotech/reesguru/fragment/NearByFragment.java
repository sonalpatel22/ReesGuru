package com.sabrewinginfotech.reesguru.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.sabrewinginfotech.reesguru.MainActivity;
import com.sabrewinginfotech.reesguru.PropertyFilterActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.adapter.NearestPropertyAdapter;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.constant.ApiConstant;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.events.SearchEvent;
import com.sabrewinginfotech.reesguru.helper.LogHelper;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NearByFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener, GoogleMap.OnMarkerClickListener {

    private Toolbar toolbar;
    private GoogleMap mMap;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5 * 1000;  //min*sec*millisecond
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private static final int REQUEST_PERMISSION = 2001;
    private static final int INTENT_LOCATION_SETTINGS = 2002;
    private final static String TAG = NearByFragment.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private LatLng mLastLocation;
    private RelativeLayout filter_view, footer, draw_toolbar;
    private RobotoTextView txt_clear, txt_back, txt_apply;
    private ImageView img_btn_filter;
    private boolean isMapDrawable = false;

    //
    Boolean Is_MAP_Moveable = false; // to detect map is movable
    Projection projection;
    public double latitude;
    public double longitude;
    ArrayList<LatLng> val = new ArrayList<>();
    PolygonOptions rectOptions;
    Polygon polygon;
    int flag = 0;
    LatLng lastpoint;
    LatLng Currentpoint;
    //
    private Call lastCall;
    private Handler handler;
    BitmapDescriptor iconRed;
    BitmapDescriptor iconBlue;
    public Context context;
    ViewPager viewpager;
    NearestPropertyAdapter nearestPropertyAdapter;
    public Location targetLocation;
    private List<NearestPropertiesModel> propertiesModels = new ArrayList<>();
    private List<NearestPropertiesModel> propertiesModelsSelected = new ArrayList<>();

//dashrarth
    public NearByFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here

                return false;
            default:
                break;
        }

        return false;
    }
    public static NearByFragment newInstance() {
        NearByFragment fragment = new NearByFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UrlConstant.fragmenttag = 3;
        MainActivity.ivfilter.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_near_by, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        iconBlue = BitmapDescriptorFactory.fromResource(R.drawable.icn_marker_blue);
        iconRed = BitmapDescriptorFactory.fromResource(R.drawable.icn_marker_red);

        filter_view = (RelativeLayout) view.findViewById(R.id.filter_view);
        footer = (RelativeLayout) view.findViewById(R.id.footer);
        draw_toolbar = (RelativeLayout) view.findViewById(R.id.draw_toolbar);
        txt_clear = (RobotoTextView) view.findViewById(R.id.txt_clear);
        txt_back = (RobotoTextView) view.findViewById(R.id.txt_back);
        txt_apply = (RobotoTextView) view.findViewById(R.id.txt_apply);

        img_btn_filter = (ImageView) view.findViewById(R.id.img_btn_filter);

        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        img_btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PropertyFilterActivity.class));
            }
        });

        final MySupportMapFragment customMapFragment = ((MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        customMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMarkerClickListener(NearByFragment.this);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
                    return;
                }

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                initiateLocationExecution();

//                if (flag == 1) {
//                    if (targetLocation != null) {
//                        onLocationChanged(targetLocation);
//                        Log.e("location", "" + targetLocation);
//
//                    }
//                } else {
                if (mMap.getMyLocation() != null) {
                    mLastLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                    actionGetNearestProperties();
                } else {

                }
                actionGetNearestProperties();
//                }
            }
        });

        FrameLayout fram_map = (FrameLayout) view.findViewById(R.id.fram_map);
        final FloatingActionButton btn_draw_State = (FloatingActionButton) view.findViewById(R.id.fab_free_draw);
        FloatingActionButton fab_user_define_pin = (FloatingActionButton) view.findViewById(R.id.fab_user_define_pin);
        FloatingActionButton fab_drive_mode = (FloatingActionButton) view.findViewById(R.id.fab_drive_mode);

        fab_user_define_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMyLocation() != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            }
        });
        fab_drive_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_draw_State.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isMapDrawable) {
                    if (Is_MAP_Moveable != true) {
                        Is_MAP_Moveable = true;
                    } else {
                        Is_MAP_Moveable = false;
                    }
                    mMap.clear();
                    ((MainActivity) getActivity()).drawMapView(true);
                    drawMapView(true);
                    isMapDrawable = true;
                } else {
                    isMapDrawable = false;
                    btn_draw_State.setImageResource(R.drawable.icn_draw_transparent_icon);
                    addMarkers();
                }
            }
        });

        txt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                val = new ArrayList<>();
                Is_MAP_Moveable = true;
            }
        });

        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                val = new ArrayList<>();
                Is_MAP_Moveable = false;
                isMapDrawable = false;
                drawMapView(false);
                ((MainActivity) getActivity()).drawMapView(false);
                addMarkers();
            }
        });

        txt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Is_MAP_Moveable = false;
                drawMapView(false);
                ((MainActivity) getActivity()).drawMapView(false);
                addMarkersonPolyline();
                btn_draw_State.setImageResource(R.drawable.ic_remove_draw);
                isMapDrawable = true;
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (!isMapDrawable) {
                    for (int i = 0; i < propertiesModels.size(); i++) {
                        if (position == i) {
                            m.get(i).setIcon(iconBlue);
                       /* CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Double.valueOf(propertiesModels.get(i).getLatitude()), Double.valueOf(propertiesModels.get(i).getLongitude())))
                                .zoom(12).build();
                        //Zoom in and animate the camera.
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                        } else {
                            m.get(i).setIcon(iconRed);
                        }
                    }
                } else {
                    for (int i = 0; i < propertiesModelsSelected.size(); i++) {
                        if (position == i) {
                            m.get(i).setIcon(iconBlue);
                       /* CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Double.valueOf(propertiesModels.get(i).getLatitude()), Double.valueOf(propertiesModels.get(i).getLongitude())))
                                .zoom(12).build();
                        //Zoom in and animate the camera.
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                        } else {
                            m.get(i).setIcon(iconRed);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (Is_MAP_Moveable == true) {
                    float x = event.getX();
                    float y = event.getY();

                    int x_co = Math.round(x);
                    int y_co = Math.round(y);

                    projection = mMap.getProjection();
                    Point x_y_points = new Point(x_co, y_co);

                    LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;

                    int eventaction = event.getAction();
                    switch (eventaction) {
                        case MotionEvent.ACTION_DOWN:
                            // finger touches the screen
                            lastpoint = new LatLng(latitude, longitude);
                            val.add(new LatLng(latitude, longitude));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // finger moves on the screen
                            val.add(new LatLng(latitude, longitude));
                            Currentpoint = new LatLng(latitude, longitude);
                            Draw_MapLine();
                            lastpoint = Currentpoint;

                            break;
                        case MotionEvent.ACTION_UP:
                            // finger leaves the screen
                            if (Is_MAP_Moveable == true)
                                Draw_Map();
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            // finger leaves the screen
                            if (Is_MAP_Moveable == true)
                                Draw_Map();
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            if (Is_MAP_Moveable == true)
                                Draw_Map();
                            break;
                    }
                }

                if (Is_MAP_Moveable == true) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
    public void Draw_Map() {
        rectOptions = new PolygonOptions();
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.parseColor("#805d5dff"));
        polygon = mMap.addPolygon(rectOptions);
        Is_MAP_Moveable = false;
        val = new ArrayList<>();
        rectOptions = null;
    }
    public void Draw_MapLine() {
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(lastpoint, Currentpoint)
                .width(5)
                .color(Color.RED));
    }
    private void initiateLocationExecution() {
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }
    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        LogHelper.e(TAG, "Building GoogleApiClient");
        //  if (mGoogleApiClient == null) {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //  }
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationSettings();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void checkLocationSettings() {
        try {
            buildLocationSettingsRequest();
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
            result.setResultCallback(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                LogHelper.d(TAG, "All location settings are satisfied." + location);
                if (location != null) {
                    mMap.clear();
                    mLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(mLastLocation).title("I am hire !");
                    mMap.addMarker(markerOptions);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastLocation).zoom(12).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    actionGetNearestProperties();
                } else {
                    com.google.android.gms.location.LocationListener listener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            try {
                                if (location != null) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    mLastLocation = latLng;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LocationRequest request = LocationRequest.create();
                    request.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                    request.setNumUpdates(1);
                    request.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, listener);
                    }
                }
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                LogHelper.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    if (getActivity() instanceof Activity && isAdded())
                        this.startIntentSenderForResult(status.getResolution().getIntentSender(), REQUEST_CHECK_SETTINGS, null, 0, 0, 0, null);
                    // status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    LogHelper.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                LogHelper.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogHelper.e("onActivityResult", resultCode + "");
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS) {
            LogHelper.d("onActivityResult", resultCode + "");
            LocationRequest request = LocationRequest.create();
            request.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            request.setNumUpdates(1);
            request.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mLastLocation = latLng;
            mMap.clear();
            mLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(mLastLocation).title("I am hire !");
            mMap.addMarker(markerOptions);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastLocation).zoom(16).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            actionGetNearestProperties();
        }
    }
    private void actionGetNearestProperties() {

        RequestBody requestBody = new FormBody.Builder()
                .addEncoded("latitude", "21.1702")
                .addEncoded("longitude", "72.8311").build();
        lastCall = MyHttpClientHelper.getInstance().enqueueCall(getActivity(), UrlConstant.API_GET_NEAREST_PROPERTIES_URL, requestBody, mNearestPropertyResponseHelper);
    }
    private MyHttpClientHelper.ProcessResponseHelper mNearestPropertyResponseHelper = new MyHttpClientHelper.ProcessResponseHelper() {
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
                    JSONArray dataObject = object.getJSONArray(ApiConstant.JSON_KEY_DATA);
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
                                addMarkers();
                                nearestPropertyAdapter = new NearestPropertyAdapter(getActivity(), propertiesModels);
                                viewpager.setAdapter(nearestPropertyAdapter);
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
            LogHelper.d("Tag", "Something Want Wrong");
        }
    };
    List<Marker> m = new ArrayList<>();
    private void addMarkers() {
        m = new ArrayList<>();
        if (mMap != null && propertiesModels.size() > 0) {
            mMap.clear();
            for (int i = 0; i < propertiesModels.size(); i++) {
                Marker mar = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(propertiesModels.get(i).getLatitude()), Double.valueOf(propertiesModels.get(i).getLongitude())))
                        .icon(iconRed)
                        .title(propertiesModels.get(i).getTitle()));
                mar.setTag(String.valueOf(i));
                m.add(mar);
            }
            //Build camera position
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.valueOf(propertiesModels.get(0).getLatitude()), Double.valueOf(propertiesModels.get(0).getLongitude())))
                    .zoom(12).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            nearestPropertyAdapter = new NearestPropertyAdapter(getActivity(), propertiesModels);
            viewpager.setAdapter(nearestPropertyAdapter);
        }
    }
    private void addMarkersonPolyline() {

        m = new ArrayList<>();
        propertiesModelsSelected = new ArrayList<>();
        if (mMap != null && propertiesModels.size() > 0) {
            for (int i = 0; i < propertiesModels.size(); i++) {
                LatLng latLng = new LatLng(Double.valueOf(propertiesModels.get(i).getLatitude()), Double.valueOf(propertiesModels.get(i).getLongitude()));
                if (PolyUtil.containsLocation(latLng, polygon.getPoints(), false)) {
                    Marker mar = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.valueOf(propertiesModels.get(i).getLatitude()), Double.valueOf(propertiesModels.get(i).getLongitude())))
                            .icon(iconRed)
                            .title(propertiesModels.get(i).getTitle()));

                    propertiesModelsSelected.add(propertiesModels.get(i));
                    mar.setTag(String.valueOf(propertiesModelsSelected.size() - 1));
                    m.add(mar);

                    nearestPropertyAdapter = new NearestPropertyAdapter(getActivity(), propertiesModelsSelected);
                    viewpager.setAdapter(nearestPropertyAdapter);
                }
            }

            //Build camera position
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(Double.valueOf(propertiesModels.get(0).getLatitude()), Double.valueOf(propertiesModels.get(0).getLongitude())))
                    .zoom(12).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                com.google.android.gms.location.LocationListener listener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        try {
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mLastLocation = latLng;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                LocationRequest request = LocationRequest.create();
                request.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                request.setNumUpdates(1);
                request.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                initiateLocationExecution();
            } else {
                Toast.makeText(getActivity(), "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void drawMapView(Boolean flage) {

        if (flage) {
            filter_view.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
            viewpager.setVisibility(View.GONE);
            draw_toolbar.setVisibility(View.VISIBLE);
            txt_clear.setVisibility(View.VISIBLE);
        } else {
            filter_view.setVisibility(View.VISIBLE);
            footer.setVisibility(View.VISIBLE);
            draw_toolbar.setVisibility(View.GONE);
            txt_clear.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (viewpager != null) {
            if (viewpager.getVisibility() == View.GONE)
                viewpager.setVisibility(View.VISIBLE);
            viewpager.setCurrentItem(Integer.valueOf(marker.getTag().toString()));
        }
        return true;
    }
    public LatLng getLocationFromAddress(String strAddress, Context c) {

        Geocoder coder = new Geocoder(c);
        List<Address> address;
        LatLng latlang = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);

            if (address == null) {
                return null;
            } else {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                latlang = new LatLng(location.getLongitude(), location.getLatitude());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return latlang;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchEvent event) {
        String[] eve = event.getEvent().split(",");
        double lat = Double.parseDouble(eve[0]);
        double lng = Double.parseDouble(eve[1]);
        targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(lat);//your coords of course
        targetLocation.setLongitude(lng);
        mLastLocation = new LatLng(targetLocation.getLatitude(), targetLocation.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions().position(mLastLocation).icon(iconBlue).title("I am hire !");
        mMap.addMarker(markerOptions);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastLocation).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        flag = 2;
        Log.e("string", "" + eve);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
