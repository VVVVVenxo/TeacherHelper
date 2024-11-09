package com.example.teacherhelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_Manager_Course_Increase extends AppCompatActivity {// 添加教学记录
    private DBHelper dbHelper;// DBHelper对象
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本
    private int teacherID = 0;// 教师ID
    // 下拉框适配器
    private ArrayAdapter<String> adapter_Spinner_Course, adapter_Spinner_Class;
    private ArrayAdapter<Integer> adapter_Spinner_RollCall_count, adapter_Spinner_regularHomework_count, adapter_spinner_operate_count;
    // 下拉框选定的班级和课程
    private String course_Chosen = null;
    private int class_Chosen = 0, rollCall_count = 0, regular_Homework_count = 0, operate_count = 0;
    private Spinner spinner_chooseClass, spinner_chooseCourse;// 班级、课程下拉框
    // 获取组件对象
//    private EditText editText_rollCall_count;
    private Spinner spinner_rollCall_count;
    private EditText editText_rollCall_proportion;
//    private EditText editText_regularHomework_count;
    private Spinner spinner_regularHomework_count;
    private EditText editText_regularHomework_proportion;
//    private EditText editText_operate_count;
    private Spinner spinner_operate_count;
    private EditText editText_operate_proportion;
    Button button_submit;// 提交按钮对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_course_increase);
        Init_assembly();// 初始化组件
        dbHelper = new DBHelper(Activity_Manager_Course_Increase.this, "TeacherHepler.db", null, 1);
        SetSpinner();// 设置下拉框
        button_submit = findViewById(R.id.activity_manager_course_increase_button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClickListener_Submit();
            }
        });
    }

    private void Init_assembly() {// 初始化editText组件
        spinner_rollCall_count = findViewById(R.id.activity_manager_course_increase_Spinner_rollCall_count);
        editText_rollCall_proportion = findViewById(R.id.activity_manager_course_increase_EditText_rollCall_proportion);
        spinner_regularHomework_count = findViewById(R.id.activity_manager_course_increase_Spinner_regularHomework_count);
        editText_regularHomework_proportion = findViewById(R.id.activity_manager_course_increase_EditText_regularHomework_proportion);
        spinner_operate_count = findViewById(R.id.activity_manager_course_increase_Spinner_operate_count);
        editText_operate_proportion = findViewById(R.id.activity_manager_course_increase_EditText_operate_proportion);
    }

    public void ButtonClickListener_Submit() {// 点击提交按钮的事件
        List<Object> data = GetData();
        if (data != null) {
            // 依次获数据
            String courseName = (String) data.get(0);// 课程名
            int classID = (int) data.get(1);// 班级ID
            int rollCall_count = (int) data.get(2);// 点名次数
            int rollCall_proportion = (int) data.get(3);// 点名成绩占比
            int regularHomework_count = (int) data.get(4); // 作业次数
            int regularHomework_proportion = (int) data.get(5);// 作业成绩占比
            int operate_count = (int) data.get(6);// 上机次数
            int operate_proportion = (int) data.get(7);// 上机成绩占比
            // 获取数据库对象
            DBHelper dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
            if (courseName.equals("请选择课程")){
                Toast.makeText(getApplicationContext(), "选课错误", Toast.LENGTH_SHORT).show();
                courseName = "计算机网络";
            }
            // 获取课程ID
            int courseId = dbHelper.getCourseIdByName(courseName);
            // 添加教师数据
            teacherID = GetIntentData();
            if (dbHelper.checkChooseCourseData(teacherID, courseId, classID)){
                Toast.makeText(getApplicationContext(), "重复创建", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rollCall_proportion + regularHomework_proportion + operate_proportion == 100) {
                // 选择学生
                Intent intent = new Intent(Activity_Manager_Course_Increase.this, Activity_Manager_Course_Increase_ChooseStudent.class);
                intent.putExtra("点名次数", Integer.toString(rollCall_count));
                intent.putExtra("点名占比", Integer.toString(rollCall_proportion));
                intent.putExtra("上机次数", Integer.toString(operate_count));
                intent.putExtra("上机占比", Integer.toString(operate_proportion));
                intent.putExtra("平时作业次数", Integer.toString(regularHomework_count));
                intent.putExtra("平时作业占比", Integer.toString(regularHomework_proportion));
                intent.putExtra("班级ID", Integer.toString(classID));
                intent.putExtra("课程ID", Integer.toString(courseId));
                intent.putExtra("教师ID", Integer.toString(teacherID));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "成绩占比错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void GetSpinnerData() {// 设置列表项选中监听
        spinner_chooseCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 获取选项中的值
                course_Chosen = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 可以在这里处理当没有任何项被选中的情况
            }
        });
        spinner_chooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 获取选项中的值
                if (!adapterView.getItemAtPosition(i).toString().equals("请选择班级")){
                    class_Chosen = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 可以在这里处理当没有任何项被选中的情况
            }
        });
        spinner_regularHomework_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regular_Homework_count = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_operate_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operate_count = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_rollCall_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rollCall_count = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int GetIntentData() {// 获取Intent数据
        Intent intent = getIntent();

        teacherID = Integer.parseInt(intent.getStringExtra("教师ID"));
        return teacherID;
    }

    private void SetSpinner() {// 设置下拉框
        // 获取下拉框数据
        List<String> courseNames = new ArrayList<>();
        courseNames = dbHelper.getAllCourseNames();
        courseNames.add(0, "请选择课程");
        List<Integer> classIds = new ArrayList<>();
        List<String> classIds_String = new ArrayList<>();
        classIds = dbHelper.getAllClassIds();
        classIds_String.add("请选择班级");
        for (int i = 0; i < classIds.size(); i++) {
            classIds_String.add(Integer.toString(classIds.get(i)));
        }
        List<Integer> count = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            count.add(i);
        }
        // 初始化下拉框
        spinner_chooseCourse = findViewById(R.id.activity_manager_course_increase_spinner_chooseCourse);
        spinner_chooseClass = findViewById(R.id.activity_manager_course_increase_spinner_chooseClass);
        // 初始化适配器
        adapter_Spinner_Course = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter_Spinner_Class = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classIds_String);
        adapter_Spinner_RollCall_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);
        adapter_Spinner_regularHomework_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);
        adapter_spinner_operate_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);

        // 绑定适配器
        spinner_chooseCourse.setAdapter(adapter_Spinner_Course);
        spinner_chooseClass.setAdapter(adapter_Spinner_Class);
        spinner_rollCall_count.setAdapter(adapter_Spinner_RollCall_count);
        spinner_operate_count.setAdapter(adapter_spinner_operate_count);
        spinner_regularHomework_count.setAdapter(adapter_Spinner_regularHomework_count);
        GetSpinnerData();
    }

    public void Reset(View view) {// 点击重置按钮
        spinner_chooseClass.setSelection(0);
        spinner_chooseCourse.setSelection(0);
        editText_regularHomework_proportion.setText("");
//        editText_regularHomework_count.setText("");
//        editText_operate_count.setText("");
        editText_operate_proportion.setText("");
        editText_rollCall_proportion.setText("");
//        editText_rollCall_count.setText("");
    }

    private List<Object> GetData() {// 获取组件数据
        List<Object> data = new ArrayList<>();
        data.add(course_Chosen);
        data.add(class_Chosen);
        data.add(rollCall_count);
        data.add(Integer.parseInt(editText_rollCall_proportion.getText().toString()));
        data.add(regular_Homework_count);
        data.add(Integer.parseInt(editText_regularHomework_proportion.getText().toString()));
        data.add(operate_count);
        data.add(Integer.parseInt(editText_operate_proportion.getText().toString()));
        return data;
    }
}
