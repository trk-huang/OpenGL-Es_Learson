package com.example.huangdaju.androidopengl.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.huangdaju.androidopengl.R;
import com.example.huangdaju.androidopengl.widget.CubMonitor;

public class CubeActivity extends AppCompatActivity implements SensorEventListener {

    private float[] matrix=new float[16];
    private CubMonitor monitor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube);
        monitor = findViewById(R.id.sdww);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorManager.getRotationMatrixFromVector(matrix,event.values);
        monitor.setMatrix(matrix);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
