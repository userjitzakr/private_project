package com.example.bedtimestory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Context context;
	public JSONArray jArray;
	private Boolean err ;
	private Boolean data_fetch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		err = false;
		data_fetch = false;
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		new Thread(new Runnable() {

			@Override
			public void run() {
				data_fetch = false;
				String result = "";
				InputStream is = null;
				//the year data to send
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id","1"));
				//http post
				try{
					HttpClient httpclient = new DefaultHttpClient();
					// ip address in lumia network
			//		HttpPost httppost = new HttpPost("http://192.168.137.196/storytime/req.php");
					// ip address in akhils mts wifi network 192.168.1.102
					HttpPost httppost = new HttpPost("http://192.168.1.102/storytime/req.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				}catch(Exception e){
					Log.e("log_tag", "Error in http connection "+e.toString());
				}
				//convert response to string
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();

					result=sb.toString();
				}catch(Exception e){
					Log.e("log_tag", "Error converting result "+e.toString());
				}

				//parse json data
				try{
					jArray = new JSONArray(result);
					data_fetch = true;
					Log.v("jithin","data is fetched");
				}
				catch(JSONException e){
					Log.e("log_tag", "Error parsing data "+e.toString());
					err = true;
				}
			}

		}).start();

		if (err != true) {

			try {
				while(data_fetch != true);
				int prevId = 0;
				for(int i=0;i<jArray.length();i++){
					
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag","id: "+json_data.getInt("id")+
							", title: "+json_data.getString("title")
							);

					Button btn = new Button(context);
					btn.setId(json_data.getInt("id"));
					btn.setText(json_data.getString("title"));
					btn.setLayoutParams(new LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					LinearLayout l_layout = (LinearLayout) findViewById(R.id.ll); 
					l_layout.setOrientation(LinearLayout.VERTICAL);
					l_layout.addView(btn);
					btn.setOnClickListener(new View.OnClickListener() {
			            public void onClick(View view) {
			                Log.v("jithin","clicked " + view.getId());
			                Intent myIntent = new Intent(view.getContext(), StoryActivity.class);
			              //Sending data to another Activity
			                myIntent.putExtra("id",  view.getId());
			                startActivityForResult(myIntent, 0);
			                
			                
			            }
			        });
					prevId = json_data.getInt("id");
				}
			}
			catch (JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}


		}
	}
}
