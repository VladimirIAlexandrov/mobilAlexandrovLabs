package com.example.myandroidparasha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private DatabaseOperationExecutor databaseExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseExecutor = new DatabaseOperationExecutor(this);

        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button createButton = findViewById(R.id.createButton);
        Button deleteButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                databaseExecutor.checkUserAsync(enteredUsername, enteredPassword, new DatabaseOperationExecutor.OnUserQueryCompleted() {

                    @Override
                    public void onOperationCompleted(boolean success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Intent intent = new Intent(MainActivity.this, DynamicDataActivity.class);
                                    intent.putExtra("userName", enteredUsername);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = nameEditText.getText().toString();
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                databaseExecutor.addUserAsync(enteredName, enteredUsername, enteredPassword, new DatabaseOperationExecutor.OnUserQueryCompleted() {

                    @Override
                    public void onOperationCompleted(boolean success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Toast.makeText(MainActivity.this, "Пользователь успешно добавлен", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Ошибка при добавлении пользователя", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameToDelete = usernameEditText.getText().toString();

                databaseExecutor.deleteUserAsync(usernameToDelete, new DatabaseOperationExecutor.OnUserQueryCompleted() {

                    @Override
                    public void onOperationCompleted(boolean success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (success) {
                                    Toast.makeText(MainActivity.this, "Пользователь удалён из базы данных.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Пользователь НЕ удалён из базы данных.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
