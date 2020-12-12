package com.fantastix.sprite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.fantastix.android.sprite.R;

public class AboutActivity extends SpriteActivity {

    Button btnCustom, btnAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        btnAlert = (Button)findViewById(R.id.btn_connect);
//        btnAlert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
////                builder.setView(R.layout.ok_cancel);
//                builder.setMessage("Are you sure you wanna exit?");
//                builder.setIcon(android.R.drawable.sym_action_chat);
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        AboutActivity.this.finish();
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//            }
//        });
//
//        btnCustom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(AboutActivity.this);
//                dialog.setContentView(R.layout.popover);
//                dialog.setTitle("This is a dialog!!");
//                dialog.show();
//            }
//        });
    }
}
