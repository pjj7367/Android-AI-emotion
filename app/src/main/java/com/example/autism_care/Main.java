package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    TextView tv_face, tv_emotion, tv_mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_face = findViewById(R.id.tv_video);
        tv_emotion = findViewById(R.id.tv_emotion);
        tv_mypage = findViewById(R.id.tv_mypage);


        tv_face.setOnClickListener(view -> {
            Intent intent = new Intent(Main.this, Pic_ready.class);
            startActivity(intent);
        });

        tv_emotion.setOnClickListener(view -> {
            Intent intent = new Intent(Main.this, Choose_cameratype.class);
            startActivity(intent);
        });

        tv_mypage.setOnClickListener(view -> {
            Intent intent = new Intent(Main.this, Mypage.class);
            startActivity(intent);
        });

    }
}