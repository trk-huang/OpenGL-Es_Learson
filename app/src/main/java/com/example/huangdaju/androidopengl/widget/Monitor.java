package com.example.huangdaju.androidopengl.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class Monitor extends GLSurfaceView {

    private MonitorRender mRender;

    public Monitor(Context context) {
        super(context);
        this.mRender = mRender;
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
        mRender = new MonitorRender(context);
        this.setRenderer(mRender);
        this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
