package com.fantastix.sprite;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;

import java.io.IOException;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holdMe;
    private Camera theCamera;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        theCamera = camera;
        holdMe = getHolder();
        holdMe.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
