package com.example.webprojectandroid;

import java.io.IOException;
import java.net.InetAddress;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends Activity {
	WebView webView;
	int sleeptime = 100;
	Boolean isFlagSet = false;
	Boolean isServerupFlag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFlagSet = false;
		isServerupFlag = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(sleeptime);
						sleeptime = 20000;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
					// do what you want
					Log.d("timeout","TIMEOUT OCCURED");
					
					if(!isServerUp())
					{
						Log.d("internet","server is not up");
						isServerupFlag = false;
					}
					else
						isServerupFlag = true;
					isFlagSet = true;
					
				}
			}
		}).start();
		setContentView(R.layout.activity_main);
		webView = (WebView)findViewById(R.id.fullscreen_content);
		webView.setWebViewClient(new MyWebViewClient());

		if(haveNetworkConnection())
		{
			Log.d("internet","INTERNET IS AVAILABLE");
			while(!isFlagSet) {
				try {
					Thread.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d("internet","Server check is finished "+isServerupFlag);
			if(!isServerupFlag) {
				Log.d("internet","server is not up load regret page");
				webView.loadUrl("file:///android_asset/www/regret.html");
			}
			else
			{
				webView.loadUrl("https://play.google.com/apps/publish/?dev_acc=06275214020653525287");
				//webView.loadUrl("https://dividely.com/");
				//I have included web permissions in the AndroidManifest.xml
				webView.setWebChromeClient(new WebChromeClient());
				WebSettings webSettings = webView.getSettings();
				webSettings.setDomStorageEnabled(true);
			}
		}
		else
		{
			Log.d("internet", "INTERNET IS NOT AVAILABLE");
			webView.loadUrl("file:///android_asset/www/noconnection.html");
		}
	}	
private boolean isServerUp() {
	if(true)
	return true;
	else {
	try {
		InetAddress ina = InetAddress.getByName("192.168.1.102");
		if(ina.isReachable(3000)) {
			return true;
		}
		else {
			return false;
		}
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	}
	return false;
}
	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}
	@Override
	public void onBackPressed()
	{
		if(webView.canGoBack())
			webView.goBack();
		else
			super.onBackPressed();
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

}
