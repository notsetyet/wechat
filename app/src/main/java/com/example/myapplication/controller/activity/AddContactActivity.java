package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AddContactActivity extends Activity {
    private TextView tv_add_find;
    private EditText ed_add_name;
    private RelativeLayout rl_add;
    private TextView tv_add_name;
    private Button bt_add_add;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        
        initView();

        initListerner();
    }

    private void initListerner() {
        //查找按钮
        tv_add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
        //查找姓名
        ed_add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //
        tv_add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //添加按钮
        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void find(){
        String name= ed_add_name.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddContactActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //这么做是因为暂时没有自己的服务器，默认有这个用户，这里缺少判断
                userInfo=new UserInfo(name);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);
                        tv_add_name.setText(userInfo.getName());
                    }
                });
            }
        });
    }

    private void add(){

        if(TextUtils.isEmpty(userInfo.getName())){
            Toast.makeText(AddContactActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //第二个参数是指理由
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),"添加好友");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送邀请成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this,"发送邀请失败"+e.getDescription(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        tv_add_find=(TextView)findViewById(R.id.tv_add_find);
        ed_add_name=(EditText)findViewById(R.id.ed_add_name);
        rl_add=(RelativeLayout)findViewById(R.id.rl_add);
        tv_add_name=(TextView) findViewById(R.id.tv_add_name);
        bt_add_add=(Button)findViewById(R.id.bt_add_add);
    }
}
