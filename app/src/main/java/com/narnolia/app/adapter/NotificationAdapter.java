package com.narnolia.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.narnolia.app.R;
import com.narnolia.app.model.GetMessagesModel;
import com.narnolia.app.model.LeadInfoModel;

import java.util.List;

/**
 * Created by Sudesi on 08/03/2017.
 */

public class NotificationAdapter extends BaseAdapter {
    public Context context;
    private static LayoutInflater inflater = null;
    private GetMessagesModel getMessagesModel;
    private List<GetMessagesModel> getMessagesModels;
    public NotificationAdapter(Context mContext,List<GetMessagesModel> getMessagesModels) {
        this.context = mContext;
        this.getMessagesModels = getMessagesModels;


    }

    @Override
    public int getCount() {
        return getMessagesModels.size();
    }

    @Override
    public Object getItem(int position) {
        return getMessagesModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final NotificationAdapter.ViewHolder viewHolder;
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.message_item_list_notify, null);

            viewHolder = new NotificationAdapter.ViewHolder();

            viewHolder.message = (TextView) convertView.findViewById(R.id.message);
            viewHolder.btnReceiver = (TextView) convertView.findViewById(R.id.btnReceiver);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationAdapter.ViewHolder) convertView.getTag();
        }
        getMessagesModel = getMessagesModels.get(position);
        viewHolder.message.setText(getMessagesModel.getMessage());
        viewHolder.btnReceiver.setText(getMessagesModel.getReceiver());
        viewHolder.date.setText(getMessagesModel.getDate());

        return convertView;
    }
    public class ViewHolder {
        private TextView message;
        private TextView btnReceiver;
        private TextView date;

    }
}
