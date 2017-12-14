package com.sabrewinginfotech.reesguru.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sabrewinginfotech.reesguru.AgentDetailActivity;
import com.sabrewinginfotech.reesguru.Progressbar.AVLoadingIndicatorView;
import com.sabrewinginfotech.reesguru.R;
import com.sabrewinginfotech.reesguru.api.model.AgentModel;
import com.sabrewinginfotech.reesguru.constant.UrlConstant;
import com.sabrewinginfotech.reesguru.custom.RobotoTextView;
import com.sabrewinginfotech.reesguru.helper.MyHttpClientHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Dashrath on 10/11/2017.
 */

public class AdapterAgents extends RecyclerView.Adapter<AdapterAgents.AgentHolder> {

    private LayoutInflater mInflater;
    private Context context;
    ArrayList<AgentModel> listofallagent = new ArrayList<>();
    AVLoadingIndicatorView  Avi;
    // AgentModel agentModel = new AgentModel();
    public AdapterAgents(Context context, ArrayList<AgentModel> listallagent, AVLoadingIndicatorView avLoadingIndicatorView) {
        this.listofallagent = listallagent;
        Log.e("list", "" + listofallagent);
        this.context = context;
        this.Avi = avLoadingIndicatorView;
        mInflater = android.view.LayoutInflater.from(context);

    }

    @Override
    public AgentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if (viewType == AdapterUtil.VIEW_EMPTY) {
//            return new AgentHolder(mInflater.inflate(viewType, parent, false), "No post to show");
//        } else {
        return new AgentHolder(mInflater.inflate(R.layout.items_ajents, parent, false));
//        }
    }


    @Override
    public void onBindViewHolder(AgentHolder holder, int position) {

        if (holder.getItemViewType() == AdapterUtil.VIEW_EMPTY) {
            return;
        } else {
            ((AgentHolder) holder).bind(listofallagent.get(position));

        }


    }

    @Override
    public int getItemViewType(int position) {

        if (listofallagent.size() == 0) {
            return AdapterUtil.VIEW_EMPTY;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        int size = listofallagent.size();
        if (size == 0) {
            size = 1;
        }
        return size;
    }


    public class AgentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View itemView;
        private CardView cardOne;
        private RobotoTextView txtAjentsName;
        private RobotoTextView txtAgentsPhone;
        private RobotoTextView txtAgentsEmail;
        private CardView cardTwo;
        private ImageView ajentImage;


        public AgentHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            cardOne = (CardView) itemView.findViewById(R.id.card_one);
            txtAjentsName = (RobotoTextView) itemView.findViewById(R.id.txt_ajents_name);
            txtAgentsPhone = (RobotoTextView) itemView.findViewById(R.id.txt_agents_phone);
            txtAgentsEmail = (RobotoTextView) itemView.findViewById(R.id.txt_agents_email);
            cardTwo = (CardView) itemView.findViewById(R.id.card_two);
            ajentImage = (ImageView) itemView.findViewById(R.id.ajent_image);
        }

        public void bind(AgentModel a) {
            Log.e("name of agent", "" + a.getFirstName());
            Picasso.with(ajentImage.getContext()).load(UrlConstant.APPLICATION_URL + a.getUserImage1()).fit().placeholder(R.color.colorAccent).into(ajentImage);
            txtAjentsName.setText("" + a.getFirstName() + ", " + a.getLastName());
            txtAgentsPhone.setText("" + a.getTelephone());
            txtAgentsEmail.setText("" + a.getEmailID1());
            itemView.setTag(getLayoutPosition());
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            AgentModel agentModel = listofallagent.get(pos);
            Intent i = new Intent(context, AgentDetailActivity.class);
            i.putExtra("agentid", "" + agentModel.getAgentID());
            i.putExtra("agentname",""+agentModel.getFirstName()+" "+agentModel.getLastName());
            i.putExtra("agentemail",""+agentModel.getEmailID1());
            i.putExtra("agentphone",""+agentModel.getTelephone());
            i.putExtra("C_name", "" + agentModel.getC_FName() + "" + agentModel.getC_LName());
            i.putExtra("Mobile", "" + agentModel.getMobile());
            i.putExtra("Landline", "" + agentModel.getLandLine());
            i.putExtra("Address", "" + agentModel.getRegisterAddress() + " , " + agentModel.getCity() + " , " + agentModel.getState() + " , " + agentModel.getPostalCode());
            i.putExtra("Email", "" + agentModel.getEmailID());
            i.putExtra("Image", "" + agentModel.getUserImage1());
            i.putExtra("compImage",""+agentModel.getUserImage());
            context.startActivity(i);
            Log.e("id", "" + agentModel.getAgentID());


        }


    }
}
