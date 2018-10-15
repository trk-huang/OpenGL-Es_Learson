package com.example.huangdaju.androidopengl.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.render.MonitorTextureRender;

public class Monitor extends GLSurfaceView {
    private final static String TAG = "Monitor";

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

//    private MonitorFullRender mRender;
    MonitorTextureRender mRender;
    public Monitor(Context context) {
        super(context);
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
//        MonitorRender mRender = new MonitorRender(context);
        mRender = new MonitorTextureRender(context);
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

    float x, y;
    private int mode;
    private float oldDist = 1f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float normalizedX =
                (event.getX() / (float) getWidth()) * 2 - 1;
        final float normalizedY =
                -((event.getY() / (float) getHeight()) * 2 - 1);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                Log.e(TAG, "点击事件");
                x = normalizedX;
                y = normalizedY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                oldDist = (float) spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "移动事件");
                if (mode == DRAG) {
                    move(normalizedX - x, normalizedY - y);
                }else if (mode == ZOOM){
                    float newDist = (float) spacing(event);
                    float scale = newDist / oldDist;
                    scale(scale);
                }
                x = normalizedX;
                y = normalizedY;
                requestRender();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "点击事件");

                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "抬起事件");
                break;
            case MotionEvent.ACTION_SCROLL:
                Log.e(TAG, "滚动事件");
                break;
            default:
                break;
        }
        return true;
    }

    private void scale(float scale) {
        mRender.scale(scale);
    }

    // 触碰两点间距离
    private double spacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }


    private void move(float v, float v1) {
        mRender.move(v,v1);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
