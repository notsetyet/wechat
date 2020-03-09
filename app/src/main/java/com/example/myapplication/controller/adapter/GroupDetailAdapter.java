package com.example.myapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean canModify;
    private List<UserInfo> userInfos=new ArrayList<>();
    private boolean isDelMode;

    private OnGroupDetailListener mOnGroupDetailListener;

    public GroupDetailAdapter() {
    }

    public GroupDetailAdapter(Context mContext, boolean canModify, OnGroupDetailListener onGroupDetailListener) {
        this.mContext = mContext;
        this.canModify = canModify;
        mOnGroupDetailListener=onGroupDetailListener;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public void refresh(List<UserInfo> users){
        if(users!=null&&users.size()>=0){
            userInfos.clear();
            initUsers();
            userInfos.addAll(0,users);
        }
        //刷新页面
        notifyDataSetChanged();
    }

    //保证图标顺序
    private void initUsers() {
        //加号图标
        UserInfo add = new UserInfo("add");
        //减号图标
        UserInfo del = new UserInfo("del");
        userInfos.add(del);
        userInfos.add(0,add);
    }

    @Override
    public int getCount() {
        return userInfos==null?0:userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder= new ViewHolder();

            convertView=View.inflate(mContext,R.layout.item_groupdetail,null);

            holder.iv_groupdetail_del=(ImageView)convertView.findViewById(R.id.iv_groupdetail_del);
            holder.iv_groupdetail_photo=(ImageView)convertView.findViewById(R.id.iv_groupdetail_photo);
            holder.tv_groupdetail_name=(TextView)convertView.findViewById(R.id.tv_groupdetail_name);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        //获取数据
        UserInfo userInfo = userInfos.get(position);
        //页面显示处理
        if(canModify){//群主或公开群
            //减号处理
            if(position==getCount()-1){//减号
                //是删除模式
                if(isDelMode){
                    convertView.setVisibility(View.INVISIBLE);
                }else{//不是删除模式
                    convertView.setVisibility(View.VISIBLE);

                    holder.iv_groupdetail_photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    holder.iv_groupdetail_del.setVisibility(View.GONE);
                    holder.tv_groupdetail_name.setVisibility(View.INVISIBLE);
                }
            }else if(position==getCount()-2){//加号
                if(isDelMode){
                    convertView.setVisibility(View.INVISIBLE);
                }else{//不是删除模式
                    convertView.setVisibility(View.VISIBLE);

                    holder.iv_groupdetail_photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    holder.iv_groupdetail_del.setVisibility(View.GONE);
                    holder.tv_groupdetail_name.setVisibility(View.INVISIBLE);
                }
            }else{//成员
                convertView.setVisibility(View.VISIBLE);
                holder.tv_groupdetail_name.setVisibility(View.VISIBLE);

                holder.tv_groupdetail_name.setText(userInfo.getName());
                holder.iv_groupdetail_photo.setImageResource(R.drawable.em_default_avatar);

                if(isDelMode){
                    holder.iv_groupdetail_del.setVisibility(View.VISIBLE);
                }else{
                    holder.iv_groupdetail_del.setVisibility(View.GONE);
                }
            }

            //点击事件
            if(position==getCount()-1){
                holder.iv_groupdetail_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isDelMode){
                            isDelMode=true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if(position==getCount()-2){
                holder.iv_groupdetail_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.addGroupMem();
                    }
                });
            }else {
                holder.iv_groupdetail_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.delGroupMem(userInfo);
                    }
                });
            }
        }else{//普通成员
            if(position==getCount()-1||position==getCount()-2){
                //加号或减号位置
                convertView.setVisibility(View.GONE);
            }else{
                convertView.setVisibility(View.VISIBLE);

                holder.tv_groupdetail_name.setText(userInfo.getName());
                holder.iv_groupdetail_photo.setImageResource(R.drawable.em_default_avatar);
                holder.iv_groupdetail_del.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolder{
        private ImageView iv_groupdetail_photo;
        private ImageView iv_groupdetail_del;
        private TextView tv_groupdetail_name;
    }

    public interface OnGroupDetailListener{
        public void addGroupMem();
        public void delGroupMem(UserInfo userInfo);
    }
}
