package com.example.myapplication.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPickContactInfos=new ArrayList<>();
    private List<String> mExistMem=new ArrayList<>();

    public PickContactAdapter(Context context,List<PickContactInfo> pickContactInfos,List<String> existMem) {
        this.mContext = context;
        if(pickContactInfos!=null&&pickContactInfos.size()>=0){
            mPickContactInfos.clear();
            mPickContactInfos.addAll(pickContactInfos);
        }
        if(existMem!=null&&existMem.size()>=0){
            mExistMem.clear();
            mExistMem.addAll(existMem);
        }
    }

    @Override
    public int getCount() {
        return mPickContactInfos==null?0:mPickContactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mPickContactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder=new ViewHolder();

            convertView=View.inflate(mContext, R.layout.item_pick,null);
            holder.cb=(CheckBox)convertView.findViewById(R.id.cb_pick);
            holder.name=(TextView)convertView.findViewById(R.id.tv_pick_name);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        PickContactInfo pickContactInfo = mPickContactInfos.get(position);

        holder.name.setText(pickContactInfo.getUserInfo().getName());
        holder.cb.setChecked(pickContactInfo.getChecked());

        //如果已经在群里，则不能再次添加
        if(mExistMem.contains(pickContactInfo.getUserInfo().getHxid())){
            holder.cb.setChecked(true);
            pickContactInfo.setChecked(true);
        }
        return convertView;
    }

    public List<String> pickContacts(){
        List<String> mPicks=new ArrayList<>();

        for(PickContactInfo pickContactInfo:mPickContactInfos){
            if(pickContactInfo.getChecked()==true){
                mPicks.add(pickContactInfo.getUserInfo().getName());
            }
        }

        return mPicks;
    }

    private class ViewHolder{
        private CheckBox cb;
        private TextView name;
    }

}
