package com.example.myandroidparasha;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperationExecutor {

    private DatabaseHelper databaseHelper;

    public DatabaseOperationExecutor(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public interface OnUserQueryCompleted {
        void onOperationCompleted(boolean success);
    }

    public void addUserAsync(final String name, final String username, final String password, final OnUserQueryCompleted callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper.open();
                long result = databaseHelper.addUser(name, username, password);
                databaseHelper.close();
                if (callback != null) {
                    callback.onOperationCompleted(result != -1);
                }
            }
        }).start();
    }

    public void deleteUserAsync(final String username, final OnUserQueryCompleted callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper.open();
                databaseHelper.deleteUser(username);
                databaseHelper.close();
                if (callback != null) {
                    callback.onOperationCompleted(true);
                }
            }
        }).start();
    }

    public void checkUserAsync(final String username, final String password, final OnUserQueryCompleted callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                databaseHelper.open();
                boolean exists = databaseHelper.checkUser(username, password);
                databaseHelper.close();
                if (callback != null) {
                    callback.onOperationCompleted(exists);
                }
            }
        }).start();
    }

}
