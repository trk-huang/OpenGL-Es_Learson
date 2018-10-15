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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MonitorTextureRender implements GLSurfaceView.Renderer {


    private int mProgram, glHPosition, glHTexture, glHCoordinate, glHMatrix;
    private Bitmap bitmap;
    private int textureId;

    //顶点坐标
    private final float[] sPos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };
    //纹理坐标
    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
    };
    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    private Context mContext;

    private VaryTools varyTools;

    public MonitorTextureRender(Context mContext) {
        this.mContext = mContext;
        varyTools = new VaryTools();
        initBuffer();
    }

    public void setImageBuffer(int[] buffer, int width, int height) {
        bitmap = Bitmap.createBitmap(buffer, width, height, Bitmap.Config.RGB_565);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //将背景设置为灰色
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        mProgram = ShaderUtils.createProgram(mContext, "texture_vertex_shader", "texture_frag_shader");
        glHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        glHCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate");
        glHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        glHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");

    }

    private void initBuffer() {
        //申请底层空间
        ByteBuffer bb = ByteBuffer.allocateDirect(sPos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        bPos = bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc = ByteBuffer.allocateDirect(sCoord.length * 4);
        cc.order(ByteOrder.nativeOrder());
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        bCoord = cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float sWH = w / (float) h;
        float sWidthHeight = width / (float) height;
        if (width > height) {
            if (sWH > sWidthHeight) {
                varyTools.ortho(-sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 5);
            } else {
                varyTools.ortho(-sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 5);
            }
        } else {
            if (sWH > sWidthHeight) {
                varyTools.ortho(-1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 5);
            } else {
                varyTools.ortho(-1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 5);
            }
        }
        //设置相机位置
        varyTools.setCamera(0, 0, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


        //计算变换矩阵
        varyTools.getFinalMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(glHMatrix, 1, false, varyTools.getFinalMatrix(), 0);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        textureId = createTexture();
        //传入顶点坐标
        GLES20.glVertexAttribPointer(glHPosition, 2, GLES20.GL_FLOAT, false, 0, bPos);
        //传入纹理坐标
        GLES20.glVertexAttribPointer(glHCoordinate, 2, GLES20.GL_FLOAT, false, 0, bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }


    private int createTexture() {
        int[] texture = new int[1];
        if (bitmap != null && !bitmap.isRecycled()) {
            //生成纹理
            GLES20.glGenTextures(1, texture, 0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            return texture[0];
        }
        return 0;
    }


    public void move(float v, float v1) {
        varyTools.translate(v, v1, 0);
    }

    public void scale(float scale) {
        varyTools.scale(scale, scale, 0);
    }

}
