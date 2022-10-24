package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText et_id, et_pw;
    String id, pw;
    LinearLayout join_button;
    LinearLayout login_button;
    ProgressDialog progress;
    Boolean status;
    RequestQueue queue;
    MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        join_button = findViewById(R.id.join_button);
        login_button = findViewById(R.id.login_button);
        app = (MyApp) getApplication();


        join_button.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Join.class);
            startActivity(intent);
        });

        login_button.setOnClickListener(view -> {
            id = et_id.getText().toString();
            pw = et_pw.getText().toString();
            // 통신중..
            progress = new ProgressDialog(Login.this);
            progress.setMessage("Uploading...");
            progress.show();
            sendLogin(id, pw);
            Intent intent = new Intent(Login.this, Main.class);
            startActivity(intent);
        });

    }

    //flask로 전송
    private void sendLogin(String id, String pw) {

        // flask 연결
        String flask_url = "http://10.0.2.2:5000/login";

        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    progress.dismiss();
                    Log.e("flask", "sendLogin: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        status = obj.optBoolean("status");
                        Log.e("flask", "status: " + status);

                        if(status){
                            app.ID = id;
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Login.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("pw", pw);

                return params;
            }
        };

        queue = Volley.newRequestQueue(Login.this);
        queue.add(request);
    }




}