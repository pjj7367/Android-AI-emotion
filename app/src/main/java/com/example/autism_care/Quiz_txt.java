package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Quiz_txt extends AppCompatActivity {
    TextView tv_answer;
    PreviewView pv_camera;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_txt);

        tv_answer = findViewById(R.id.tv_answer);
        pv_camera = findViewById(R.id.pv_camera);

        String[] emotion_list = {"기쁨", "당황", "불안", "슬픔", "분노", "상처"};

        Random random = new Random();

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
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(pv_camera.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

        preview.setSurfaceProvider(pv_camera.getSurfaceProvider());

        ViewPort viewPort = pv_camera.getViewPort();

        if (viewPort != null) {
            UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .setViewPort(viewPort)
                    .build();

            cameraProvider.unbindAll();
            CameraControl cameraControl = camera.getCameraControl();
            cameraControl.setLinearZoom((float)0.3);
        }


    }
}