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
}
