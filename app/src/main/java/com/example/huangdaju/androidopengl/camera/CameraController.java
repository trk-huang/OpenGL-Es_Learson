package com.example.huangdaju.androidopengl.camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * huangdaju
 * 2018/11/18
 */


public class CameraController {
    private CameraManager manager;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mpreviewBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest mRequest;
    private SurfaceHolder mSurfaceHolder;
    private boolean mFlashSupported;
    private String mCameraId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CameraController(Context context) {
        initCamera(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCamera(Context context) {
        manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    break;
                }
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    break;
                }

                // 检查闪光灯是否支持。
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;
                mCameraId = cameraId;
                Log.e(TAG, " 相机可用 ");
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开预览
     */
    public void openCamera() {
        if (mCameraId != null) {
            try {
                manager.openCamera(mCameraId, mStateCallback, mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public void setSurfaceHolder(SurfaceHolder mSurfaceHolder) {
        this.mSurfaceHolder = mSurfaceHolder;
    }

    /**
     * 相机状态监听
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            mCameraDevice = null;
        }
    };

    /**
     * 创建CameraCaptureSession
     */
    private void createCameraPreviewSession() {
        if (mCameraDevice != null){
            try {
                mpreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                if (mSurfaceHolder != null) {
                    mpreviewBuilder.addTarget(mSurfaceHolder.getSurface());
                    mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface()),
                            new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                    if (null == mCameraDevice){
                                        return;
                                    }
                                    mCameraCaptureSession = cameraCaptureSession;
                                    //自动对焦
                                    mpreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                    mRequest = mpreviewBuilder.build();
                                    //发送请求
                                    try {
                                        mCameraCaptureSession.setRepeatingRequest(mRequest,
                                                null, mHandler);
                                    } catch (CameraAccessException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                                }
                            },null);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
