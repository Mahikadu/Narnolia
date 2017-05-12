package com.narnolia.app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.model.AttendenceReportModel;
import com.narnolia.app.network.LoginWebService;
import com.narnolia.app.network.SOAPWebService;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@TargetApi(3)
public class MyCalendarActivity extends AbstractActivity implements OnClickListener {
    private static final String tag = "MyCalendarActivity";

    private TextView currentMonth;
    //	private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private TextView admin;
    private String empcode;
    private ProgressDialog progressDialog;
    private String result = "";
    private SharedPref sharedPref;
    String fromLoginKey;
    AttendenceReportModel attendenceReportModel;
    String responseId = "";
    private List<AttendenceReportModel> attendenceReportModelList;
    @SuppressLint("NewApi")
    private int month, year;
    private int month2, year2;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private Dialog dialog;
    public SharedPref pref;
    private Context mContext;
    private String strAttendance = "";
    String LoginCheck;
    String fromHomeKey;
    SimpleDateFormat sdf;
    LinearLayout header_Layout;
    int day1, year1, month1;
    String attendanceDate = "", attendmonth;
    private ArrayList<HashMap<String, String>> eventsPerMonthMap = new ArrayList<HashMap<String, String>>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_calendar_view);
        mContext = MyCalendarActivity.this;
        progressDialog = new ProgressDialog(mContext);
        pref = new SharedPref(mContext);
        //   progressDialog.setTitle("Login Status");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        sharedPref = new SharedPref(mContext);
        empcode = sharedPref.getLoginId();
        attendenceReportModelList = new ArrayList<>();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
