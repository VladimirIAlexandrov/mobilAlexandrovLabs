package com.example.myandroidparasha;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_credentials.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "user_credentials";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    public long addUser(String name, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        return database.insert(TABLE_NAME, null, values);
    }

    public int getCountUser(String username, String password) {
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = database.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public boolean checkUser(String username, String password) {
        return getCountUser(username, password) > 0;
    }

    public List<String> getAllUsers() {
        List<String> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_NAME, COLUMN_USERNAME, COLUMN_PASSWORD};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                String userData = "Name: " + name + ", Username: " + username + ", Password: " + password;
                userList.add(userData);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return userList;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        User user = null;

        String[] columns = {COLUMN_NAME, COLUMN_USERNAME, COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            user = new User(name, username, password);
            cursor.close();
        }

        return user;
    }

    public void deleteUser(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }
}
