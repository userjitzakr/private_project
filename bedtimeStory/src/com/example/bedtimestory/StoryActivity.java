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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StoryActivity extends Activity {
	public JSONArray jArray;
	public int id;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storypage);
		Intent story = getIntent();
		id = story.getIntExtra("id",0);
		Log.v("jithin", "ID is "+id);
		new Thread(new Runnable() {

			@Override
			public void run() {

				String result = "";
				InputStream is = null;
				//the year data to send
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id",""+id));
				//http post
				try{
					HttpClient httpclient = new DefaultHttpClient();
					//HttpPost httppost = new HttpPost("http://192.168.137.196/storytime/req_story.php");
					HttpPost httppost = new HttpPost("http://192.168.1.102/storytime/req_story.php");
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
				Log.v("jithin","data is responded");
				//parse json data
				try{
					jArray = new JSONArray(result);
					Log.v("jithin","data is fetched");
					for(int i=0;i<jArray.length();i++){

						JSONObject json_data = jArray.getJSONObject(i);
						Log.i("log_tag","id: "+json_data.getInt("id")+""
						//		", story: "+json_data.getString("story")
								);
						TextView txtView_title = (TextView)findViewById(R.id.textView_title);
						TextView txtView_story = (TextView)findViewById(R.id.textView_story);
						txtView_title.setText(json_data.getString("title"));
						txtView_story.setText(json_data.getString("story"));

					}
				}
				catch(JSONException e){
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}

		}).start();

		

	}
}
