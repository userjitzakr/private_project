package com.jaApps.cquiz;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

class Questions
{
	public int catagory;
	public int no;
	public String question;
	public String optiona;
	public String optionb;
	public String optionc;
	public String optiond;
	public int info;
	public String data;
	public int answer;

}

@SuppressLint("ClickableViewAccessibility")
public class QuizActivity extends Activity {	

	// FOR TOUCH EVENT SWIPE DETECTION
	private float x1,x2;
	public boolean toLeft = true;
	static final int MIN_DISTANCE = 150;

	//XML data for loading questions
	XmlPullParserFactory pullParserFactory;
	XmlPullParser parser;
	InputStream in_stream;
	ArrayList<Questions> ques;
	public String questionFile;
	public int no_of_quetions;
	public int gQuesIndex;

	//questions
	TextView txt_qno;
	TextView txt_question;
	TextView txt_optionA;
	TextView txt_optionB;
	TextView txt_optionC;
	TextView txt_optionD;

	Button btn_info;
	public String infoData;

	//option selection images
	ImageView img_optiona;
	ImageView img_optionb;
	ImageView img_optionc;
	ImageView img_optiond;

	// answer selection
	public boolean selected;
	public int selected_option;
	public boolean isGreen;

	// Animations
	public Animation animLeftToRight;	
	public Animation animRightToLeft;

	//ads
	int clicks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		selected = false;
		selected_option = 0;
		isGreen = false;
		clicks = 0;

		btn_info = (Button) findViewById(R.id.btn_info);
		btn_info.setVisibility(View.INVISIBLE);

