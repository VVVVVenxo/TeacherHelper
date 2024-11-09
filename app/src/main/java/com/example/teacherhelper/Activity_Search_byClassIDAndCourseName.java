package com.example.teacherhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Search_byClassIDAndCourseName extends AppCompatActivity {// 实现通过班级ID和课程名称、教师ID查找整个班级的学生信息
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private String classID, courseName, teacherID;// 班级ID,课程名称,教师ID
    private List<ListRow_StudentIDAndStudentName> listItems = new ArrayList<>();// 所有学生数据
    private Adapter_Student adapter_student;// 创建适配器对象
    private ListView listView;// 获取ListView组件对象
    private DBHelper dbHelper;// DBHelper对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_byclassidandcoursename);
        dbHelper = new DBHelper(this, DATABASE_NAME, null, 1);// 初始化DBHelper组件

        GetIntentData();// 获取教师ID、班级ID、课程名称
        GetStudentsData();// 根据教师ID、班级ID、课程名称查询所有学生数据

        adapter_student = new Adapter_Student(Activity_Search_byClassIDAndCourseName.this, listItems);// 初始化适配器
        listView = findViewById(R.id.activity_search_byclassidandcoursename_listview_showAllStudents);// 初始化listview
        listView.setAdapter(adapter_student);// 绑定适配器
        adapter_student.notifyDataSetChanged();// 更新ListView

        ButtonListener();// 按钮监听器
        ListviewClickListener();// 添加ListView点击事件
    }

    private void ListviewClickListener() {// 点击学生
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击位置的数据
                ListRow_StudentIDAndStudentName currentItem = listItems.get(position);
                // 调用函数，计算总分
                int courseID = dbHelper.getCourseIdByName(courseName);
                int scoreID = dbHelper.getScoreRecordID(courseID, Integer.parseInt(teacherID), Integer.parseInt(currentItem.getStudentID()));
                dbHelper.calculateFinalScore(scoreID);
                // 将数据传递到以班级为单位查询学生数据
                Intent intent = new Intent(Activity_Search_byClassIDAndCourseName.this, Activity_ShowStudentGrade.class);
                intent.putExtra("教师ID", teacherID);
                intent.putExtra("班级ID", classID);
                intent.putExtra("课程名", courseName);
                intent.putExtra("学生ID", currentItem.getStudentID());
                intent.putExtra("学生姓名", currentItem.getStudentName());
                startActivity(intent);
            }
        });
    }

    private void ButtonListener() {// 按钮监听器
        // 点击查找按钮
        Button button_search = findViewById(R.id.activity_searchStudent_byclassidandcoursename_button_submit);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取editText的数据
                EditText editText = findViewById(R.id.activity_search_byclassidandcoursename_editText_search);
                String search = editText.getText().toString();
                GetStudentsDataBySearch(search);// 根据班级ID和输入的内容查找学生
                adapter_student = new Adapter_Student(Activity_Search_byClassIDAndCourseName.this, listItems);// 初始化适配器
                listView.setAdapter(adapter_student);// 绑定适配器
                adapter_student.notifyDataSetChanged();// 更新ListView
            }

            private void GetStudentsDataBySearch(String search) {// 根据班级ID和输入的内容查找学生
                listItems.clear();
                // 查找学生ID
                HashMap<Integer, String> studentLists = null;
                ArrayList<Integer> studentID = dbHelper.getStudentIdsByClassIdAndKeyword(Integer.parseInt(classID), search);
                // 根据查找到的学生ID获取学生姓名
                for (int i = 0; i < studentID.size(); i++) {
                    int studentid = studentID.get(i);
                    studentLists = dbHelper.getStudentIdAndNameById(studentid);
                }
                for (Map.Entry<Integer, String> entry : studentLists.entrySet()) {
                    int id = entry.getKey();
                    String name = entry.getValue();
                    listItems.add(new ListRow_StudentIDAndStudentName(Integer.toString(id), name));
                }
            }
        });
    }

    private void GetStudentsData() {// 根据教师ID、班级ID、课程名称查询所有学生数据
//        String[] studentIDs = {"32101", "32102"}, studentNames = {"32101", "32102"};
//        for (int i = 0; i < 2; i ++){
//            listItems.add(new ListRow_StudentIDAndStudentName(studentIDs[i], studentNames[i]));
//        }
        /// listItems = dbHelper.getStudentsByClassId(Integer.parseInt(classID));
        List<Integer> studentIDs = dbHelper.getStudentIDs(Integer.parseInt(classID), Integer.parseInt(teacherID), dbHelper.getCourseIdByName(courseName));
        for (Integer id : studentIDs) {
            HashMap<Integer, String> studentInformation = dbHelper.getStudentIdAndNameById(id);
            for (Map.Entry<Integer, String> entry : studentInformation.entrySet()) {
                Integer studentID = entry.getKey();
                String studentName = entry.getValue();
                listItems.add(new ListRow_StudentIDAndStudentName(Integer.toString(studentID), studentName));
            }
        }
    }

    private void GetIntentData() {// 获取教师ID、班级ID、课程名称
        Intent intent = getIntent();
        if (intent.getStringExtra("班级ID") != null) {
            classID = intent.getStringExtra("班级ID");
            teacherID = intent.getStringExtra("教师ID");
            courseName = intent.getStringExtra("课程名");
        }
    }
}
