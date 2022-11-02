package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Question_video extends AppCompatActivity {

    TextView tv_answer;
    PreviewView pv_camera;
    String imageString;
    ProgressDialog progress;
    RequestQueue queue;
    Handler handler;
    Runnable runnable;
    int predict;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    String[] tv_dap_ok = {"당황😦이 느껴져요", "불안🤨이 느껴져요", "슬픔😭이 느껴져요", "중립😐이 느껴져요", "기쁨😄이 느껴져요", "분노😡가 느껴져요", "상처😢가 느껴져요"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_video);

        tv_answer = findViewById(R.id.tv_answer);
        pv_camera = findViewById(R.id.pv_camera);

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
                Bitmap bitmap = pv_camera.getBitmap();

                if (bitmap != null) {
                    // need to do tasks on the UI thread
                    Log.d("capture loop", "run test");

                    progress = new ProgressDialog(Question_video.this);
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

                handler.postDelayed(this, 20000);
            }
        };


        handler.postDelayed(runnable, 5000);
        // 첫 화면 자동 실행
        handler.post(runnable);
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(pv_camera.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

        pv_camera.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);
        pv_camera.setScaleType(PreviewView.ScaleType.FILL_CENTER);
    }

    //이미지 flask로 전송
    private void sendImage(byte[] byteArray) {
        //비트맵 이미지를 byte로 변환 -> base64형태로 변환
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //base64형태로 변환된 이미지 데이터를 플라스크 서버로 전송
        String flask_url = "http://192.168.0.12:5000/run_model";

        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    progress.dismiss();
                    Log.e("flask", "sendImage: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        predict = obj.optInt("predict");
                        Log.e("flask", "predict: " + predict);

                        tv_answer.setText(tv_dap_ok[predict]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Question_video.this, "Some error occurred -> " + error, Toast.LENGTH_LONG).show();
                    Log.e("Quiz_txt", "sendImage: " + error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", imageString);

                return params;
            }
        };

        queue = Volley.newRequestQueue(Question_video.this);
        queue.add(request);



    }
}