		ScrollView scroll = (ScrollView) findViewById(R.id.scrl_quesion);
		scroll.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				screenTouch(v,event);
				return false;
			}
		});
		animLeftToRight = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.translate_left_to_right);
		animRightToLeft = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.translate_right_to_left);
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


		questionFile = getResources().getString(R.string.quizfile);


		TextView txt_sub = (TextView)findViewById(R.id.subject);
		txt_sub.setText("C Quiz");


		txt_optionA = (TextView)findViewById(R.id.txt_optiona);
		txt_optionB = (TextView)findViewById(R.id.txt_optionb);
		txt_optionC = (TextView)findViewById(R.id.txt_optionc);
		txt_optionD = (TextView)findViewById(R.id.txt_optiond);

		img_optiona = (ImageView)findViewById(R.id.img_optiona);
		img_optionb = (ImageView)findViewById(R.id.img_optionb);
		img_optionc = (ImageView)findViewById(R.id.img_optionc);
		img_optiond = (ImageView)findViewById(R.id.img_optiond);

		img_optiona.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;

				//				Toast.makeText(getApplicationContext(), "option a", Toast.LENGTH_SHORT).show();
				img_optiona.setImageResource(R.drawable.page3_selected_ans);
				txt_optionA.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 2 ){
					txt_optionB.setTextColor(Color.BLACK);
					img_optionb.setImageResource(R.drawable.page3_options);
				}
				else if(selected_option == 3 ){
					txt_optionC.setTextColor(Color.BLACK);
					img_optionc.setImageResource(R.drawable.page3_options);
				}
				else if(selected_option == 4 ){
					txt_optionD.setTextColor(Color.BLACK);
					img_optiond.setImageResource(R.drawable.page3_options);
				}

				selected_option = 1;

			}
		});       
		img_optionb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;

				//			Toast.makeText(getApplicationContext(), "option b", Toast.LENGTH_SHORT).show();
				img_optionb.setImageResource(R.drawable.page3_selected_ans);
				txt_optionB.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 3 ){
					img_optionc.setImageResource(R.drawable.page3_options);
					txt_optionC.setTextColor(Color.BLACK);
				}
				else if(selected_option == 4 ){
					txt_optionD.setTextColor(Color.BLACK);
					img_optiond.setImageResource(R.drawable.page3_options);
				}

				selected_option = 2;

			}
		});   
		img_optionc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;
				//		Toast.makeText(getApplicationContext(), "option c", Toast.LENGTH_SHORT).show();

				img_optionc.setImageResource(R.drawable.page3_selected_ans);
				txt_optionC.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 2 ){
					img_optionb.setImageResource(R.drawable.page3_options);
					txt_optionB.setTextColor(Color.BLACK);
				}
				else if(selected_option == 4 ){
					img_optiond.setImageResource(R.drawable.page3_options);
					txt_optionD.setTextColor(Color.BLACK);
				}

				selected_option = 3;

			}
		});
		img_optiond.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;
				//	Toast.makeText(getApplicationContext(), "option d", Toast.LENGTH_SHORT).show();

				img_optiond.setImageResource(R.drawable.page3_selected_ans);
				txt_optionD.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 2 ){
					img_optionb.setImageResource(R.drawable.page3_options);
					txt_optionB.setTextColor(Color.BLACK);
				}
				else if(selected_option == 3 ){
					txt_optionC.setTextColor(Color.BLACK);
					img_optionc.setImageResource(R.drawable.page3_options);
				}

				selected_option = 4;
			}
		});

		final Button btn_check = (Button)findViewById(R.id.btn_check);
		btn_check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen){
					btn_check.setText(R.string.check_answer);
					swipeLeft();
				}
				else if(selected){

					Questions data = ques.get(gQuesIndex);
					if(data.answer != selected_option)
					{
						if(selected_option == 1 ){
							img_optiona.setImageResource(R.drawable.page3_wrong_ans);
							txt_optionA.setTextColor(Color.RED);
						}
						else if(selected_option == 2 ){
							img_optionb.setImageResource(R.drawable.page3_wrong_ans);
							txt_optionB.setTextColor(Color.RED);
						}
						else if(selected_option == 3 ){
							img_optionc.setImageResource(R.drawable.page3_wrong_ans);
							txt_optionC.setTextColor(Color.RED);
						}
						else if(selected_option == 4){
							img_optiond.setImageResource(R.drawable.page3_wrong_ans);
							txt_optionD.setTextColor(Color.RED);
						}
					}
					if(data.answer == 1 ){
						img_optiona.setImageResource(R.drawable.page3_correct_ans);
						txt_optionA.setTextColor(Color.parseColor("#006400"));
					}
					else if(data.answer == 2 ){
						txt_optionB.setTextColor(Color.parseColor("#006400"));
						img_optionb.setImageResource(R.drawable.page3_correct_ans);
					}
					else if(data.answer == 3 ){
						img_optionc.setImageResource(R.drawable.page3_correct_ans);
						txt_optionC.setTextColor(Color.parseColor("#006400"));
					}
					else if(data.answer == 4){
						img_optiond.setImageResource(R.drawable.page3_correct_ans);
						txt_optionD.setTextColor(Color.parseColor("#006400"));
					}

					isGreen = true;


					if(data.info == 1){
						infoData =data.data;
						btn_info.setVisibility(View.VISIBLE);
					}


					btn_check.setText(R.string.get_next_question);
					clicks++;

				}
				else
					Toast.makeText(getApplicationContext(), "Choose an answer from the options", Toast.LENGTH_SHORT).show();

			}
		});

		btn_info.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent info = new Intent(QuizActivity.this, InfoScreen.class);
				info.putExtra("data",infoData);
				startActivity(info);
				//     overridePendingTransition(R.anim.zoomout, 0);


			}
		});
		// LOAD QUESTION VIA XML
		int quesno = 1;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			parser = pullParserFactory.newPullParser();
			Log.d("JKS","question file = "+questionFile);
			in_stream = getApplicationContext().getAssets().open(questionFile);
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_stream, null);

			no_of_quetions = 0;
			getQuestion(parser, quesno);

			no_of_quetions = ques.size();


		} catch (XmlPullParserException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txt_qno = (TextView)findViewById(R.id.txt_qno);
		txt_question = (TextView)findViewById(R.id.txt_question);

		txt_optionA.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				screenTouch(v,event);
				return false;
			}
		});
		txt_optionB.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				screenTouch(v,event);
				return false;
			}
		});
		txt_optionC.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				screenTouch(v,event);
				return false;
			}
		});
		txt_optionD.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				screenTouch(v,event);
				return false;
			}
		});

		txt_optionA.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;

				//				Toast.makeText(getApplicationContext(), "option a", Toast.LENGTH_SHORT).show();
				img_optiona.setImageResource(R.drawable.page3_selected_ans);
				txt_optionA.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 2 ){
					txt_optionB.setTextColor(Color.BLACK);
					img_optionb.setImageResource(R.drawable.page3_options);
				}
				else if(selected_option == 3 ){
					txt_optionC.setTextColor(Color.BLACK);
					img_optionc.setImageResource(R.drawable.page3_options);
				}
				else if(selected_option == 4 ){
					txt_optionD.setTextColor(Color.BLACK);
					img_optiond.setImageResource(R.drawable.page3_options);
				}

				selected_option = 1;

			}
		});       
		txt_optionB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;

				//			Toast.makeText(getApplicationContext(), "option b", Toast.LENGTH_SHORT).show();
				img_optionb.setImageResource(R.drawable.page3_selected_ans);
				txt_optionB.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 3 ){
					img_optionc.setImageResource(R.drawable.page3_options);
					txt_optionC.setTextColor(Color.BLACK);
				}
				else if(selected_option == 4 ){
					txt_optionD.setTextColor(Color.BLACK);
					img_optiond.setImageResource(R.drawable.page3_options);
				}

				selected_option = 2;

			}
		});   
		txt_optionC.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;
				//		Toast.makeText(getApplicationContext(), "option c", Toast.LENGTH_SHORT).show();

				img_optionc.setImageResource(R.drawable.page3_selected_ans);
				txt_optionC.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 2 ){
					img_optionb.setImageResource(R.drawable.page3_options);
					txt_optionB.setTextColor(Color.BLACK);
				}
				else if(selected_option == 4 ){
					img_optiond.setImageResource(R.drawable.page3_options);
					txt_optionD.setTextColor(Color.BLACK);
				}

				selected_option = 3;

			}
		});
		txt_optionD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isGreen) return; // selection of another option for an already answered question is not done
				selected = true;
				//	Toast.makeText(getApplicationContext(), "option d", Toast.LENGTH_SHORT).show();

				img_optiond.setImageResource(R.drawable.page3_selected_ans);
				txt_optionD.setTextColor(Color.parseColor("#ff6600"));
				if(selected_option == 1 ){
					img_optiona.setImageResource(R.drawable.page3_options);
					txt_optionA.setTextColor(Color.BLACK);
				}
				else if(selected_option == 2 ){
					img_optionb.setImageResource(R.drawable.page3_options);
					txt_optionB.setTextColor(Color.BLACK);
				}
				else if(selected_option == 3 ){
					txt_optionC.setTextColor(Color.BLACK);
					img_optionc.setImageResource(R.drawable.page3_options);
				}

				selected_option = 4;

			}
		});



		//Display First Question
		gQuesIndex = 0;
		displayQuestion();

	}

	public void screenTouch(View v, MotionEvent event){

		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN : x1 = event.getX();
		//   		Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
		break;
		case MotionEvent.ACTION_UP : x2 = event.getX();
		//  		Toast.makeText(this, "released", Toast.LENGTH_SHORT).show();
		float deltaX = x2 - x1;
		if (deltaX < 0) 
			toLeft = true;
		else
			toLeft = false;
		if(Math.abs(deltaX) > MIN_DISTANCE)
		{
			if(toLeft)
				//				Toast.makeText(this, "swiped to Left", Toast.LENGTH_SHORT).show();
				swipeLeft();
			else 
				swipeRight();
			//				Toast.makeText(this, "swiped to Right", Toast.LENGTH_SHORT).show();

		}
		else
		{
			// consider as something else for ex:swipe up
		}
		break;
		}

	}


	public void swipeRight(){
		// clear the selected answers
		img_optiona.setImageResource(R.drawable.page3_options);
		img_optionb.setImageResource(R.drawable.page3_options);
		img_optionc.setImageResource(R.drawable.page3_options);
		img_optiond.setImageResource(R.drawable.page3_options);

		txt_optionA.setTextColor(Color.BLACK);
		txt_optionB.setTextColor(Color.BLACK);
		txt_optionC.setTextColor(Color.BLACK);
		txt_optionD.setTextColor(Color.BLACK);

		isGreen = false;
		selected = false;
		selected_option = 0;
		btn_info.setVisibility(View.INVISIBLE);

		if(gQuesIndex == 0)
		{
			gQuesIndex = no_of_quetions - 1;
		}
		else gQuesIndex--;

		displayQuestion();

		ScrollView scrl = (ScrollView)findViewById(R.id.scrl_quesion);
		scrl.startAnimation(animLeftToRight);

	}
	public void swipeLeft (){

		// clear the selected answers
		Button btn_check = (Button)findViewById(R.id.btn_check);
		img_optiona.setImageResource(R.drawable.page3_options);
		img_optionb.setImageResource(R.drawable.page3_options);
		img_optionc.setImageResource(R.drawable.page3_options);
		img_optiond.setImageResource(R.drawable.page3_options);

		txt_optionA.setTextColor(Color.BLACK);
		txt_optionB.setTextColor(Color.BLACK);
		txt_optionC.setTextColor(Color.BLACK);
		txt_optionD.setTextColor(Color.BLACK);

		btn_check.setText(R.string.check_answer);
		isGreen = false;
		selected = false;
		selected_option = 0;
		btn_info.setVisibility(View.INVISIBLE);

		gQuesIndex++;
		if(gQuesIndex == no_of_quetions )
			gQuesIndex = 0;

		displayQuestion();

		ScrollView scrl = (ScrollView)findViewById(R.id.scrl_quesion);
		scrl.startAnimation(animRightToLeft);
	}
	public void displayQuestion() {

		Questions data = ques.get(gQuesIndex);

		txt_qno.setText(data.no+".");
		txt_question.setText(data.question);
		txt_optionA.setText(data.optiona);
		txt_optionB.setText(data.optionb);
		txt_optionC.setText(data.optionc);
		txt_optionD.setText(data.optiond);


	}
	public void getQuestion(XmlPullParser parser, int quesNo) throws XmlPullParserException,IOException
	{
		int eventType = parser.getEventType();
		Questions currentQuestion = null;

		while (eventType != XmlPullParser.END_DOCUMENT){			
			String name = null;
			switch (eventType){
			case XmlPullParser.START_DOCUMENT:
				ques = new ArrayList<Questions>();
				break;
			case XmlPullParser.START_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("question")){
					currentQuestion = new Questions();
				} else if (currentQuestion != null){
					if (name.equalsIgnoreCase("catagory")){
						String data = parser.nextText();
						int temp = Integer.parseInt(data);
						currentQuestion.catagory = temp;
						//						Log.d("JITZ","xmdata:"+currentQuestion.catagory);
					} else if (name.equalsIgnoreCase("no")){
						currentQuestion.no = Integer.parseInt(parser.nextText());
						//						Log.d("JITZ","xmdata:"+currentQuestion.no);
					}else if (name.equalsIgnoreCase("q")){
						currentQuestion.question = parser.nextText();
						//						Log.d("JITZ","xmdata:"+currentQuestion.question);
					}else if (name.equalsIgnoreCase("optiona")){
						currentQuestion.optiona = parser.nextText();
						//						Log.d("JITZ","xmdata:"+currentQuestion.optiona);
					}else if (name.equalsIgnoreCase("optionb")){
						currentQuestion.optionb = parser.nextText();
						//						Log.d("JITZ","xmdata:"+currentQuestion.optionb);
					}else if (name.equalsIgnoreCase("optionc")){
						currentQuestion.optionc = parser.nextText();
						//						Log.d("JITZ","xmdata:"+currentQuestion.optionc);
					}else if (name.equalsIgnoreCase("optiond")){
						currentQuestion.optiond = parser.nextText();
						//						Log.d("JITZ","xmdata:"+currentQuestion.optiond);
					}else if (name.equalsIgnoreCase("ans")){
						currentQuestion.answer = Integer.parseInt(parser.nextText());
						//						Log.d("JITZ","xmdata:"+currentQuestion.answer );
					}
					else if (name.equalsIgnoreCase("info")){
						currentQuestion.info = Integer.parseInt(parser.nextText());
					}
					else if (name.equalsIgnoreCase("data")){
						currentQuestion.data = parser.nextText();
					}
				}

				break;
			case XmlPullParser.END_TAG:
				name = parser.getName();
				if (name.equalsIgnoreCase("question") && currentQuestion != null){
					ques.add(currentQuestion);
				} 
			}

			eventType = parser.next();
		}
	}


}