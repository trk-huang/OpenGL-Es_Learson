package com.example.huangdaju.androidopengl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.huangdaju.androidopengl.activity.OpenGlActivity1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = this.findViewById(R.id.first1);
        button1.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.first1) {
            intent = new Intent(MainActivity.this, OpenGlActivity1.class);

        }

        startActivity(intent);
    }
}
