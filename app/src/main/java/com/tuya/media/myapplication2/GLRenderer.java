package com.tuya.media.myapplication2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.tuya.media.myapplication2.item.Triange;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by huangdaju on 2017/9/20.
 */

public class GLRenderer implements GLSurfaceView.Renderer,IGLRender {

    private Context mContext;

    private final float[] mMVPMatrix = new float[16];
    private final float[] projectionMatrix=new float[16];  //引入投影矩阵概念，解决绘制图形变形问题
    private final float[] mViewMatrix = new float[16];//定义相机视图矩阵变量
    private final float[] mRotationMatrix = new float[16];//定义旋转视图矩阵变量
    private Triange mTriange;
    private float mAngle;

    public GLRenderer(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTriange = new Triange(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio=width>height?
                (float)width/height:
                (float)height/width;
        if (width>height){
            Matrix.orthoM(projectionMatrix,0,-ratio,ratio,-1f,1f,-1f,1f);
        }else
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-ratio,ratio,-1f,1f);
//        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        float[] scratch = new float[16];

        // 设置相机的位置(视图矩阵)
//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        // 将mProjectionMatrix和mViewMatrix矩阵相乘并赋给mMVPMatrix
//        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mViewMatrix, 0);

        // 创建旋转矩阵
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // 将mProjectionMatrix和mViewMatrix矩阵相乘并赋给mMVPMatrix
//        Matrix.multiplyMM(mViewMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        mTriange.drawFrame(projectionMatrix);
    }

    @Override
    public void setAngle(float angle) {
        mAngle = angle;
    }

    @Override
    public float getAngle() {
        return mAngle;
    }
}
