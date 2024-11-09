package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Manager_Course extends AppCompatActivity {// 教师管理课程
    private Adapter_Course adapterCourse;// 获取适配器
    private List<ListRow_CourseAndClassID> listItems = new ArrayList<>();// 创建list集合
    private ListView listView;// listview组件对象
    private int teacherID = 123;// 教师ID
    private DBHelper dbHelper;// DBHelper对象
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本
    private Button button_increase, button_home;// 增加按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_course);
        dbHelper = new DBHelper(this, DATABASE_NAME, null, version);
        // GetIntentData();// 获取教师ID
        GetTeacherIDFormFile();// 从文件获取教师ID
        listView = findViewById(R.id.activity_manager_course_listview_showAll);// 获取listview组件对象
        // 添加ListView点击和长按事件
        registerForContextMenu(listView);// 定义ListView的长按菜单
        ListviewClickListener();// 添加ListView点击事件
//        button_increase = findViewById(R.id.activity_manager_course_button_increase);
        ButtonClickListener_Insert();
    }

    private void GetTeacherIDFormFile() {// 从文件获取教师ID
        SharedPreferences sp = getSharedPreferences("teaInfo_Save", MODE_PRIVATE);
        teacherID = sp.getInt("teacherId", -1);
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        // 如果intent中存在数据，就获取数据
        if (intent.getStringExtra("教师ID") != null) {
            teacherID = Integer.parseInt(intent.getStringExtra("教师ID"));
        }
    }

    private void ListviewClickListener() {// Listview点击事件：通过班级ID和课程名称、教师ID查找整个班级的学生信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击项在ListView中的位置
                // 获取点击位置的数据
                ListRow_CourseAndClassID currentItem = listItems.get(position);
                // 将数据传递到以班级为单位查询学生数据
                Intent intent = new Intent(Activity_Manager_Course.this, Activity_Search_byClassIDAndCourseName.class);
                intent.putExtra("教师ID", Integer.toString(teacherID));
                intent.putExtra("班级ID", currentItem.getClassID());
                intent.putExtra("课程名", currentItem.getCourse());
                startActivity(intent);
            }
        });
    }

    private void GetListData() {// 获取要在ListView中显示的数据
        HashMap<String, Integer> courseLists = dbHelper.getCourseClassRelationsByTeacherId(teacherID);
        for (Map.Entry<String, Integer> entry : courseLists.entrySet()) {
            String courseName = entry.getKey();
            int classId = entry.getValue();
            listItems.add(new ListRow_CourseAndClassID(courseName, Integer.toString(classId)));
        }
    }

    public void ButtonClickListener_Insert() {// 设置按钮点击事件
        // 点击按钮跳转到课程设置界面
        Button button_increase = findViewById(R.id.activity_manager_course_button_increase);
        button_home = findViewById(R.id.activity_manager_course_button_home);
        button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Manager_Course.this, Activity_Manager_Course_Increase.class);
                intent.putExtra("教师ID", Integer.toString(teacherID));
                startActivity(intent);
            }
        });
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Manager_Course.this, ActivityTeaMain.class);
                startActivity(intent);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {// 为长按显示的添加详细的项目题图标
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        contextMenu.add(0, 1, 0, "编辑");
        // contextMenu.add(0, 2, 0, "删除");
    }

    // 定义点击菜单里每个项目的动作
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        switch (menuItem.getItemId()) {
            // 点击编辑事件，修改分数占比
            case 1:
                // 获取点击项在ListView中的位置
                int position1 = info.position;
                // 通过这个位置获取数据
                ListRow_CourseAndClassID currentItem1 = listItems.get(position1);
                Intent intent = new Intent(Activity_Manager_Course.this, Activity_Update_proportion.class);
                intent.putExtra("课程名称", currentItem1.getCourse());
                intent.putExtra("班级ID", currentItem1.getClassID());
                intent.putExtra("教师ID", Integer.toString(teacherID));
                startActivity(intent);
                break;
//            // 点击删除事件
//            case 2:
//                // 获取点击项在ListView中的位置
//                int position2 = info.position;
//                // 通过这个位置获取数据
//                ListRow_CourseAndClassID currentItem2 = listItems.get(position2);
//
//                break;
//            default:
//                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listItems.clear();
        GetListData();// 获取要在ListView中显示的数据
        adapterCourse = new Adapter_Course(Activity_Manager_Course.this, listItems);// 初始化适配器
        listView.setAdapter(adapterCourse);// 绑定适配器
        adapterCourse.notifyDataSetChanged();// 更新listview
    }
}