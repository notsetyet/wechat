package com.example.myapplication;

import android.app.Application;
import android.content.Context;

import com.example.myapplication.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

public class MyApplication extends Application {
    private static Context mContext;
    private static EMOptions options;
    @Override
    public void onCreate() {
        super.onCreate();

        options=new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);

        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 设置自动下载附件类消息的缩略图，默认为true，这里需要设置options.setAutoTransferMessageAttachments(true);才起作用
        options.setAutoDownloadThumbnail(true);

        EaseUI.getInstance().init(this,options);

        //初始化全局數據類
        Model.getInstance().init(this);

        mContext=this;
    }

    public static Context getGlobalApplication(){
        return mContext;
    }
    public static EMOptions getOptions(){return options;}
}
