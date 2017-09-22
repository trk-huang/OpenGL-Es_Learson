package com.tuya.media.myapplication2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huangdaju on 2017/9/21.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private float mPreviousX, mPreviousY;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private IGLRender mIGLRender;

    public MyGLSurfaceView(Context context) {
        super(context);
        initView();
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mIGLRender = new GLRenderer(getContext());
        this.setEGLContextClientVersion(2);
        this.setRenderer((GLSurfaceView.Renderer) mIGLRender);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = 0f;
        float eY = 0f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                eX = event.getX();
                eY = event.getY();
                float dx = eX - mPreviousX;
                float dy = eY - mPreviousY;

                // 反向旋转至中线以上
                if (eY > getHeight() / 2) {
                    dx = dx * -1;
                }

                // 反向旋转至中线左面
                if (eX < getWidth() / 2) {
                    dy = dy * -1;
                }
                mIGLRender.setAngle(mIGLRender.getAngle() + (dx + dy) * TOUCH_SCALE_FACTOR);
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                mPreviousX = eX;
                mPreviousY = eY;
                break;
        }

        return true;
    }
}
