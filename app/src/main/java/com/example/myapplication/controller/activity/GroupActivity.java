package com.example.myapplication.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.myapplication.R;

public class GroupActivity extends Activity {
    private ListView lv_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        
        initView();

        initData();
    }

    private void initData() {
    }

    private void initView() {
        lv_group=(ListView)findViewById(R.id.lv_grouplist);
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        lv_group.addHeaderView(headerView);
    }

}
