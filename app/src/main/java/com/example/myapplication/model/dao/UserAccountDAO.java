package com.example.myapplication.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.model.db.UserAccountDB;

//操作類
public class UserAccountDAO {
    private final UserAccountDB mHelper;

    public UserAccountDAO(Context context) {
        mHelper = new UserAccountDB(context);
    }

    public void addAccount(UserInfo userInfo){//
        SQLiteDatabase db=mHelper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(UserAccountTable.COL_HXID,userInfo.getHxid());
        values.put(UserAccountTable.COL_NAME,userInfo.getName());
        values.put(UserAccountTable.COL_NICK,userInfo.getNick());
        values.put(UserAccountTable.COL_PHOTO,userInfo.getPhoto());
        db.replace(UserAccountTable.TAB_NAME,null,values);
    }

    public UserInfo getAccountByHXID(String hxid){
        SQLiteDatabase db=mHelper.getReadableDatabase();

        String sql="select * from "+UserAccountTable.TAB_NAME+" where "+UserAccountTable.COL_HXID+" =? ";
        Cursor cursor = db.rawQuery(sql,new String[]{hxid});

        UserInfo userInfo=null;
        if(cursor.moveToNext()){
            userInfo=new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }

        cursor.close();

        return userInfo;
    }
}
