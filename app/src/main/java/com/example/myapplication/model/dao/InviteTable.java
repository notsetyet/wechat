package com.example.myapplication.model.dao;

public class InviteTable {

    public static final String TAB_NAME="tab_invite";

    public static final String COL_NAME="user_name";
    public static final String COL_HXID="user_hxid";

    public static final String COL_GROUP_NAME="group_name";
    public static final String COL_GROUP_GID="group_hxid";

    public static final String COL_REASON="invite_reason";
    public static final String COL_STATUS="invite_status";//是枚举

    public static final String CREATE_TAB="create table "
            + TAB_NAME +" ("
            +COL_HXID +" text primary key,"
            +COL_NAME+" text,"
            +COL_GROUP_GID+" text,"
            +COL_GROUP_NAME+" text,"
            +COL_REASON+" text,"
            +COL_STATUS+" integer);";
}
