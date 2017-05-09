package com.narnolia.app.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narnolia.app.R;
import com.narnolia.app.SharedPref;
import com.narnolia.app.model.AttendenceReportModel;
import com.narnolia.app.model.StatusReportModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 5/9/2017.
 */
public class AttendanceReportAdapter extends BaseAdapter{

    public Context context;
    private static LayoutInflater inflater = null;
    private AttendenceReportModel attendenceReportModel;
    private SharedPref sharedPref;
    private List<AttendenceReportModel> attendenceReportModelList = new ArrayList<>();

    public AttendanceReportAdapter(Context mContext, List<AttendenceReportModel> attendenceReportModelList) {
        this.context = mContext;
        sharedPref = new SharedPref(mContext);
        this.attendenceReportModelList = attendenceReportModelList;

    }
    @Override
    public int getCount() {
        return attendenceReportModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendenceReportModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AttendanceReportAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_attendance_report, null);

            viewHolder = new AttendanceReportAdapter.ViewHolder();
            viewHolder.emp_id = (TextView) convertView.findViewById(R.id.emp_id);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.attendence_date = (TextView) convertView.findViewById(R.id.attendence_date);
            viewHolder.attendence1 = (TextView) convertView.findViewById(R.id.attendence1);
            viewHolder.location = (TextView) convertView.findViewById(R.id.location);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AttendanceReportAdapter.ViewHolder) convertView.getTag();
        }
        attendenceReportModel = attendenceReportModelList.get(position);
        viewHolder.emp_id.setText(attendenceReportModel.getEmpId1());
        viewHolder.name.setText(attendenceReportModel.getNameAttendence());
        viewHolder.attendence_date.setText(attendenceReportModel.getInsertdate());
        viewHolder.attendence1.setText(attendenceReportModel.getAttendance());
        viewHolder.location.setText(attendenceReportModel.getLocation());
        return convertView;
    }

    public class ViewHolder {
        private TextView emp_id, name, attendence_date, attendence1, location;

    }
}
