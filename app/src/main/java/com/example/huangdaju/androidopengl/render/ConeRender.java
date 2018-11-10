package com.example.huangdaju.androidopengl.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.huangdaju.androidopengl.uitls.ShaderUtils;
import com.example.huangdaju.androidopengl.uitls.VaryTools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ConeRender implements GLSurfaceView.Renderer {

    private FloatBuffer vertexBuffer, colorBuffer;
    private String vertexShaderCode, fragmentShaderCode;
    private ShortBuffer indexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    private int mProgram;

    private int n=360;  //切割份数
    private float height=2.0f;  //圆锥高度
    private float radius=1.0f;  //圆锥底面半径
    private float[] colors={1.0f,1.0f,1.0f,1.0f};

    float color[] = {
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
            1f,0f,0f,1f,
    };

    private int mPositionHandle;
    private int mColorHandle;
    private int mMatrixHandle;

    //顶点之间的偏移量
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 每个顶点四个字节

    private Context mContext;

    private VaryTools mVaryTools;

    private int vSize;

    private int step = 1;

    public ConeRender(Context mContext) {
        this.mContext = mContext;
        mVaryTools = new VaryTools();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //将背景设置为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

        ArrayList<Float> pos=new ArrayList<>();
        //圆锥
//        pos.add(0.0f);
//        pos.add(0.0f);
//        pos.add(height);
//        float angDegSpan=360f/n;
//        //圆柱
//        for(float i=0;i<360+angDegSpan;i+=angDegSpan){
//            pos.add((float) (radius*Math.sin(i*Math.PI/180f)));
//            pos.add((float)(radius*Math.cos(i*Math.PI/180f)));
//            pos.add(height);
//            pos.add((float) (radius*Math.sin(i*Math.PI/180f)));
//            pos.add((float)(radius*Math.cos(i*Math.PI/180f)));
//            pos.add(0.0f);
//        }
        //球体
        float r1,r2;
        float h1,h2;
        float sin,cos;
        for(float i=-90;i<90+step;i+=step){
            r1 = (float)Math.cos(i * Math.PI / 180.0);
            r2 = (float)Math.cos((i + step) * Math.PI / 180.0);
            h1 = (float)Math.sin(i * Math.PI / 180.0);
            h2 = (float)Math.sin((i + step) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            float step2=step*2;
            for (float j = 0.0f; j <360.0f+step; j +=step2 ) {
                cos = (float) Math.cos(j * Math.PI / 180.0);
                sin = -(float) Math.sin(j * Math.PI / 180.0);

                pos.add(r2 * cos);
                pos.add(h2);
                pos.add(r2 * sin);
                pos.add(r1 * cos);
                pos.add(h1);
                pos.add(r1 * sin);
            }
        }

        float[] d=new float[pos.size()];
        for (int i=0;i<d.length;i++){
            d[i]=pos.get(i);
        }
        vSize=d.length / 3;
        ByteBuffer buffer=ByteBuffer.allocateDirect(d.length * 4);
        buffer.order(ByteOrder.nativeOrder());
        vertexBuffer=buffer.asFloatBuffer();
        vertexBuffer.put(d);
        vertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                color.length * 4);
        dd.order(ByteOrder.nativeOrder());
        colorBuffer = dd.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

        vertexShaderCode = ShaderUtils.getVertexShader(mContext, "cube_vertex_shader");
        fragmentShaderCode = ShaderUtils.getFragShader(mContext, "cube_frag_shader");
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
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //计算宽高比
        float ratio=(float)width/height;
        //设置透视投影
        mVaryTools.frustum(-ratio, ratio, -1, 1, 3, 20);
        //设置相机位置
        mVaryTools.setCamera(5.0f, 5.0f, 10.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        mVaryTools.getFinalMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //将程序加入到OpenGLES2.0环境
        GLES20.glUseProgram(mProgram);
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandle= GLES20.glGetUniformLocation(mProgram,"vMatrix");
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandle,1,false,mVaryTools.getFinalMatrix(),0);
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,vSize);
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, colors, 0);
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize);

        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
