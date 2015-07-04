package com.example.xpense;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {

	CalendarView calendarMain;
	String selectedCaledarDate;
	SQLiteDatabase db;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
/*        ActionBar actionbar = getActionBar();
        //set icon
        actionbar.setIcon(R.drawable.ic_action_name);*/
        
      //initializes the calendar view
        initializeCalendar();
        
      //create table
/*        db = openOrCreateDatabase("ExpenseDB", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS ExpenseDB.Expense;");
        db.execSQL("CREATE TABLE IF NOT EXISTS Expense (id INTEGER PRIMARY KEY,type INTEGER,amount INTEGER,date VARCHAR);");
        db.execSQL("INSERT INTO Expense(type,amount,date) VALUES(1,126,'2015-06-16');");
        
        Cursor c = db.rawQuery("SELECT * FROM Expense", null);
        if(c.getCount() == 0)
        {
        	showMessage("Error", "no recs found");
        	return;
        }
        else
        {
        	StringBuffer bfr = new StringBuffer();
        	while(c.moveToNext())
        	{
        		bfr.append("id : "+c.getString(0)+"\n");
        		bfr.append("type : "+c.getString(1)+"\n");
        		bfr.append("amount : "+c.getString(2)+"\n");
        		bfr.append("date : "+c.getString(3)+"\n");
        	}
        	showMessage("Detais", bfr.toString());
        }*/
    }
        


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
        switch (item.getItemId()) {
			case R.id.action_add:
				Intent intent = new Intent(this, AddActivity.class);
				this.startActivity(intent);
			return true;
			
			case R.id.action_settings:
			Toast.makeText(getBaseContext(), "U selected Settings", Toast.LENGTH_SHORT).show();
			return true;
			
			default:
				return super.onOptionsItemSelected(item);
				
		}
    }
    
    public void initializeCalendar(){
    	
    	calendarMain = (CalendarView)findViewById(R.id.calendarViewMain);
    	
    	// sets whether to show the week number.
    		calendarMain.setShowWeekNumber(false);
    	// sets the first day of week according to Calendar.
    	// here we set Monday as the first day of the Calendar
    		calendarMain.setFirstDayOfWeek(1);
    	//The background color for the selected week.
    		calendarMain.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
    	//sets the color for the dates of an unfocused month. 
    		calendarMain.setUnfocusedMonthDateColor(getResources().getColor(R.color.grey));
    	//sets the color for the separator line between weeks.
    		calendarMain.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
    	//sets the color for the vertical bar shown at the beginning and at the end of the selected date.
    		calendarMain.setSelectedDateVerticalBar(R.color.green);
    	//sets the listener to be notified upon selected date change.
    		calendarMain.setOnDateChangeListener(new OnDateChangeListener() {
				
				@Override
				public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
					// TODO Auto-generated method stub
					selectedCaledarDate = year + "-";
					month += 1;
					if(month < 10){
						selectedCaledarDate += "0" + month + "-";
					}
					else {
						selectedCaledarDate += month + "-";
					}
					if(dayOfMonth < 10){
						selectedCaledarDate += "0" + dayOfMonth;
					}
					else {
						selectedCaledarDate += dayOfMonth;
					}
					Toast.makeText(getApplicationContext(), selectedCaledarDate, Toast.LENGTH_SHORT).show();
				}
			});
    }
    
    public void showMessage(String title,String message)
    {
    	Builder builder=new Builder(this);
    	builder.setCancelable(true);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.show();
	}
    
    private String getDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        //Date newDate = new Date();
        return dateFormat.format(date);
    }
}
