package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz_mix extends AppCompatActivity {

    TextView tv_happy, tv_embarrassed, tv_anxious, tv_sad, tv_angry, tv_hurt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mix);

        tv_happy = findViewById(R.id.tv_happy);
        tv_embarrassed = findViewById(R.id.tv_embarrassed);
        tv_anxious = findViewById(R.id.tv_anxious);
        tv_sad = findViewById(R.id.tv_sad);
        tv_angry = findViewById(R.id.tv_angry);
        tv_hurt = findViewById(R.id.tv_hurt);

        tv_happy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "기쁨 선택", Toast.LENGTH_LONG).show();
            }
        });






    }
}