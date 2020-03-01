package com.example.myapplication.controller.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.R;
import com.example.myapplication.controller.fragment.ChiefFragment;
import com.example.myapplication.controller.fragment.ContactListFragment;
import com.example.myapplication.controller.fragment.PostFragment;
import com.example.myapplication.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;

    private ChiefFragment chiefFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;
    private PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        initListener();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Fragment fragment=null;

                switch (checkedId){
                    case R.id.rb_main_chat:
                        fragment=chiefFragment;
                        break;
                    case R.id.rb_main_contact:
                        fragment=contactListFragment;
                        break;
                    case R.id.rb_main_post:
                        fragment=postFragment;
                        break;
                    case R.id.rb_main_setting:
                        fragment=settingFragment;
                        break;
                }

                //切换fragment
                switchFragment(fragment);
            }
        });

        //默认选择
        rg_main.check(R.id.rb_main_chat);
    }

    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }

    //创建三个fragment
    private void initData() {
        chiefFragment = new ChiefFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
        postFragment=new PostFragment();
    }

    private void initView() {
        rg_main=(RadioGroup)findViewById(R.id.rg_main);
    }

}
