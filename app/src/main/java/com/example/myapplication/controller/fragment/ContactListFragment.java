package com.example.myapplication.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;
import com.example.myapplication.controller.activity.AddContactActivity;
import com.example.myapplication.controller.activity.ChatActivity;
import com.example.myapplication.controller.activity.GroupActivity;
import com.example.myapplication.controller.activity.InviteActivity;
import com.example.myapplication.model.Model;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.SPUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListFragment extends EaseContactListFragment {
    private ImageView iv_contact_red;
    private LinearLayout ll_contact_invite;
    private LinearLayout ll_contact_group;

    private String mHxid;

    private LocalBroadcastManager mLBM;
    private BroadcastReceiver ContactInviteChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
        }
    };

    private BroadcastReceiver GroupChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点显示
            iv_contact_red.setVisibility(View.VISIBLE);
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
        }
    };

    private BroadcastReceiver ContactChangedReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshContact();
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

        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                //参数合法性
                if(user==null){
                    return;
                }

                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //给会话页面传参
                intent.putExtra(EaseConstant.EXTRA_USER_ID,user.getUsername());

                startActivity(intent);

            }
        });

        ll_contact_group = (LinearLayout) headerView.findViewById(R.id.ll_contact_group);
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), GroupActivity.class);
                startActivity(intent1);
            }
        });
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

        mLBM.registerReceiver(ContactChangedReceiver, new IntentFilter(Constant.CONTACT_CHANGED));

        mLBM.registerReceiver(GroupChangedReceiver,new IntentFilter(Constant.GROUP_INVITE_CHANGED));

        getContactFromHx();

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        EaseUser itemAtPosition = (EaseUser) listView.getItemAtPosition(position);

        mHxid=itemAtPosition.getUsername();

        getActivity().getMenuInflater().inflate(R.menu.delete,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.it_contact_del){
            deleteContact();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteContact() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(mHxid);

                    Model.getInstance().getDbManager().getContactTableDAO().delContactByHxid(mHxid);

                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"删除"+mHxid+"成功",Toast.LENGTH_SHORT).show();
                            refreshContact();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    if(getActivity()==null){
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"删除"+mHxid+"失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getContactFromHx(){
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    if(hxids!=null&&hxids.size()>=0){
                        List<UserInfo> userInfoList=new ArrayList<>();
                        for (String hxid:hxids){
                            UserInfo userInfo=new UserInfo(hxid);
                            userInfoList.add(userInfo);
                        }

                        Model.getInstance().getDbManager().getContactTableDAO().saveContacts(userInfoList, true);

                        if(getActivity()==null){
                            return;
                        }

                        //刷新页面
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshContact();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void refreshContact(){

        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDAO().getContacts();

        if(contacts!=null&&contacts.size()>=0){
            Map<String, EaseUser> contactsMap=new HashMap<>();
            for (UserInfo contact:contacts){
                EaseUser easeUser = new EaseUser(contact.getHxid());
                contactsMap.put(contact.getHxid(),easeUser);
            }
            setContactsMap(contactsMap);
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLBM.unregisterReceiver(ContactInviteChangedReceiver);
        mLBM.unregisterReceiver(ContactChangedReceiver);
        mLBM.unregisterReceiver(GroupChangedReceiver);
    }
}
