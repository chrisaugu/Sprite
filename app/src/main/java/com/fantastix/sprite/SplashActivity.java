package com.fantastix.sprite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.fantastix.android.sprite.R;

/**
 * Created by Christian Augustyn on 26/04/2017.
 */
public class SplashActivity extends SpriteActivity {
    SharedPreferences sharedPreferences;
    private Intent messengerIntent, serviceIntent;
    private TextView waitAsec, title;
    private Toolbar toolbarWidget;
    String myName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        toolbarWidget = (Toolbar) findViewById(R.id.toolbar_mid);
        title = (TextView)findViewById(R.id.toolbar_title);
        title.setText(R.string.title_activity_messenger);
        toolbarWidget.setTitle(R.string.title_activity_messenger);
        toolbarWidget.setNavigationIcon(R.drawable.ic_launcher);
        setSupportActionBar(toolbarWidget);

        TextView textview = new TextView(this);
        textview.setText("Hello Android!");
        setContentView(textview);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        messengerIntent = new Intent(getApplicationContext(), MessengerActivity.class);
        serviceIntent = new Intent(getApplicationContext(), SetNameActivity.class);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        // METHOD 1
        /** Create Thread that will sleep for 5 seconds */
        Thread background = new Thread(){
            public void run(){
                try {
                    //Check for existing user name and prints it to the screen
                    myName = sharedPreferences.getString("Nickname", "Kitten");
                    if (myName.isEmpty()) {
                        Toast.makeText(SplashActivity.this, "Please register your nickname", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SplashActivity.this, SetNameActivity.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(SplashActivity.this, "Welcome " + myName.toString(), Toast.LENGTH_SHORT).show();
                        // Thread will sleep for 5 seconds
//                    sleep(5*1000);
                        // After 5 seconds redirect to another intent
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        // Remove activity
                        finish();
                    }
                } catch (Exception e){}
            }
        };
        // start thread
        background.start();

//        // METHOD3
//        Thread t = new Thread(new Runnable(){
//            public void run() {
//                // code to run
//            }
//        });
//        t.start();

        //METHOD 2

        // Intent intent = getIntent();
        // String currentUser = intent.getStringExtra("NAME");

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable(){
//            //        new Handler.postDelayed(new Runnable(){
//            //Using handler with postDelayed caller runnable eun method
//            @Override
//            public void run(){
//                Intent i = new Intent(SplashActivity.this, SetNameActivity.class);
//                startActivity(i);
//                //close this activity
//                finish();
//
//                // if (currentUser != null) {
//                //     startService(serviceIntent);
//                //     Toast.makeText(SplashActivity.this, "Please set your name to continue", Toast.LENGTH_SHORT).show();
//                // } else {
//                //     startActivity(messengerIntent);
//                // }
//                // //close this activity
//                // finish();
//            }
//        }, 5*1000); // wait for 5 seconds
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
