package com.narnolia.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.narnolia.app.libs.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AbstractActivity implements View.OnClickListener {

    LinearLayout linear_dashboard, linear_create_lead, linear_update_lead, linear_master,
            linear_setting, linear_mis_reports, linear_notification;
    private Context mContext;
    private Utils utils;
    String[] strLeadArray = null;


    private List<String> spinSourceLeadList;

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

            utils = new Utils(mContext);
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
                    builder.setTitle("Narnolia");
                    builder.setMessage("Are you sure you want to LogOut?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    utils.logout(mContext);
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
                pushActivity(mContext, DashboardActivity.class, null, true);
                break;
            case R.id.linear_create_lead:
                pushActivity(mContext, LeadActivity.class, null, true);
                break;
            case R.id.linear_update_lead:
                pushActivity(mContext, UpdateLeadActivity.class, null, true);
                break;
            case R.id.linear_master:

                showMasterDialog();

                break;
            case R.id.linear_setting:

                showSettingDialog();

                break;
            case R.id.linear_mis_reports:

                showReportDialog();

            break;
            case R.id.linear_notification:

                break;
        }

    }


    private void showMasterDialog(){

        spinSourceLeadList = new ArrayList<>();
       final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.master));
        tvnameMaster.setText(mContext.getResources().getString(R.string.master));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);


        spinSourceLeadList.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));

        if (spinSourceLeadList.size() > 0) {
            strLeadArray = new String[spinSourceLeadList.size() + 1];
            strLeadArray[0] = "---Select---";
            for (int i = 0; i < spinSourceLeadList.size(); i++) {
                strLeadArray[i + 1] = spinSourceLeadList.get(i);
            }
        }

        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strLeadArray);
        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);

        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });


    }

    private void showSettingDialog(){

        spinSourceLeadList = new ArrayList<>();
        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.setting));
        tvnameMaster.setText(mContext.getResources().getString(R.string.setting));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);

        spinSourceLeadList.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));

        if (spinSourceLeadList.size() > 0) {
            strLeadArray = new String[spinSourceLeadList.size() + 1];
            strLeadArray[0] = "---Select---";
            for (int i = 0; i < spinSourceLeadList.size(); i++) {
                strLeadArray[i + 1] = spinSourceLeadList.get(i);
            }
        }


        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strLeadArray);
        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);

        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });
    }

    private void showReportDialog(){

        spinSourceLeadList = new ArrayList<>();
        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.master_custom_dialog, null);
        DialogMaster.setView(dialogViewMaster);

        final AlertDialog showMaster = DialogMaster.show();

        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close);
        TextView tvHeaderMaster = (TextView) showMaster.findViewById(R.id.textTitle);
        TextView tvnameMaster = (TextView) showMaster.findViewById(R.id.txtmaster);

        tvHeaderMaster.setText(mContext.getResources().getString(R.string.mis_report));
        tvnameMaster.setText(mContext.getResources().getString(R.string.mis_report));

        Spinner spinnerMaster = (Spinner) showMaster
                .findViewById(R.id.spin_master);


        spinSourceLeadList.addAll(Arrays.asList(getResources().getStringArray(R.array.titles)));

        if (spinSourceLeadList.size() > 0) {
            strLeadArray = new String[spinSourceLeadList.size() + 1];
            strLeadArray[0] = "---Select---";
            for (int i = 0; i < spinSourceLeadList.size(); i++) {
                strLeadArray[i + 1] = spinSourceLeadList.get(i);
            }
        }
        ArrayAdapter<String> adapterMaster = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, strLeadArray);
        adapterMaster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaster.setAdapter(adapterMaster);

        spinnerMaster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int arg2, long arg3) {
                String selItem = parent.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster.dismiss();
            }
        });
    }


}
