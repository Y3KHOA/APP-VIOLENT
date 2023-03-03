package com.example.appviolent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void btn_notification_click(View v)
    {
        startActivity(new Intent(HomePage.this,Notification.class));
    }public void btn_list_click(View v)
    {
        startActivity(new Intent(HomePage.this,ListFolderVideo.class));
    }public void btn_screenCam_click(View v)
    {
        startActivity(new Intent(HomePage.this,ScreenCam.class));
    }public void btn_info_click(View v)
    {
        startActivity(new Intent(HomePage.this,Info.class));
    }
}