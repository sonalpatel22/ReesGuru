package com.sabrewinginfotech.reesguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sabrewinginfotech.reesguru.PropertyDetailActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dashrath on 11/9/2017.
 */


public class NearestPropertyAdapter extends PagerAdapter {
    private Context mContext;
    private List<NearestPropertiesModel> propertiesModels = new ArrayList<>();

    private ImageView property_image;
    private TextView txt_address, txt_price, txt_bed, txt_kitchen, txt_property_size;


    public NearestPropertyAdapter(Context context, List<NearestPropertiesModel> propertiesModels) {
        mContext = context;
        this.propertiesModels = propertiesModels;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.items_similar_homes, collection, false);
        collection.addView(itemView);

        final NearestPropertiesModel mItem = propertiesModels.get(position);

        property_image = (ImageView) itemView.findViewById(R.id.property_image);
        txt_address = (RobotoTextView) itemView.findViewById(R.id.txt_address);
        txt_price = (RobotoTextView) itemView.findViewById(R.id.txt_price);
        txt_bed = (RobotoTextView) itemView.findViewById(R.id.txt_bed);
        txt_property_size = (RobotoTextView) itemView.findViewById(R.id.txt_property_size);
        txt_kitchen = (RobotoTextView) itemView.findViewById(R.id.txt_kitchen);

        Picasso.with(property_image.getContext()).load(UrlConstant.APPLICATION_URL + mItem.getPropertyImage()).fit().placeholder(R.color.colorAccent).into(property_image);
        txt_address.setText("" + mItem.getTitle() + ", " + mItem.getKeyLandmark());
        txt_price.setText("" + mItem.getUsdSalePrice());
        txt_bed.setText("" + mItem.getNoofBedrooms());
        txt_property_size.setText("" + mItem.getLandArea() + " Sq");
        txt_kitchen.setText("" + mItem.getNoofKitchen());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PropertyDetailActivity.class);
                i.putExtra("pid", String.valueOf(mItem.getPropertyID()));
                mContext.startActivity(i);
            }
        });
      /*  Picasso.with(imageView.getContext()).load(UrlConstant.JOINT_IMAGE_PREFIX + mItems.get(position).getImage()).placeholder(R.drawable.img_placeholder_small).into(imageView);
        textViewName.setText(mItems.get(position).getName());*/

       /* imageButtonHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckSusbcribtion())
                    JointDetailDialogFragment.instantiate(mItems.get(position)).show(getSupportFragmentManager(), "JOINT");
                else
                    CreateJointDialogFragment.instantiate(mItems.get(position)).show(getSupportFragmentManager(), "CREATE-ACTIVITY");
            }
        });*/

        return itemView;
    }


    @Override
    public int getCount() {
        return propertiesModels.size();
        // return 10;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
