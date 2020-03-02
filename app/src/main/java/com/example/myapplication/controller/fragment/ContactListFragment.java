package com.example.myapplication.controller.fragment;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.controller.activity.AddContactActivity;
import com.example.myapplication.controller.activity.MainActivity;
import com.hyphenate.easeui.ui.EaseContactListFragment;

public class ContactListFragment extends EaseContactListFragment {

    @Override
    protected void initView() {
        super.initView();

        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setBackgroundColor(0xff99cc00);

        //
        View headerView = View.inflate(getActivity(),R.layout.header_fragment_contact,null);

        listView.addHeaderView(headerView);

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
    }
}
