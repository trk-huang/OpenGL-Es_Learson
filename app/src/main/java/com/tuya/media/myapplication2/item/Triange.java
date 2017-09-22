package com.tuya.media.myapplication2.item;

import android.content.Context;
import android.opengl.GLES20;

import com.tuya.media.myapplication2.R;
import com.tuya.media.myapplication2.utils.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by huangdaju on 2017/9/21.
 */

public class Triange {
    private FloatBuffer vertexBuffer;
    private int programId;
    private int mPositionHandle;
    private int mColorHandle;
    private int uMatrixHandle;

    private static final int COORDS_PER_VERTEX = 3;

    private final float[] vertexData = {
            0f, 0f, 0f,
            1f, -1f, 0f,
            1f, 1f, 0f
    };

    // 设置三角形颜色和透明度（r,g,b,a）
    float color[] = {0.0f, 1.0f, 0f, 1.0f};//绿色不透明

    public Triange(Context context) {
        String vertexShader = ShaderUtils.readRawTextFile(context, R.raw.vertex_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(context, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);
        mPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        mColorHandle = GLES20.glGetUniformLocation(programId, "aColor");
        uMatrixHandle=GLES20.glGetUniformLocation(programId,"uMatrix");
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);
    }

    public void drawFrame(float[] mvpMatrix) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(programId);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer);
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, COORDS_PER_VERTEX);
        // 禁用指向三角形的顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
