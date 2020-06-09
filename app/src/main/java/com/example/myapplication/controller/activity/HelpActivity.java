package com.example.myapplication.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class HelpActivity extends AppCompatActivity {
    private LinearLayout ll_que1,ll_que2,ll_que3;
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        iv_back = findViewById(R.id.iv_back);
        ll_que1 = findViewById(R.id.ll_que1);
        ll_que2 = findViewById(R.id.ll_que2);
        ll_que3 = findViewById(R.id.ll_que3);

        ll_que1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,WebActivity.class);
                intent.putExtra("url","https://kf.qq.com/touch/wxappfaq/170524v2iaAr17052477Bn2y.html?platform=15");
                intent.putExtra("name","发送朋友圈失败怎么办？");
                startActivity(intent);
            }
        });
        ll_que2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,WebActivity.class);
                intent.putExtra("url","https://kf.qq.com/touch/wxappfaq/120813euEJVf141211vYzMbi.html?platform=15");
                intent.putExtra("name","微信怎么建群？如何邀请别人加入群聊");
                startActivity(intent);
            }
        });
        ll_que3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,WebActivity.class);
                intent.putExtra("url","https://kf.qq.com/touch/wxappfaq/120813euEJVf141211q6Zzyu.html?platform=15");
                intent.putExtra("name","微信添加好友的方法");
                startActivity(intent);
            }
        });



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
