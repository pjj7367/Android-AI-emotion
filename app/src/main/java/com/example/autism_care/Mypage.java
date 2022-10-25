package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Mypage extends AppCompatActivity {

    // data : {'id':string, 'emotion':int, 'bool':BOOL, 'date':SYSDATE, 'type':int}
    // emotion -> 0:기쁨, 1:당황, 2:불안, 3:슬픔, 4:분노, 5:상처
    // quiz -> 0:얼굴로따라하기(사진+글씨), 1:얼굴로따라하기(사진), 2:얼굴로따라하기(글씨), 3:퀴즈로풀기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
    }
}