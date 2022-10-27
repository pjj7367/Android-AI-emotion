package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Quiz_txt extends AppCompatActivity {

    TextView tv_answer;
    ImageView iv_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_txt);

        tv_answer = findViewById(R.id.tv_answer);
        iv_camera = findViewById(R.id.iv_camera);

        String[] emotion_list = {"기쁨", "당황", "불안", "슬픔", "분노", "상처"};

        Random random = new Random();


        iv_camera.setOnClickListener(view -> {
            int index = random.nextInt(emotion_list.length);
            tv_answer.setText(emotion_list[index]);

            Toast.makeText(this, emotion_list[index], Toast.LENGTH_SHORT).show();
        });



    }
}