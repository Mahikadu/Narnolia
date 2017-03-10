package com.narnolia.app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.narnolia.app.dbconfig.DbHelper;
import com.narnolia.app.network.LoginWebService;

import org.ksoap2.serialization.SoapObject;

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
	private ProgressDialog progressDialog;
	private String result = "";
	@SuppressLint("NewApi")
	private int month, year;
	@SuppressWarnings("unused")
	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" })
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";
	private Dialog dialog;
	public SharedPref pref;
	private Context mContext;
	private String strAttendance = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_calendar_view);
		mContext = MyCalendarActivity.this;
		progressDialog = new ProgressDialog(mContext);
		pref = new SharedPref(mContext);
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: "
				+ year);

		/*selectedDayMonthYearButton = (Button) this
				.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");*/

		prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate,
				_calendar.getTime()));

		nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) this.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(getApplicationContext(),
				R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	/**
	 *
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
			setGridCellAdapterToDate(month, year);
		}

	}

	@Override
	public void onDestroy() {
		Log.d(tag, "Destroying View ...");
		super.onDestroy();
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue",
				"Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
				31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
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
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
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
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
																	int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
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
			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row
							.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
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

				if (!currDate.equalsIgnoreCase(selDate)){
					Toast.makeText(_context, "PLease, select current date only..!", Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(_context, "current date ..!", Toast.LENGTH_SHORT).show();
					show_attendence();
 //					open();
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//....................popup attendence ........................

		public void open(){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyCalendarActivity.this);
			alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");
					alertDialogBuilder.setPositiveButton("yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									Toast.makeText(_context,"You clicked yes button",Toast.LENGTH_LONG).show();
								}
							});

			alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
 				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		private void show_attendence(){
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
			final String[] columns = new String[] { "emp_id", "Adate",
					"attendance","absent_type", "lat", "lon", "savedServer", "month",
					"holiday_desc", "year" };

			present.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if(isConnectingToInternet())
					{
						gridcell.setBackgroundColor(getResources().getColor(R.color.border_color));
						/*Intent intent = new Intent(MyCalendarActivity.this, HomeActivity.class);
						startActivity(intent);*/
						strAttendance = "P";
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

					}
					else
					{
						Toast.makeText(_context, "Please check internet Connectivity & Try Again", Toast.LENGTH_LONG).show();
					}

				}
			});

			absent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					strAttendance = "A";
					if(isConnectingToInternet())
					{
 						// TODO Auto-generated method stub

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

			SoapObject object1 = webService.LoginLead(pref.getLoginId(), pref.getUserPass(),pref.getApp(), pref.getVersion_name(),pref.getLat(),pref.getLang(),"P",pref.getCurrentDate());

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
					insertDataInDb(strAttendance);

                   /* pushActivity(LoginActivity.this, HomeActivity.class, null, true);*/
					pushActivity(MyCalendarActivity.this,HomeActivity.class,null,true);
					pref.setSharedPrefLoginWithPass(pref.getLoginId(), pref.getUserPass(),pref.getStatus(), "App", pref.getVersion_name(), pref.getLat(), pref.getLang(),strAttendance,pref.getCurrentDate());

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
}
