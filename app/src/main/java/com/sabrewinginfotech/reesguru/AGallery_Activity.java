package com.sabrewinginfotech.reesguru;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.helper.TintDrawableHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Policy;
import java.util.ArrayList;

public class AGallery_Activity extends AppCompatActivity {

    String ja;
    ArrayList<String> images = new ArrayList<>();
    String[] imgarray;
    GridView gridview;
    LinearLayout llfullimage;
    ImageView ivfullimage;
    ViewPager viewpager;
    ImageView ivshare;
    Toolbar toolbar;

    @Override
    public void onBackPressed() {

        if (viewpager.getVisibility() == View.VISIBLE) {
            finish();
            Intent i = new Intent(this, AGallery_Activity.class);
            i.putExtra("images", "" + ja);
            startActivity(i);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(TintDrawableHelper.getTintedResource(this, android.support.design.R.drawable.abc_ic_ab_back_material, R.color.colorWhite));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gridview = (GridView) findViewById(R.id.gallerygridview);
        llfullimage = (LinearLayout) findViewById(R.id.llfullimage);
        ivfullimage = (ImageView) findViewById(R.id.ivfullimage);
        viewpager = (ViewPager) findViewById(R.id.vpgallery);
        ivfullimage.setVisibility(View.GONE);
        ivshare = (ImageView) findViewById(R.id.ivshare);
        Intent i = getIntent();

        try {
            ja = i.getStringExtra("images");
            Log.e("ja", "" + ja);
            JSONArray jsonArray = new JSONArray(ja);
            Log.e("ja", "" + jsonArray);
            for (int j = 0; j < jsonArray.length(); j++) {
                String s = String.valueOf(jsonArray.get(j));
                Log.e("s", "" + s);
                images.add(s);
            }
            Log.e("array", "" + images);
            gridview.setAdapter(new GalleryAdapter(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public class GalleryAdapter extends BaseAdapter {
        Context context;
        ImageView imageView;

        public GalleryAdapter(AGallery_Activity aGallery_activity) {
            this.context = aGallery_activity;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            int type = getItemViewType(position);
            if (v == null) {
                // Inflate the layout according to the view type
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                if (type == 0) {
//                 Inflate the layout with image
                    v = inflater.inflate(R.layout.item_gallery, parent, false);
                } else {
                    v = inflater.inflate(R.layout.proprtylistitem, parent, false);
                }
            }
            //

            imageView = (ImageView) v.findViewById(R.id.ivfullimageitem);
            imageView.setImageResource(R.drawable.icnlst_gas);

            Picasso.with(imageView.getContext()).load(UrlConstant.APPLICATION_URL + images.get(position)).fit().centerCrop().placeholder(R.color.colorAccent).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpager.setVisibility(View.VISIBLE);
                    viewpager.setAdapter(new GalleryvpAdaptor(context));
                    gridview.setVisibility(View.GONE);
                    ivshare.setVisibility(View.VISIBLE);
                }
            });
            return v;
        }
    }

    public class GalleryvpAdaptor extends PagerAdapter {
        Context c;
        ImageView imageView;

        public GalleryvpAdaptor(Context context) {
            this.c = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(c);
            ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.item_vpgallery, container, false);
            container.addView(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.vpimageview);
            Picasso.with(imageView.getContext()).load(UrlConstant.APPLICATION_URL + images.get(position)).fit().centerCrop().placeholder(R.color.colorAccent).into(imageView);
            return itemView;
        }

    }

}
