package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Question_emo extends AppCompatActivity {


    LinearLayout ll_upload;
    Bitmap bitmap;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_emo);

        ll_upload = findViewById(R.id.ll_upload);

        ll_upload.setOnClickListener(view -> {
            Intent send_img = new Intent();
            send_img.setType("image/*");
            send_img.setAction(Intent.ACTION_GET_CONTENT); // gallery open

            startActivityForResult(Intent.createChooser(send_img, "Select Picture"), PICK_IMAGE);
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            // action
            Toast.makeText(this, "submitted image", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Question_emo.this, Answer_emo.class);

            Uri imageSelected = data.getData();

            //TODO: flask code
            // flask에서 파일로 받을건지, bytearray로 받을건지 생각해볼것!


            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try {
                // Uri img를 bitmap으로 변환
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageSelected);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // bitmap -> compress를 사용해서 압축 -> stream에 담기
            // stream -> bytearray로 변환
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Log.e("Question_emo", "이미지 크기 : " + byteArray);
            intent.putExtra("image", byteArray);
            startActivity(intent);
        }
    }
}