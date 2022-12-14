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

    String[] tv_dap_ok = {"๋นํฉ๐ฆ์ด ๋๊ปด์ ธ์", "๋ถ์๐คจ์ด ๋๊ปด์ ธ์", "์ฌํ๐ญ์ด ๋๊ปด์ ธ์", "์ค๋ฆฝ๐์ด ๋๊ปด์ ธ์", "๊ธฐ์จ๐์ด ๋๊ปด์ ธ์", "๋ถ๋ธ๐ก๊ฐ ๋๊ปด์ ธ์", "์์ฒ๐ข๊ฐ ๋๊ปด์ ธ์"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_video);

        tv_answer = findViewById(R.id.tv_answer);
        pv_camera = findViewById(R.id.pv_camera);

        // TODO : 1. '๋ด ์นด๋ฉ๋ผ' ๋ถ๋ถ์ Surface ํ๋ฆฌ๋ทฐ ๋์ฐ๊ธฐ

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

        // TODO : 2. Surface ํ๋ฆฌ๋ทฐ ์บก์ณํ๊ธฐ
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


                    // bitmap -> compress๋ฅผ ์ฌ์ฉํด์ ์์ถ -> stream์ ๋ด๊ธฐ
                    // stream -> bytearray๋ก ๋ณํ
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Log.e("Question_emo", "์ด๋ฏธ์ง ํฌ๊ธฐ : " + byteArray);

                    sendImage(byteArray);
                } else {
                    Log.d("capture loop", "no bitmap");
                }

                handler.postDelayed(this, 20000);
            }
        };


        handler.postDelayed(runnable, 5000);
        // ์ฒซ ํ๋ฉด ์๋ ์คํ
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

    //์ด๋ฏธ์ง flask๋ก ์ ์ก
    private void sendImage(byte[] byteArray) {
        //๋นํธ๋งต ์ด๋ฏธ์ง๋ฅผ byte๋ก ๋ณํ -> base64ํํ๋ก ๋ณํ
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //base64ํํ๋ก ๋ณํ๋ ์ด๋ฏธ์ง ๋ฐ์ดํฐ๋ฅผ ํ๋ผ์คํฌ ์๋ฒ๋ก ์ ์ก
        String flask_url = "http://43.200.131.5:5000/run_model";

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

        request.setRetryPolicy((new com.android.volley.DefaultRetryPolicy(
                70000,
                1,
                4)));

        queue = Volley.newRequestQueue(Question_video.this);
        queue.add(request);



    }
}