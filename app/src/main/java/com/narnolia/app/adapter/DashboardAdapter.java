package com.narnolia.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narnolia.app.R;
import com.narnolia.app.model.LeadInfoModel;

import java.util.List;

/**
 * Created by Admin on 25-10-2016.
 */

public class DashboardAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    public Context _context;
    private LeadInfoModel leadInfoModel;
    private List<LeadInfoModel> leadInfoModles;

    public DashboardAdapter(Context mContext, List<LeadInfoModel> leadInfoModles) {
        this._context = mContext;
        this.leadInfoModles = leadInfoModles;


    }

    @Override
    public int getCount() {
        return leadInfoModles.size();
    }

    @Override
    public Object getItem(int position) {
        return leadInfoModles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (inflater == null)
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.dashboard_row_item, null);

            viewHolder = new ViewHolder();

            viewHolder.tvLeadId = (TextView) convertView.findViewById(R.id.tvLeadId);
            viewHolder.tvCustName = (TextView) convertView.findViewById(R.id.tvCustName);
            viewHolder.tvMobile = (TextView) convertView.findViewById(R.id.tvMobile);
            viewHolder.tvCity = (TextView) convertView.findViewById(R.id.tvCity);
            viewHolder.tvPincode = (TextView) convertView.findViewById(R.id.tvPincode);
            viewHolder.tvLastMeet_date = (TextView) convertView.findViewById(R.id.tvLastMeet_date);
            viewHolder.tvLastMeet_update = (TextView) convertView.findViewById(R.id.tvLastMeet_update);
            viewHolder.tvLeadStatus = (TextView) convertView.findViewById(R.id.tvLeadStatus);
            viewHolder.tvNextMeet = (TextView) convertView.findViewById(R.id.tvNextMeet);
            viewHolder.tvCloseLead = (TextView) convertView.findViewById(R.id.tvCloseLead);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        leadInfoModel = leadInfoModles.get(position);
        viewHolder.tvLeadId.setText(leadInfoModel.getLead_id());
        viewHolder.tvCustName.setText(leadInfoModel.getFirstname());
        viewHolder.tvMobile.setText(leadInfoModel.getMobile_no());
        viewHolder.tvCity.setText(leadInfoModel.getCity());
        viewHolder.tvPincode.setText(leadInfoModel.getPincode());
        viewHolder.tvLastMeet_date.setText(leadInfoModel.getMeetingdt());
        viewHolder.tvLastMeet_update.setText(leadInfoModel.getUpdatedby());
        viewHolder.tvLeadStatus.setText(leadInfoModel.getLeadstatus());
        // viewHolder.tvNextMeet.setText(leadInfoModel.getUpdateddt());
        // viewHolder.tvCloseLead.setText(leadInfoModel.getLead_id());

        return convertView;
    }


    public class ViewHolder {
        private TextView tvLeadId;
        private TextView tvCustName;
        private TextView tvMobile;
        private TextView tvCity;
        private TextView tvPincode;
        private TextView tvLastMeet_date;
        private TextView tvLastMeet_update;
        private TextView tvLeadStatus;
        private TextView tvNextMeet;
        private TextView tvCloseLead;
    }
}
