package com.example.myapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.bean.InviterInfo;
import com.example.myapplication.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InviterInfo> mInvitationInfo=new ArrayList<>();
    private OnInviteListener onInviteListener;
    private InviterInfo inviterInfo;
    public InviteAdapter(Context context, OnInviteListener onInviteListener) {
        mContext=context;
        this.onInviteListener=onInviteListener;
    }

    public void refresh(List<InviterInfo> inviterInfos){
        if(inviterInfos!=null&&inviterInfos.size()>=0){
            //不清空则同一申请多次同意后会重复显示
            mInvitationInfo.clear();
            mInvitationInfo.addAll(inviterInfos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfo==null?0:mInvitationInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取或创建viewholder
        ViewHolder viewHolder=null;

        if(convertView==null){
            viewHolder=new ViewHolder();

            convertView=View.inflate(mContext, R.layout.item_invite,null);
            viewHolder.tv_invite_name=(TextView) convertView.findViewById(R.id.tv_invite_name);
            viewHolder.tv_invite_reason=(TextView) convertView.findViewById(R.id.tv_invite_reason);
            viewHolder.bt_invite_acc=(Button) convertView.findViewById(R.id.bt_invite_acc);
            viewHolder.bt_invite_rej=(Button) convertView.findViewById(R.id.bt_invite_rej);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //获取当前数据
        inviterInfo=mInvitationInfo.get(position);

        //显示数据
        //判断是群或联系人
        UserInfo userInfo=inviterInfo.getUserInfo();
        if(userInfo!=null){

            viewHolder.tv_invite_name.setText(inviterInfo.getUserInfo().getName());

            viewHolder.bt_invite_rej.setVisibility(View.GONE);
            viewHolder.bt_invite_acc.setVisibility(View.GONE);

            //如果状态是新的邀请
            if(inviterInfo.getInvitationStatus()==InviterInfo.InvitationStatus.NEW_INVITE){

                if(inviterInfo.getReason()==null){
                    viewHolder.tv_invite_reason.setText("添加好友");
                }else{
                    viewHolder.tv_invite_reason.setText(inviterInfo.getReason());
                }
                viewHolder.bt_invite_rej.setVisibility(View.VISIBLE);
                viewHolder.bt_invite_acc.setVisibility(View.VISIBLE);
            }else if(inviterInfo.getInvitationStatus()==InviterInfo.InvitationStatus.INVITE_ACCEPT){

                if(inviterInfo.getReason()==null){
                    viewHolder.tv_invite_reason.setText(" 接受邀请");
                }else{
                    viewHolder.tv_invite_reason.setText(inviterInfo.getReason());
                }
            }else if(inviterInfo.getInvitationStatus()==InviterInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER){

                if(inviterInfo.getReason()==null){
                    viewHolder.tv_invite_reason.setText("邀请被接受");
                }else{
                    viewHolder.tv_invite_reason.setText(inviterInfo.getReason());
                }
            }

            viewHolder.bt_invite_rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onInviteListener.OnReject(inviterInfo);
                }
            });

            viewHolder.bt_invite_acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onInviteListener.OnAccept(inviterInfo);
                }
            });
        }else{

        }
        //返回view
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_invite_name;
        private TextView tv_invite_reason;
        private Button bt_invite_acc;
        private Button bt_invite_rej;
    }

    //为了让adapter都是数据操作
    public interface OnInviteListener{
        void OnAccept(InviterInfo inviterInfo);

        void OnReject(InviterInfo inviterInfo);
    }
}
