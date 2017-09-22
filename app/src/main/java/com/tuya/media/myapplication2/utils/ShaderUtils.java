package com.tuya.media.myapplication2.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by huangdaju on 2017/9/20.
 */

public class ShaderUtils {
    private static final String TAG = "ShaderUtils";

    public static int createProgram(String vexSource, String fragmentSource){
        int vexShader = loadShader(GLES20.GL_VERTEX_SHADER,vexSource);
        if (vexShader == 0){
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if (pixelShader == 0 ){
            return 0;
        }

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vexShader);
        checkGLError("glAttachShader");
        GLES20.glAttachShader(program, pixelShader);
        checkGLError("glAttachShader");
        GLES20.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Could not link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }
        return program;
    }

    public static void checkGLError(String label){
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.d("GLError","errorCode " + error + "label "+ label);
            throw new RuntimeException("label "+ label + "glError" + error);
        }
    }

    public static int loadShader(int shaderType, String source){
        int shader = GLES20.glCreateShader(shaderType);   //1. 创建shader
        if (shader != 0){
            GLES20.glShaderSource(shader,source);        //2. 上传着色器代码
            GLES20.glCompileShader(shader);              //3. 编译着色器代码
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,compiled,0);   //读取编译状态
            if (compiled[0] == 0){
                GLES20.glDeleteShader(shader);  // 如果创建编译失败，则删除这个着色器
                shader = 0;
            }
        }
        return shader;
    }

    public static String readRawTextFile(Context context, @RawRes int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
