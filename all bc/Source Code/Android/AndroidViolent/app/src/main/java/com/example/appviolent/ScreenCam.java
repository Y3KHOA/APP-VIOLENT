package com.example.appviolent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ScreenCam extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_cam);
    }

    public void btn_rollback(View v)
    {
        startActivity(new Intent(ScreenCam.this,HomePage.class));
    }
}