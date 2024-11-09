package com.example.teacherhelper;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_ShowStudentGrade extends AppCompatActivity {// 显示学生成绩
    private String teacherID, classID, courseName, studentID, studentName;// 教师ID，班级ID，课程名，学生ID，学生姓名
    private List<ListRow_TestNameAndGrade_Show> listItems = new ArrayList<>();// 学生的所有成绩
    private Adapter_StudentGrade adapter_studentGrade;// 获取适配器对象
    private ListView listView;// 获取ListView组件对象
    private TextView textView_studentID, textView_studentName;// 获取学生ID、学生姓名组件
    private DBHelper dbHelper;// DBHelper对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_studentgrade);
        GetIntentData();// 获取教师ID、班级ID、课程名称
        GetStudentsData();// 根据课程名称、学生ID查询学生成绩
        InitTextView();// 更新学生ID、学生姓名
        listView = findViewById(R.id.activity_show_studentgrade_listview_showGrade);// 初始化ListView
        adapter_studentGrade = new Adapter_StudentGrade(Activity_ShowStudentGrade.this, listItems);// 初始化适配器
        listView.setAdapter(adapter_studentGrade);// 绑定适配器
        adapter_studentGrade.notifyDataSetChanged();// 更新ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Activity_ShowStudentGrade.this, Activity_Manager_Grade_rollCall.class);
                intent.putExtra("update", "update");
                intent.putExtra("成绩类型", listItems.get(i).getTestName().substring(0, listItems.get(i).getTestName().indexOf("_")).replace("备注", ""));
                intent.putExtra("次数", listItems.get(i).getTestName().substring(listItems.get(i).getTestName().indexOf("_")).replace("_", ""));
                intent.putExtra("课程ID", Integer.toString(dbHelper.getCourseIdByName(courseName)));
                intent.putExtra("班级ID", classID);
                intent.putExtra("学生ID", studentID);
                intent.putExtra("学生姓名", studentName);
                startActivity(intent);
            }
        });
    }

    private void InitTextView() {
        textView_studentID = findViewById(R.id.activity_show_studentgrade_textview_studentID);
        textView_studentName = findViewById(R.id.activity_show_studentgrade_textview_studentName);
        textView_studentID.setText(studentID);
        textView_studentName.setText(studentName);
    }

    private void GetStudentsData() {
//        String[] TestName = {"上机1", "上机2"};
//        double[] grade = {5.5, 4.5};
//        for (int i = 0; i < 2; i++) {
//            listItems.add(new ListRow_TestNameAndGrade(TestName[i], grade[i]));
//        }
        dbHelper = new DBHelper(this, "TeacherHepler.db", null, 1);
        // 获取点名次数、上机次数、平时作业次数
        int[] counts = dbHelper.getCourseStatistics(dbHelper.getCourseIdByName(courseName), Integer.parseInt(classID), Integer.parseInt(teacherID));
        int rollCall_count = counts[0];// 点名次数
        int normalHomework_count = counts[1];// 作业次数
        int operate_count = counts[2];// 上机次数
        // 获取课程ID
        int courseID = dbHelper.findCourseIdByName(courseName);
        // 根据学生ID和课程ID获取学生成绩ID
        int id = dbHelper.getScoreRecordID(courseID, Integer.parseInt(teacherID), Integer.parseInt(studentID));
        // 根据学生成绩ID获取学生成绩
        ArrayList<ContentValues> courseScore = dbHelper.getCourseScoreById(id);
        ContentValues contentValues = courseScore.get(0);
//        List<String> homeworkR = new ArrayList<>(), homeworkS = new ArrayList<>(),
//                kqR = new ArrayList<>(), kqS = new ArrayList<>(),
//                sjR = new ArrayList<>(), sjS = new ArrayList<>();
//        String FSCORE = null;
        for (int i = 0; i < rollCall_count; i++) {
//            Log.d("StudentGrade", studentName + contentValues.getAsString("kqS_" + Integer.toString(i + 1)));
            listItems.add(new ListRow_TestNameAndGrade_Show("点名_" + Integer.toString(i+1), contentValues.getAsString("kqS_" + Integer.toString(i + 1))));
            listItems.add(new ListRow_TestNameAndGrade_Show("点名备注_" + Integer.toString(i+1), contentValues.getAsString("kqR_" + Integer.toString(i + 1))));
//            kqR.add(contentValues.getAsString("kqR_" + Integer.toString(i)));
//            kqS.add(contentValues.getAsString("kqS_" + Integer.toString(i)));
        }
        for (int i = 0; i < normalHomework_count; i++) {
            listItems.add(new ListRow_TestNameAndGrade_Show("平时作业_" + Integer.toString(i+1), contentValues.getAsString("homeworkS_" + Integer.toString(i + 1))));
            listItems.add(new ListRow_TestNameAndGrade_Show("平时作业备注_" + Integer.toString(i+1), contentValues.getAsString("homeworkR_" + Integer.toString(i + 1))));
//            homeworkR.add(contentValues.getAsString("homeworkR_" + Integer.toString(i)));
//            homeworkS.add(contentValues.getAsString("homeworkS_" + Integer.toString(i)));
        }
        for (int i = 0; i < operate_count; i++) {
            listItems.add(new ListRow_TestNameAndGrade_Show("上机_" + Integer.toString(i+1), contentValues.getAsString("sjS_" + Integer.toString(i + 1))));
            listItems.add(new ListRow_TestNameAndGrade_Show("上机备注_" + Integer.toString(i+1), contentValues.getAsString("sjR_" + Integer.toString(i + 1))));
//            sjR.add(contentValues.getAsString("sjR_" + Integer.toString(i)));
//            sjS.add(contentValues.getAsString("sjS_" + Integer.toString(i)));
        }
//        FSCORE = contentValues.getAsString("FSCORE");
        listItems.add(new ListRow_TestNameAndGrade_Show("总分", contentValues.getAsString("FSCORE")));
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.getStringExtra("教师ID") != null) {
            teacherID = intent.getStringExtra("教师ID");
            classID = intent.getStringExtra("班级ID");
            courseName = intent.getStringExtra("课程名");
            studentID = intent.getStringExtra("学生ID");
            studentName = intent.getStringExtra("学生姓名");
        }
    }

}
