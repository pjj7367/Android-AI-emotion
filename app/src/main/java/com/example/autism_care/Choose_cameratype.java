package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Choose_cameratype extends AppCompatActivity {

    TextView tv_picture, tv_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cameratype);

        tv_video = findViewById(R.id.tv_video);
        tv_picture = findViewById(R.id.tv_picture);


        tv_video.setOnClickListener(view -> {
            Intent intent = new Intent(Choose_cameratype.this, Question_video.class);
            startActivity(intent);
        });

        tv_picture.setOnClickListener(view -> {
            Intent intent = new Intent(Choose_cameratype.this, Question_img.class);
            startActivity(intent);
        });
    }
}