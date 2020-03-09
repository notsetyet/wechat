package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.controller.adapter.GroupDetailAdapter;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

//群详情页面
public class GroupDetailActivity extends Activity {
    private GridView gv_groupdetail;
    private Button bt_groupdetail_out;
    private EMGroup mGroup;
    private List<UserInfo> userInfos;

    private GroupDetailAdapter groupDetailAdapter;

    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener=new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void addGroupMem() {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(GroupDetailActivity.this, PickContctActivity.class);
                    intent.putExtra(Constant.GROUP_ID,mGroup.getGroupId());
                    //请求码是随便编的，不影响
                    startActivityForResult(intent,2);
                }
            });
        }

        @Override
        public void delGroupMem(UserInfo userInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(),userInfo.getHxid());

                        //刷新页面
                        getMembersFromHX();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this,"删除成员成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this,"删除成员失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            //要邀请的人
           String[] members=data.getStringArrayExtra("members");
           Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
               @Override
               public void run() {
                   try {
                       EMClient.getInstance().groupManager().addUsersToGroup(mGroup.getGroupId(),members);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(GroupDetailActivity.this,"添加成员成功",Toast.LENGTH_SHORT).show();
                           }
                       });
                   } catch (HyphenateException e) {
                       e.printStackTrace();
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(GroupDetailActivity.this,"添加成员失败",Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }
           });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        initView();

        getdata();

        initData();

        initListener();
    }

    //点击任意项目，可以从删除模式切换回正常模式
    private void initListener() {
        gv_groupdetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //如果是删除模式
                        if(groupDetailAdapter.isCanModify()){
                            groupDetailAdapter.setCanModify(false);
                            //通知刷新
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        initButtonDisplay();

        initGridView();
        
        //获取群成员并刷新页面
        getMembersFromHX();
    }

    private void getMembersFromHX() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //第二个参数表示获取群成员
                    EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(mGroup.getGroupId(),true);
                    List<String> members = groupFromServer.getMembers();

                    if(members!=null&&members.size()>=0){
                        userInfos=new ArrayList<>();
                        for(String member:members){
                            UserInfo userInfo = new UserInfo(member);
                            userInfos.add(userInfo);
                        }
                    }

                    //刷新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groupDetailAdapter.refresh(userInfos);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this,"获取所有群成员失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void initGridView() {
        //true条件：当前是群主||是公开群
        boolean canModify=EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())||mGroup.isPublic();
        groupDetailAdapter = new GroupDetailAdapter(this, canModify,mOnGroupDetailListener);
        gv_groupdetail.setAdapter(groupDetailAdapter);
    }

    private void initButtonDisplay() {
        //当前用户为群主
        if(EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())){
            bt_groupdetail_out.setText("解散群");

            bt_groupdetail_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().destroyGroup(mGroup.getGroupId());
                                //发送退群广播
                                exitGroupBroadcast();
                                //r
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"解散群成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"解散群失败"+e.getDescription(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }else{
            bt_groupdetail_out.setText("退群");

            bt_groupdetail_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(mGroup.getGroupId());
                                //发送退群广播
                                exitGroupBroadcast();
                                //r
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"退群成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this,"退群失败"+e.getDescription(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    //发送退群和解散群广播
    private void exitGroupBroadcast() {
        LocalBroadcastManager mBLM = LocalBroadcastManager.getInstance(GroupDetailActivity.this);
        Intent intent = new Intent(Constant.EXIT_GROUP);
        intent.putExtra(Constant.GROUP_ID,mGroup.getGroupId());
        mBLM.sendBroadcast(intent);
    }

    private void getdata() {
        Intent intent = getIntent();
        String GID = intent.getStringExtra(Constant.GROUP_ID);
        if(GID==null){
            return;
        }else{
            mGroup= EMClient.getInstance().groupManager().getGroup(GID);
        }
    }

    private void initView() {
        gv_groupdetail=(GridView)findViewById(R.id.gv_groupdetail);
        bt_groupdetail_out=(Button)findViewById(R.id.bt_groupdetail_out);
    }
}
