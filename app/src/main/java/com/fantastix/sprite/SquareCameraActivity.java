//package com.fantastix.sprite;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.*;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import com.fantastix.android.sprite.R;
//
//import java.io.IOException;
//
//public class SquareCameraActivity extends SpriteActivity {
//    private SurfaceView cameraPreview;
//    private RelativeLayout overlay;
//    private ImageButton close;
//    private Button shutter;
//    private ImageView userImage;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //Optional: Hide the status bar at the top of the window
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        // Set the content view and get references to our views
//        setContentView(R.layout.activity_square_camera);
//        userImage = (ImageView)findViewById(R.id.profile_image);
//        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
//        overlay = (RelativeLayout) findViewById(R.id.overlay);
//        close = (ImageButton)findViewById(R.id.action_bar_button_close);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SquareCameraActivity.this.finish();
//            }
//        });
//
//        shutter = (Button)findViewById(R.id.camera_shutter);
//        shutter.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                capture();
//            }
//        });
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        // Get the preview size
//        int previewWidth = cameraPreview.getMeasuredWidth(), previewHeight = cameraPreview.getMeasuredHeight();
//        // Set the height of the overlay so that it makes the preview a square
//        RelativeLayout.LayoutParams overlayParams = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
//        overlayParams.height = previewHeight - previewWidth;
//        overlay.setLayoutParams(overlayParams);
//    }
//
//    Camera camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//    Camera.Parameters camParams = camera.getParameters();
//    // Find a preview size that is at least the size of our IMAGE_SIZE
//    Camera.Size previewSize = camParams.getSupportedPreviewSizes().get(0);
//    for(Camera.Size size : camParams.getSupportedPreviewSizes()){
//        if (size.width >= IMAGE_SIZE && size.height >= IMAGE_SIZE) {
//            previewSize = size;
//            break;
//        }
//    }
//
//    camParams.setPreviewSize(previewSize.width, previewSize.height);
//    // Try to find the closest picture size to match the preview size.
//    Camera.Size pictureSize = camParams.getSupportedPictureSizes().get(0);
//    for (Camera.Size size : camParams.getSupportedSizes()){
//        if (size.width == previewSize.width && size.height == previewSize.height){
//            pictureSize = size;
//            break;
//        }
//    }
//    camParams.setPictureSize(pictureSize.width, pictureSize.height);
//
//
//    privateBitmap processImage(byte[] data)throws IOException {
//
//        // Determine the width/height of the image
//        int width = camera.getParameters().getPictureSize().width;
//        int height = camera.getParameters().getPictureSize().height;
//
//        // Load the bitmap from the byte array
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0, data.length, options);
//
//        // Rotate and crop the image into a square
//        int croppedWidth =(width > height)? height : width;
//        int croppedHeight =(width > height)? height : width;
//        Matrix matrix = new Matrix();
//        matrix.postRotate(IMAGE_ORIENTATION);
//        Bitmap cropped = Bitmap.createBitmap(bitmap,0,0, croppedWidth, croppedHeight, matrix,true);
//        bitmap.recycle();
//
//        // Scale down to the output size
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(cropped, IMAGE_SIZE, IMAGE_SIZE,true);
//        cropped.recycle();
//        return scaledBitmap;
//    }
//
//    void capture(){
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(i, 0);
//    }
//    void flipCamera(){}
//    void goToAlbum(){
//        Intent intent = new Intent(Intent.CATEGORY_APP_GALLERY);
//        startActivityForResult(intent, 0);
//    }
//
//    public void open(){
//        Intent i = new Intent(this, SquareCameraActivity.class);
//        startActivity(i);
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap m = (Bitmap)data.getExtras().get("data");
//        userImage.setImageBitmap(m);
//        userImage.setScaleType(ImageView.ScaleType.FIT_XY);
//    }
//
//    // Options Menu Item Click
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        //Take back to Home Screen on pressing back button
//        if (id == R.id.action_bar_button_close){
//            //Simply finish this activity
//            SquareCameraActivity.this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//}
