package com.jaApps.abdulkalamquotes;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

//Admob
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;



@SuppressWarnings("deprecation")
class Quotes
{
	public String msg;
	public int id;
	public String id_s;
}
class Constants {
	// Please make sure the ENV value is qa, this token will be replaced at compile time (both in ant and eclipse)
	// If you want to use this in development and customize the ENV, go change the build_replace_env.xml and build_restore_env.xml 
	// builders
	public static int ENV_CONFIGURATIONS = R.raw.slots;

	public static final String AD_FORMAT_KEY = "kIMAdMAdFormat";
	public static final String TITLE_KEY = "kIMAdMAdTitle";
	public static final String TYPE_KEY = "kIMAdMAdType";
}
public class QuoteActivity extends Activity  {
	public JSONArray jArray;
	public TextView text_quote;
	public String str_quote;
	public int currentQuoteId = 0;
	public int quoteIndex = 0;
	public int img_cnt = 0;
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

	static final int TOTAL_QUOTE_SIZE = 50;
	static final int TOTAL_BG_IMG_SIZE = 14;
	static final int INTERSTITIAL_ADD_DISPLAY_COUNT = 10;
	static final int INVALID_ID = -11;
	public static final int AD_REQUEST_SUCCEEDED = 101;
	public static final int AD_REQUEST_FAILED = 102;
	public static final int ON_SHOW_MODAL_AD = 103;
	public static final int ON_DISMISS_MODAL_AD = 104;
	public static final int ON_LEAVE_APP = 105;
	public static final int ON_CLICK = 106;

	public int quoteIndexArray[];
	public int favQuotes[];
	public Button button_favorite;
	@SuppressWarnings("deprecation")
	public SlidingDrawer slidingDrawer;
	Button slideHandleButton;

	public boolean viewFav = false;
	public int favIndex = 0;
	int retry =10;
	int quoteCount = 0;

	private InterstitialAd interstitial;

