package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Pic_ready extends AppCompatActivity {

    TextView tv_face;
    TextView tv_quiz;
    MyApp app;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_ready);

        tv_face = findViewById(R.id.tv_video);
        tv_quiz = findViewById(R.id.tv_quiz);
        app = (MyApp) getApplication();

        id = app.ID;
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        tv_face.setOnClickListener(view -> {
            Intent intent = new Intent(Pic_ready.this, Choose_quiztype.class);
            startActivity(intent);
        });

        tv_quiz.setOnClickListener(view -> {
            Intent intent = new Intent(Pic_ready.this, Quiz_mix.class);
            startActivity(intent);
        });

    }
}