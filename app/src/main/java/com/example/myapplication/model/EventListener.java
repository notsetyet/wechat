package com.example.myapplication.model;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.model.bean.GroupInfo;
import com.example.myapplication.model.bean.InviterInfo;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.utils.Constant;
import com.example.myapplication.utils.SPUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;

import java.util.List;

//全局
public class EventListener {
    private Context mContext;
    private LocalBroadcastManager mLbm;

    public EventListener(Context context){
        mContext=context;

        mLbm = LocalBroadcastManager.getInstance(mContext);

        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        EMClient.getInstance().groupManager().addGroupChangeListener(emGroupChangeListener);
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

    private final EMGroupChangeListener emGroupChangeListener=new EMGroupChangeListener() {
        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,inviter));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.NEW_GROUP_INVITE);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,applicant));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.NEW_GROUP_APPLICATION);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被接受
        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,accepter));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被拒绝
        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupName,groupId,decliner));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被接受
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {
            InviterInfo invitationInfo=new InviterInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroupInfo(new GroupInfo(groupId,groupId,inviter));
            invitationInfo.setInvitationStatus(InviterInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDAO().addInvitation(invitationInfo);

            SPUtils.getInstance().save(SPUtils.IS_NEW_INVITE,true);

            mLbm.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName) {

        }

        //群被解散
        @Override
        public void onGroupDestroyed(String groupId, String groupName) {

        }

        //收到群邀请被自动接受
        //不知为何总是直接进这个函数，所以没写auto邀请进组的函数
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            onInvitationReceived(groupId,groupId,inviter,inviteMessage);
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {

        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {

        }

        @Override
        public void onAdminAdded(String s, String s1) {

        }

        @Override
        public void onAdminRemoved(String s, String s1) {

        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {

        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    };

}
