package com.example.teacherhelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Manager_Grade extends AppCompatActivity {
    private Adapter_Course adapterCourse;// 获取适配器
    private List<ListRow_CourseAndClassID> listItems = new ArrayList<>();// 创建list集合
    private ListView listView;// listview组件对象
    private String teacherID = "123";// 教师ID
    private DBHelper dbHelper;// DBHelper对象
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manager_grade);
        dbHelper = new DBHelper(this, DATABASE_NAME, null, version);
        // GetIntentData();// 获取教师ID
        GetTeacherIDFormFile();
        GetListData();// 获取要在ListView中显示的数据
        listView = findViewById(R.id.activity_manager_grade_listview_show);
        adapterCourse = new Adapter_Course(this, listItems);
        listView.setAdapter(adapterCourse);
        adapterCourse.notifyDataSetChanged();
        ListviewClickListener();// 添加ListView点击事件
    }
    private void GetTeacherIDFormFile() {// 从文件获取教师ID
        SharedPreferences sp = getSharedPreferences("teaInfo_Save", MODE_PRIVATE);
        teacherID = Integer.toString(sp.getInt("teacherId", -1));
    }
    private void ListviewClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // minNumber 和 maxNumber
            int minNumber = 0; // 示例最小值
            int maxNumber = 100; // 示例最大值
            // 点名次数、上机次数、作业次数
            int rollCall_count = 0, normalHomework_count = 0, operate_count = 0;
            // 课程名，班级ID、课程ID
            String courseName = null, classID = null, courseID;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取成绩类型、成绩次数
                showGradeTypeDialog(new GradeTypeSelectedListener() {
                    @Override
                    public void onGradeTypeSelected(String type) {
                        // 获取班级ID和课程名
                        GetClassIDAndCourseNameAndCourseID();
                        // 获取总的点名、上机、作业次数
                        GetCountData();
                        if (type.equals("点名")){
                            maxNumber = rollCall_count;
                        } else if (type.equals("平时作业")) {
                            maxNumber = normalHomework_count;
                        } else if (type.equals("上机")) {
                            maxNumber = operate_count;
                        }
                        // 获取已有的上机、点名、作业次数
                        GetCountExited(type);
                        // 显示数字选择对话框
                        showNumberPickerDialog(0, maxNumber, new NumberSelectedListener() {
                            @Override
                            public void onNumberSelected(int number) {
                                Intent intent = null;
                                intent = new Intent(Activity_Manager_Grade.this, Activity_Manager_Grade_rollCall.class);
                                if (intent != null) {
                                    // 可以将需要的数据放入intent中
                                    intent.putExtra("成绩类型", type);
                                    intent.putExtra("次数", Integer.toString(number));
                                    intent.putExtra("课程ID", courseID);
                                    intent.putExtra("班级ID", classID);
                                    intent.putExtra("教师ID", teacherID);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    private void GetCountExited(String type) {
                        // minNumber =  dbHelper.findScoreValue(Integer.parseInt(courseID), Integer.parseInt(classID), type) - 1;
                        int[] number = dbHelper.getCourseStatistics(Integer.parseInt(courseID), Integer.parseInt(classID), Integer.parseInt(teacherID));
                        if (type.equals("点名")){
                            minNumber = number[0] - 1;
                        } else if (type.equals("上机")) {
                            minNumber = number[1] - 1;
                        } else if (type.equals("平时作业")) {
                            minNumber = number[2] - 1;
                        }
                    }
                    // public int getCourseScoreIdByStudentAndCourseId(int studentId, int courseId) {

                    private void GetCountData() {// 获取各个部分的次数
                        //        rollCall_count = 5;// 点名次数
                        //        normalHomework_count = 5;// 作业次数
                        //        operate_count = 5;// 上机次数
                        int[] counts = dbHelper.getCourseStatistics(Integer.parseInt(courseID), Integer.parseInt(classID), Integer.parseInt(teacherID));
                        rollCall_count = counts[0];// 点名次数
                        normalHomework_count = counts[1];// 作业次数
                        operate_count = counts[2];// 上机次数
                    }

                    private void GetClassIDAndCourseNameAndCourseID() {
                        classID = listItems.get(position).getClassID();
                        courseName = listItems.get(position).getCourse();
                        courseID = Integer.toString(dbHelper.getCourseIdByName(courseName));
                    }

                });
            }
        });
    }

    private void showNumberPickerDialog(final int minNumber, final int maxNumber, final NumberSelectedListener numberSelectedListener) {// 选择是第几次成绩
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(minNumber + 1); // 设置最小值为 minNumber + 1，因为不包含 minNumber
        numberPicker.setMaxValue(maxNumber); // 设置最大值为 maxNumber

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择一个数字");
        builder.setView(numberPicker);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedNumber = numberPicker.getValue(); // 获取选择的数字
                numberSelectedListener.onNumberSelected(selectedNumber); // 通过回调接口传递选中的数字
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showGradeTypeDialog(final GradeTypeSelectedListener listener) {// 获取成绩类型
        final String[] items = {"点名", "平时作业", "上机"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Manager_Grade.this);
        builder.setTitle("请选择一个选项");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onGradeTypeSelected(items[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void GetListData() {// 从数据库获取课程数据
        // String course[] = {"操作系统", "线性代数"};
        // String classID[] = {"2101", "2102"};
        // for (int i = 0; i < course.length; i++) {
        //     listItems.add(new ListRow_CourseAndClassID(course[i], classID[i]));
        // }
        HashMap<String, Integer> courseLists = dbHelper.getCourseClassRelationsByTeacherId(Integer.parseInt(teacherID));
        for (Map.Entry<String, Integer> entry : courseLists.entrySet()) {
            String courseName = entry.getKey();
            int classId = entry.getValue();
            listItems.add(new ListRow_CourseAndClassID(courseName, Integer.toString(classId)));
        }
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        // 如果intent中存在数据，就获取数据
        if (intent.getStringExtra("教师ID") != null) {
            teacherID = intent.getStringExtra("教师ID");
        }
    }
}
