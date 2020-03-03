package com.example.myapplication.model.dao;

public class ContactTable {
    public static final String TAB_NAME="tab_contact";

    public static final String COL_NAME="contact_name";
    public static final String COL_HXID="contact_hxid";
    public static final String COL_NICK="contact_nick";
    public static final String COL_PHOTO="contact_photo";

    //群里可以非好友
    public static final String COL_IS_CONTACT="is_contact";

    public static final String CREATE_TAB="create table "
            + TAB_NAME +" ("
            +COL_HXID +" text primary key,"
            +COL_NAME+" text,"
            +COL_NICK+" text,"
            +COL_PHOTO+" text,"
            +COL_IS_CONTACT+" integer);";//0:not 1:is
}
