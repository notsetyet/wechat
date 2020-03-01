package com.example.myapplication.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.myapplication.R;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;

//欢迎页面
public class SplashActivity extends Activity {

    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(isFinishing()){
                return;
            }
            
            toMainOrLogin();
        }
    };

    //去主页面还是登陆页面
    private void toMainOrLogin() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if(EMClient.getInstance().isLoggedInBefore()){
                    //獲取信息
                    UserInfo account = Model.getInstance().getUserAccountDAO().getAccountByHXID(EMClient.getInstance().getCurrentUser());
                    //校验
                    if(account==null){
                        //跳轉到登錄頁面
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        //跳轉到主頁面
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    //跳轉到登錄頁面
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //延時
        handler.sendMessageDelayed(Message.obtain(),2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}
