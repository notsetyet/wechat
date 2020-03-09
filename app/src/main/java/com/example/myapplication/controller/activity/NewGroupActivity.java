package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

public class NewGroupActivity extends Activity {
    private EditText et_newgroup_name;
    private EditText et_newgroup_description;
    private CheckBox cb_newgroup_public;
    private CheckBox cb_newgroup_invite;
    private Button bt_newgroup_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        
        initView();

        initListener();
    }

    private void initListener() {
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGroupActivity.this, PickContctActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            createGroup(data.getStringArrayExtra("members"));
        }
    }

    private void createGroup(String[] members) {
        // 群名称
        final String groupName = et_newgroup_name.getText().toString();
        // 群描述
        final String groupDesc = et_newgroup_description.getText().toString();
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMGroupOptions options=new EMGroupOptions();
                //可创建的类型
                EMGroupManager.EMGroupStyle groupStyle=null;
                if (cb_newgroup_public.isChecked()) {//公开
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {
                    if (cb_newgroup_invite.isChecked()) {// 开放群邀请
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                options.maxUsers=200;
                options.style=groupStyle;
                options.inviteNeedConfirm=true;
                // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置
                try {
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, members, "申请加入群", options);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();

                            // 结束当前页面
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        et_newgroup_name= (EditText) findViewById(R.id.et_newgroup_name);
        et_newgroup_description= (EditText) findViewById(R.id.et_newgroup_description);
        cb_newgroup_public= (CheckBox) findViewById(R.id.cb_newgroup_public);
        cb_newgroup_invite= (CheckBox) findViewById(R.id.cb_newgroup_invite);
        bt_newgroup_create= (Button) findViewById(R.id.bt_newgroup_create);
    }

}
