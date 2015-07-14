package com.jaApps.quotes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.jaApps.quotes.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

@SuppressWarnings("deprecation")
class Quotes
{
	public String msg;
	public int id;
	public String id_s;
}

public class QuoteActivityMain extends Activity  {
	public JSONArray jArray;
	public TextView text_quote;
	public String str_quote;
	public int currentQuoteId = 0;
	public int quoteIndex = 0;
	public boolean isQuoteAvailable;
	// Animation
	public Animation animFadein;
	public Animation animFadein2;
	public Animation animLeftToRight;	
	public Animation animRightToLeft;

	// FOR TOUCH EVENT SWIPE DETECTION
	private float x1,x2;
	public boolean toLeft = true;
	static final int MIN_DISTANCE = 150;
	static final int BTN_FONT_SIZE = 24;

	static final int TOTAL_QUOTE_SIZE = 100;
	static final int INVALID_ID = -11;

	public int quoteIndexArray[];
	public int favQuotes[];
	public Button button_favorite;
	@SuppressWarnings("deprecation")
	public SlidingDrawer slidingDrawer;
	Button slideHandleButton;

	public boolean viewFav = false;
	public int favIndex = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN : x1 = event.getX();
		break;
		case MotionEvent.ACTION_UP : x2 = event.getX();
		float deltaX = x2 - x1;
		if (deltaX < 0) 
			toLeft = true;
		else
			toLeft = false;
		if(Math.abs(deltaX) > MIN_DISTANCE)
		{
			//		Toast.makeText(this, "swiped", Toast.LENGTH_SHORT).show();
			try {
				//putQuote();
				putQuote_xml();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			// consider as something else for ex:swipe up
		}
		break;
		}
		return super.onTouchEvent(event);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote_activity_main);

		str_quote = "";
		text_quote = (TextView) findViewById(R.id.textView_text);
		//text_quote.setTextColor(Color.WHITE);	

		// initialize favQuotes array with zeros
		favQuotes = new int[100];
		for(int i=0 ; i < TOTAL_QUOTE_SIZE; i++)
			favQuotes[i] = 0;
		quoteIndexArray = new int[100];
		for(int i=0 ; i < TOTAL_QUOTE_SIZE; i++)
		{
			Random rand = new Random();
			quoteIndexArray[i] = rand.nextInt() % TOTAL_QUOTE_SIZE;
			if (quoteIndexArray[i] < 0) quoteIndexArray[i] *= -1;

		}

		try {
			fillFavQuote();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// LOAD THE FONT SAVED IN asset directory
		//		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/DancingScript-Regular.otf"); 
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Aller_Rg.ttf");

		slideHandleButton = (Button) findViewById(R.id.slideHandleButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);

		Button btn_fav = (Button) findViewById(R.id.Button01);
		btn_fav.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				slidingDrawer.animateClose();
				RelativeLayout rel_layout = (RelativeLayout)findViewById(R.id.rel_layout);
				Button btnFav = (Button)findViewById(R.id.Button01);
				rel_layout.startAnimation(animFadein);
				if(viewFav == false){
					rel_layout.setBackgroundResource(R.drawable.red_bg2);
					btnFav.setText("Quotes ");
					viewFav = true;
					favIndex = 0;		
					//text_quote.setTextColor(Color.WHITE);
				}
				else{
					rel_layout.setBackgroundResource(R.drawable.blue_bg);
					btnFav.setText("Favorites ");
					viewFav = false;
				//	text_quote.setTextColor(Color.WHITE);
				}
				try {
					//putQuote();
					putQuote_xml();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btn_fav.setTypeface(type);
		btn_fav.setTextSize(BTN_FONT_SIZE);

		Button btn_info = (Button) findViewById(R.id.Button02);
		btn_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				TextView info = (TextView)findViewById(R.id.textView_info_help);
				info.setText("An app to display inspirational quotes which helps to make the mind peaceful." +
						" Developed by jaApps"
						);
				info.startAnimation(animFadein2);
			}
		});
		btn_info.setTypeface(type);
		btn_info.setTextSize(BTN_FONT_SIZE);

		Button btn_abt = (Button) findViewById(R.id.Button03);
		btn_abt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TextView abt = (TextView)findViewById(R.id.textView_info_help);
				abt.setText("Swipe to LEFT or RIGHT to get new Quotes. " +
						"Click on heart icon to make a quote as favorite quote. All favorite quotes can be viewed from the favorite menu." +
						"Share icon at the bottom will share the quote with your social network. " +
						""

						);
				abt.startAnimation(animFadein2); 
			}
		});
		btn_abt.setTypeface(type);
		btn_abt.setTextSize(BTN_FONT_SIZE);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			public void onDrawerOpened() {
				slideHandleButton.setBackgroundResource(R.drawable.openarrow);
			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			public void onDrawerClosed() {
				slideHandleButton.setBackgroundResource(R.drawable.closearrow);
				TextView info = (TextView)findViewById(R.id.textView_info_help);
				info.setText("");
			}
		});

		// load the animation
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);
		animFadein2 = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in2);
		animLeftToRight = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.translate_left_to_right);
		animRightToLeft = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.translate_right_to_left);

		// set animation listener
		animFadein.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// check for fade in animation
				if (animation == animFadein) {
					//		Toast.makeText(getApplicationContext(), "Animation Stopped",
					//			Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
		// set animation listener
		animFadein2.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});


		// set animation listener
		animLeftToRight.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// check for fade in animation

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});

		animRightToLeft.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// check for fade in animation

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});

		// TEXT VIEW PARAMTERS
		TextView text = (TextView) findViewById(R.id.textView_text);
		text.setTypeface(type);
		text.setTextSize(32);

		// SHARE BUTTON
		Button button_share = (Button) findViewById(R.id.button_share);
		button_share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				String shareBody = (String) text_quote.getText();
				//		    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Quotes");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));

			}

		});

		// FAVORITE BUTTON
		button_favorite = (Button) (TextView) findViewById(R.id.button_favorite);
		button_favorite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if( currentQuoteId == INVALID_ID)
					return;
				if(favQuotes[currentQuoteId] == 0){
					favQuotes[currentQuoteId] = 1;
					try {
						addquoteToFav(currentQuoteId);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					favQuotes[currentQuoteId] = 0;
					try {
						removequoteFromFav(currentQuoteId);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if(favQuotes[currentQuoteId] == 1)
					button_favorite.setBackgroundResource(R.drawable.favorite);
				else
					button_favorite.setBackgroundResource(R.drawable.not_favorite);


			}
		});
		/*
		 // NEXT BUTTON
		Button button_next = (Button) (TextView) findViewById(R.id.button_next);
		button_next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				try {
					//putQuote();
					putQuote_xml();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		 */		
		// WHEN APP IS STARTED THE BELOW PORTION OF CODE GETS CALLED TO GET THE FIRST QUOTE 
		try {
			//			putQuote();
			putQuote_xml();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ADDS
		AdView newAdview = (AdView)findViewById(R.id.adView);
        //AdRequest newAdReq = new AdRequest.Builder().build();
        AdRequest newAdReq = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("548C643D6A36F2D96EE1BD44A4CB5794").build();
        //AdRequest newAdReq = new AdRequest.Builder().addTestDevice("548C643D6A36F2D96EE1BD44A4CB5794").build();
        newAdview.loadAd(newAdReq);
	}
	private void addquoteToFav(int id) throws IOException
	{
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File picDirectory = new File(dir+"/.quotes");
		picDirectory.mkdirs();
		//Log.d("JITZ","path is "+dir);
		BufferedWriter writer = new BufferedWriter(new FileWriter(dir+"/.quotes/fav.txt",true));
		writer.write(Integer.toString(id));
		writer.newLine();
		writer.close();
	}
	private void removequoteFromFav(int id) throws IOException{
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.quotes/";
		File inputFile = new File(dir+ "fav.txt");
		File tempFile = new File(dir+"favtmp.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String lineToRemove = Integer.toString(id);
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			// trim newline when comparing with lineToRemove
			String trimmedLine = currentLine.trim();
			if(trimmedLine.equals(lineToRemove)) continue;
			writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		tempFile.renameTo(inputFile);

	}
	private void fillFavQuote () throws IOException {
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.quotes/";
		File inputFile = new File(dir+ "fav.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();

			favQuotes[Integer.parseInt(trimmedLine)] = 1;
		}
		reader.close();
	}
	public void putQuote_xml() throws InterruptedException {

		int id = 0;
		text_quote.setText("Cannot Display Quotes Right Now;");
		// TODO optimise the koothara code below
		if(viewFav == true){
			int count = 0;

			do{
				if(toLeft ==true)
					favIndex++;
				else 
					favIndex--;

				if(favIndex >= TOTAL_QUOTE_SIZE)
					favIndex = 0;
				else if (favIndex < 0)
					favIndex = TOTAL_QUOTE_SIZE - 1;

				id = favIndex;
				count++;
			}while(favQuotes[id] == 0 && count <= TOTAL_QUOTE_SIZE );
			if(favQuotes[id] == 0) {
				text_quote.setText("click on the love symbol to set your favorites");
				currentQuoteId = INVALID_ID;
				return;
			}
		}else{
			if(toLeft == true)
				quoteIndex++;
			else 
				quoteIndex--;


			if (quoteIndex >= TOTAL_QUOTE_SIZE)
				quoteIndex = 0;
			else if ( quoteIndex < 0)
				quoteIndex = TOTAL_QUOTE_SIZE - 1;
			id = quoteIndexArray[quoteIndex];

		}

		// this function gets the quote from an internal xml file "file:///android_asset/data/quotes.xml
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			InputStream in_s = getApplicationContext().getAssets().open("data/quotes.xml");
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_s, null);

			parseXML(parser,id);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void parseXML(XmlPullParser parser, int id) throws XmlPullParserException,IOException
	{

		ArrayList<Quotes> products = null;
		int eventType = parser.getEventType();
		Quotes currentQuote = null;

		while (eventType != XmlPullParser.END_DOCUMENT){			
			String name = null;
			switch (eventType){
			case XmlPullParser.START_DOCUMENT:
				products = new ArrayList<Quotes>();
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();	

				if (name.equalsIgnoreCase("quotes")){
					currentQuote = new Quotes();
				} else if (currentQuote != null){
					if (name.equalsIgnoreCase("msg")){

						currentQuote.msg = parser.nextText();
						//Log.d("JITZ_LOG",currentQuote.msg);
					} else if (name.equalsIgnoreCase("id")){

						currentQuote.id_s = parser.nextText();
						currentQuote.id = Integer.parseInt(currentQuote.id_s);
						if (id == currentQuote.id) {
							text_quote.setTextColor(Color.WHITE);	
							text_quote.setText(currentQuote.msg);
							currentQuoteId = id;
							// for animation
							if(toLeft)
								text_quote.startAnimation(animRightToLeft);
							else
								text_quote.startAnimation(animLeftToRight);
							if(favQuotes[currentQuoteId] == 1)
								button_favorite.setBackgroundResource(R.drawable.favorite);
							else
								button_favorite.setBackgroundResource(R.drawable.not_favorite);
						}
					}
				}

				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("quote") && currentQuote != null){
					products.add(currentQuote);
				} 
			}

			eventType = parser.next();
		}
	}
	public void putQuote() throws InterruptedException {
		// TODO Auto-generated method stub
		isQuoteAvailable = false;
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				int id =2;
				String result = "";
				InputStream is = null;
				//the year data to send
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id",""+id));
				//http post
				try{
					HttpClient httpclient = new DefaultHttpClient();
					//HttpPost httppost = new HttpPost("http://192.168.137.196/storytime/req_story.php");
					HttpPost httppost = new HttpPost("http://192.168.1.7/storytime/req_story2.php");
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

					for(int i=0;i<jArray.length();i++){

						JSONObject json_data = jArray.getJSONObject(i);
						Log.i("log_tag","quote: "+json_data.getString("quote")+""
								//", story: "+json_data.getString("quote")
								);
						str_quote = json_data.getString("quote");
						isQuoteAvailable = true;

					}
				}
				catch(JSONException e){
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
		}).start();

		int iteration = 10;
		while(iteration-- != 0){
			if(isQuoteAvailable)
				break;
			else {
				Log.d("JITZ_QUOTES","sleep for 100 ms");
				Thread.sleep(100);
			}
		}
		text_quote.setTextSize(getFontSize(str_quote.length()));
		if (iteration > 0)
			text_quote.setText(str_quote);
		else
			text_quote.setText("Cannot Display Quotes Right Now");
	}
	public int getFontSize(int size)
	{
		int minSize = 20;    
		int ret_size = minSize;

		if( size < 45 )
			ret_size = 55;
		else
		{
			int fontSize = 55;
			size -= 45;
			while (size > 0)
			{
				fontSize -= 5;
				if( fontSize < minSize)
				{
					fontSize = minSize;
					break;
				}
				size -= 10;
			}
			ret_size = fontSize;
		}

		return ret_size;
	}
}
