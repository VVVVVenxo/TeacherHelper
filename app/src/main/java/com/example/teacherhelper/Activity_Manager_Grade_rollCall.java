package com.example.teacherhelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Manager_Grade_rollCall extends AppCompatActivity {
    private String type = null, number = null, courseID = null, classID = null, teacherID = null; // 成绩类型，次数，课程ID，班级ID
    private String studentID = null, studentName = null;// 学生学号，学生姓名
    private String record = "无";// 教师输入的备注
    private String whether;
    private List<ListRow_StudentIDAndStudentName> listItems_StudentIdAndStudentName = new ArrayList<>();// 存储该班级所有学生数据
    private EditText editText_Record;// 输入框组件
    private Button button_attend, button_absent;// 按钮组件
    private DBHelper dbHelper;// dbhelper对象
    private TextView textView_studentID, textView_studentName;// 学生ID和学生姓名的textview
    private TextView textView_typeName;// 成绩名称
    private int order = 0;// 第几个学生
    private Spinner spinner_score;
    private ArrayAdapter adapter_spinner;
    private String score_chosen;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manager_grade_rollcall);
        dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
        GetIntentData();// 从Intent中获取数据
        InitAssembly();// 初始化组件
        GetStudents();// 获取学生数据
        UpdateStudent();// 更新学生
        List<String> score = new ArrayList<>();
        for (double i = 0; i < 10.5; i+= 0.5){
            score.add(Double.toString(i));
        }
        adapter_spinner = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, score);
        spinner_score.setAdapter(adapter_spinner);
        //设置列表项选中监听
        spinner_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // 获取选项中的值
                score_chosen = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // 可以在这里处理当没有任何项被选中的情况
            }
        });
        button_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRecord();// 获取教师输入的备注
                if (type.equals("点名")) {
                    SendGrade(10);// 向数据库传递数据
                } else {
                    SendGrade(Double.parseDouble(score_chosen));// 向数据库传递数据
                }
                UpdateStudent();// 更新学生
            }
        });
        button_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRecord();// 获取教师输入的备注
                if (type.equals("点名")) {
                    SendGrade(1);// 向数据库传递数据
                }
                UpdateStudent();// 更新学生
            }
        });
    }

    private void SendGrade(double grade) {// 向数据库传递数据
        dbHelper.updateCourseScore(Integer.parseInt(courseID), Integer.parseInt(classID), Integer.parseInt(studentID), type, Integer.parseInt(number), grade, record);

    }

    private void UpdateStudent() {// 更新学生
        // 如果没有遍历完，就继续，否则给出提示并退出
        if (order != listItems_StudentIdAndStudentName.size()) {
            studentID = listItems_StudentIdAndStudentName.get(order).getStudentID();
            studentName = listItems_StudentIdAndStudentName.get(order).getStudentName();
            order++;
        } else {
            finish();
        }
        textView_studentName.setText(studentName);
        textView_studentID.setText(studentID);
    }

    private void GetStudents() {// 获取学生数据
        Intent intent = getIntent();
        if (intent.getStringExtra("update")!=null){
            listItems_StudentIdAndStudentName.add(new ListRow_StudentIDAndStudentName(intent.getStringExtra("学生ID"), intent.getStringExtra("学生姓名")));
        }else{
            List<Integer> studentIDs = dbHelper.getStudentIDs(Integer.parseInt(classID), Integer.parseInt(teacherID), Integer.parseInt(courseID));
            for (Integer id : studentIDs) {
                HashMap<Integer, String> studentInformation = dbHelper.getStudentIdAndNameById(id);
                for (Map.Entry<Integer, String> entry : studentInformation.entrySet()) {
                    Integer studentID = entry.getKey();
                    String studentName = entry.getValue();
                    listItems_StudentIdAndStudentName.add(new ListRow_StudentIDAndStudentName(Integer.toString(studentID), studentName));
                }
            }
        }
    }

    private void GetRecord() {// 获取教师输入的备注
        record = editText_Record.getText().toString();
        if (record == null){
            record = "无";
        }
    }

    private void InitAssembly() {// 初始化组件
        editText_Record = findViewById(R.id.activity_manager_grade_editText_record);
        // editText_score = findViewById(R.id.activity_manager_grade_editText_score);
        button_absent = findViewById(R.id.activity_manager_grade_button_absent);
        button_attend = findViewById(R.id.activity_manager_grade_button_attend);
        textView_studentID = findViewById(R.id.activity_manager_grade_textview_rollcall_studentID);
        textView_studentName = findViewById(R.id.activity_manager_grade_textview_rollcall_studentName);
        textView_typeName = findViewById(R.id.activity_manager_grade_textview_rollcall_information);
        spinner_score = findViewById(R.id.activity_manager_grade_Spinner_score);

        // 设置成绩名称
        textView_typeName.setText(type + number);
        // 如果是点名，设置成绩输入框不可见
        if (type.equals("点名")) {
            spinner_score.setVisibility(View.GONE);
        } else if (type.equals("上机") || type.equals("平时作业")) {
            button_absent.setVisibility(View.GONE);
            button_attend.setText("确认");
        }
    }

    private void GetIntentData() {// 从Intent中获取数据
        Intent intent = getIntent();
        type = intent.getStringExtra("成绩类型");
        number = intent.getStringExtra("次数");
        courseID = intent.getStringExtra("课程ID");
        classID = intent.getStringExtra("班级ID");
        whether = intent.getStringExtra("update");
        teacherID = intent.getStringExtra("教师ID");
    }
}
