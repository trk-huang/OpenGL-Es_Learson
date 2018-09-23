package com.example.huangdaju.androidopengl.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.huangdaju.androidopengl.render.MonitorFullRender;
import com.example.huangdaju.androidopengl.render.MonitorRender;

public class Monitor extends GLSurfaceView {

//    private MonitorFullRender mRender;

    public Monitor(Context context) {
        super(context);
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
//        MonitorRender mRender = new MonitorRender(context);
        MonitorFullRender mRender = new MonitorFullRender(context);
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
