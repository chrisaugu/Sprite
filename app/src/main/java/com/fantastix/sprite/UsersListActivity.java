package com.fantastix.sprite;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.fantastix.android.sprite.R;
import com.unclouded.android.Network;
import com.unclouded.android.NetworkListener;
import com.unclouded.android.RemoteReference;
import com.unclouded.android.Unclouded;

import java.net.InetAddress;
import java.util.ArrayList;

public class UsersListActivity extends SpriteActivity {
    final Context mContext;
    private String currentUserId;
    private ArrayAdapter<String> namesArrayAdapter;
    private ArrayList<String> names;
    private ListView usersListView;
    private Switch wifiSwitch;
    private TextView holder;
    private ProgressDialog progressDialog;
    private BroadcastReceiver receiver = null;
    private String msg = "";

    Toolbar toolbarWidget;

    // Unclouded event loop
    private Unclouded unclouded;

    // Flag to indicate whether device is connected to the usersListView or not
    private boolean isOnline;

    // Name that is entered in the splash activity
    private String myName;

    // Adapter to update the list view with string messages
    private ArrayAdapter<String> usersListArrayAdapter;

    // List of remote references to other devices in the usersListView
    private ArrayList<RemoteReference> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_users_list);

        toolbarWidget = (Toolbar) findViewById(R.id.network_control_tools);
        toolbarWidget.setTitle("Chat");
        toolbarWidget.setNavigationIcon(R.drawable.ic_launcher);
        setSupportActionBar(toolbarWidget);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        myName = intent.getStringExtra("NAME");

        holder = (TextView) findViewById(R.id.net_ussid);

        wifiSwitch = (Switch) findViewById(R.id.wifiap_btn_createap);
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                stopService(new Intent(getApplicationContext(), MessageAdapter.class));
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);

                if (isChecked) {
                    holder.setText("Wifi: on");
                    Toast.makeText(UsersListActivity.this, "Wi-Fi is On", Toast.LENGTH_SHORT).show();
                } else {
                    holder.setText("Wifi: off");
                    Toast.makeText(UsersListActivity.this, "Hotspot is created", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usersList = new ArrayList<RemoteReference>();
        isOnline = false;

        ListView chatList = (ListView) findViewById(R.id.usersListView);
        Button createWifi = (Button) findViewById(R.id.wifiap_btn_createap);
        Button backBtn = (Button) findViewById(R.id.wifiap_btn_back);

        usersListArrayAdapter = new ArrayAdapter<String>(this, R.layout.users_list_item);
        chatList.setAdapter(usersListArrayAdapter);
        // Make listView to scroll down automatically
        chatList.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        //Check if WiFi is On
        ConnectivityManager com = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TextView tv2 = (TextView) findViewById(R.id.connecting_connected);
        boolean wifi = com.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        if (wifi) {
            tv2.setText("Online");
        } else {
            tv2.setText("Offline");
            tv2.setBackgroundColor(Color.RED);
            tv2.setTextColor(Color.WHITE);
        }

        // Obtain Unclouded event loop
        unclouded = Unclouded.getInstance();

        // Go online and connected to the usersListView
        Network network = unclouded.goOnline();

        // Monitor the usersListView connection to update the isOnline flag
        network.whenever(new NetworkListener(){

            public void isConnected(InetAddress ipAddress){
                runOnUiThread(new Runnable(){
                    public void run(){
                        TextView txtView = (TextView) findViewById(R.id.textView1);
                        txtView.setText("I'm online at " + ipAddress.toString());
                    }
                });
            }

            public void isDisconnected(InetAddress ipAddress){
                network.cancel();
            }

            public void isConnectedTo(InetAddress ipAddress){ /*...*/ }

            public void isDisconnectedFrom(InetAddress ipAddress){ /*...*/ }

            @Override
            public void isOnline(InetAddress ip) {
                isOnline = true;
            }

            @Override
            public void isOffline(InetAddress ip) {
                isOnline = false;
                usersList.clear();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart event.");
    }

    @Override
    protected void onResume() {
        setConversationsList();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause event");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop event");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy event.");
    }

    public UsersListActivity(Context context) {
        this.mContext = context;
    }

    public void pickContact(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, /*PICK_REQUEST*/1);
    }

    //display clickable a list of all users
    private void setConversationsList() {
//        currentUserId = unclouded.getCurrentUser().getObjectId();
        names = new ArrayList<String>();
    }

    //open a conversation with one person
    public void openConversation(ArrayList<String> names, int pos) {
        // TODO fix this
        names.get(pos);
    }

    public static UsersListActivity makeText(Context context, CharSequence text) {
        UsersListActivity result = new UsersListActivity(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflate.inflate(R.id.network_overlay, null);
//        TextView tv = v.findViewById(R.id.net_ussid);
//        tv.setText(text);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buddylist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                break;
            case R.id.wifiap_btn_createap:
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}