package com.example.teacherhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Student extends BaseAdapter {
    private List<ListRow_StudentIDAndStudentName> listItems = new ArrayList<>();// 所有学生的数据
    private Context context;
    public Adapter_Student(Context context, List<ListRow_StudentIDAndStudentName> listItems){
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
            view = inflater.inflate(R.layout.listview_student_item_layout, null);
        }
        // 获取组件对象
        TextView textView_studentName = view.findViewById(R.id.listview_student_item_layout_studentName);
        TextView textView_studentID = view.findViewById(R.id.listview_student_item_layout_studentID);
        // 设置数据
        // 从listItems中获取当前位置的Row_CourseAndClassID对象
        ListRow_StudentIDAndStudentName currentItem = listItems.get(i);
        textView_studentName.setText(currentItem.getStudentName());
        textView_studentID.setText(currentItem.getStudentID());
        return view;
    }
}
