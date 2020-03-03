package com.example.myapplication.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.controller.adapter.InviteAdapter;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.InviterInfo;
import com.example.myapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class InviteActivity extends AppCompatActivity {
    private ListView lv_invite;
    private LocalBroadcastManager mLBM;
    private InviteAdapter.OnInviteListener mOnInviteListener=new InviteAdapter.OnInviteListener() {
        @Override
        public void OnAccept(InviterInfo inviterInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(inviterInfo.getUserInfo().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDAO().updateInvitation(InviterInfo.InvitationStatus.INVITE_ACCEPT,inviterInfo.getUserInfo().getHxid());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面更新
                                Toast.makeText(InviteActivity.this,"接受了邀请",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面更新
                                Toast.makeText(InviteActivity.this,"接受邀请失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void OnReject(InviterInfo inviterInfo) {

            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(inviterInfo.getUserInfo().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDAO().removeInvitation(inviterInfo.getUserInfo().getHxid());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面更新
                                Toast.makeText(InviteActivity.this,"拒绝成功",Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //页面更新
                                Toast.makeText(InviteActivity.this,"拒绝失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };
    private InviteAdapter inviteAdapter;
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 刷新页面
            refresh();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        
        initView();
        
        initData();
    }

    private void initData() {
        inviteAdapter=new InviteAdapter(this, mOnInviteListener);
        lv_invite.setAdapter(inviteAdapter);

        refresh();

        // 注册邀请信息变化的广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
    }

    private void refresh() {
        //获取所有邀请信息
        List<InviterInfo> invitations = Model.getInstance().getDbManager().getInviteTableDAO().getInvitations();
        //刷新适配器
        inviteAdapter.refresh(invitations);

    }

    private void initView() {
        lv_invite=(ListView)findViewById(R.id.lv_invite);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLBM.unregisterReceiver(InviteChangedReceiver);
    }
}
