package com.narnolia.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AbstractActivity implements View.OnClickListener {

    LinearLayout linear_dashboard, linear_create_lead, linear_update_lead, linear_master,
            linear_setting, linear_mis_reports, linear_notification;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        try {
            mContext = HomeActivity.this;

            setHeader();

            //Dashboard icon reference
            linear_dashboard = (LinearLayout) findViewById(R.id.linear_dashboard);
            linear_create_lead = (LinearLayout) findViewById(R.id.linear_create_lead);
            linear_update_lead = (LinearLayout) findViewById(R.id.linear_update_lead);
            linear_master = (LinearLayout) findViewById(R.id.linear_master);
            linear_setting = (LinearLayout) findViewById(R.id.linear_setting);
            linear_mis_reports = (LinearLayout) findViewById(R.id.linear_mis_reports);
            linear_notification = (LinearLayout) findViewById(R.id.linear_notification);

            linear_dashboard.setOnClickListener(this);
            linear_create_lead.setOnClickListener(this);
            linear_update_lead.setOnClickListener(this);
            linear_master.setOnClickListener(this);
            linear_setting.setOnClickListener(this);
            linear_mis_reports.setOnClickListener(this);
            linear_notification.setOnClickListener(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);

            tvHeader.setText(mContext.getResources().getString(R.string.header_text_home));

            ivHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            ivLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("IDBILifeInsurance");
                    builder.setMessage("Are you sure you want to LogOut?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    pushActivity(mContext, LoginActivity.class, null, true);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.linear_dashboard:

                break;
            case R.id.linear_create_lead:
                pushActivity(HomeActivity.this, LeadActivity.class, null, true);
                break;
            case R.id.linear_update_lead:
                pushActivity(HomeActivity.this, UpdateLeadActivity.class, null, true);
                break;
            case R.id.linear_master:

                break;
            case R.id.linear_setting:

                break;
            case R.id.linear_mis_reports:

                break;
            case R.id.linear_notification:

                break;
        }

    }
}
