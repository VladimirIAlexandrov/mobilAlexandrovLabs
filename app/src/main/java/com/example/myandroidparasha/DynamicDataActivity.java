package com.example.myandroidparasha;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DynamicDataActivity extends AppCompatActivity {

    private EditText dataEditText;
    private Button addButton;
    private ListView listView;
    private TextView userNameTextView;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;

    private String userName;

    //private String NameData;
   // private String LoginData;
   // private String PassData;
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


        setContentView(R.layout.activity_dynamic_data);
        userNameTextView = findViewById(R.id.nameTextView);
        dataEditText = findViewById(R.id.dataEditText);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        dataList.add(getIntent().getStringExtra("NameData"));
        dataList.add(getIntent().getStringExtra("LoginData"));
        dataList.add(getIntent().getStringExtra("PassData"));

        adapter.notifyDataSetChanged();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = dataEditText.getText().toString();

                if (!data.isEmpty()) {
                    dataList.add(data);
                    adapter.notifyDataSetChanged();
                    dataEditText.setText("");

                    Log.i("msg","Запись успешно добавлена");

                    Toast mytoast = new Toast(DynamicDataActivity.this);
                    Toast.makeText(DynamicDataActivity.this, "Запись успешно добавлена", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("Ошибка","данные не введены");
                    Toast mytoast = new Toast(DynamicDataActivity.this);
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
    @Override
    protected void onStop() {
        super.onStop();

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




                        dialog.dismiss();
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
    // В DynamicDataActivity


    @Override
    protected void onDestroy() {
        super.onDestroy();

        dataList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Преобразование списка в строку
        String dataListString = TextUtils.join(",", dataList);
        editor.putString("DataList", dataListString);
        editor.apply();
        dataList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String dataListString = preferences.getString("DataList", "");

        if (!TextUtils.isEmpty(dataListString)) {
            String[] dataListArray = dataListString.split(",");
            dataList.addAll(Arrays.asList(dataListArray));
        }

        SharedPreferences preferences1 = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String entered = preferences1.getString("Login", "");
        entered += ";"+ preferences1.getString("Password", "");
        entered += ";"+ preferences1.getString("Name", "");
        dataList.add(entered);

        getIntent().removeExtra("NameData");
        getIntent().removeExtra("LoginData");
        getIntent().removeExtra("PassData");

        adapter.notifyDataSetChanged();
    }



}