package com.fantastix.sprite;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessengerBackgroundClipService extends Service {
	private static String TAG = "Inchoo.net tutorial";
	private boolean isRunning = false;
	private boolean isPlaying = false;
	private Context context;
	private int result = Activity.RESULT_CANCELED;
	public static final String URL = "urlpath";
	public static final String FILENAME = "filename";
	public static final String RESULT = "result";

	private final IBinder mBinder = new MyBinder();

	public MessengerBackgroundClipService() {
//		super("MessengerBackgroundClipService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO for communication return IBinder implementation
//		addResultValues();
		return mBinder;
	}

	public class MyBinder extends Binder {
		MessengerBackgroundClipService getService() {
			return MessengerBackgroundClipService.this;
		}
	}

	@Override
	public void onCreate() {
		this.context = this;
		this.isRunning = false;
//		this.backgroundThread = new Thread(myTask);
	}

	private Runnable myTask = new Runnable() {
		public void run() {
			// Do something here
			stopSelf();
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!this.isRunning) {
			this.isRunning = true;
//			this.backgroundThread.start();
		}
		return Service.START_NOT_STICKY;
	}

	@Override
		public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(TAG, "MessengerBackgroundClipService started");
		this.stopSelf();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.isRunning = false;
		super.onDestroy();
		Log.d(TAG, "MessengerBackgroundClipService destroyed");
	}

}