package com.example.huangdaju.androidopengl.camera.uitls;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

public class CameraUtils {


    /**
     * check if this device has camera
     *
     * @param context
     * @return
     */
    public static boolean checkCameraHardWare(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has camera
            return true;
        }
        //  no camera on the device
        return false;
    }


    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {

        }

        return camera;
    }

}
