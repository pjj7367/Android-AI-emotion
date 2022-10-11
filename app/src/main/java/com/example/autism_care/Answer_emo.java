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

import java.io.IOException;

public class Answer_emo extends AppCompatActivity {

    ImageView iv_emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_emo);

        iv_emotion = findViewById(R.id.iv_emotion);

        Intent intent = getIntent();

        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        iv_emotion.setImageBitmap(image);

    }
}