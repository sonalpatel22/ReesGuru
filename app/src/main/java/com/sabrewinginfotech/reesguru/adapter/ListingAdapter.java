package com.sabrewinginfotech.reesguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sabrewinginfotech.reesguru.AgentDetailActivity;
import com.sabrewinginfotech.reesguru.PropertyDetailActivity;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.api.model.NearestPropertiesModel;
import com.sabrewinginfotech.reesguru.api.model.PropertyModel;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dashrath on 10/8/2017.
 */

public class ListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<NearestPropertiesModel> mItems = new ArrayList<>();
    private List<PropertyModel> mAdItems = new ArrayList<>();
    private String tag = null;

    public ListingAdapter(Context context, List<NearestPropertiesModel> propertiesModels) {
        this.context = context;
        this.mItems = propertiesModels;
        this.tag = "";
        mInflater = android.view.LayoutInflater.from(context);
    }

    public ListingAdapter(Context context, ArrayList<PropertyModel> list, String tag) {
        this.context = context;
        this.mAdItems = list;
        this.tag = tag;
        mInflater = android.view.LayoutInflater.from(context);
        Log.e("items", "" + mAdItems);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == AdapterUtil.VIEW_EMPTY) {
            return new AdapterUtil.EmptyViewHolder(mInflater.inflate(viewType, parent, false), "No post to show");
        } else {
            return new ListingHolder(mInflater.inflate(R.layout.items_listing, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (tag.equals("")) {
            if (holder.getItemViewType() == AdapterUtil.VIEW_EMPTY) {
                return;
            } else {
                ((ListingHolder) holder).bind(mItems.get(position));
            }
        } else if (tag.equals("AgentDetailActivity")) {
            if (holder.getItemViewType() == AdapterUtil.VIEW_EMPTY) {
                return;
            } else {
                ((ListingHolder) holder).Adbind(mAdItems.get(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (tag.equals("")) {
            if (mItems.size() == 0) {
                return AdapterUtil.VIEW_EMPTY;
            } else {
                return super.getItemViewType(position);
            }
        } else if (tag.equals("AgentDetailActivity")) {
            if (mAdItems.size() == 0) {
                return AdapterUtil.VIEW_EMPTY;
            } else {
                return super.getItemViewType(position);
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (tag.equals("")) {
            size = mItems.size();
            if (size == 0) {
                size = 1;
            }
        } else if (tag.equals("AgentDetailActivity")) {
            size = mAdItems.size();
            if (size == 0) {
                size = 1;
            }
        }

        return size;
    }


    class ListingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;

        ImageView property_image;
        RobotoTextView txt_address;
        RobotoTextView txt_property_id;
        RobotoTextView txt_price;
        RobotoTextView txt_bed;
        RobotoTextView txt_property_size;
        RobotoTextView txt_kitchen;

        public ListingHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.itemView.setOnClickListener(this);
            property_image = (ImageView) itemView.findViewById(R.id.property_image);
            txt_address = (RobotoTextView) itemView.findViewById(R.id.txt_address);
            txt_property_id = (RobotoTextView) itemView.findViewById(R.id.txt_property_id);
            txt_price = (RobotoTextView) itemView.findViewById(R.id.txt_price);
            txt_bed = (RobotoTextView) itemView.findViewById(R.id.txt_bed);
            txt_property_size = (RobotoTextView) itemView.findViewById(R.id.txt_property_size);
            txt_kitchen = (RobotoTextView) itemView.findViewById(R.id.txt_kitchen);
        }

        public void bind(NearestPropertiesModel mItem) {

            // property_image = (ImageView) itemView.findViewById(R.id.property_image);
            Picasso.with(property_image.getContext()).load(UrlConstant.APPLICATION_URL + mItem.getPropertyImage()).fit().placeholder(R.color.colorAccent).into(property_image);
            txt_address.setText("" + mItem.getTitle() + ", " + mItem.getKeyLandmark());
            txt_property_id.setText("" + mItem.getPropertyID());
            txt_price.setText("" + mItem.getUsdSalePrice());
            txt_bed.setText("" + mItem.getNoofBedrooms());
            txt_property_size.setText("" + mItem.getLandArea() + " Sq");
            txt_kitchen.setText("" + mItem.getNoofKitchen());
            itemView.setTag(getLayoutPosition());
        }

        public void Adbind(PropertyModel mItem) {
            Log.e("url", "" + mItem.getUrl());
            // property_image = (ImageView) itemView.findViewById(R.id.property_image);
//            Picasso.with(property_image.getContext()).load(UrlConstant.APPLICATION_URL + mItem.getPropertyImage()).fit().placeholder(R.color.colorAccent).into(property_image);
            txt_address.setText("" + mItem.getTitle() + ", " + mItem.getKeyLandmark());
            txt_property_id.setText("" + mItem.getPropertyID());
            txt_price.setText("" + mItem.getUsdSalePrice());
            txt_bed.setText("" + mItem.getNoofBedrooms());
            txt_property_size.setText("" + mItem.getLandArea() + " Sq");
            txt_kitchen.setText("" + mItem.getNoofKitchen());
            itemView.setTag(getLayoutPosition());
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            Intent i = null;
            if (tag.equals("")) {
                NearestPropertiesModel nearestPropertiesModel = mItems.get(pos);
                i = new Intent(context, PropertyDetailActivity.class);
                i.putExtra("pid",String.valueOf(nearestPropertiesModel.getPropertyID()));
            } else if (tag.equals("AgentDetailActivity")) {
                PropertyModel propertyModel = mAdItems.get(pos);
                i = new Intent(context, PropertyDetailActivity.class);
                i.putExtra("pid", propertyModel.getPropertyID());
            }
            context.startActivity(i);
        }
    }
}
