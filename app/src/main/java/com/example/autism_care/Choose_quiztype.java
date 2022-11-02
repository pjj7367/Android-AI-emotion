package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Choose_quiztype extends AppCompatActivity {

    TextView tv_mix, tv_img, tv_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        tv_mix = findViewById(R.id.tv_mix);
        tv_img = findViewById(R.id.tv_img);
        tv_txt = findViewById(R.id.tv_txt);


        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);

        tv_mix.setOnClickListener(view -> {
            Intent intent = new Intent(Choose_quiztype.this, Quiz_mix.class);
            startActivity(intent);
        });

        tv_img.setOnClickListener(view -> {
            Intent intent = new Intent(Choose_quiztype.this, Quiz_img.class);
            startActivity(intent);
        });

        tv_txt.setOnClickListener(view -> {
            Intent intent = new Intent(Choose_quiztype.this, Quiz_txt.class);
            startActivity(intent);
        });





    }
}