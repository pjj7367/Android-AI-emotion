package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Quiz_btn extends AppCompatActivity {

    ImageView iv_emotion;
    TextView tv_happy, tv_embarrassed, tv_anxious, tv_sad, tv_angry, tv_hurt, tv_dap;
    int select, emotion;


    String[] tv_dap_ok = {"기쁜표정 \n 정답입니다❤","불안표정 \n 정답입니다❤","당황표정 \n 정답입니다❤","슬픈표정 \n 정답입니다❤","분노표정 \n 정답입니다❤","상처표정 \n 정답입니다❤"};
    String[] tv_dap_no = {"틀렸습니다 \n 답은 기쁨!","틀렸습니다 \n 답은 불안!","틀렸습니다 \n 답은 당황!","틀렸습니다 \n 답은 슬픔!","틀렸습니다 \n 답은 분노!","틀렸습니다 \n 답은 상처!",};
    // 통신
    Bitmap bitmap;
    String imageString;
    ProgressDialog progress;
    RequestQueue queue;
    MyApp app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_btn);

        select = 0;
        app = (MyApp) getApplication();

        iv_emotion = findViewById(R.id.iv_emotion);
        tv_happy = findViewById(R.id.tv_happy);
        tv_embarrassed = findViewById(R.id.tv_embarrassed);
        tv_anxious = findViewById(R.id.tv_anxious);
        tv_sad = findViewById(R.id.tv_sad);
        tv_angry = findViewById(R.id.tv_angry);
        tv_hurt = findViewById(R.id.tv_hurt);
        tv_dap = findViewById(R.id.tv_txt_dap);

        tv_dap.setVisibility(View.INVISIBLE);

        quiz_btn(select, "True");

        tv_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 0;

                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }

            }
        });

        tv_embarrassed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 1;


                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_anxious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 2;


                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               select = 3;

                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 4;

                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_hurt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 5;

                if (emotion == select){
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_ok[select]);
                    tv_dap.setVisibility(View.VISIBLE);
                } else {
                    quiz_btn(select, "True");
                    tv_dap.setText(tv_dap_no[emotion]);
                    tv_dap.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_dap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_dap.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void quiz_btn(int select, String mode) {
        if (mode.equals("True")) { //사진 받아오기
            // 2. response로 받은 값 저장(image, emotion)
            // 3. image(base64) to bitmap
            // 4. iv_emotion 을 setBitmap

            //비트맵 이미지를 byte로 변환 -> base64형태로 변환
            // imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //base64형태로 변환된 이미지 데이터를 플라스크 서버로 전송
            String flask_url = "http://192.168.0.12:5000/quiz_btn";
            progress = new ProgressDialog(Quiz_btn.this);
            progress.setMessage("Uploading...");
            progress.show();

            StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                    response -> {
                        progress.dismiss();
                        Log.e("flask", "sendImage: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            emotion = obj.optInt("emotion");
                            String base64 = obj.optString("image");

                            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                            Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            iv_emotion.setImageBitmap(image);

                            Log.e("flask", "emotion: " + emotion);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("QUIZ", "quiz_btn: " + error);
                        progress.dismiss();
                        Toast.makeText(Quiz_btn.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("mode", mode);

                    return params;
                }
            };

            queue = Volley.newRequestQueue(Quiz_btn.this);
            queue.add(request);

        } else if(mode.equals("False")){ // db에 정답 넣기
            String id = app.ID;
        }
    }
}