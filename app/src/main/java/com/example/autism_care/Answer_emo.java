package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class Answer_emo extends AppCompatActivity {

    ImageView iv_emotion;
    TextView tv_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_emo);

        iv_emotion = findViewById(R.id.iv_emotion);
        tv_answer = findViewById(R.id.tv_answer);

        Intent intent = getIntent();

        byte[] arr = getIntent().getByteArrayExtra("image");
        int emo = getIntent().getIntExtra("emotion", 0);
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        String[] list = {"기쁨", "불안", "분노", "중립", "당황", "상처", "슬픔"};

        // 넘어온게 값 result이 1
        // ar[] = 'happy', 'anxious', 'angry', 'neutral', 'embarrassed', 'hurt', 'sad'
        // emo = ar[result]
        iv_emotion.setImageBitmap(image);
        tv_answer.setText(list[emo]+"이(가) 느껴져요");

    }
}