//        attendenceReportModel = new AttendenceReportModel();
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
                + year);
        /*selectedDayMonthYearButton = (Button) this
                .findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");*/
        admin = (TextView) findViewById(R.id.admin);
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) this.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);
        setHeader();
        calendarView = (GridView) this.findViewById(R.id.calendar);
        header_Layout = (LinearLayout) findViewById(R.id.header);


        try {
            if (getIntent() != null) {
                fromHomeKey = getIntent().getStringExtra("from_home");
                if (fromHomeKey != null) {
                    if (fromHomeKey.equals("FromHome")) {
                        String mnt = String.valueOf(month);
                        String yrs = String.valueOf(year);

                        String Capempcode = empcode.substring(0, 1).toUpperCase() + empcode.substring(1);
                        admin.setText(Capempcode);
                        new AttendenceReport().execute(mnt, yrs);
                    }
                }
                fromLoginKey = getIntent().getStringExtra("from_login");
                if (fromLoginKey != null) {
                    if (fromLoginKey.equals("FromLogin")) {
                        header_Layout.setVisibility(View.GONE);
                        String mnt = String.valueOf(month);
                        String yrs = String.valueOf(year);
                        new AttendenceReport().execute(mnt, yrs);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //LoginCheck=getIntent().getStringExtra("from_Login");
        /*// Initialised
        adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		LoginCheck=getIntent().getStringExtra("from_Login");*/
        //setHeader();
    }

    private void setHeader() {
        try {
            TextView tvHeader = (TextView) findViewById(R.id.textTitle);
            ImageView ivHome = (ImageView) findViewById(R.id.iv_home);
            ImageView ivLogout = (ImageView) findViewById(R.id.iv_logout);
            tvHeader.setText(mContext.getResources().getString(R.string.attendence_report));

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

        if (getIntent() != null) {
            fromHomeKey = getIntent().getStringExtra("from_home");
            if (fromHomeKey != null) {
                if (fromHomeKey.equals("FromHome")) {
                    goBack();
                }
            }
            fromLoginKey = getIntent().getStringExtra("from_login");
            if (fromLoginKey != null) {
                if (fromLoginKey.equals("FromLogin")) {
                    header_Layout.setVisibility(View.GONE);
                    goLogin();
                }
            }
        }
    }

    private void goBack() {
        pushActivity(mContext, HomeActivity.class, null, true);
        finish();
    }

    private void goLogin() {
        pushActivity(mContext, LoginActivity.class, null, true);
        finish();
    }

    private ArrayList<HashMap<String, String>> findNumberOfEventsPerMonth(ArrayList<AttendenceReportModel> attendanceReportList) {
        ArrayList<HashMap<String, String>> arraymap = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < attendanceReportList.size(); i++) {
            if (attendanceReportList.get(i).getInsertdate() != null && !TextUtils.isEmpty(attendanceReportList.get(i).getInsertdate())) {
                String date_month_year = attendanceReportList.get(i).getInsertdate();
                String mon = String.valueOf(month);
                if (mon.length() == 1) {
                    mon = "0" + mon;
                }
                DateFormat dateFormatter2 = new DateFormat();
                Log.e("mon===", String.valueOf(mon));
                Log.e("month===", String.valueOf(month));
                try {

                    HashMap<String, String> map = new HashMap<String, String>();
                    String d[] = date_month_year.split("/");
                    //	String date = getmonthName(d[1]) + "-" + d[0] + "-"
                    //			+ d[2]; ------27.10.2014

                    if (d[0].length() == 1)
                        d[0] = "0" + d[0];
                    if (d[1].length() == 1)
                        d[1] = "0" + d[1];
                    String date = getmonthNo1(d[0]) + "-" + d[1] + "-"
                            + d[2];

                    Log.e("date", date);
                    //	Date dateCreated = new Date(date);
                    //	Log.e("dateCreated", String.valueOf(dateCreated));
                    //	String day = dateFormatter2.format("dd", dateCreated)
                    //			.toString();
                    //	String month1 = DateFormat.format("MM", dateCreated)
                    //			.toString();
                    String[] ddd = d[2].split(" ");

					/*Log.v("", "ddd[0]==" + ddd[0]);
					Log.v("", "ddd[1]==" + ddd[1]);*/

//					String yyyy = c.getString(2);
                    String yyyy = ddd[0];
                    Log.v("", "yyyy==" + yyyy);

                    Log.e("day", d[1]);
                    Log.e("month1", d[0]);
                    //map.put("day", day);
                    //	map.put("month", month1);
                    map.put("day", d[1]);
                    map.put("month", d[0]);
                    map.put("attendance", attendanceReportList.get(i).getAttendance());
                    map.put("year", yyyy);
                    arraymap.add(map);

                    Log.e("selected DateMAp", arraymap.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }    // end of for
        }
        return arraymap;
    }

    /**
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(getApplicationContext(),
                R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            String mnt = String.valueOf(month);
            String yrs = String.valueOf(year);
            new AttendenceReport().execute(mnt, yrs);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: "
                    + month + " Year: " + year);
            String mnt = String.valueOf(month);
            String yrs = String.valueOf(year);
            new AttendenceReport().execute(mnt, yrs);
            setGridCellAdapterToDate(month, year);
        }

    }

    @Override
    public void onDestroy() {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    public String getmonthNo(String monthName) {
        String month = "";

        if (monthName.equals("January")) {
            month = "01";
        } else if (monthName.equals("February")) {
            month = "02";
        } else if (monthName.equals("March")) {
            month = "03";
        } else if (monthName.equals("April")) {
            month = "04";
        } else if (monthName.equals("May")) {
            month = "05";
        } else if (monthName.equals("June")) {
            month = "06";
        } else if (monthName.equals("July")) {
            month = "07";
        } else if (monthName.equals("August")) {
            month = "08";
        } else if (monthName.equals("September")) {
            month = "09";
        } else if (monthName.equals("October")) {
            month = "10";
        } else if (monthName.equals("November")) {
            month = "11";
        } else if (monthName.equals("December")) {
            month = "12";
        }

        return month;
    }

    public String getmonthNo1(String monthName) {
        String month = "";

        if (monthName.equals("01")) {
            month = "1";
        } else if (monthName.equals("02")) {
            month = "2";
        } else if (monthName.equals("03")) {
            month = "3";
        } else if (monthName.equals("04")) {
            month = "4";
        } else if (monthName.equals("05")) {
            month = "5";
        } else if (monthName.equals("06")) {
            month = "6";
        } else if (monthName.equals("07")) {
            month = "7";
        } else if (monthName.equals("08")) {
            month = "8";
        } else if (monthName.equals("09")) {
            month = "9";
        } else if (monthName.equals("10")) {
            month = "10";
        } else if (monthName.equals("11")) {
            month = "11";
        } else if (monthName.equals("12")) {
            month = "12";
        }

        return month;
    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        //		private final HashMap<String, Integer> eventsPerMonthMap;
        private ProgressDialog progressDialog;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " "
                    + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
//			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having "
                    + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                        + prevMonth + " NextMonth: " + nextMonth
                        + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is "
                    + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= "
                                + prevMonth
                                + " => "
                                + getMonthAsString(prevMonth)
                                + " "
                                + String.valueOf((daysInPrevMonth
                                - trailingSpaces + DAY_OFFSET)
                                + i));
                list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.d(currentMonthName, String.valueOf(i) + " "
                        + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private ArrayList<HashMap<String, String>> findNumberOfEventsPerMonth(int year,
                                                                              int month) {
            ArrayList<HashMap<String, String>> arraymap = new ArrayList<HashMap<String, String>>();


            String mon = String.valueOf(month);
            if (mon.length() == 1) {
                mon = "0" + mon;
            }
            DateFormat dateFormatter2 = new DateFormat();
            Log.e("mon===", String.valueOf(mon));
            Log.e("month===", String.valueOf(month));
            try {

                String colnames[] = new String[]{"Adate", "attendance", "year"};
//				db.open();
                // Cursor c=db.fetchallOrder("attendance", colnames, null);
                Cursor c = Narnolia.dbCon.fetchallSpecify("attendance", colnames, "month",
                        String.valueOf(month), null);
                Log.v("", "c======" + c.getCount());
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    do {

                        HashMap<String, String> map = new HashMap<String, String>();
                        Log.e("c.getString(0)", String.valueOf(c.getString(0)));
                        String d[] = c.getString(0).split("-");
                        //	String date = getmonthName(d[1]) + "-" + d[0] + "-"
                        //			+ d[2]; ------27.10.2014

                        String date = getmonthNo1(d[1]) + "-" + d[0] + "-"
                                + d[2];

                        Log.e("date", date);
                        //	Date dateCreated = new Date(date);
                        //	Log.e("dateCreated", String.valueOf(dateCreated));
                        //	String day = dateFormatter2.format("dd", dateCreated)
                        //			.toString();
                        //	String month1 = DateFormat.format("MM", dateCreated)
                        //			.toString();
                        String[] ddd = d[2].split(" ");

                        Log.v("", "ddd[0]==" + ddd[0]);
                        Log.v("", "ddd[1]==" + ddd[1]);

                        String yyyy = c.getString(2);
                        Log.v("", "yyyy==" + yyyy);

                        Log.e("day", ddd[0]);
                        Log.e("month1", d[1]);
                        //map.put("day", day);
                        //	map.put("month", month1);
                        map.put("day", ddd[0]);
                        map.put("month", d[1]);
                        map.put("attendance", c.getString(1));
                        map.put("year", yyyy);
                        arraymap.add(map);

                        Log.e("selected DateMAp", arraymap.toString());
                    } while (c.moveToNext());
                }
//				db.close();
                // String[] dates =new
                // String[]{"May-24-2014","May-22-2014","May-14-2014"};
                //
                // for(int i=0;i<dates.length;i++)
                // {
                // Date dateCreated=new Date(dates[i]);
                // Log.e("dateCreated",String.valueOf(dateCreated));
                // String day = dateFormatter2.format("dd",
                // dateCreated).toString();
                // String month1=dateFormatter2.format("M",
                // dateCreated).toString();
                // String monthname="";
                // Log.e("day",day);
                // Log.e("month1",month1);
                //
                // Log.e("monthname",monthname);
                // map.put(day, monthname);
                //
                // Log.e("selected DateMAp",map.toString());
                // }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return arraymap;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            //temp comment
			/*if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.contains(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}
*/
            // TODO set each date value to calender
            Log.v("", "eventsPerMonthMap=" + eventsPerMonthMap.toString());
            String day = theday;
            if (day.length() == 1) {
                day = "0" + day;
            }
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {

                for (int i = 0; i < eventsPerMonthMap.size(); i++) {

                    Log.v("", "eventsPerMonthMap- year)=" + eventsPerMonthMap.get(i).get("year"));
                    Log.v("", "theyear==" + theyear);
                    if (eventsPerMonthMap.get(i).get("year").equals(theyear)) {
                        Log.e("", "inside year true");
                        if (eventsPerMonthMap.get(i).get("month").equals(getmonthNo(themonth))) {
                            if (eventsPerMonthMap.get(i).get("day").equals(day)) {
                                if (eventsPerMonthMap.get(i).get("attendance").equals("Present")) {
                                    gridcell.setBackgroundResource(R.drawable.green);
                                } else if (eventsPerMonthMap.get(i).get("attendance").equals("Absent")) {
                                    gridcell.setBackgroundResource(R.drawable.red);
                                } else if (eventsPerMonthMap.get(i).get("attendance").equals("H")) {
                                    gridcell.setBackgroundResource(R.color.sky);
                                }
                            }
                        }
                    } else {

                        gridcell.setBackgroundResource(R.drawable.calendar_tile_small);
                    }
                }

            }


            // Set the Day GridCell
            gridcell.setText(theday);
//			gridcell.setTag(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-"
                    + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources()
                        .getColor(R.color.lightgray));
            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(
                        R.color.lightgray02));
            }
            if (day_color[1].equals("BLUE")) {
                gridcell.setTextColor(getResources().getColor(R.color.orrange));
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();

            //	selectedDayMonthYearButton.setText("Selected: " + date_month_year);
            Log.e("Selected date", date_month_year);
            try {
                Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + parsedDate.toString());
//				Toast.makeText(_context, "Currentselected Date is :"+parsedDate, Toast.LENGTH_SHORT).show();

                String selDate = dateFormatter.format(parsedDate.getTime());
                String currDate = dateFormatter.format(Calendar.getInstance().getTime());

                if (!currDate.equalsIgnoreCase(selDate)) {
                    if (getIntent() != null) {
                        fromHomeKey = getIntent().getStringExtra("from_home");
                        if (fromHomeKey != null) {
                            if (!fromHomeKey.equals("FromHome")) {
                                Toast.makeText(_context, "PLease, select current date only..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else {
                    //Toast.makeText(_context, "current date ..!", Toast.LENGTH_SHORT).show();
                    if (getIntent() != null) {
                        fromLoginKey = getIntent().getStringExtra("from_login");
                        if (fromLoginKey != null) {
                            if (fromLoginKey.equals("FromLogin")) {
                                show_attendence();
                            }
                        }
                    }


                    //					open();
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //....................popup attendence ........................

        public void open() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyCalendarActivity.this);
            alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(_context, "You clicked yes button", Toast.LENGTH_LONG).show();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        private void show_attendence() {
			/*
			final AlertDialog.Builder DialogMaster = new AlertDialog.Builder(_context);
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View dialogViewMaster = li.inflate(R.layout.popup_attendance, null);
			DialogMaster.setView(dialogViewMaster);
			final AlertDialog showMaster = DialogMaster.show();
			Button present=(Button)showMaster.findViewById(R.id.btn_present);
			Button absent=(Button)showMaster.findViewById(R.id.btn_absent);
			//.............present Click ..........
			present.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(_context, HomeActivity.class);
					startActivity(intent);
				}
			});
			//............Absent Click.............
			absent.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent=new Intent(_context,LoginActivity.class);
					startActivity(intent);
				}
			});*/


            dialog = new Dialog(MyCalendarActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_attendance);

            Button present = (Button) dialog.findViewById(R.id.btn_present);
            Button absent = (Button) dialog.findViewById(R.id.btn_absent);
            final String[] columns = new String[]{"emp_id", "Adate",
                    "attendance", "absent_type", "lat", "lon", "savedServer", "month",
                    "holiday_desc", "year"};

            present.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (isConnectingToInternet()) {
                        gridcell.setBackgroundColor(getResources().getColor(R.color.border_color));
						/*Intent intent = new Intent(MyCalendarActivity.this, HomeActivity.class);
						startActivity(intent);*/
                        strAttendance = "Present";
                        new UserLogin().execute();

					/*	db.close();

						String attendmonth1 = getmonthNo1(attendmonth);
						db.open();

						values = new String[] { username,
								attendanceDate1,
								"P",
								"",
								String.valueOf(lat),
								String.valueOf(lon),
								"0",
								attendmonth1,
								"",
								year };

						db.insert(values, columns, "attendance");

						db.close();

						view.setBackgroundResource(R.drawable.green);
						dialog.dismiss();
						LocationLibrary.forceLocationUpdate(context);


						new SaveAttendance().execute("b");*/

                    } else {
                        Toast.makeText(_context, "Please check internet Connectivity & Try Again", Toast.LENGTH_LONG).show();
                    }

                }
            });

            absent.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    strAttendance = "Absent";
                    if (isConnectingToInternet()) {
                        // TODO Auto-generated method stub
                        new UserLogin().execute();
                        Intent intent = new Intent(MyCalendarActivity.this, LoginActivity.class);
                        startActivity(intent);
						/*final Dialog d = new Dialog(_context);
						d.requestWindowFeature(Window.FEATURE_NO_TITLE);
						d.setContentView(R.layout.absent_popup);

						RadioGroup rg_attendance_type = (RadioGroup) d
								.findViewById(R.id.rg_absent_type);

						final RadioButton rb_seek_leave = (RadioButton) d
								.findViewById(R.id.rb_seek_leave);

						final RadioButton rb_weekly_off = (RadioButton) d
								.findViewById(R.id.rb_weekly_off);

						final RadioButton rb_holiday = (RadioButton) d
								.findViewById(R.id.rb_holiday);

						rg_attendance_type
								.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(
											RadioGroup group, int checkedId) {
										// TODO Auto-generated method stub
										String absent_falg="a";
										if (rb_seek_leave.isChecked()) {



											String attendmonth1 = getmonthNo1(attendmonth);
											db.open();

											values = new String[] {
													username,
													attendanceDate1,
													"A",
													" Leave",
													String.valueOf(lat),
													String.valueOf(lon),
													"0", attendmonth1,
													"", year };

											db.insert(values, columns,
													"attendance");

											db.close();

											view.setBackgroundResource(R.drawable.red);

											d.dismiss();
											dialog.dismiss();

											new SaveAttendance().execute(absent_falg);





										}

										if (rb_weekly_off.isChecked()) {



											String attendmonth1 = getmonthNo1(attendmonth);
											db.open();

											values = new String[] {
													username,
													attendanceDate1,
													"A",
													" Weekly Off",
													String.valueOf(lat),
													String.valueOf(lon),
													"0", attendmonth1,
													"", year };

											db.insert(values, columns,
													"attendance");

											db.close();

											view.setBackgroundResource(R.drawable.red);

											d.dismiss();
											dialog.dismiss();

											new SaveAttendance().execute(absent_falg);*/


                    }

										/*if (rb_holiday.isChecked()) {



											String attendmonth1 = getmonthNo1(attendmonth);
											db.open();

											values = new String[] {
													username,
													attendanceDate1,
													"A",
													" Holiday",
													String.valueOf(lat),
													String.valueOf(lon),
													"0", attendmonth1,
													"", year };

											db.insert(values, columns,
													"attendance");

											db.close();

											view.setBackgroundResource(R.drawable.red);

											d.dismiss();
											dialog.dismiss();

											new SaveAttendance().execute(absent_falg);





										}


									}
								});*/
//						d.show();

                }/*else
					{
						Toast.makeText(_context, "Please check internet Connectivity & Try Again", Toast.LENGTH_LONG).show();
					}*/

//				}


                /////
            });

            dialog.show();

        }


        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }
    }

    public class UserLogin extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            PackageManager manager = getPackageManager();
            String versionName = "";
            try {
                PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                String packageName = info.packageName;
                int versionCode = info.versionCode;
                versionName = info.versionName;

            } catch (Exception e) {
                // TODO Auto-generated catch block
            }
            LoginWebService webService = new LoginWebService(MyCalendarActivity.this);

            SoapObject object1 = webService.LoginLead(pref.getLoginId(), pref.getUserPass(), pref.getApp(), pref.getVersion_name(), pref.getLat(), pref.getLang(), strAttendance, pref.getCurrentDate(), pref.getKey_Location(), "");

            return object1;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String response = String.valueOf(soapObject);

                System.out.println("Life : : " + response);

                SoapObject res = (SoapObject) soapObject.getProperty(0);
                result = res.getPropertyAsString("Result");

                if (result.equalsIgnoreCase("false")) {

                    displayMessage(getString(R.string.error_login));
                } else {

				/*	email = res.getPropertyAsString("Email");
					loginId = res.getPropertyAsString("LoginId");
					mobile = res.getPropertyAsString("Mobile");
					status = res.getPropertyAsString("Status");
					userId = res.getPropertyAsString("UserId");
					sharedPref.setSharedPrefLogin(email, loginId, mobile, result, status, userId);*/
                    // TODO insert Present in DB


                   /* pushActivity(LoginActivity.this, HomeActivity.class, null, true);*/
                    if (strAttendance.equals("Present")) {
                        insertDataInDb(strAttendance);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("from_cal", "FromCal"); ///bundle1.putString("key", "value");
                        pushActivity(MyCalendarActivity.this, HomeActivity.class, bundle1, true);
                        pref.setSharedPrefLoginWithPass(pref.getLoginId(), pref.getUserPass(), pref.getStatus(), "App", pref.getVersion_name(), pref.getLat(), pref.getLang(), strAttendance, pref.getCurrentDate(), pref.getKey_Location());
                    }
                }


            } catch (Exception e) {
                System.out.println("AyushmanBhav : " + e);
                e.printStackTrace();
            }
        }
    }

    private void insertDataInDb(String attend) {
        try {


            String columnNames[] = {mContext.getString(R.string.column_mobile), mContext.getString(R.string.column_email),
                    mContext.getString(R.string.column_loginId), mContext.getString(R.string.column_mobile),
                    mContext.getString(R.string.column_result), mContext.getString(R.string.column_status),
                    mContext.getString(R.string.column_userId), mContext.getString(R.string.column_username),
                    mContext.getString(R.string.column_password), mContext.getString(R.string.column_attendance)};
            String valuesArray[] = {pref.getEmail(), pref.getLoginId(), pref.getMobile(), result, pref.getStatus(), pref.getUserId(), pref.getUserId(), pref.getUserPass(), attend};
            // WHERE   clause
            String selection = mContext.getString(R.string.column_userId) + " = ?";

            // WHERE clause arguments
            String[] selectionArgs = {pref.getLoginId()};

            boolean result = Narnolia.dbCon.update(DbHelper.TABLE_USER_DETAIL, selection, valuesArray, columnNames, selectionArgs);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AttendenceReport extends AsyncTask<String, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog != null && !progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected SoapObject doInBackground(String... params) {

            SoapObject object = null;
            try {
                SOAPWebService webService = new SOAPWebService(mContext);
                String attendance = "ALL";
                DecimalFormat mFormat = new DecimalFormat("00");
                String Month = params[0].toString(); //String.valueOf(mFormat.format(Double.valueOf(month)));
                String Year = params[1].toString();//String.valueOf(year);
                if (Month.length() == 1)
                    Month = "0" + Month;
                object = webService.Attendence_Report_Monthwise(sharedPref.getIsRM(), sharedPref.getLoginId(), Month, Year, attendance);//Month
              /*  statusReportModel.getStatus_1()*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                responseId = String.valueOf(soapObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            attendenceReportModelList.clear();

            try {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                    SoapObject root = (SoapObject) soapObject.getProperty(i);
//                    attendenceReportModel = new AttendenceReportModel();
                    String attendance = "", insertdate = "", latitude = "", longitude = "", name = "";

                    if (root.getProperty("attendance1") != null && !root.getProperty("attendance1").toString().equalsIgnoreCase("?")) {

                        if (!root.getProperty("attendance1").toString().equalsIgnoreCase("anyType{}")) {
                            attendance = root.getProperty("attendance1").toString();
//                            attendenceReportModel.setAttendance(attendance);


                        } else {
                            attendance = "";
                        }
                    } else {
                        attendance = "";
                    }

                    if (root.getProperty("insertdate") != null && !root.getProperty("insertdate").toString().equalsIgnoreCase("?")) {

                        if (!root.getProperty("insertdate").toString().equalsIgnoreCase("anyType{}")) {
                            insertdate = root.getProperty("insertdate").toString();
//                            attendenceReportModel.setInsertdate(insertdate);

                        } else {
                            insertdate = "";
                        }
                    } else {
                        insertdate = "";
                    }

                    if (root.getProperty("latitude") != null) {

                        if (!root.getProperty("latitude").toString().equalsIgnoreCase("anyType{}")) {
                            latitude = root.getProperty("latitude").toString();
//                            attendenceReportModel.setLatitude(latitude);

                        } else {
                            latitude = "";
                        }
                    } else {
                        latitude = "";
                    }
                    if (root.getProperty("longitude") != null) {

                        if (!root.getProperty("longitude").toString().equalsIgnoreCase("anyType{}")) {
                            longitude = root.getProperty("longitude").toString();
//                            attendenceReportModel.setLongitude(longitude);


                        } else {
                            longitude = "";
                        }
                    } else {
                        longitude = "";
                    }
                    if (root.getProperty("name") != null) {

                        if (!root.getProperty("name").toString().equalsIgnoreCase("anyType{}")) {
                            name = root.getProperty("name").toString();
//                            attendenceReportModel.setNameAttendence(name);

                        } else {
                            name = "";
                        }
                    } else {
                        name = "";
                    }

                    attendenceReportModelList.add(attendenceReportModel);

                }
                if (attendenceReportModelList != null && attendenceReportModelList.size() > 0) {
                    eventsPerMonthMap = findNumberOfEventsPerMonth((ArrayList<AttendenceReportModel>) attendenceReportModelList);
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Initialised
                adapter = new GridCellAdapter(getApplicationContext(),
                        R.id.calendar_day_gridcell, month, year);
                adapter.notifyDataSetChanged();
                calendarView.setAdapter(adapter);

            }


        }
    }
}
