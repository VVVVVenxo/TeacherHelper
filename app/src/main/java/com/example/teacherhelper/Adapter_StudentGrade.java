package com.example.teacherhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_StudentGrade extends BaseAdapter {// Activity_ShowStudentGrade里面的ListView的Adapter
    List<ListRow_TestNameAndGrade_Show> listItems = new ArrayList<>();// 学生的所有成绩
    Context context;
    public Adapter_StudentGrade(Context context, List<ListRow_TestNameAndGrade_Show> listItems){
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
            view = inflater.inflate(R.layout.listview_student_grade_item_layout, null);
        }
        // 获取组件对象
        TextView textView_testName = view.findViewById(R.id.listview_student_grade_item_layout_textView_testName);
        TextView textView_content = view.findViewById(R.id.listview_student_grade_item_layout_textView_grade);
        // 设置数据
        // 从listItems中获取当前位置的Row_CourseAndClassID对象
        ListRow_TestNameAndGrade_Show currentItem = listItems.get(i);
        textView_testName.setText(currentItem.getTestName() + ":");
        textView_content.setText(currentItem.getContent());
        return view;
    }
}
