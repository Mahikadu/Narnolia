package com.narnolia.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narnolia.app.R;

/**
 * Created by Admin on 25-10-2016.
 */

public class DashboardAdapter extends BaseAdapter{

    private static LayoutInflater inflater = null;
    public Context _context;
    String countryList[];


    public DashboardAdapter(Context mContext,String[] countryList) {
        this._context = mContext;
        this.countryList = countryList;

    }

    @Override
    public int getCount() {
        return countryList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        viewHolder.tvLeadId.setText("Lead ID");
        viewHolder.tvCustName.setText("Name");
        viewHolder.tvMobile.setText("Mobile Number");
        viewHolder.tvCity.setText("City");
        viewHolder.tvPincode.setText("Pincode");
        viewHolder.tvLastMeet_date.setText("Last Meeting Date");
        viewHolder.tvLastMeet_update.setText("Last Meeting Update");
        viewHolder.tvLeadStatus.setText("Lead Status");
        viewHolder.tvNextMeet.setText("Next Meeting");
        viewHolder.tvCloseLead.setText("Close Lead");

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
