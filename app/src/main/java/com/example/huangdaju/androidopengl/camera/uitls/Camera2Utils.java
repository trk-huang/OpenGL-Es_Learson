package com.example.huangdaju.androidopengl.camera.uitls;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import static android.content.ContentValues.TAG;

public class Camera2Utils {

    /**
     * 当前相机的ID。
     */
    private String mCameraId;

    private Surface mSurface;
    private SurfaceHolder mSurfaceHolder;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private boolean mFlashSupported;
    private CaptureRequest mPreviewRequest;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     *
     * 一个信号量以防止应用程序在关闭相机之前退出。
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private CameraDevice mCameraDevice;

    /**
     * check if this device has camera
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


    /**
     * CameraDevice状态更改时被调用。
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // 打开相机时调用此方法。 在这里开始相机预览。
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            //创建CameraPreviewSession
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

    };

    /**
     * 为相机预览创建新的CameraCaptureSession
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createCameraPreviewSession() {

        try {
            Log.e(TAG,"创建PreviewSession");
            //设置了一个具有输出Surface的CaptureRequest.Builder。
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Log.e(TAG,"添加mSurface");
            mPreviewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            Log.e(TAG,"添加完成");
            //创建一个CameraCaptureSession来进行相机预览。
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // 相机已经关闭
                            if (null == mCameraDevice) {
                                Log.e(TAG,"相机已经关闭");
                                return;
                            }
                            // 会话准备好后，我们开始显示预览
                            mCaptureSession = cameraCaptureSession;
                            Log.e(TAG,"会话准备好后，我们开始显示预览");
                            try {
                                // 自动对焦应
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // 闪光灯
                                setAutoFlash(mPreviewRequestBuilder);
                                // 最终开启相机预览并添加事件
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        null, mBackgroundHandler);
                                Log.e(TAG," 最终开启相机预览并添加事件");
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            Log.e(TAG," onConfigureFailed 开启预览失败");
                        }
                    }, null);
        } catch (CameraAccessException e) {
            Log.e(TAG," CameraAccessException 开启预览失败2");
            e.printStackTrace();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

}
