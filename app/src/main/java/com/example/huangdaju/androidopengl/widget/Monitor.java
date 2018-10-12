package com.example.huangdaju.androidopengl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.render.MonitorFullRender;
import com.example.huangdaju.androidopengl.render.MonitorOvalRender;
import com.example.huangdaju.androidopengl.render.MonitorRender;
import com.example.huangdaju.androidopengl.render.MonitorSquareRender;
import com.example.huangdaju.androidopengl.render.MonitorTextureRender;
import com.example.huangdaju.androidopengl.render.SGLRender;

public class Monitor extends GLSurfaceView {

//    private MonitorFullRender mRender;

    public Monitor(Context context) {
        super(context);
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
//        MonitorRender mRender = new MonitorRender(context);
        MonitorTextureRender mRender = new MonitorTextureRender(context);
//        SGLRender sglRender = new SGLRender(this);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test1);
        mRender.setBitmap(bitmap);
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
