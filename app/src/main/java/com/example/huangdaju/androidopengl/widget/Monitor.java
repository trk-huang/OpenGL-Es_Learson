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
import com.example.huangdaju.androidopengl.render.MonitorTextureRender;
import com.example.huangdaju.androidopengl.render.MonitorTextureRender1;
import com.example.huangdaju.androidopengl.uitls.VaryTools;

public class Monitor extends GLSurfaceView {
    private final static String TAG = "Monitor";

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;

    private float mMaxScale = 1.5f;
    private float mMinScale = 1.0f;

    private float[] lastScale;

    //    private MonitorFullRender mRender;
    MonitorTextureRender1 mRender;

    private ScaleGestureDetector scaleGestureDetector;

    public Monitor(Context context) {
        super(context);
    }

    public Monitor(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(2);
//        MonitorRender mRender = new MonitorRender(context);
        mRender = new MonitorTextureRender1(context);

        scaleGestureDetector = new ScaleGestureDetector(context,new scaleListener());
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

    private void refreshView(){
        this.requestRender();
    }

    float x, y;
    private int mode;
    private float oldDist = 1f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
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
                oldDist = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "移动事件");
                if (mode == DRAG) {
                    move(normalizedX - x, normalizedY - y);
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

    private void scale(float scale, float[] s) {
        Log.d("scale==", "scale " + scale);
        mRender.scale(scale, s);
    }

    // 触碰两点间距离
    private float spacing(MotionEvent event) {
        //通过三角函数得到两点间的距离
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        return 0;
    }


    private void move(float v, float v1) {

        mRender.move(v, v1);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private float minScale = 1f,maxScale = 2f;
    private float preScale = 1f;
    public class scaleListener implements ScaleGestureDetector.OnScaleGestureListener{
        float initialSpan,currentSpan;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mode == ZOOM) {
                currentSpan = detector.getCurrentSpan();
                Log.d("TAG", "onScale: current init" + currentSpan + " " + initialSpan);
                if (Math.abs(currentSpan - initialSpan) > 10) {
                    float currentScale = detector.getScaleFactor() * preScale;
                    Log.d("TAG", "onScale: current scale and pre scale = " + currentScale + " " + preScale);
                float scale = preScale * currentScale;
                    if (currentScale < minScale) {
                        currentScale = minScale;
                    } else if (currentScale > maxScale) {
                        currentScale = maxScale;
                    }
                    scale(detector.getScaleFactor(), lastScale);
                    preScale = currentScale;
                }
            }
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initialSpan = detector.getCurrentSpan();
            lastScale = mRender.varyTools.getScale();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }

    }
}