	//database to store the data
	ArrayList<Quotes> quoteBase = null;
	XmlPullParserFactory pullParserFactory;
	XmlPullParser parser;
	InputStream in_s;
	boolean dataLoaded = false;

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
				if(dataLoaded == true)
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
	}/*
	private LayoutParams getLayoutParams(String adFormat){
		final float scale = getResources().getDisplayMetrics().density;
		adFormat = adFormat.replace("{", "");
		adFormat = adFormat.replace("}", "");
		String[] vals = adFormat.split(",",2);
		int width = (int) (Integer.parseInt(vals[0]) * scale + 0.5f);
		int height = (int) (Integer.parseInt(vals[1]) * scale + 0.5f);		
		return new LinearLayout.LayoutParams(width, height);
	}*/


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote);

		// load data
		// this function gets the quote from an internal xml file "file:///android_asset/data/quotes.xml

		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			parser = pullParserFactory.newPullParser();

			in_s = getApplicationContext().getAssets().open("data/apj_quotes.xml");
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_s, null);

			parseXML_toLoad(parser,0);

		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScrollView scroll = (ScrollView) findViewById(R.id.scrl_quesion);
		scroll.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override	
			public boolean onTouch(View v, MotionEvent event) {
				onTouchEvent(event);
				return false;
			}
		});

		//InMobi.initialize(this, getResources().getString(R.string.inmobi_property_id));
		str_quote = "";
		text_quote = (TextView) findViewById(R.id.textView_text);
		//text_quote.setTextColor(Color.WHITE);	

		// initialize favQuotes array with zeros
		favQuotes = new int[TOTAL_QUOTE_SIZE];
		for(int i=0 ; i < TOTAL_QUOTE_SIZE; i++)
			favQuotes[i] = 0;
		quoteIndexArray = new int[TOTAL_QUOTE_SIZE];
		int idx = 0;
		for(int i=0 ; i < TOTAL_QUOTE_SIZE; i++)
		{
			Random rand;
			do {
				rand = new Random();
				idx = rand.nextInt() % TOTAL_QUOTE_SIZE;
				if(idx<0) idx *= -1;
			}
			
			while(isIndexAlreadyPresent(idx, i));	
			quoteIndexArray[i] = idx;
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
				//rel_layout.startAnimation(animFadein);
				if(viewFav == false){
					rel_layout.setBackgroundColor(Color.BLACK);
					btnFav.setText("Quotes ");
					viewFav = true;
					favIndex = 0;		
					//text_quote.setTextColor(Color.WHITE);
				}
				else{
					rel_layout.setBackgroundColor(Color.BLACK);
					btnFav.setText("Favorites ");
					viewFav = false;
					//	text_quote.setTextColor(Color.WHITE);
				}
				try {
					//putQuote();
					if(dataLoaded == true)
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
				info.setText("Avul Pakir Jainulabdeen Abdul Kalam (15 October 1931 – 27 July 2015) was the 11th President of India from 2002 to 2007. A career scientist turned reluctant politician, Kalam was born and raised in Rameswaram, Tamil Nadu and studied physics and aerospace engineering. He spent the next four decades as a scientist and science administrator, mainly at the Defence Research and Development Organisation (DRDO) and Indian Space Research Organisation (ISRO) and was intimately involved in India's civilian space program and military missile development efforts. He thus came to be known as the Missile Man of India for his work on the development of ballistic missile and launch vehicle technology."+ 
"Kalam was elected President of India in 2002 with the support of both the ruling Bharatiya Janata Party and the opposition Indian National Congress. After serving a term of five years, he returned to his civilian life of education, writing and public service. He received several prestigious awards, including the Bharat Ratna, India's highest civilian honour."+
"This app contains few of his thoughts. It will help us to reach success."
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
			if(dataLoaded == true)
				putQuote_xml();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/******************************* ADVERTISEMENT SECTION********************/
		//ADDS
		final AdView newAdview = (AdView)findViewById(R.id.adView);
		//final AdRequest newAdReq = new AdRequest.Builder().build();
		final AdRequest newAdReq = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("548C643D6A36F2D96EE1BD44A4CB5794").addTestDevice("1AC7C85821FE0F8206BE8179465240FD").build();
		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(QuoteActivity.this);
		// Insert the Ad Unit ID
		interstitial.setAdUnitId(getResources().getString(R.string.ad_Unit_ID_interstitial));
		// Prepare an Interstitial Ad Listener
		interstitial.setAdListener(new AdListener() {

			public void onAdLoaded() {
				Log.d("JKS","add loaded");
				// Call displayInterstitial() function
				displayAdd();

			}
		});

		newAdview.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
				Log.d("JKS","onAdLoaded");
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
				// Code to be executed when an ad request fails.

				Log.d("JKS","onAdFailedToLoad error= "+errorCode);
				
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
				Log.d("JKS","onAdOpened");
			}

			@Override
			public void onAdLeftApplication() {
				// Code to be executed when the user has left the app.
				Log.d("JKS","onAdLeftApplication");
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when when the user is about to return
				// to the application after tapping on an ad.
				Log.d("JKS","onAdclosed");
			}
		});

		newAdview.loadAd(newAdReq);
		/******************************* ADVERTISEMENT SECTION********************/

	}
	boolean isIndexAlreadyPresent(int index, int size)
	{
		for(int i=0;i<size;i++){
			if(quoteIndexArray[i] == index)
			{
				Log.d("JKS","Already added "+index);
				return true;
				
			}
		}
		return false;
	}
	private void displayAdd()
	{
		if (interstitial.isLoaded()) {
			interstitial.show();
		}

	}
	private void addquoteToFav(int id) throws IOException
	{
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File picDirectory = new File(dir+"/.quotes");
		picDirectory.mkdirs();
		//Log.d("JITZ","path is "+dir);
		BufferedWriter writer = new BufferedWriter(new FileWriter(dir+"/.quotes/fav_apj.txt",true));
		writer.write(Integer.toString(id));
		writer.newLine();
		writer.close();
	}
	private void removequoteFromFav(int id) throws IOException{
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.quotes/";
		File inputFile = new File(dir+ "fav_apj.txt");
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
		File inputFile = new File(dir+ "fav_apj.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
			String trimmedLine = currentLine.trim();

			favQuotes[Integer.parseInt(trimmedLine)] = 1;
		}
		reader.close();
	}
	public void putQuote_xml() throws InterruptedException {

		if(dataLoaded ==false)
		{
			Log.d("JKS","data is not loaded; return");
			return;
		}
		int id = 0;
		quoteCount++;
		if(quoteCount % INTERSTITIAL_ADD_DISPLAY_COUNT == 0)
		{

			//AdRequest newAdReq = new AdRequest.Builder().build();
			AdRequest newAdReq = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("548C643D6A36F2D96EE1BD44A4CB5794").build();

			// Load ads into Interstitial Ads
			interstitial.loadAd(newAdReq);
		}
		
		String bg_img = "img_bg_"+img_cnt%TOTAL_BG_IMG_SIZE;
		img_cnt++;
		ImageView img_bg = (ImageView)findViewById(R.id.img_bg);
		Resources res = getResources();
		int resID = res.getIdentifier(bg_img , "drawable", getPackageName());
		img_bg.setImageResource(resID);
		img_bg.setScaleType(ScaleType.FIT_XY);
		img_bg.startAnimation(animFadein);
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
		Quotes quote = quoteBase.get(id);
		text_quote.setTextColor(Color.WHITE);	
		text_quote.setText(quote.msg);
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
	private void parseXML_toLoad(XmlPullParser parser, int id) throws XmlPullParserException,IOException
	{
		if(dataLoaded == true) return;
		dataLoaded = true;
		int eventType = parser.getEventType();
		Quotes currentQuote = null;
		while (eventType != XmlPullParser.END_DOCUMENT){			
			String name = null;
			switch (eventType){
			case XmlPullParser.START_DOCUMENT:
				quoteBase = new ArrayList<Quotes>();
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();	

				if (name.equalsIgnoreCase("quote")){
					currentQuote = new Quotes();
				} else if (currentQuote != null){
					if (name.equalsIgnoreCase("msg")){

						currentQuote.msg = parser.nextText();
						//Log.d("JITZ_LOG",currentQuote.msg);
					} else if (name.equalsIgnoreCase("id")){

						currentQuote.id_s = parser.nextText();
						currentQuote.id = Integer.parseInt(currentQuote.id_s);

					}
				}

				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("quote") && currentQuote != null){
					quoteBase.add(currentQuote);
				} 
			}

			eventType = parser.next();
		}
		dataLoaded = true;

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
