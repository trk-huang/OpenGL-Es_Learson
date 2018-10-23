package com.example.huangdaju.androidopengl.camera.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.content.ContentValues.TAG;

public class CameraSufaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    public CameraSufaceView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
    }

    public CameraSufaceView(Context context, AttributeSet attrs, Camera camera) {
        super(context, attrs);
        mCamera = camera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraSufaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error stop camera preview: " + e.getMessage());
        }

        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error start camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
