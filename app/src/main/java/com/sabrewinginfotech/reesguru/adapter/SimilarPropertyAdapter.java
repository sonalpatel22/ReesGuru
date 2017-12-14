package com.sabrewinginfotech.reesguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sabrewinginfotech.reesguru.PropertyDetailActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.api.model.PropertyModel;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Dashrath on 11/3/2017.
 */

public class SimilarPropertyAdapter extends PagerAdapter {
    private ImageView propertyImage;
    private RobotoTextView txtAddress;
    private RobotoTextView txtPrice;
    private RobotoTextView txtBed;
    private RobotoTextView txtKitchen;
    private RobotoTextView txtPropertySize;
    private Context mContext;
    ArrayList<PropertyModel> pmodellist = new ArrayList<>();


    public SimilarPropertyAdapter(Context context, ArrayList<PropertyModel> list) {
        mContext = context;
        pmodellist = list;
        Log.e("list", "" + pmodellist);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.items_similar_homes, collection, false);


        propertyImage = (ImageView) itemView.findViewById(R.id.property_image);
        txtAddress = (RobotoTextView) itemView.findViewById(R.id.txt_address);
        txtPrice = (RobotoTextView) itemView.findViewById(R.id.txt_price);
        txtBed = (RobotoTextView) itemView.findViewById(R.id.txt_bed);
        txtKitchen = (RobotoTextView) itemView.findViewById(R.id.txt_kitchen);
        txtPropertySize = (RobotoTextView) itemView.findViewById(R.id.txt_property_size);


        final PropertyModel propertyModel = pmodellist.get(position);
//        Picasso.with(propertyImage.getContext()).load(UrlConstant.APPLICATION_URL + propertyModel.getprope).fit().placeholder(R.color.colorAccent).into(propertyImage);
        txtAddress.setText(propertyModel.getLocation() + "," + propertyModel.getCity());
        txtPrice.setText(propertyModel.getSalePrice());
        txtBed.setText(propertyModel.getNoofBedrooms());
        txtKitchen.setText(propertyModel.getNoofKitchen());
        txtPropertySize.setText(propertyModel.getBuiltArea());
        collection.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PropertyDetailActivity.class);
                i.putExtra("pid", propertyModel.getPropertyID());
                mContext.startActivity(i);
//                mContext.startActivity(new Intent(mContext, PropertyDetailActivity.class));
            }
        });
      /*  imageView = (ImageView) itemView.findViewById(R.id.imageView);
        textViewName = (RobotoTextView) itemView.findViewById(R.id.textView_name);*/

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
        //return mItems.size();
        return pmodellist.size();
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
