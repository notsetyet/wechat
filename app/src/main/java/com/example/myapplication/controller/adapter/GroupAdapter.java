package com.example.myapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mGroup=new ArrayList<>();
    private EMGroup mGroupInfo;
    public GroupAdapter(Context context) {
        mContext=context;
    }

    public void refresh(List<EMGroup> groups){
        if(groups!=null||groups.size()>=0){
            mGroup.clear();
            mGroup.addAll(groups);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mGroup==null?0:mGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext,R.layout.item_group,null);
            holder.tv_group_name=(TextView)convertView.findViewById(R.id.tv_group_name);
            holder.tv_group_reason=(TextView)convertView.findViewById(R.id.tv_group_reason);
            holder.bt_group_acc=(Button)convertView.findViewById(R.id.bt_group_acc);
            holder.bt_group_rej=(Button)convertView.findViewById(R.id.bt_group_rej);

            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        mGroupInfo=mGroup.get(position);

        holder.tv_group_name.setText(mGroupInfo.getGroupName());


        return convertView;
    }

    private class ViewHolder{
        private TextView tv_group_name;
        private TextView tv_group_reason;
        private Button bt_group_acc;
        private Button bt_group_rej;
    }

    public interface onGroupListener{
        public void onAccept();
        public void onReject();
    }
}
