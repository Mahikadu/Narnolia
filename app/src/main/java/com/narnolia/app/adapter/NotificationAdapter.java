package com.narnolia.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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
    Animation animation = null;
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
            animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);
            viewHolder = new NotificationAdapter.ViewHolder();

            viewHolder.message = (TextView) convertView.findViewById(R.id.message);
            viewHolder.btnReceiver = (TextView) convertView.findViewById(R.id.btnReceiver);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NotificationAdapter.ViewHolder) convertView.getTag();
        }
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        getMessagesModel = getMessagesModels.get(position);
        viewHolder.message.setText(getMessagesModel.getMessage());
        viewHolder.btnReceiver.setText(getMessagesModel.getReceiver());
        viewHolder.date.setText(getMessagesModel.getDate());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                70, 70);
        params.setMargins(5,15,0, 0);



        Log.v("",""+(position % 2 == 0));

        if(position % 2 == 0){

            convertView.setBackgroundColor(Color.parseColor("#696969"));

        }else{

            convertView.setBackgroundColor(Color.parseColor("#808080"));
        }


        animation.setDuration(500);

        convertView.startAnimation(animation);

        animation = null;

        return convertView;
    }
    public class ViewHolder {
        private TextView message;
        private TextView btnReceiver;
        private TextView date;

    }
}
