package com.example.myandroidparasha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myandroidparasha.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<String, String> loginCredentials = new HashMap<>();
    String enteredname;
    String enteredUsername;
    String enteredPassword;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;

    ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 enteredname = nameEditText.getText().toString();
                 enteredUsername = usernameEditText.getText().toString();
                 enteredPassword = passwordEditText.getText().toString();
                readLoginCredentials();
                if (loginCredentials.containsKey(enteredUsername) && loginCredentials.get(enteredUsername).equals(enteredPassword) && !enteredname.isEmpty()) {

                    Intent intent = new Intent(MainActivity.this, DynamicDataActivity.class);
                    intent.putExtra("userName", enteredname);


                    intent.putExtra("NameData", "Имя пользователя: "+enteredname);
                    intent.putExtra("LoginData", "Логин пользователя: "+enteredUsername);
                    intent.putExtra("PassData", "Пароль: "+enteredPassword);

                    startActivity(intent);


                    Log.i("msg","Авторизация успешна");
                    Toast mytoast = new Toast(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("Ошибка","Ошибка авторизации");

                    Toast mytoast = new Toast(MainActivity.this);
                    Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void readLoginCredentials() {
        Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(R.raw.logins_passwords);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 2) {
                    loginCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onPause() {
        super.onPause();


        ////////////////////////////////////////////////////////////////

        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Login", enteredUsername);
        editor.putString("Password", enteredPassword);
        editor.putString("Name", enteredname);

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences1 = getSharedPreferences("AppSettings", MODE_PRIVATE);
        enteredUsername = preferences1.getString("Login", "");
        enteredPassword = preferences1.getString("Password", "");
        enteredname = preferences1.getString("Name", "");


        nameEditText.setText(enteredname);
        usernameEditText.setText(enteredUsername);
        passwordEditText.setText(enteredPassword);

    }
}
