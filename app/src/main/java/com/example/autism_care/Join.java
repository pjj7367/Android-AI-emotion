package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Join extends AppCompatActivity {

    EditText et_id, et_pw, et_cp, et_age;
    Spinner spinner_gender;
    LinearLayout join_button;
    String id, pw, age, gender;
    ProgressDialog progress;
    Boolean status;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        String [] items = {"남","여"};

        et_id = findViewById(R.id.tv_video);
        et_pw = findViewById(R.id.et_pw);
        et_cp = findViewById(R.id.et_cp);
        spinner_gender = findViewById(R.id.spinner_gender);
        et_age = findViewById(R.id.et_age);
        join_button = findViewById(R.id.join_button);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner_gender.setAdapter(adapter);

        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                gender = Integer.toString(position); // 0:남, 1:여
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = "0";
            }
        });

        join_button.setOnClickListener(view -> {
            id = et_id.getText().toString().trim();
            pw = et_pw.getText().toString().trim();
            age = et_age.getText().toString().trim();

            Boolean Validation = (id.length() == 0 || pw.length() == 0 || age.length() == 0);

            if(Validation){
                Toast.makeText(this, "작성되지 않은 값이 있습니다!", Toast.LENGTH_SHORT).show();
            } else{
                if(pw.equals(et_cp.getText().toString())){
                    // 통신중..
                    progress = new ProgressDialog(Join.this);
                    progress.setMessage("Uploading...");
                    progress.show();

                    sendJoin(id, pw, gender, age);
                } else {
                    Toast.makeText(this, "비밀번호가 맞지 않습니다!", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    //flask로 전송
    private void sendJoin(String id, String pw, String gender, String age) {

        // flask 연결
        String flask_url = "http://192.168.0.12:5000/join";

        StringRequest request = new StringRequest(Request.Method.POST, flask_url,
                response -> {
                    progress.dismiss();
                    Log.e("flask", "sendJoin: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        status = obj.optBoolean("status");
                        Log.e("flask", "status: " + status);
                        
                        if(status){
                            this.finish();
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress.dismiss();
                    Toast.makeText(Join.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("pw", pw);
                params.put("gender", gender);
                params.put("age", age);

                return params;
            }
        };

        queue = Volley.newRequestQueue(Join.this);
        queue.add(request);
    }
}