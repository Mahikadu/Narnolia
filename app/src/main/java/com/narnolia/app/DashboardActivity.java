package com.narnolia.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.narnolia.app.adapter.DashboardAdapter;
import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.model.LeadInfoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Admin on 25-10-2016.
 */

public class DashboardActivity extends AbstractActivity {

    private Context mContext;
    private ListView lvLead;
    private DashboardAdapter dashBoardAdapter;
    String countryList[] = {""};
    private LeadInfoModel leadInfoModel;
    private TableLayout table_layout;
    private List<LeadInfoModel> leadInfoModelList;
    private DatePickerDialog datePickerDialog;  //date picker declare
    private SimpleDateFormat dateFormatter;
    String spinLeadStatusArray[]={"Select Lead Status","Hot","Warm","Cold","Not Intersted","Wrong Contact Details","Lost","Lost to Competitor","Research Servicing","On-boarding"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        mContext = DashboardActivity.this;
        leadInfoModelList = new ArrayList<LeadInfoModel>();
       // lvLead = (ListView) findViewById(R.id.biList);
        table_layout = (TableLayout) findViewById(R.id.tableLaout_lead);

        createInitialModel();
        fetchDataOfLeadDetails();
        // buildTable();
       /* dashBoardAdapter = new DashboardAdapter(mContext, leadInfoModelList);
        lvLead.setAdapter(dashBoardAdapter);*/

        setHeader();
    }

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);

            tvHeader.setText(mContext.getResources().getString(R.string.header_text_dashboard));

            ivHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushActivity(mContext, HomeActivity.class, null, true);
                }
            });

            ivLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pushActivity(mContext, HomeActivity.class, null, true);
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goBack();
    }

    private void goBack() {
        pushActivity(mContext, HomeActivity.class, null, true);
        finish();
    }

    public void fetchDataOfLeadDetails() {
        try {
            // WHERE   clause

            Cursor cursor = Narnolia.dbCon.getAllDataFromTable(DbHelper.TABLE_DIRECT_LEAD);
            Log.i("TAG", "Cursor count:" + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {

                    leadInfoModel = createDashboardModel(cursor);
                    leadInfoModelList.add(leadInfoModel);

                } while (cursor.moveToNext());
                cursor.close();
            }
            buildTable();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (leadInfoModelList != null) {
                if (leadInfoModelList.size() > 0) {
                    leadInfoModelList.clear();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*createInitialModel();
        fetchDataOfLeadDetails(leadSelect);*/
        ;
       /* fetchDataFromDB();
        buildTable();
*/
    }

    private void createInitialModel() {
        LeadInfoModel leadInfoModel = null;
        try {
            leadInfoModel = new LeadInfoModel();
            leadInfoModel.setLead_id("Lead ID");
            leadInfoModel.setFirstname("Name");
            leadInfoModel.setMobile_no("Mobile Number");
            leadInfoModel.setCity("City");
            leadInfoModel.setPincode("Pincode");
            leadInfoModel.setLast_meeting_date("Last Metting Date");
            leadInfoModel.setLast_meeting_update("Last Metting Update");
            leadInfoModel.setLeadstatus("Lead Status");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        leadInfoModelList.add(leadInfoModel);
    }


    private LeadInfoModel createDashboardModel(Cursor cursor) {

        LeadInfoModel leadInfoModel = null;
        try {
            leadInfoModel = new LeadInfoModel();

            leadInfoModel.setLead_id(cursor.getString(cursor.getColumnIndex("lead_id")));
            leadInfoModel.setFirstname(cursor.getString(cursor.getColumnIndex("fname")));
            leadInfoModel.setMobile_no(cursor.getString(cursor.getColumnIndex("mobile_no")));
            leadInfoModel.setCity(cursor.getString(cursor.getColumnIndex("city")));
            leadInfoModel.setPincode(cursor.getString(cursor.getColumnIndex("pincode")));
            leadInfoModel.setLast_meeting_date(cursor.getString(cursor.getColumnIndex("last_meeting_date")));
            leadInfoModel.setLast_meeting_update(cursor.getString(cursor.getColumnIndex("last_meeting_update")));
            leadInfoModel.setLeadstatus(cursor.getString(cursor.getColumnIndex("lead_status")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadInfoModel;
    }

    private void buildTable() {
        try {
            for (int i = 0; i < leadInfoModelList.size(); i++) {


                TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.dashboard_row_item, null);
                if(i==0){
                    tr.setVisibility(View.GONE);
                }
                TextView tvLeadId = (TextView) tr.findViewById(R.id.tvLeadId);
                TextView tvCustName = (TextView) tr.findViewById(R.id.tvCustName);
                TextView tvMobile = (TextView) tr.findViewById(R.id.tvMobile);
                TextView tvCity = (TextView) tr.findViewById(R.id.tvCity);
                TextView tvPincode = (TextView) tr.findViewById(R.id.tvPincode);
                TextView tvLastMeet_date = (TextView) tr.findViewById(R.id.tvLastMeet_date);
                TextView tvLastMeet_update = (TextView) tr.findViewById(R.id.tvLastMeet_update);
                TextView tvLeadStatus = (TextView) tr.findViewById(R.id.tvLeadStatus);
                TextView tvNextMeet = (TextView) tr.findViewById(R.id.tvNextMeet);
               tvNextMeet.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       showNext_Metting_Dialog();
                   }
               });
                TextView tvCloseLead = (TextView) tr.findViewById(R.id.tvCloseLead);
                tvCloseLead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showClose_lead_Dialog();
                    }
                });


             /*   tvLeadId.setText("Lead ID");
                tvCustName.setText("Name");
                tvMobile.setText("Mobile Number");
                tvCity.setText("City");
                tvPincode.setText("Pincode");
                tvLastMeet_date.setText("Last Metting Date");
                tvLastMeet_update.setText("Last Metting Update");
                tvLeadStatus.setText("Lead Status");
                tvNextMeet.setText("Next Metting");
                tvCloseLead.setText("Close Lead");*/

                final LeadInfoModel leadInfoModel = leadInfoModelList.get(i);

                tvLeadId.setText(leadInfoModel.getLead_id());
                tvCustName.setText(leadInfoModel.getFirstname());
                tvMobile.setText(leadInfoModel.getMobile_no());
                tvCity.setText(leadInfoModel.getCity());
                tvPincode.setText(leadInfoModel.getPincode());
                tvLastMeet_date.setText(leadInfoModel.getLast_meeting_date());
                tvLastMeet_update.setText(leadInfoModel.getLast_meeting_update());
                tvLeadStatus.setText(leadInfoModel.getLeadstatus());
//                tvNextMeet.setText(leadInfoModel.getUpdateddt());
//                tvCloseLead.setText(leadInfoModel.getLead_id());

                table_layout.addView(tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showNext_Metting_Dialog(){
        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.custom_next_meeting_dialog, null);
        DialogMaster.setView(dialogViewMaster);
        final AlertDialog showMaster = DialogMaster.show();
        try {
            final EditText edt_next_meeting_dialog=(EditText)showMaster.findViewById(R.id.edt_next_meeting_dialog);
            final Calendar newCalendar = Calendar.getInstance();
            dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(monthOfYear, dayOfMonth, year);
                    edt_next_meeting_dialog.setText(dateFormatter.format(newDate.getTime()));
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            edt_next_meeting_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        Button btnDismissMaster = (Button) showMaster.findViewById(R.id.iv_close_dial);
        Button submit = (Button) showMaster.findViewById(R.id.btn1_submit);
        Button close = (Button) showMaster.findViewById(R.id.btn1_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaster.dismiss();
            }
        });
        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaster.dismiss();
            }
        });
    }
    private void showClose_lead_Dialog(){
        final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(this);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogViewMaster = li.inflate(R.layout.close_lead_dialog, null);
        DialogMaster.setView(dialogViewMaster);
        final AlertDialog showMaster1 = DialogMaster.show();
        Spinner spinner_lead_status=(Spinner)showMaster1.findViewById(R.id.spin_lead_status_dilog);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinLeadStatusArray) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = null;
                // If this is the initial dummy entry, make it hidden
                if (position == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(View.GONE);
                    v = tv;
                }
                else {
                    // Pass convertView as null to prevent reuse of special case views
                    v = super.getDropDownView(position, null, parent);
                }
                // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lead_status.setAdapter(adapter);

        Button btnDismissMaster = (Button) showMaster1.findViewById(R.id.iv_close);
        Button submit = (Button) showMaster1.findViewById(R.id.btn1_create_close_lead);
        Button close = (Button) showMaster1.findViewById(R.id.btn1_closelead);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMaster1.dismiss();
            }
        });
        btnDismissMaster.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                showMaster1.dismiss();
            }
        });


    }
}
