package com.example.autism_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class Agreement_1 extends AppCompatActivity {

    LinearLayout btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement1);

        btn_next = findViewById(R.id.btn_next);

        btn_next.setOnClickListener(view -> {
            Intent intent = new Intent(Agreement_1.this, Agreement_2.class);
            startActivity(intent);
        });

    }
}