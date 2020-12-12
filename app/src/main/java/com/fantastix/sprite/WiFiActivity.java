package com.fantastix.sprite;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.fantastix.android.sprite.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class WiFiActivity extends SpriteActivity {
	WifiManager mWifiManager;
	String mApStr;
	IntentFilter mIntentFilter;
	@SuppressLint("WifiManagerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);

//		ApManager.isApOn(getApplicationContext());

		// Set up WiFiManager
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Create listener object for button. When button is pressed, scan for
		// APs nearby
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean scanStarted = mWifiManager.startScan();

				// If the scan failed, log it
				if (!scanStarted) Log.e(TAG, "Wifi scan failed");
			}
		});

		// Set up IntentFilter for "Wifi scan result available" intent.
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
				Log.e(TAG, "Scan results available");
				List<ScanResult> scanResults = mWifiManager.getScanResults();
				mApStr = "";
				for (ScanResult result : scanResults) {
					mApStr = mApStr + result.SSID + "; ";
					mApStr = mApStr + result.BSSID + "; ";
					mApStr = mApStr + result.capabilities + "; ";
					mApStr = mApStr + result.frequency + "; ";
					mApStr = mApStr + result.level + "; ";
				}
				// Update UI to show all this information
				setTextView(mApStr);
			}
			Intent serviceIntent = new Intent(context, ConnectionService.class);  // start ConnectionService
			serviceIntent.setAction(action);   // put in action and extras
			serviceIntent.putExtras(intent);
			context.startService(serviceIntent);  // start the connection service		
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	private void setTextView(String str) {
		TextView tv = (TextView)findViewById(R.id.textview);
		tv.setMovementMethod(new ScrollingMovementMethod());
		tv.setText(str);
	}
}