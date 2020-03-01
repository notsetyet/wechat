package com.example.myapplication.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.model.dao.UserAccountTable;

public class UserAccountDB extends SQLiteOpenHelper {
    public UserAccountDB(@Nullable Context context) {
        super(context, "account.db", null, 1);
    }

    //數據庫創建調用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    //數據庫更新調用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
