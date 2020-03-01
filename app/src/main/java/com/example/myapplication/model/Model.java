package com.example.myapplication.model;

import android.content.Context;

import com.example.myapplication.model.dao.UserAccountDAO;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//數據模型層全局類，與控制類交互
public class Model {
    //單例
    private Context mContext;
    private static Model model=new Model();
    private ExecutorService executorService= Executors.newCachedThreadPool();
    private UserAccountDAO userAccountDAO;

    private Model(){

    }
    public static Model getInstance(){
        return model;
    }
    public void init(Context context){
        mContext=context;

        //創建用戶對象數據庫
        userAccountDAO=new UserAccountDAO(mContext);
    }
    //獲取全局線程池
    public ExecutorService getGlobalThreadPool(){
        return executorService;
    }

    public void loginSuccess() {
    }

    //獲取用戶數據賬號操作類
    public UserAccountDAO getUserAccountDAO(){
        return userAccountDAO;
    }
}
