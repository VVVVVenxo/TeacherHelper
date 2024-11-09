package com.example.teacherhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class Adapter_Home extends BaseAdapter {
    private List<Map<String,Object>> datas;
    private Context context;
    private Activity homeActivity;
    public Adapter_Home(List<Map<String,Object>> maps, Context context, Activity homeActivity){
        this.datas=maps;
        this.context=context;
        this.homeActivity=homeActivity;
    }
    /**
     * 返回item的个数
     * @return
     */
    @Override
    public int getCount() {
        return datas.size();
    }
    /**
     * 返回每一个item对象
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }
    /**
     * 返回每一个item的id
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Adapter_Home.ViewHolder holder;
        if(view==null){
            view= LayoutInflater.from(context).
                    inflate(R.layout.item_main,null);
            holder=new ViewHolder();
            holder.iv_icon=view.findViewById(R.id.iv_icon);
            holder.tv_name=view.findViewById(R.id.tv_name);

            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(homeActivity.getApplication(),holder.nextActivity);
                    homeActivity.startActivity(intent1);
                }
            });
            view.setTag(holder);
        }
        else {
            holder=(ViewHolder)view.getTag();
        }

        holder.iv_icon.setImageResource((int)datas.get(i).get("imageId"));
        holder.tv_name.setText(datas.get(i).get("names").toString());
        holder.nextActivity= (Class<Activity>) datas.get(i).get("nextActivity");
        return view;
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        Class<Activity> nextActivity;
    }
}
