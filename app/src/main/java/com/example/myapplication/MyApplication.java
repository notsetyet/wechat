package com.example.myapplication;

import android.app.Application;
import android.view.Display;

import com.example.myapplication.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options=new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);
        EaseUI.getInstance().init(this,options);

        //初始化全局數據類
        Model.getInstance().init(this);
    }
}
