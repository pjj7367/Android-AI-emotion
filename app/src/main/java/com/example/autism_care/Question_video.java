package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class Question_video extends AppCompatActivity {

    TextView tv_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_video);

        tv_answer = findViewById(R.id.tv_answer);

//        String content = tv_answer.getText().toString(); //텍스트 가져옴.
//        SpannableString spannableString = new SpannableString(content); //객체 생성
//
//        String word ="&quot;슬픔\uD83D\uDE2D&quot;";
//        int start = content.indexOf(word);
//        int end = start + word.length();
//
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new RelativeSizeSpan(1.3f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv_answer.setText(spannableString);

    }
}