package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Pic_ready extends AppCompatActivity {

    TextView tv_face;
    TextView tv_quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_ready);

        tv_face = findViewById(R.id.tv_face);
        tv_quiz = findViewById(R.id.tv_quiz);

        tv_face.setOnClickListener(view -> {
            Intent intent = new Intent(Pic_ready.this, Live_camera.class);
            startActivity(intent);
        });

        tv_quiz.setOnClickListener(view -> {
            Intent intent = new Intent(Pic_ready.this, Pic_quiz.class);
            startActivity(intent);
        });

    }
}