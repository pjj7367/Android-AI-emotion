package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.util.concurrent.ListenableFuture;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Quiz_txt extends AppCompatActivity {

    TextView tv_answer, tv_txt_dap;
    PreviewView pv_camera;
    String imageString, id, bool;
    ProgressBar pro_cnt;
    ProgressDialog progress;
    RequestQueue queue;
    Handler handler;
    Runnable runnable;
    MyApp app;
    int predict, label, type;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    String[] emotion_list = {"당황", "불안", "슬픔", "중립", "기쁨", "분노", "상처"};
    String[] tv_dap_ok = {"당황표정 \n 정답입니다❤", "불안표정 \n 정답입니다❤", "슬픈표정 \n 정답입니다❤", "중립표정 \n 정답입니다❤", "기쁜표정 \n 정답입니다❤", "분노표정 \n 정답입니다❤", "상처표정 \n 정답입니다❤"};
    String tv_dap_no = "틀렸습니다!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_txt);

        tv_answer = findViewById(R.id.tv_answer);
        tv_txt_dap = findViewById(R.id.tv_txt_dap);
        pv_camera = findViewById(R.id.pv_camera);

        pro_cnt = findViewById(R.id.pro_cnt);
        pro_cnt.setProgress(0);
        pro_cnt.setMax(3000);

        app = (MyApp) getApplication();
        id = app.ID;

        type = 2;


        Random random = new Random();
        label = random.nextInt(6);

        tv_answer.setText(emotion_list[label]);

        // TODO : 1. '내 카메라' 부분에 Surface 프리뷰 띄우기

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));

        // TODO : 2. Surface 프리뷰 캡쳐하기
        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                pro_cnt.setProgress(pro_cnt.getProgress() + 500);

                if(pro_cnt.getProgress() == 3000){
                    Bitmap bitmap = pv_camera.getBitmap();

                    if (bitmap != null) {
                        // need to do tasks on the UI thread
                        Log.d("capture loop", "run test");

                        progress = new ProgressDialog(Quiz_txt.this);
                        progress.setMessage("Uploading...");
                        progress.show();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();


                        // bitmap -> compress를 사용해서 압축 -> stream에 담기
                        // stream -> bytearray로 변환
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Log.e("Question_emo", "이미지 크기 : " + byteArray);

                        sendImage(byteArray);
                    } else {
                        Log.d("capture loop", "no bitmap");
                    }
                }

                handler.postDelayed(this, 500);
            }
        };

        // 첫 화면 자동 실행 2초 딜레이
        handler.postDelayed(runnable, 2000);


        tv_txt_dap.setOnClickListener(v -> {
            tv_txt_dap.setVisibility(View.INVISIBLE);
            label = random.nextInt(6);
            tv_answer.setText(emotion_list[label]);
            handler.postDelayed(runnable, 2000);
        });
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(pv_camera.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

        pv_camera.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);
        pv_camera.setScaleType(PreviewView.ScaleType.FILL_CENTER);
    }

    public void quiz_btn(String mode) {
        if (mode.equals("False")) {
            // db에 정답 넣기
            String id = app.ID;

            String flask_url = "http://192.168.0.12:5000/quiz_btn";

            StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                    response -> {
                        Log.e("flask", "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            String status_json = obj.optString("status");

                            // expected OK
                            Log.e("flask", "status: " + status_json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("QUIZ", "quiz_btn: " + error);
                        Toast.makeText(Quiz_txt.this, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("mode", mode);
                    params.put("id", id);
                    params.put("bool", bool);
                    params.put("emotion", String.valueOf(label));
                    params.put("type", String.valueOf(type));

                    return params;
                }
            };

            queue = Volley.newRequestQueue(Quiz_txt.this);
            queue.add(request);
        }
    }

    //이미지 flask로 전송
    private void sendImage(byte[] byteArray) {
        progress.dismiss();
        //비트맵 이미지를 byte로 변환 -> base64형태로 변환
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //base64형태로 변환된 이미지 데이터를 플라스크 서버로 전송
        String flask_url = "http://192.168.0.12:5000/run_model";

        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    Log.e("flask", "sendImage: " + response);

                    try {
                        progress.dismiss();
                        pro_cnt.setProgress(0);

                        JSONObject obj = new JSONObject(response);
                        predict = obj.optInt("predict");
                        Log.e("flask", "predict: " + predict);
                        Log.e("android", "label: " + label);

                        if (predict == label) {
                            bool = "True";
                            quiz_btn("False");
                            tv_txt_dap.setText(tv_dap_ok[predict]);
                            tv_txt_dap.setVisibility(View.VISIBLE);
                        } else {
                            bool = "False";
                            quiz_btn("False");
                            tv_txt_dap.setText(tv_dap_no);
                            tv_txt_dap.setVisibility(View.VISIBLE);
                        }

                        // 반복 멈춰!
                        handler.removeCallbacksAndMessages(null);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Quiz_txt.this, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                    Log.e("Quiz_txt", "sendImage: " + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString);

                return params;
            }
        };

        queue = Volley.newRequestQueue(Quiz_txt.this);
        queue.add(request);
    }
}