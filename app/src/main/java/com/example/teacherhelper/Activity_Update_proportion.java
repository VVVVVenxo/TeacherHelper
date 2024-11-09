package com.example.teacherhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Activity_Update_proportion extends AppCompatActivity{
    private TextView textView_courseName, textView_classID;// 课程名、班级ID
    private EditText editText_rollCallProportion, editText_operateProportion, editText_normalHomework;// 点名、上机、平直作业占比
    private Button button_submit, button_restart;// 提交和重置按钮
    private String classID, courseName, teacherID;// 班级ID， 课程名称， 教师ID
    private DBHelper dbHelper;// DBHelper对象
    private Spinner spinner_rollCall_count, spinner_regularHomework_count, spinner_operate_count;// 选择成绩次数
    private ArrayAdapter<Integer> adapter_Spinner_Class, adapter_Spinner_RollCall_count, adapter_Spinner_regularHomework_count, adapter_spinner_operate_count;// 下拉框适配器
    private int class_Chosen = 0, rollCall_count = 0, regular_Homework_count = 0, operate_count = 0;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_update_proportion);
        dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
        GetIntentData();// 获取intent数据
        InitAssembly();// 初始化组件
        SetSpinner();// 设置下拉框
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button_Submit_ClickListeneer();
            }
        });
        button_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button_Restart_ClickListener();
            }
        });
    }

    private void SetSpinner() {// 设置下拉框
        List<Integer> count = new ArrayList<>();
        for (int i = 1; i <= 10; i++){
            count.add(i);
        }
        adapter_Spinner_RollCall_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);
        adapter_Spinner_regularHomework_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);
        adapter_spinner_operate_count = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, count);

        spinner_rollCall_count.setAdapter(adapter_Spinner_RollCall_count);
        spinner_operate_count.setAdapter(adapter_spinner_operate_count);
        spinner_regularHomework_count.setAdapter(adapter_Spinner_regularHomework_count);

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

    private void Button_Restart_ClickListener() {// 点击重置按钮
        editText_normalHomework.setText("");
        editText_operateProportion.setText("");
        editText_rollCallProportion.setText("");
    }

    private void Button_Submit_ClickListeneer() {// 点击提交按钮
        dbHelper.updateChooseCourseScores(dbHelper.getCourseIdByName(courseName),
                Integer.parseInt(teacherID), Integer.parseInt(classID),
                Integer.parseInt(editText_rollCallProportion.getText().toString()),
                Integer.parseInt(editText_operateProportion.getText().toString()),
                Integer.parseInt(editText_normalHomework.getText().toString()));
        dbHelper.updateScoreCounts(Integer.parseInt(teacherID), dbHelper.getCourseIdByName(courseName), Integer.parseInt(classID),
                rollCall_count, operate_count, regular_Homework_count);
        finish();
    }

    private void GetIntentData() {// 获取intent数据
        Intent intent = getIntent();
        teacherID = intent.getStringExtra("教师ID");
        classID = intent.getStringExtra("班级ID");
        courseName = intent.getStringExtra("课程名称");
    }

    private void InitAssembly() {// 初始化组件
        textView_classID = findViewById(R.id.activity_update_proportion_textview_classID);
        textView_courseName = findViewById(R.id.activity_update_proportion_textview_courseName);

        textView_classID.setText(classID);
        textView_courseName.setText(courseName);

        editText_normalHomework = findViewById(R.id.activity_update_proportion_EditText_regularHomework_proportion);
        editText_operateProportion = findViewById(R.id.activity_update_proportion_EditText_operate_proportion);
        editText_rollCallProportion = findViewById(R.id.activity_update_proportion_EditText_rollCall_proportion);
        button_restart = findViewById(R.id.activity_update_proportion_button_reset);
        button_submit = findViewById(R.id.activity_update_proportion_button_submit);

        spinner_rollCall_count = findViewById(R.id.activity_update_proportion_Spinner_rollCall_count);
        spinner_regularHomework_count = findViewById(R.id.activity_update_proportion_Spinner_regularHomework_count);
        spinner_operate_count = findViewById(R.id.activity_update_proportion_Spinner_operate_count);
    }
}
