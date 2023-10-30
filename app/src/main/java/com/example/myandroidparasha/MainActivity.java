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

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createButton = findViewById(R.id.createButton);
        Button deliteButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = nameEditText.getText().toString();
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                dbHelper.open();
                if (dbHelper.checkUser(enteredUsername, enteredPassword) && !enteredName.isEmpty()) {


                    Intent intent = new Intent(MainActivity.this, DynamicDataActivity.class);

                    intent.putExtra("userName", enteredName);
                    intent.putExtra("NameData", "Имя пользователя: " + enteredName);
                    intent.putExtra("LoginData", "Логин пользователя: " + enteredUsername);
                    intent.putExtra("PassData", "Пароль: " + enteredPassword);

                    dbHelper.close();
                    startActivity(intent);

                    Log.i("msg", "Авторизация успешна");
                    Toast.makeText(MainActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.close();

                    Log.e("Ошибка", "Ошибка авторизации");
                    Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = nameEditText.getText().toString();
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();


                dbHelper.open();
                if (dbHelper.getCountUser(enteredUsername, enteredPassword) >= 1) {
                    Toast.makeText(MainActivity.this, "Пользователь уже существует", Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                }
                else{
                    long result = dbHelper.addUser(enteredName, enteredUsername, enteredPassword);
                    dbHelper.close();

                    if (result != -1) {
                        Log.i("msg", "Пользователь успешно добавлен");
                        Toast.makeText(MainActivity.this, "Пользователь успешно добавлен", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Ошибка", "Ошибка при добавлении пользователя");
                        Toast.makeText(MainActivity.this, "Ошибка при добавлении пользователя", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        deliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameToDelete = usernameEditText.getText().toString();


                dbHelper.open();
                User userToDelete = dbHelper.getUserByUsername(usernameToDelete);
                dbHelper.close();

                if (userToDelete != null) {

                    dbHelper.open();
                    dbHelper.deleteUser(usernameToDelete);
                    dbHelper.close();

                    nameEditText.setText("");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    Toast.makeText(MainActivity.this, "Пользователь удалён из базы данных.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Пользователь НЕ удалён из базы данных.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
