package com.example.myapplication.model;

import android.content.Context;

import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.model.dao.UserAccountDAO;
import com.example.myapplication.model.dao.UserAccountTable;
import com.example.myapplication.model.db.DBManager;

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

    private DBManager dbManager;

    private EventListener eventListener;

    private Model(){

    }
    public static Model getInstance(){
        return model;
    }
    public void init(Context context){
        mContext=context;

        //創建用戶對象數據庫
        userAccountDAO=new UserAccountDAO(mContext);

        //开启全局监听
        eventListener=new EventListener(mContext);
    }
    //獲取全局線程池
    public ExecutorService getGlobalThreadPool(){
        return executorService;
    }

    public void loginSuccess(UserInfo userInfo) {
        if(userInfo==null){
            return;
        }
        if(dbManager!=null){
            dbManager.close();
        }
        dbManager = new DBManager(mContext, userInfo.getName());
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    //獲取用戶數據賬號操作類
    public UserAccountDAO getUserAccountDAO(){
        return userAccountDAO;
    }
}
