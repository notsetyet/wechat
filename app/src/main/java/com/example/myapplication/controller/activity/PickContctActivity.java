package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.controller.adapter.PickContactAdapter;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.PickContactInfo;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class PickContctActivity extends Activity {
    private TextView tv_pick_save;
    private ListView lv_pick_contact;

    private List<PickContactInfo> contactInfos;

    private PickContactAdapter pickContactAdapter;

    private List<String> mExistMem=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_contct);

        initView();
        
        getdata();

        initData();

        initListener();
    }

    private void getdata() {
        String groupID = getIntent().getStringExtra(Constant.GROUP_ID);
        if(groupID!=null){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupID);
            mExistMem=group.getMembers();
        }
    }

    private void initListener() {
        lv_pick_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb_checked=view.findViewById(R.id.cb_pick);
                cb_checked.setChecked(!cb_checked.isChecked());
                //修改数据
                contactInfos.get(position).setChecked(cb_checked.isChecked());
                //refresh
                pickContactAdapter.notifyDataSetChanged();
            }
        });

        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> mPicks=pickContactAdapter.pickContacts();

                Intent intent = new Intent();

                intent.putExtra("members",mPicks.toArray(new String[0]));

                setResult(RESULT_OK,intent);

                //结束当前页面
                finish();
            }
        });
    }

    private void initData() {
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDAO().getContacts();
        contactInfos=new ArrayList<>();
        if(contacts!=null&&contacts.size()>=0){
            for(UserInfo contact:contacts){
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                contactInfos.add(pickContactInfo);
            }
        }

        pickContactAdapter=new PickContactAdapter(this,contactInfos,mExistMem);
        lv_pick_contact.setAdapter(pickContactAdapter);
    }

    private void initView() {
        tv_pick_save=(TextView)findViewById(R.id.tv_pick_save);
        lv_pick_contact=(ListView)findViewById(R.id.lv_pick_contact);
    }
}
