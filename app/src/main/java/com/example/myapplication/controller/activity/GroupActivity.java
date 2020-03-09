package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.controller.adapter.GroupAdapter;
import com.example.myapplication.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupActivity extends Activity {
    private ListView lv_group;
    private LinearLayout ll_grouplist;
    private GroupAdapter groupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        
        initView();

        initData();

        initListener();
    }

    private void initListener() {
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("TAG","position"+position);

                if(position==0){
                    return;
                }

                Intent intent = new Intent(GroupActivity.this, ChatActivity.class);

                //传参
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);

                //注意此处减一,headerview也被算进去了
                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(position - 1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,emGroup.getGroupId());

                startActivity(intent);
            }
        });

        ll_grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        groupAdapter=new GroupAdapter(this);
        lv_group.setAdapter(groupAdapter);

        //从环信服务器获取所有的群信息
        getGroupsFromHx();
    }

    private void getGroupsFromHx() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<EMGroup> joinedGroupsFromServer = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    //更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupActivity.this,"加载群列表成功",Toast.LENGTH_SHORT).show();

                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupActivity.this,"加载群列表失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refresh(){
        //刷新页面
        groupAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView() {
        lv_group=(ListView)findViewById(R.id.lv_grouplist);
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        lv_group.addHeaderView(headerView);
        ll_grouplist=(LinearLayout)headerView.findViewById(R.id.ll_grouplist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
