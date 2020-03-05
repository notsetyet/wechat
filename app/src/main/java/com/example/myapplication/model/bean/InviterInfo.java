package com.example.myapplication.model.bean;

public class InviterInfo {
    private UserInfo userInfo;//邀请人（谁加的你）
    private GroupInfo groupInfo;
    private String reason;//邀请原因

    private InvitationStatus invitationStatus;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    @Override
    public String toString() {
        return "InviterInfo{" +
                "userInfo=" + userInfo +
                ", groupInfo=" + groupInfo +
                ", reason='" + reason + '\'' +
                ", invitationStatus=" + invitationStatus +
                '}';
    }

    public InviterInfo(UserInfo userInfo, GroupInfo groupInfo, String reason, InvitationStatus invitationStatus) {
        this.userInfo = userInfo;
        this.groupInfo = groupInfo;
        this.reason = reason;
        this.invitationStatus = invitationStatus;
    }

    public InviterInfo() {
    }



    public enum InvitationStatus{
        NEW_INVITE,
        INVITE_ACCEPT,//接受邀请
        INVITE_ACCEPT_BY_PEER,//被邀请

        INVITE_REJECT,
        INVITE_REJECT_BY_PEER,

        NEW_GROUP_INVITE,
        INVITE_GROUP_ACCEPT,//接受邀请
        INVITE_GROUP_ACCEPT_BY_PEER,//被邀请

        INVITE_GROUP_REJECT,
        INVITE_GROUP_REJECT_BY_PEER,

        DEFAULT
    }
}
