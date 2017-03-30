package com.narnolia.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narnolia.app.R;
import com.narnolia.app.UpdateLeadActivity;
import com.narnolia.app.model.SubStatusReportModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sudesi on 24/03/2017.
 */

public class SubStatusReportAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    private SubStatusReportModel subStatusReportModel;
    private List<SubStatusReportModel> subStatusReportModels = new ArrayList<>();
    public SubStatusReportAdapter(Context mContext, List<SubStatusReportModel> subStatusReportModels) {
        this.context = mContext;
        this.subStatusReportModels = subStatusReportModels;


    }
    @Override
    public int getCount() {
        return subStatusReportModels.size();
    }

    @Override
    public Object getItem(int position) {
        return subStatusReportModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final SubStatusReportAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_sub_status_report, null);

            viewHolder = new SubStatusReportAdapter.ViewHolder();

            viewHolder.sub_lead_id = (TextView) convertView.findViewById(R.id.sub_lead_id);
            viewHolder.sub_name = (TextView) convertView.findViewById(R.id.sub_name);
            viewHolder.sub_mobile_no = (TextView) convertView.findViewById(R.id.sub_mobile_no);
            viewHolder.sub_city = (TextView) convertView.findViewById(R.id.sub_city);
            viewHolder.sub_pincode = (TextView) convertView.findViewById(R.id.sub_pincode);
            viewHolder.sub_last_meeting_date = (TextView) convertView.findViewById(R.id.sub_last_meeting_date);
            viewHolder.sub_last_meeting_update = (TextView) convertView.findViewById(R.id.sub_last_meeting_update);
            viewHolder.sub_lead_status = (TextView) convertView.findViewById(R.id.sub_lead_status);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubStatusReportAdapter.ViewHolder) convertView.getTag();
        }
        subStatusReportModel = subStatusReportModels.get(position);
        viewHolder.sub_lead_id.setText(subStatusReportModel.getSub_lead_id());
        viewHolder.sub_lead_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subStatusReportModel.getSub_lead_id()!=null&& !subStatusReportModel.getSub_lead_id().equals("")){
                    Intent intent = new Intent(context, UpdateLeadActivity.class);
                    intent.putExtra("sub_lead_Id", subStatusReportModel.getSub_lead_id());
                    context.startActivity(intent);

                }

        }
    });
        viewHolder.sub_name.setText(subStatusReportModel.getSub_name());
        viewHolder.sub_mobile_no.setText(subStatusReportModel.getSub_mobile_no());
        viewHolder.sub_city.setText(subStatusReportModel.getSub_city());
        viewHolder.sub_pincode.setText(subStatusReportModel.getSub_pincode());
        viewHolder.sub_last_meeting_date.setText(subStatusReportModel.getSub_last_meeting_date());
        viewHolder.sub_last_meeting_update.setText(subStatusReportModel.getSub_last_meeting_update());
        viewHolder.sub_lead_status.setText(subStatusReportModel.getSub_lead_status());

        return convertView;

}
public class ViewHolder {

    private TextView sub_lead_id,sub_name,sub_mobile_no,sub_city,sub_pincode,sub_last_meeting_date,sub_last_meeting_update,sub_lead_status;

}
}
