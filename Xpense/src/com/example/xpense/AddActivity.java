package com.example.xpense;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddActivity extends Activity {
	
	String[] strings = {"tax", "insurance", "car", "travel", "entertainment"};
	int arr_images[] = {R.drawable.tax, R.drawable.insurance, R.drawable.car,
						R.drawable.travel, R.drawable.entertainment};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		Spinner mySpinner = (Spinner)findViewById(R.id.spinner1);
		mySpinner.setAdapter(new myAdapter(AddActivity.this, R.layout.spinner_row, strings));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class myAdapter extends ArrayAdapter<String>{
		public myAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
        }
		
		@Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
		
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        
        public View getCustomView(int position, View convertView, ViewGroup parent) {
        	 
            LayoutInflater inflater=getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label = (TextView)row.findViewById(R.id.textViewExpense);
            label.setText(strings[position]);
 
            //	TextView sub=(TextView)row.findViewById(R.id.sub);
        	//  sub.setText(subs[position]);
 
            ImageView icon =(ImageView)row.findViewById(R.id.image);
            icon.setImageResource(arr_images[position]);
 
            return row;
        }
	}
	
}
