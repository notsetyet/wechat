package com.example.myapplication.model.db;

import android.content.Context;

import com.example.myapplication.model.dao.ContactTable;
import com.example.myapplication.model.dao.ContactTableDAO;
import com.example.myapplication.model.dao.InviteTable;
import com.example.myapplication.model.dao.InviteTableDAO;

public class DBManager {
    private final DBHelper dbHelper;

    private final ContactTableDAO contactTableDAO;
    private final InviteTableDAO inviteTableDAO;

    public DBManager(Context context,String name) {
        dbHelper=new DBHelper(context,name);

        contactTableDAO = new ContactTableDAO(dbHelper);
        inviteTableDAO = new InviteTableDAO(dbHelper);
    }

    public ContactTableDAO getContactTableDAO() {
        return contactTableDAO;
    }

    public InviteTableDAO getInviteTableDAO() {
        return inviteTableDAO;
    }

    public void close() {
        dbHelper.close();
    }
}
