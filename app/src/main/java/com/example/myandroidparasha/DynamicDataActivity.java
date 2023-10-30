package com.example.myandroidparasha;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataActivity extends AppCompatActivity {

    private EditText dataEditText;
    private Button addButton;
    private ListView listView;
    private TextView userNameTextView;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;
    private String userName;

    @Override
    protected void onStart() {
        super.onStart();
        userName = getIntent().getStringExtra("userName");
        userNameTextView.setText(userName);
        userNameTextView.setTextColor(Color.GRAY);
        userNameTextView.setTextScaleX(1.5F);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_dynamic_data);
        userNameTextView = findViewById(R.id.nameTextView);
        dataEditText = findViewById(R.id.dataEditText);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        String nameData = getIntent().getStringExtra("NameData");
        String loginData = getIntent().getStringExtra("LoginData");
        String passData = getIntent().getStringExtra("PassData");

        if (nameData != null) dataList.add(nameData);
        if (loginData != null) dataList.add(loginData);
        if (passData != null) dataList.add(passData);

        loadUserData();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = dataEditText.getText().toString();
                if (!data.isEmpty()) {
                    dataList.add(data);
                    adapter.notifyDataSetChanged();
                    dataEditText.setText("");
                    Toast.makeText(DynamicDataActivity.this, "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DynamicDataActivity.this, "Вы не ввели текст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
            }
        });
    }

    private void loadUserData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper.open();
                List<String> userDataList = dbHelper.getAllUsers();
                dbHelper.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userDataList != null) {
                            dataList.addAll(userDataList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление элемента")
                .setMessage("Вы уверены, что хотите удалить этот элемент?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataList.clear();
        adapter.notifyDataSetChanged();
    }
}
