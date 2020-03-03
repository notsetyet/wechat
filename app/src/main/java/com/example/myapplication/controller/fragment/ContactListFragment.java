package com.example.myapplication.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.controller.activity.AddContactActivity;
import com.example.myapplication.controller.activity.InviteActivity;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.SPUtils;
import com.hyphenate.easeui.ui.EaseContactListFragment;

public class ContactListFragment extends EaseContactListFragment {
    private ImageView iv_contact_red;
    private LinearLayout ll_contact_invite;

    private LocalBroadcastManager mLBM;
    private BroadcastReceiver ContactInviteChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
        }
    };

    @Override
    protected void initView() {
        super.initView();

        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setBackgroundColor(0xff99cc00);
        titleBar.setTitle("通讯录");

        //
        View headerView = View.inflate(getActivity(),R.layout.header_fragment_contact,null);

        listView.addHeaderView(headerView);

        iv_contact_red=(ImageView) headerView.findViewById(R.id.iv_contact_red);
        ll_contact_invite=(LinearLayout)headerView.findViewById(R.id.ll_contact_invite);

    }

    @Override
    protected void setUpView() {
        super.setUpView();

        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });

        //初始化红点显示
        Boolean isNewInvite = SPUtils.getInstance().getBoolean(SPUtils.IS_NEW_INVITE, false);
        iv_contact_red.setVisibility(isNewInvite?View.VISIBLE:View.GONE);

        //邀请信息条目的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击红点消失
                iv_contact_red.setVisibility(View.GONE);
                SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,false);

                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactInviteChangedReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLBM.unregisterReceiver(ContactInviteChangedReceiver);
    }
}
