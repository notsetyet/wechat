package com.example.myapplication.controller.fragment;

import android.content.Intent;

import com.example.myapplication.controller.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

public class ChiefFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();

        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //传参
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                //是否是群聊
                if(conversation.getType()== EMConversation.EMConversationType.GroupChat){
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                }

                startActivity(intent);
            }
        });
        //避免闪动会导致同一会话多次出现在主页
        conversationList.clear();

        EMClient.getInstance().chatManager().addMessageListener(eMessageListener);
    }

    private EMMessageListener eMessageListener=new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {

            EaseUI.getInstance().getNotifier().notify(list);

            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}
