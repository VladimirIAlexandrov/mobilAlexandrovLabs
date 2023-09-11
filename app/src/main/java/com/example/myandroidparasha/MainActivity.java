package com.example.myandroidparasha;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int counter = 0;
    private TextView counterTextView;
    private TextView counterTextView1;
    private Button incrementButton1;
    private Button incrementButton2;
    private Button incrementButton3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int color = Color.parseColor("#349188");
        ActionBar actionBar = getSupportActionBar(); // Получите ActionBar
        if (actionBar != null) {
            actionBar.setTitle("Laba1");
        }



        counterTextView = findViewById(R.id.textView3);

        counterTextView1 = findViewById(R.id.textView);

        incrementButton1 = findViewById(R.id.button);
        incrementButton2 = findViewById(R.id.button2);
        incrementButton3 = findViewById(R.id.button3);

        incrementButton1.setBackgroundColor(color);
        incrementButton2.setBackgroundColor(color);
        incrementButton3.setBackgroundColor(color);

        incrementButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                counterTextView1.setText("Нажата кнопка 1");
                updateCounter();
            }
        });

        incrementButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                counterTextView1.setText("Нажата кнопка 2");
                updateCounter();
            }
        });

        incrementButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                updateCounter();
            }
        });
    }

    private void updateCounter() {
        counterTextView.setText("Счетчик: " + counter);
    }
}

