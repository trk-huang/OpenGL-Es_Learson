package com.example.huangdaju.androidopengl.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.huangdaju.androidopengl.uitls.ShaderUtils;
import com.example.huangdaju.androidopengl.uitls.VaryTools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SkyRender implements GLSurfaceView.Renderer {

    private Bitmap mBitmap;
    private VaryTools mVaryTools;
    private float r = 2f; // 球的半径
    private int mProgram;

    private float radius = 2f;
    final double angleSpan = Math.PI / 90f;// 将球进行单位切分的角度
    int vCount = 0;// 顶点个数，先初始化为0
    private FloatBuffer posBuffer;
    private FloatBuffer cooBuffer;

    private Context mContext;

    private String vertexShaderCode, fragmentShaderCode;

    private int mHUTexture;
    private int mHProjMatrix;
    private int mHViewMatrix;
    private int mHModelMatrix;
    private int mHRotateMatrix;
    private int mHPosition;
    private int mHCoordinate;

    private int textureId;


    public SkyRender(Context context, Bitmap mBitmap) {
        this.mVaryTools = new VaryTools();
        mContext = context;
        this.mBitmap = mBitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);

        vertexShaderCode = ShaderUtils.getVertexShader(mContext, "sky_vertex_shader");
        fragmentShaderCode = ShaderUtils.getFragShader(mContext, "sky_frag_shader");

        int vertextShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);


        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertextShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);

        mHProjMatrix = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mHViewMatrix = GLES20.glGetUniformLocation(mProgram, "uViewMatrix");
        mHModelMatrix = GLES20.glGetUniformLocation(mProgram, "uModelMatrix");
        mHRotateMatrix = GLES20.glGetUniformLocation(mProgram, "uRotateMatrix");
        mHUTexture = GLES20.glGetUniformLocation(mProgram, "uTexture");
        mHPosition = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mHCoordinate = GLES20.glGetAttribLocation(mProgram, "aCoordinate");
        textureId=createTexture();
        calculateAttribute();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        //计算宽高比
        float ratio = (float) width / height;
        //设置透视投影
        mVaryTools.perspectiveM(0, 45, ratio, 1f, 300);
        //设置相机位置
        mVaryTools.setCamera( 0f, 0.0f,5.0f, 0.0f, 0.0f,-1.0f, 0f,1.0f, 0.0f);
        //模型矩阵
        Matrix.setIdentityM(mVaryTools.mModelMatrix,0);


    }

    public void setMatrix(float[] matrix){
        System.arraycopy(matrix,0,mVaryTools.mRotateMatrix,0,16);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(1,1,1,1);
        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(mHProjMatrix,1,false,mVaryTools.mMatrixProjection,0);
        GLES20.glUniformMatrix4fv(mHViewMatrix,1,false,mVaryTools.mMatrixCamera,0);
        GLES20.glUniformMatrix4fv(mHModelMatrix,1,false,mVaryTools.mModelMatrix,0);
        GLES20.glUniformMatrix4fv(mHRotateMatrix,1,false,mVaryTools.mRotateMatrix,0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId);

        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3,GLES20.GL_FLOAT,false,0,posBuffer);
        GLES20.glEnableVertexAttribArray(mHCoordinate);
        GLES20.glVertexAttribPointer(mHCoordinate,2,GLES20.GL_FLOAT,false,0,cooBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);

        GLES20.glDisableVertexAttribArray(mHPosition);
    }

    private void calculateAttribute() {
        ArrayList<Float> alVertix = new ArrayList<>();
        ArrayList<Float> textureVertix = new ArrayList<>();
        for (double vAngle = 0; vAngle < Math.PI; vAngle = vAngle + angleSpan) {

            for (double hAngle = 0; hAngle < 2 * Math.PI; hAngle = hAngle + angleSpan) {
                float x0 = (float) (radius * Math.sin(vAngle) * Math.cos(hAngle));
                float y0 = (float) (radius * Math.sin(vAngle) * Math.sin(hAngle));
                float z0 = (float) (radius * Math.cos((vAngle)));

                float x1 = (float) (radius * Math.sin(vAngle) * Math.cos(hAngle + angleSpan));
                float y1 = (float) (radius * Math.sin(vAngle) * Math.sin(hAngle + angleSpan));
                float z1 = (float) (radius * Math.cos(vAngle));

                float x2 = (float) (radius * Math.sin(vAngle + angleSpan) * Math.cos(hAngle + angleSpan));
                float y2 = (float) (radius * Math.sin(vAngle + angleSpan) * Math.sin(hAngle + angleSpan));
                float z2 = (float) (radius * Math.cos(vAngle + angleSpan));

                float x3 = (float) (radius * Math.sin(vAngle + angleSpan) * Math.cos(hAngle));
                float y3 = (float) (radius * Math.sin(vAngle + angleSpan) * Math.sin(hAngle));
                float z3 = (float) (radius * Math.cos(vAngle + angleSpan));

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

                float s0 = (float) (hAngle / Math.PI / 2);
                float s1 = (float) ((hAngle + angleSpan) / Math.PI / 2);
                float t0 = (float) (vAngle / Math.PI);
                float t1 = (float) ((vAngle + angleSpan) / Math.PI);

                textureVertix.add(s1);// x1 y1对应纹理坐标
                textureVertix.add(t0);
                textureVertix.add(s0);// x0 y0对应纹理坐标
                textureVertix.add(t0);
                textureVertix.add(s0);// x3 y3对应纹理坐标
                textureVertix.add(t1);

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);

                textureVertix.add(s1);// x1 y1对应纹理坐标
                textureVertix.add(t0);
                textureVertix.add(s0);// x3 y3对应纹理坐标
                textureVertix.add(t1);
                textureVertix.add(s1);// x2 y3对应纹理坐标
                textureVertix.add(t1);
            }
        }
        vCount = alVertix.size() / 3;
        posBuffer = convertToFloatBuffer(alVertix);
        cooBuffer = convertToFloatBuffer(textureVertix);
    }

    private FloatBuffer convertToFloatBuffer(ArrayList<Float> data) {
        float[] d = new float[data.size()];
        for (int i = 0; i < d.length; i++) {
            d[i] = data.get(i);
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.size() * 4);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer ret = buffer.asFloatBuffer();
        ret.put(d);
        ret.position(0);
        return ret;
    }

    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }
}
