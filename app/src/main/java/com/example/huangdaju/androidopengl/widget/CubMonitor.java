package com.example.huangdaju.androidopengl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.render.ConeRender;
import com.example.huangdaju.androidopengl.render.CubRender;
import com.example.huangdaju.androidopengl.render.MonitorTextureRender1;
import com.example.huangdaju.androidopengl.render.SkyRender;

public class CubMonitor extends GLSurfaceView {
    private final static String TAG = "Monitor";

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;


    SkyRender mRender;


    public CubMonitor(Context context) {
        super(context);
    }

    public CubMonitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
//        MonitorRender mRender = new MonitorRender(context);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.qwqw);
        mRender = new SkyRender(context,bitmap);
//        SGLRender sglRender = new SGLRender(this);

        this.setRenderer(mRender);
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

public void setMatrix(float[] matrix){
    mRender.setMatrix(matrix);
}
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void refreshView(){
        this.requestRender();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
