package com.example.huangdaju.androidopengl.uitls;

import android.opengl.Matrix;

import java.util.Arrays;
import java.util.Stack;


public class VaryTools {

    public float[] mMatrixCamera=new float[16];    //相机矩阵
    public float[] mMatrixProjection=new float[16];    //投影矩阵
    private float[] mMatrixCurrent=     //原始矩阵
            {1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1};
    private float[] mMatrix0 = mMatrixCurrent;
    private float[] xy = {0, 0};

    public float[] mViewMatrix=new float[16];
    public float[] mProjectMatrix=new float[16];
    public float[] mModelMatrix=new float[16];
    public float[] mRotateMatrix=new float[16];



    private Stack<float[]> mStack;      //变换矩阵堆栈

    public VaryTools(){
        mStack=new Stack<>();
    }

    //保护现场
    public void pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16));
    }

    //恢复现场
    public void popMatrix(){
        mMatrixCurrent=mStack.pop();
    }

    public void clearStack(){
        mStack.clear();
    }

    //平移变换
    public void translate(float x,float y,float z){
        x /= mMatrixCurrent[0];
        y /= mMatrixCurrent[5];
        if (mMatrixCurrent[12] + x > mMatrixCurrent[0] - 1) {
            mMatrixCurrent[12] = mMatrixCurrent[0] - 1;
            x = 0;
        }
        if (mMatrixCurrent[12] + x < 1 - mMatrixCurrent[0]) {
            mMatrixCurrent[12] = 1 - mMatrixCurrent[0];
            x = 0;
        }
        if (mMatrixCurrent[13] + y > mMatrixCurrent[5] - 1) {
            mMatrixCurrent[13] = mMatrixCurrent[5] - 1;
            y = 0;
        }
        if (mMatrixCurrent[13] + y < 1 - mMatrixCurrent[5]) {
            mMatrixCurrent[13] = 1 - mMatrixCurrent[5];
            y = 0;
        }
        Matrix.translateM(mMatrixCurrent,0,x,y,z);
    }

    //旋转变换
    public void rotate(float angle,float x,float y,float z){
        Matrix.rotateM(mMatrixCurrent,0,angle,x,y,z);
    }

    //缩放变换
    public void scale(float x,float y,float z, float[] s){
        mMatrixCurrent[0] = s[0];
        mMatrixCurrent[5] = s[1];
        if (mMatrixCurrent[0] * x < 1) {
            mMatrixCurrent[0] = 1;
            x = 1;
        }
        if (mMatrixCurrent[5] * y < 1) {
            mMatrixCurrent[5] = 1;
            y = 1;
        }
        Matrix.scaleM(mMatrixCurrent,0,x,y,z);

        if (mMatrixCurrent[0] - 1 < mMatrixCurrent[12]) {
            float x1 = mMatrixCurrent[12] - (mMatrixCurrent[0] - 1);
            Matrix.translateM(mMatrixCurrent,0,-x1,0,0);
        } else if (mMatrixCurrent[0] - 1 < -mMatrixCurrent[12]) {
            float x1 = -mMatrixCurrent[12] - (mMatrixCurrent[0] - 1);
            Matrix.translateM(mMatrixCurrent,0, x1,0,0);
        }


        if (mMatrixCurrent[5] - 1 < mMatrixCurrent[13]) {
            float y1 = mMatrixCurrent[13] - (mMatrixCurrent[5] - 1);
            Matrix.translateM(mMatrixCurrent,0,0,-y1,0);
        } else if (mMatrixCurrent[0] - 1 < -mMatrixCurrent[13]) {
            float y1 = -mMatrixCurrent[13] - (mMatrixCurrent[5] - 1);
            Matrix.translateM(mMatrixCurrent,0, 0,y1,0);
        }
    }

    public float[] getScale() {
        return new float[]{mMatrixCurrent[0], mMatrixCurrent[5]};
    }

    public void perspectiveM(int offset,
                                    float fovy, float aspect, float zNear, float zFar) {
        float f = 1.0f / (float) Math.tan(fovy * (Math.PI / 360.0));
        float rangeReciprocal = 1.0f / (zNear - zFar);

        mMatrixProjection[offset + 0] = f / aspect;
        mMatrixProjection[offset + 1] = 0.0f;
        mMatrixProjection[offset + 2] = 0.0f;
        mMatrixProjection[offset + 3] = 0.0f;

        mMatrixProjection[offset + 4] = 0.0f;
        mMatrixProjection[offset + 5] = f;
        mMatrixProjection[offset + 6] = 0.0f;
        mMatrixProjection[offset + 7] = 0.0f;

        mMatrixProjection[offset + 8] = 0.0f;
        mMatrixProjection[offset + 9] = 0.0f;
        mMatrixProjection[offset + 10] = (zFar + zNear) * rangeReciprocal;
        mMatrixProjection[offset + 11] = -1.0f;

        mMatrixProjection[offset + 12] = 0.0f;
        mMatrixProjection[offset + 13] = 0.0f;
        mMatrixProjection[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal;
        mMatrixProjection[offset + 15] = 0.0f;
    }

    //设置相机
    public void setCamera(float ex,float ey,float ez,float cx,float cy,float cz,float ux,float uy,float uz){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,cx,cy,cz,ux,uy,uz);
    }

    public void frustum(float left,float right,float bottom,float top,float near,float far){
        Matrix.frustumM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    public void ortho(float left,float right,float bottom,float top,float near,float far){
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    public float[] getFinalMatrix(){
        float[] ans=new float[16];
        Matrix.multiplyMM(ans,0,mMatrixCamera,0,mMatrixCurrent,0);
        Matrix.multiplyMM(ans,0,mMatrixProjection,0,ans,0);
        return ans;
    }

}
