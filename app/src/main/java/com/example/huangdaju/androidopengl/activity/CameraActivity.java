package com.example.huangdaju.androidopengl.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.SurfaceView;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.camera.CameraController;

/**
 * huangdaju
 * 2018/11/22
 **/

public class CameraActivity extends BaseActivity {

    private CameraController cameraController;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraController = new CameraController(this);
        SurfaceView surfaceView = findViewById(R.id.rl_camera_preview);
        cameraController.setSurfaceHolder(surfaceView.getHolder());
        cameraController.openCamera();
    }
}
