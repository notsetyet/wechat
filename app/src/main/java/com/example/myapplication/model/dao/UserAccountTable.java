package com.example.myapplication.model.dao;

public class UserAccountTable {
    public static final String TAB_NAME="tab_account";
    public static final String COL_NAME="account_name";
    public static final String COL_HXID="account_hxid";
    public static final String COL_NICK="account_nick";
    public static final String COL_PHOTO="account_photo";

    public static final String CREATE_TAB="create table "
            + TAB_NAME +" ("
            +COL_HXID +" text primary key,"
            +COL_NAME+" text,"
            +COL_NICK+" text,"
            +COL_PHOTO+" text);";

}
