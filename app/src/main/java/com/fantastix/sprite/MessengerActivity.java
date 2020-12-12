package com.fantastix.sprite;

import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.*;
import com.fantastix.android.sprite.R;
import com.unclouded.android.*;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MessengerActivity extends SpriteActivity {
	private static final int TAKE_VIDEO_CAMERA_REQUEST = 1;

	// Unclouded event loop
	private Unclouded unclouded;

	// Flag to indicate whether device is connected to the usersListView or not
	private boolean isOnline;

	// Name that is entered in the splash activity
	private String myName;

	// Adapter to update the list view with string messages
	private ArrayAdapter<String> conversationArrayAdapter;

	// List of remote references to other devices in the usersListView
	private ArrayList<RemoteReference> buddyList;

	private SimpleDateFormat mFormatter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger_chatroom);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		buddyList = new ArrayList<RemoteReference>();
		isOnline = false;

		Intent intent = getIntent();
		myName = intent.getStringExtra("NAME");

		final ListView conversationView = (ListView) findViewById(R.id.conversation_view);
		final ImageView mSend = (ImageView) findViewById(R.id.send_button);

		VideoView v = (VideoView)findViewById(R.id.chatroom_video_background);
		v.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/video.mp4"));
		v.setMediaController(new MediaController(this));
		v.requestFocus();
		v.start();

		mFormatter = new SimpleDateFormat("HH:mm", Locale.US);

		conversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		conversationView.setAdapter(conversationArrayAdapter);
		// Make listView to scroll down automatically
		conversationView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// When entering a message, clear the field and broadcast the message
		mSend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText msgField = (EditText) findViewById(R.id.msg_field);
				TextView txtDate = (TextView) findViewById(R.id.txtDate);
				TextView txtSender = (TextView) findViewById(R.id.txtSender);

				String msg = msgField.getText().toString();
				msgField.setText("");
				txtDate.setText(mFormatter.format("DATE"));

				broadcastMessage(msg);
			}

		});

		// Obtain Unclouded event loop
		unclouded = Unclouded.getInstance();

		// Go online and connected to the usersListView
		Network network = unclouded.goOnline();

		// Monitor the usersListView connection to update the isOnline flag
		network.whenever(new NetworkListener(){

			@Override
			public void isOnline(InetAddress ip){
				isOnline = true;
			}

			@Override
			public void isOffline(InetAddress ip){
				isOnline = false;
				buddyList.clear();
			}
		});

		// MESSENGER type tag to associate with the menu_chat service
		TypeTag MESSENGER_TYPETAG = new TypeTag("MESSENGER");

		// New instance of the Messenger class.
		Messenger myMessenger = new Messenger();

		// myMessenger is broadcasted by reference (because Messenger implements UObject)
		// This makes a remote reference to this object to be spread across the usersListView
		unclouded.broadcast(MESSENGER_TYPETAG, myMessenger);

		// Listen for remote reference associated with the MESSENGER_TYPETAG type tag
		unclouded.whenever(MESSENGER_TYPETAG, new ServiceListener<RemoteReference>(){

			String buddyName;

			@Override
			public void isDiscovered(RemoteReference remoteReference) {
				// When discovering a buddy, register the remote reference to its Messenger object
				buddyList.add(remoteReference);
				// Asynchronously ask for his name
				Promise promise = remoteReference.asyncInvoke("getName");
				// Listen for the name to be returned
				promise.when(new PromiseListener<String>(){

					@Override
					public void isResolved(String name) {
						// When name is returned, store it and print message on the screen
						buddyName = name;
						printBuddyJoinedMessage(name);
					}
				});
			}

			@Override
			public void isDisconnected(RemoteReference remoteReference){
				// When disconnected, remove buddy from list
				buddyList.remove(remoteReference);
				// If name is already resolved, print disconnection message on the screen
				if(buddyName != null){ // Null in case disconnection occurs before name is resolved
					printBuddyDisconnectedMessage(buddyName);
				}
			}

			@Override
			public void isReconnected(RemoteReference remoteReference){
				// When reconnecting, check whether name has been resolved before
				if(buddyName == null){
					// If not, treat like a new connection
					isDiscovered(remoteReference);
				} else {
					// Otherwise, add reference to list and print message on the screen
					buddyList.add(remoteReference);
					printBuddyJoinedMessage(buddyName);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		return true;
	}

	@Override
	// Dynamically change menu depending on usersListView status
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_chat, menu);
		MenuItem item = menu.findItem(R.id.action_change_network_status);
		if(isOnline){
			// If connected to the usersListView, show `go offline' action
			item.setTitle(R.string.action_go_offline);
		} else {
			// If disconnected from the usersListView, show `go online' action
			item.setTitle(R.string.action_go_online);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_change_network_status:
				if(isOnline){
					// When clicked and usersListView is online, go offline
					unclouded.goOffline();
				} else {
					// When clicked and usersListView is offline, go online
					unclouded.goOnline();
				}
				return true;
			case R.id.action_change_background:
				// Change static background to live video background
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		Intent intent = new Intent(this, MessengerBackgroundClipService.class);
//		bindService(intent, this, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		unbindService(this);
	}

	// Broadcast a message in the usersListView
	private void broadcastMessage(String msg){
		// Loop over all discovered buddies...
		for(RemoteReference reference: buddyList){
			// ... and asynchronously invoke their receiveMsg method;
			// No need to wait for return value here
			reference.asyncInvoke("receiveMsg", myName, msg);
		}
		// Print message to the screen
		printMessage(myName, msg);
	}

	private void printMessage(final String name, final String msg){
		addToAdapter(name + ": " + msg);
	}

	private void printBuddyJoinedMessage(final String name){
		addToAdapter(name + " has joined the conversation.");
	}

	private void printBuddyDisconnectedMessage(final String name){
		addToAdapter(name + " has left the conversation.");
	}

	// Main method to print something on the screen
	private void addToAdapter(final String msg){
		// Necessary because most invocations are initiated by the Unclouded event loop
		runOnUiThread(new Runnable(){
			public void run(){
				conversationArrayAdapter.add(msg);
			}
		});
	}

	/**
	 * background clip must be a service so that when user moves to another page, camera will still be working
	 * background clip service captures the user's live feed and send to receiver and displays the live feed on its background
	 * this feature only works if both party allows it to do so.
	 */
//	public void someMethod(View view) {
//		b.setEnabled(false);
//
//		if (user_accepts) {
//			// start service
//			Intent i = new Intent(this, MessengerBackgroundClipService.class);
//			i.setData(Uri.parse("http://localhost:3000/video"));
//			i.putExtra(MessengerBackgroundClipService.URL, "http://localhost:3000/video");
//			// i.putExtra(Downloader.EXTRA_MESSENGER, new Messenger(handler));
//			startService(i);
//		}
//
//		Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		startActivityForResult(videoIntent, TAKE_VIDEO_CAMERA_REQUEST);
//		URL url = new URL("urlPath");
//		stream = url.openConnection().getInputStream();
//		InputStreamReader reader = new InputStreamReader(stream);
//
//		VideoView videoView = (VideoView)findViewById(R.id.chatroom_video_background);
//		Uri uri = Uri.parse("");
//		videoView.setVideoURI(uri);
//		videoView.start();
//	}

//	public void onServiceConnected(ComponentName name, IBinder binder) {
//		MessengerBackgroundClipService.MyBinder b = (MessengerBackgroundClipService.MyBinder) binder;
//		s = b.getService();
//		Toast.makeText(MessengerActivity.this, "Background video is live", Toast.LENGTH_SHORT).show();
//	}

//	@Override
//	public void onServiceDisconnected(ComponentName name) {
//		s = null;
//	}

	// ------------------------------------------

	// Instances of the Messenger class are spread across the usersListView
	// and allow other devices to share messages
	protected class Messenger implements UObject {

		public String getName(){ return myName; }

		public void receiveMsg(String name, String msg){
			printMessage(name, msg);
		}
	}

	public void composeMessage(View v){
		Intent i = new Intent(getBaseContext(), UsersListActivity.class);
		startActivityForResult(i, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		ImageView userImage = (ImageView)findViewById(R.id.current_active_user_avatar);
		TextView userName = (TextView)findViewById(R.id.current_active_user_id);
		Bitmap m = (Bitmap)data.getExtras().get("data");
		String n = null;
		userImage.setImageBitmap(m);
		userImage.setScaleType(ImageView.ScaleType.FIT_XY);
		userName.setText(n);

		switch (requestCode) {
			case TAKE_VIDEO_CAMERA_REQUEST:
				if (resultCode == Activity.RESULT_CANCELED) {
					// Avatar camera mode was cancelled
				} else if (resultCode == Activity.RESULT_OK) {
					// TODO: Handle photo taken

				}
				break;
			default:
				break;
		}
	}
}