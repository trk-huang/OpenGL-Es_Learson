package com.example.huangdaju.androidopengl.uitls;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderUtils {


    public static String getVertexShader(Context context, String fileName) {
        return FileUtils.readAssetsTxt(context, fileName);
    }


    public static String getFragShader(Context context, String fileName) {
        return FileUtils.readAssetsTxt(context, fileName);
    }


    public static int loadShader(int shaderType, String source) {

        // Create the shader object
        int shader = GLES20.glCreateShader(shaderType);
        if (shader == 0) {
            throw new RuntimeException("Error create shader.");
        }
        int[] compiled = new int[1];
        // Load the shader source
        GLES20.glShaderSource(shader, source);
        // Compile the shader
        GLES20.glCompileShader(shader);
        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Error compile shader: " +
                    GLES20.glGetShaderInfoLog(shader));
        }
        return shader;
    }


    public static int createProgram(Context context, String vertextShaderName, String fragShaderName) {

        String vertexShaderCode = ShaderUtils.getVertexShader(context, vertextShaderName);
        String fragmentShaderCode = ShaderUtils.getFragShader(context, fragShaderName);

        int vertextShader = ShaderUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragShader = ShaderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        //创建一个空的OpenGLES程序
        int mProgram = GLES20.glCreateProgram();
        //将顶点着色器加入到程序
        GLES20.glAttachShader(mProgram, vertextShader);
        //将片元着色器加入到程序中
        GLES20.glAttachShader(mProgram, fragShader);
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram);

        return mProgram;
    }
}
