package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Login extends AppCompatActivity {

    LinearLayout join_button;
    LinearLayout login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        join_button = findViewById(R.id.join_button);
        login_button = findViewById(R.id.login_button);

        join_button.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Join.class);
            startActivity(intent);
        });

        login_button.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Main.class);
            startActivity(intent);
        });

    }




}