package com.example.teacherhelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Manager_Course_Increase_ChooseStudent extends AppCompatActivity {
    private DBHelper dbHelper;// DBHelper对象
    private SQLiteDatabase database;// 数据库对象
    private Map<String, String> Map_IntentData = new HashMap<>();// 存储获取到的数据
    private List<ListRow_StudentIDAndStudentName> listItems = new ArrayList<>();// 所有学生数据
    private ListView listView, listView_Quit;// ListView 控件，用于展示数据列表。
    private List<ListRow_StudentIDAndStudentName> listItems_Chosen, listItems_Quit = new ArrayList<>();// 所有学生数据
    private Adapter_Student adapter_student, adapter_student_quit;// 自定义适配器
    private SparseBooleanArray stateCheckedMap = new SparseBooleanArray();    // 用于存放 CheckBox 的选中状态的映射表，true 表示选中，false 表示未选中。
    private boolean isSelectedAll = true;    // 用于控制全选与全不选的切换。

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manager_course_increase_choose_student);
        GetIntentData();// 获取Intent数据
        // 初始化DBHelper
        dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
        // 初始化数据库对象
        database = dbHelper.getWritableDatabase();
        // 根据班级ID获取学生列表
        listItems = dbHelper.getStudentsByClassId(Integer.parseInt(Map_IntentData.get("班级ID")));
        listItems_Chosen = listItems;
        // listItems_Chosen = listItems;
        InitView();// 初始化组件
        setStateCheckedMap(true); // 初始化时设置所有 CheckBox 为选中状态
        adapter_student = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Chosen); // 创建适配器
        adapter_student_quit = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Quit); // 创建适配器
        // 设置适配器
        listView.setAdapter(adapter_student);
        listView_Quit.setAdapter(adapter_student_quit);
        // 设置列表项点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // listItems_Chosen中删除学生数据，并将其添加到listItems_Quit中,之后刷新listview
                listItems_Quit.add(new ListRow_StudentIDAndStudentName(listItems_Chosen.get(i).getStudentID(), listItems_Chosen.get(i).getStudentName()));
                listItems_Chosen.remove(i);
                adapter_student = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Chosen); // 创建适配器
                adapter_student_quit = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Quit); // 创建适配器
                // 设置适配器
                listView.setAdapter(adapter_student);
                listView_Quit.setAdapter(adapter_student_quit);
            }
        });
        listView_Quit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // listItems_Quit中删除学生数据，并将其添加到listItems_Chosen中,之后刷新listview
                listItems_Chosen.add(new ListRow_StudentIDAndStudentName(listItems_Quit.get(i).getStudentID(), listItems_Quit.get(i).getStudentName()));
                listItems_Quit.remove(i);
                adapter_student = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Chosen); // 创建适配器
                adapter_student_quit = new Adapter_Student(Activity_Manager_Course_Increase_ChooseStudent.this, listItems_Quit); // 创建适配器
                // 设置适配器
                listView.setAdapter(adapter_student);
                listView_Quit.setAdapter(adapter_student_quit);
            }
        });
        // 设置按钮点击事件
        Button button = findViewById(R.id.activity_manager_course_increase_choose_student_button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonClickListener(view);
            }
        });
    }

    private void ButtonClickListener(View view) {// 设置提交按钮点击提交数据
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < listItems_Chosen.size(); i++) {
            data.add(Integer.parseInt(listItems_Chosen.get(i).getStudentID()));
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        someMethod(Map_IntentData, data);
        if (data != null){
            boolean whether = dbHelper.chooseCourse(database, Integer.parseInt(Map_IntentData.get("点名次数")), Integer.parseInt(Map_IntentData.get("上机次数")),
                    Integer.parseInt(Map_IntentData.get("平时作业次数")), Integer.parseInt(Map_IntentData.get("班级ID")),
                    Integer.parseInt(Map_IntentData.get("教师ID")), Integer.parseInt(Map_IntentData.get("点名占比")),
                    Integer.parseInt(Map_IntentData.get("上机占比")), Integer.parseInt(Map_IntentData.get("平时作业占比")),
                    Integer.parseInt(Map_IntentData.get("课程ID")), data);
            if (whether){
                Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Manager_Course_Increase_ChooseStudent.this, Activity_Manager_Course.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "创建失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

//    public void someMethod(Map<String, String> Map_IntentData, List<Integer> data) {
//        try {
//            int callTimes = parseIntOrThrow(Map_IntentData, "点名次数");
//            int computerTimes = parseIntOrThrow(Map_IntentData, "上机次数");
//            int homeworkTimes = parseIntOrThrow(Map_IntentData, "平时作业次数");
//            int classId = parseIntOrThrow(Map_IntentData, "班级ID");
//            int teacherId = parseIntOrThrow(Map_IntentData, "教师ID");
//            int callPercentage = parseIntOrThrow(Map_IntentData, "点名占比");
//            int computerPercentage = parseIntOrThrow(Map_IntentData, "上机占比");
//            int homeworkPercentage = parseIntOrThrow(Map_IntentData, "平时作业占比");
//            int courseId = parseIntOrThrow(Map_IntentData, "课程ID");
//
//            dbHelper.chooseCourse(database, callTimes, computerTimes, homeworkTimes, classId, teacherId,
//                    callPercentage, computerPercentage, homeworkPercentage, courseId, data);
//        } catch (NumberFormatException e) {
//            // Handle the exception
//            e.printStackTrace();
//        }
//    }
//
//    private int parseIntOrThrow(Map<String, String> map, String key) {
//        if (map.containsKey(key) && map.get(key) != null) {
//            return Integer.parseInt(map.get(key));
//        } else {
//            Log.d("error", key);
//            throw new NumberFormatException("Value for key '" + key + "' is null or missing.");
//        }
//    }

    private void setOnListViewItemClickListener() {// 设置列表项点击事件

    }

    // 初始化时设置所有 CheckBox 为选中状态
    private void setStateCheckedMap(boolean isSelectedAll) {
        for (int i = 0; i < listItems.size(); i++) {
            stateCheckedMap.put(i, isSelectedAll); // 更新每一项的选中状态
            listView.setItemChecked(i, isSelectedAll); // 更新 ListView 的选中状态
        }
    }

    private void InitView() {// 初始化组件
        listView = (ListView) findViewById(R.id.activity_manager_course_increase_choose_student_listview);
        listView_Quit = findViewById(R.id.activity_manager_course_increase_choose_studentQuit_listview);
    }

    private void GetIntentData() {// 获取Intent数据
        Intent intent = getIntent();
        Map_IntentData.put("点名次数", intent.getStringExtra("点名次数"));
        Map_IntentData.put("点名占比", intent.getStringExtra("点名占比"));
        Map_IntentData.put("上机次数", intent.getStringExtra("上机次数"));
        Map_IntentData.put("上机占比", intent.getStringExtra("上机占比"));
        Map_IntentData.put("平时作业次数", intent.getStringExtra("平时作业次数"));
        Map_IntentData.put("平时作业占比", intent.getStringExtra("平时作业占比"));
        Map_IntentData.put("班级ID", intent.getStringExtra("班级ID"));
        Map_IntentData.put("课程ID", intent.getStringExtra("课程ID"));
        Map_IntentData.put("教师ID", intent.getStringExtra("教师ID"));
    }
}
