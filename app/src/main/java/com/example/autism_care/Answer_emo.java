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

        String[] list = {"๊ธฐ์จ๐์ด", "๋ถ์๐ฐ์ด", "๋ถ๋ธ๐ก๊ฐ", "์ค๋ฆฝ๐์ด", "๋นํฉ๐ฐ์ด", "์์ฒ๐ฅ๊ฐ", "์ฌํ๐ญ์ด"};

        // ๋์ด์จ๊ฒ ๊ฐ result์ด 1
        // ar[] = 'happy', 'anxious', 'angry', 'neutral', 'embarrassed', 'hurt', 'sad'
        // emo = ar[result]
        iv_emotion.setImageBitmap(image);
        tv_answer.setText(list[emo]+" ๋๊ปด์ ธ์");

    }
}