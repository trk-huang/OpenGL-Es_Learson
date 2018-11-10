package com.example.huangdaju.androidopengl.camera.activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.camera.uitls.CameraUtils;
import com.example.huangdaju.androidopengl.camera.uitls.PermissionUtils;
import com.example.huangdaju.androidopengl.camera.view.CameraSufaceView;

/**
 * 为应用程序创建自定义摄像头界面的一般步骤如下：
 * 1.检测和访问摄像机 - 创建代码以检查摄像机是否存在并请求访问。
 * 2.创建预览类 - 创建扩展SurfaceView并实现SurfaceHolder界面的相机预览类。这个类预览来自相机的实时图像。
 * 3.构建预览布局 - 一旦你拥有相机预览类，创建一个包含预览和你想要的用户界面控件的视图布局。
 * 4.Capture的监听器 - 为您的接口控件连接侦听器，以响应用户操作（例如按下按钮）开始捕获图像或视频。
 * 5.捕获和保存文件 - 设置用于捕获图片或视频和保存输出的代码。
 * 6.释放相机 - 使用相机后，应用程序必须正确释放它以供其他应用程序使用。
 * 7.相机硬件是一个共享资源，必须仔细管理，以便您的应用程序不会与其他可能也想使用它的应用程序冲突。以下部分讨论如何检测相机硬件，如何请求访问相机，如何捕获图片或视频以及如何在应用程序使用完成后释放相机。
 */
public class CameraActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.askPermission(this,new String[]{Manifest.permission.CAMERA,Manifest
                .permission.WRITE_EXTERNAL_STORAGE},10,initViewRunnable);

    }

    private Runnable initViewRunnable=new Runnable() {
        @Override
        public void run() {
            setContentView(R.layout.activity_camera);
            frameLayout = findViewById(R.id.rl_camera_preview);
            CameraSufaceView cameraSufaceView = new CameraSufaceView(CameraActivity.this, CameraUtils.getCameraInstance());
            frameLayout.addView(cameraSufaceView);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode == 10, grantResults, initViewRunnable,
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CameraActivity.this, "没有获得必要的权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
