package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.net.MediaType;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Question_img extends AppCompatActivity {


    LinearLayout ll_upload;
    Bitmap bitmap;
    String imageString;
    ProgressDialog progress;
    RequestQueue queue;
    Intent intent;

    int predict;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_img);

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
            progress = new ProgressDialog(Question_img.this);
            progress.setMessage("Uploading...");
            progress.show();
            intent = new Intent(Question_img.this, Answer_emo.class);

            Uri imageSelected = data.getData();

            //TODO: flask code
            //connectServer();

            // flask?????? ????????? ????????????, bytearray??? ???????????? ???????????????!

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try {
                // Uri img??? bitmap?????? ??????
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageSelected);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // bitmap -> compress??? ???????????? ?????? -> stream??? ??????
            // stream -> bytearray??? ??????
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Log.e("Question_emo", "????????? ?????? : " + byteArray);
            intent.putExtra("image", byteArray);

            sendImage(byteArray);
        }
    }

    //????????? flask??? ??????
    private void sendImage(byte[] byteArray) {

        //????????? ???????????? byte??? ?????? -> base64????????? ??????
        imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //base64????????? ????????? ????????? ???????????? ???????????? ????????? ??????
        String flask_url = "http://43.200.131.5:5000/run_model";

        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    progress.dismiss();
                    Log.e("flask", "sendImage: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        predict = obj.optInt("predict");
                        Log.e("flask", "predict: " + predict);
                        intent.putExtra("emotion", predict);

                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Question_img.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                })
            {
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

        queue = Volley.newRequestQueue(Question_img.this);
        queue.add(request);
    }

};