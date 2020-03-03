package com.example.myapplication.model;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.model.bean.InviterInfo;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.SPUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

public class EventListener {
    private Context mContext;
    private LocalBroadcastManager mLbm;

    public EventListener(Context context){
        mContext=context;

        mLbm = LocalBroadcastManager.getInstance(mContext);

        EMClient.getInstance().contactManager().setContactListener(emContactListener);
    }

    private final EMContactListener emContactListener=new EMContactListener() {
        //联系人增加后的方法
        @Override
        public void onContactAdded(String hxid) {
            //本应在服务器判断是否是自己的联系人
            Model.getInstance().getDbManager().getContactTableDAO().saveContact(new UserInfo(hxid),true);

            mLbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //联系人删除后的方法
        @Override
        public void onContactDeleted(String hxid) {
            Model.getInstance().getDbManager().getContactTableDAO().delContactByHxid(hxid);
            Model.getInstance().getDbManager().getInviteTableDAO().removeInvitation(hxid);

            mLbm.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        //接收到新的邀请的方法
        @Override
        public void onContactInvited(String hxid, String reason) {
            InviterInfo invitatioInfo=new InviterInfo();
            invitatioInfo.setUserInfo(new UserInfo(hxid));
            invitatioInfo.setReason(reason);
            invitatioInfo.setInvitationStatus(InviterInfo.InvitationStatus.NEW_INVITE);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitatioInfo);

            //红点的处理
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
            //
            mLbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人同意你的邀请
        @Override
        public void onFriendRequestAccepted(String hxid) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setUserInfo(new UserInfo(hxid));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            //红点的处理
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
            //
            mLbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        //别人拒绝你的邀请
        @Override
        public void onFriendRequestDeclined(String s) {
            //红点的处理
            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);
            //
            mLbm.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };

}
