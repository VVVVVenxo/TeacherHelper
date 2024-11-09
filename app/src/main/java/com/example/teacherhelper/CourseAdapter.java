package com.example.teacherhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseAdapter extends BaseAdapter {
    private Context context;
    private List<Row_CourseAndClassID> listItems = new ArrayList<>();// 创建list集合

    public CourseAdapter(Context context, List<Row_CourseAndClassID> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.listview_course_item_layout, null);
        }
        // 获取组件对象
        TextView textView_courseName = view.findViewById(R.id.listview_course_item_layout_textview_courseName);
        TextView textView_classID = view.findViewById(R.id.listview_course_item_layout_textview_classID);
        // 设置数据
        // 从listItems中获取当前位置的Row_CourseAndClassID对象
        Row_CourseAndClassID currentItem = listItems.get(i);
        textView_courseName.setText(currentItem.getCourse());
        textView_classID.setText(currentItem.getClassID());
        return view;
    }
}
