package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity_Mana_Import_Manual extends AppCompatActivity {
    private DBHelper dbHelper;// DBHelper对象
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本
    private int teacherID = 0;// 教师ID
    // 下拉框适配器
    private ArrayAdapter<Integer> adapter_Spinner_Class;
    // 下拉框选定的班级
    private int class_Chosen = 0;
    private Spinner spinner_chooseClass;// 班级下拉框
    // 获取组件对象
    private EditText editText_stuName;
    private EditText editText_stuId;
    Button button_submit;// 提交按钮对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mana_import_manual);

        Init_assembly();// 初始化组件
        dbHelper = new DBHelper(Activity_Mana_Import_Manual.this, "TeacherHepler.db", null, 1);
        SetSpinner();// 设置下拉框
        button_submit = findViewById(R.id.activity_mana_import_button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonClickListener_Submit();
            }
        });
    }

    private void Init_assembly() {// 初始化editText组件
        editText_stuId = findViewById(R.id.activity_mana_import_stuID);
        editText_stuName = findViewById(R.id.activity_mana_import_stuName);
    }

    private void SetSpinner() {// 设置下拉框
        // 获取下拉框数据
        List<String> courseNames = new ArrayList<>();
        courseNames = dbHelper.getAllCourseNames();
        courseNames.add(0, "请选择课程");
        List<Integer> classIds = new ArrayList<>();
        classIds = dbHelper.getAllClassIds();
        classIds.add(0, 000);
        List<Integer> count = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            count.add(i);
        }
        // 初始化下拉框
        spinner_chooseClass = findViewById(R.id.activity_mana_import_spinner_chooseClass);
        // 初始化适配器
        adapter_Spinner_Class = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, classIds);
        // 绑定适配器
        spinner_chooseClass.setAdapter(adapter_Spinner_Class);
        //设置列表项选中监听
        spinner_chooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 获取选项中的值
                class_Chosen = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }
    public void ButtonClickListener_Submit() {// 点击提交按钮的事件
        List<Object> data = GetData();
        if (data != null) {
            // 依次获数据
            int classID = (int) data.get(0);// 班级ID
            int stu_id = (int) data.get(1);// 学生id
            String stu_name = (String) data.get(2);// 学生姓名
            // 插入数据
            DBHelper dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
            dbHelper.insertStudentInfo(stu_id, stu_name, classID);
            finish();
//            System.out.println(classID);

        }
    }
    public void Reset(View view) {// 点击重置按钮
        spinner_chooseClass.setSelection(0);
        editText_stuName.setText("");
        editText_stuId.setText("");
    }

    private List<Object> GetData() {// 获取组件数据
        List<Object> data = new ArrayList<>();
        data.add(class_Chosen);
        data.add(Integer.parseInt(editText_stuId.getText().toString()));
        data.add(editText_stuName.getText().toString());
        return data;
    }
}