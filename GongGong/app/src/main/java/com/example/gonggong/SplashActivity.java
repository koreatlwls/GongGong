package com.example.gonggong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
// 어플리케이션 시작 시 로딩화면을 위한 액티비티 수정 필요 X
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

        finish();
    }
}