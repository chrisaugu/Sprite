package com.fantastix.sprite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.fantastix.android.sprite.R;

public class CameraActivity extends SpriteActivity implements View.OnClickListener {

    private Camera cameraObject;
    private ShowCamera showCamera;
    private ImageView pic;

    public static Camera isCameraAvailable() {
        Camera object = null;
        try {
            object = Camera.open();
        } catch (Exception e) {
        }
        return object;
    }

    private PictureCallback capturedIt = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }
            cameraObject.release();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_camera);
        pic = (ImageView)findViewById(R.id.imageView1);

        cameraObject = isCameraAvailable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
        preview.addView(showCamera);

        Button b3 = (Button)findViewById(R.id.camera_btn);
        b3.setOnClickListener(this);
    }

    public void snapIt(View view) {
        cameraObject.takePicture(null, null, capturedIt);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.camera_btn:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            default:
                break;
        }
    }

//    button = (Button)findViewById(R.id.button1);
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//
//            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//            startActivityForResult(intent, 0);
//
//        }
//    });
}
