package com.example.myapplication.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.bean.GroupInfo;
import com.example.myapplication.model.bean.InviterInfo;
import com.example.myapplication.model.bean.UserInfo;
import com.example.myapplication.model.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class InviteTableDAO {
    private DBHelper mDBHelper;

    public InviteTableDAO(DBHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    public void addInvitation(InviterInfo inviterInfo){
        if(inviterInfo==null){
            return;
        }
        SQLiteDatabase db=mDBHelper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(InviteTable.COL_REASON,inviterInfo.getReason());
        values.put(InviteTable.COL_STATUS,inviterInfo.getInvitationStatus().ordinal());

        UserInfo userInfo=inviterInfo.getUserInfo();

        if(userInfo!=null){//联系人
            values.put(InviteTable.COL_HXID,inviterInfo.getUserInfo().getHxid());
            values.put(InviteTable.COL_NAME,inviterInfo.getUserInfo().getName());
        }else{//群组
            values.put(InviteTable.COL_GROUP_GID,inviterInfo.getGroupInfo().getGid());
            values.put(InviteTable.COL_GROUP_NAME,inviterInfo.getGroupInfo().getGroupName());
            values.put(InviteTable.COL_HXID,inviterInfo.getGroupInfo().getInvitePerson());//邀请人,保证唯一标识
        }
        db.replace(InviteTable.TAB_NAME,null,values);
    }

    //获取全部邀请
    public List<InviterInfo> getInvitations(){
        SQLiteDatabase db=mDBHelper.getReadableDatabase();
        String sql= "select * from " +InviteTable.TAB_NAME;
        Cursor cursor=db.rawQuery(sql,null);

        List<InviterInfo> invitationInfo=new ArrayList<>();
        while(cursor.moveToNext()){
            InviterInfo inviterInfo=new InviterInfo();

            inviterInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            inviterInfo.setInvitationStatus(int2InvitationStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            String gid=cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_GID));

            //判断是群还是联系人
            if(gid==null){//联系人
                UserInfo userInfo=new UserInfo();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_NAME)));
                //这里和name一样
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.COL_NAME)));

                inviterInfo.setUserInfo(userInfo);
            }else{//群
                GroupInfo groupInfo=new GroupInfo();
                groupInfo.setGid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_GID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_HXID)));

                inviterInfo.setGroupInfo(groupInfo);
            }

            invitationInfo.add(inviterInfo);
        }
        cursor.close();
        return invitationInfo;
    }

    private InviterInfo.InvitationStatus int2InvitationStatus(int intStatus){
        if(intStatus==InviterInfo.InvitationStatus.INVITE_ACCEPT.ordinal()){
            return InviterInfo.InvitationStatus.INVITE_ACCEPT;
        }
        if(intStatus==InviterInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()){
            return InviterInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }
        if(intStatus==InviterInfo.InvitationStatus.INVITE_REJECT.ordinal()){
            return InviterInfo.InvitationStatus.INVITE_REJECT;
        }
        if(intStatus==InviterInfo.InvitationStatus.INVITE_REJECT_BY_PEER.ordinal()){
            return InviterInfo.InvitationStatus.INVITE_REJECT_BY_PEER;
        }
        if(intStatus==InviterInfo.InvitationStatus.NEW_INVITE.ordinal()){
            return InviterInfo.InvitationStatus.NEW_INVITE;
        }

        if(intStatus==InviterInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal()){
            return InviterInfo.InvitationStatus.NEW_GROUP_INVITE;
        }
        if(intStatus==InviterInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal()){
            return InviterInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }
        if(intStatus==InviterInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal()){
            return InviterInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }
        return null;
    }

    public void removeInvitation(String hxid){
        if(hxid==null){
            return;
        }

        SQLiteDatabase db=mDBHelper.getReadableDatabase();

        db.delete(InviteTable.TAB_NAME,InviteTable.COL_HXID+" =? ",new String[]{hxid});
    }

    public void updateInvitation(InviterInfo.InvitationStatus invitationStatus,String hxid){
        if(hxid==null){
            return;
        }

        SQLiteDatabase db=mDBHelper.getReadableDatabase();

        ContentValues values=new ContentValues();
        values.put(InviteTable.COL_STATUS,invitationStatus.ordinal());
        //第二个值指的是现在的值
        db.update(InviteTable.TAB_NAME,values,InviteTable.COL_HXID+" =? ",new String[]{hxid});
    }
}
