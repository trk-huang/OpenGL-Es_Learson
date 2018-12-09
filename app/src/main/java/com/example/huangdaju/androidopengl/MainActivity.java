package com.example.huangdaju.androidopengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.huangdaju.androidopengl.activity.CameraActivity;
import com.example.huangdaju.androidopengl.activity.CubeActivity;
import com.example.huangdaju.androidopengl.activity.OpenGlActivity1;
import com.example.huangdaju.androidopengl.activity.OpenGlActivity2;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = this.findViewById(R.id.first1);
        button1.setOnClickListener(this);
        button2 = this.findViewById(R.id.first2);
        button2.setOnClickListener(this);
        button3 = this.findViewById(R.id.first3);
        button3.setOnClickListener(this);
        button4 = this.findViewById(R.id.first4);
        button4.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.first1) {
            intent = new Intent(MainActivity.this, OpenGlActivity1.class);
        }else if (view.getId() == R.id.first2){
            intent = new Intent(MainActivity.this, OpenGlActivity2.class);
        }else if (view.getId() == R.id.first3){
            intent = new Intent(MainActivity.this, CameraActivity.class);
        }else if (view.getId() == R.id.first4){
            intent = new Intent(MainActivity.this, CubeActivity.class);
        }

        startActivity(intent);
    }
}
