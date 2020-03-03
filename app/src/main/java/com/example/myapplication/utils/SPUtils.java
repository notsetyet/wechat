package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.MyApplication;
import com.example.myapplication.controller.activity.SplashActivity;

//保存 & 获取
public class SPUtils {
    public static final String IS_NEW_INVITE="is_new_invite";
    private static SPUtils spUtils=new SPUtils();
    private static SharedPreferences wechat;
    private SPUtils(){

    }

    public static SPUtils getInstance(){
        if(wechat==null){
            wechat = MyApplication.getGlobalApplication().getSharedPreferences("wechat", Context.MODE_PRIVATE);
        }
        return spUtils;
    }

    public void save(String key,Object value){
        if(value instanceof String){
            wechat.edit().putString(key,(String)value).commit();
        }
        else if(value instanceof Boolean){
            wechat.edit().putBoolean(key,(Boolean) value).commit();
        }
        else if(value instanceof Integer){
            wechat.edit().putInt(key,(Integer) value).commit();
        }
    }

    public String getString(String key,String defValue){
        return wechat.getString(key,defValue);
    }

    public Boolean getBoolean(String key,Boolean defValue){
        return wechat.getBoolean(key,defValue);
    }

    public Integer getInteger(String key,Integer defValue){
        return wechat.getInt(key,defValue);
    }
}
