package com.example.myapplication.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.controller.activity.HelpActivity;
import com.example.myapplication.controller.activity.LoginActivity;
import com.example.myapplication.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class SettingFragment extends Fragment {

    private Button bt_setting_logout;
    private Button bt_setting_help;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(),R.layout.fragment_setting,null);

        initView(view);

        return view;
    }

    private void initView(View view) {
        bt_setting_logout=(Button)view.findViewById(R.id.bt_setting_logout);
        bt_setting_help=(Button)view.findViewById(R.id.bt_setting_help);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    private void initData() {
        bt_setting_logout.setText("退出登录("+ EMClient.getInstance().getCurrentUser()+")");
        bt_setting_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HelpActivity.class);

                startActivity(intent);
                                               }
                                           });
        bt_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从环信服务器退出，联网
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出成功",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getActivity(), LoginActivity.class);

                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"退出失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });
            }
        });
    }
}
