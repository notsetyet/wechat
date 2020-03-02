package com.example.myapplication.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactTableDAO {
    private DBHelper mHelper;
    public ContactTableDAO(DBHelper dbHelper) {
        mHelper=dbHelper;
    }

    public List<UserInfo> getContacts(){
        SQLiteDatabase db=mHelper.getReadableDatabase();

        String sql="select * from "+ContactTable.TAB_NAME+" where "+ ContactTable.COL_IS_CONTACT+" =1";
        Cursor cursor=db.rawQuery(sql,null);

        List<UserInfo> userInfoList=new ArrayList<>();
        while (cursor.moveToNext()){
            UserInfo userInfo=new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

            userInfoList.add(userInfo);
        }
        cursor.close();
        return userInfoList;
    }

    public UserInfo getContactByHxid(String hxid){
        //判断合法
        if(hxid==null){
            return null;
        }
        SQLiteDatabase db=mHelper.getReadableDatabase();

        String sql="select * from "+ContactTable.TAB_NAME+" where "+ ContactTable.COL_HXID+" =? ";
        Cursor cursor=db.rawQuery(sql,null);

        UserInfo userInfo=null;
        while (cursor.moveToNext()){
            userInfo=new UserInfo();

            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

        }
        cursor.close();
        return userInfo;
    }

    public List<UserInfo> getContactsByHxid(List<String> hxids){
        if(hxids==null || hxids.size() <= 0){
            return null;
        }

        SQLiteDatabase db=mHelper.getReadableDatabase();
        List<UserInfo> userInfoList=new ArrayList<>();

        for (String hxid: hxids) {
            UserInfo userInfo=getContactByHxid(hxid);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    public void saveContact(UserInfo contact, boolean isMyContact){
        if(contact==null){
            return;
        }

        SQLiteDatabase db=mHelper.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(ContactTable.COL_HXID,contact.getHxid());
        values.put(ContactTable.COL_NAME,contact.getName());
        values.put(ContactTable.COL_NICK,contact.getNick());
        values.put(ContactTable.COL_PHOTO,contact.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT,isMyContact?"1":"0");

        db.replace(ContactTable.TAB_NAME,null,values);
    }

    public void saveContacts(List<UserInfo> contacts, boolean isMyContact){
        if(contacts==null || contacts.size() <= 0){
            return;
        }

        for (UserInfo userinfo: contacts) {
            saveContact(userinfo,isMyContact);
        }
    }

    public void delContactByHxid(String hxid){
        if(hxid==null){
            return;
        }

        SQLiteDatabase db=mHelper.getReadableDatabase();
        db.delete(ContactTable.TAB_NAME,ContactTable.COL_HXID+" =? ",new String[]{hxid});
    }
}
