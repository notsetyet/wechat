package com.example.myapplication.model.bean;

public class GroupInfo {
    private String groupName;
    private String gid;
    private String invitePerson;
    private InviterInfo inviterInfo;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getInvitePerson() {
        return invitePerson;
    }

    public void setInvitePerson(String invitePerson) {
        this.invitePerson = invitePerson;
    }

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String gid, String invitePerson) {
        this.groupName = groupName;
        this.gid = gid;
        this.invitePerson = invitePerson;
    }
}
