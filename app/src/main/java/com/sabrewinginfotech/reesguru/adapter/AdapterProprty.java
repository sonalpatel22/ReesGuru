package com.sabrewinginfotech.reesguru.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.api.model.PropertyModel;

import java.util.ArrayList;

/**
 * Created by Alpesh on 12/4/2017.
 */

public class AdapterProprty extends BaseAdapter {
    ArrayList<PropertyModel> list = new ArrayList<>();
    Context context;
    private RelativeLayout propertyinfo;
    private TextView tvPropertylabel;
    private TextView tvPropertyname;
    private TextView tvPropertyaddress;
    private TextView tvPdec;

    public AdapterProprty(Context context, ArrayList<PropertyModel> list) {
        this.context = context;
        this.list = list;
        Log.e("list",""+list.size());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == 0) {
                // Inflate the layout with image
                v = inflater.inflate(R.layout.proprtylistitem, parent, false);
            } else {
                v = inflater.inflate(R.layout.proprtylistitem, parent, false);
            }
        }
        //
        PropertyModel propertyModel = list.get(position);


        propertyinfo = (RelativeLayout) v.findViewById(R.id.propertyinfo);
        tvPropertyname = (TextView) v.findViewById(R.id.tvPropertyname);
        tvPropertyaddress = (TextView) v.findViewById(R.id.tvPropertyaddress);
        tvPdec = (TextView) v.findViewById(R.id.tvPdec);

        Log.e("dsgbvfgsdh",""+propertyModel.getCity());
        tvPropertyname.setText(propertyModel.getTitle());
        tvPropertyaddress.setText(propertyModel.getCity() + "," + propertyModel.getLocation() + "," + propertyModel.getPostcode());
        tvPdec.setText(propertyModel.getDescription());

        return v;
    }
}
