package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Activity_BackUp extends AppCompatActivity {
    private Button btSend;
    private Context context = this;
    int teaId = 0;
    private DBHelper dbHelper;
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本
    private  List<ListRow_TestNameAndGrade_Show> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);
        dbHelper = new DBHelper(this, DATABASE_NAME, null, version);
        SharedPreferences sp = getSharedPreferences("teaInfo_Save", MODE_PRIVATE);
        String teaMail = sp.getString("teacherEmail","无");
        TextView tvTeamail = findViewById(R.id.activity_back_up_mail);
        tvTeamail.setText(teaMail);

        btSend = findViewById(R.id.activity_backup_button);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teaMail = sp.getString("teacherEmail","");
                teaId = sp.getInt("teacherId",0);
                if(teaMail.equals("")){
                    Toast.makeText(Activity_BackUp.this,"请设置邮箱！",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Toast.makeText(Activity_BackUp.this, "创建成功", Toast.LENGTH_SHORT).show();
                        creat_Excel(teaId);
                    } catch (Exception e) {
                        Toast.makeText(Activity_BackUp.this, "创建失败", Toast.LENGTH_SHORT).show();
                        System.out.println("导出失败");
                        e.printStackTrace();
                    }
//                    System.out.println("send");
                    sendEMail(teaMail);
                }
            }
        });
    }

//    public void sendTextMail() {
//
//        SendMailUtil.send("x1437367259@163.com");
//        Toast.makeText(Activity_BackUp.this, "邮件已发送", Toast.LENGTH_SHORT).show();
//    }
//
//    public void sendFileMail() {
//
//        File file = new File(context.getDataDir().getPath() + File.separator + "databases/TeacherHepler.db");
//        SendMailUtil.send(file, "x1437367259@163.com");
//        Toast.makeText(Activity_BackUp.this, "邮件已发送", Toast.LENGTH_SHORT).show();
//    }

    private void creatExcel(Integer teaId) throws Exception {
        long t1 = System.currentTimeMillis();
        //获取当前教师的课程信息
        HashMap<String, Integer> courseLists = dbHelper.getCourseClassRelationsByTeacherId(teaId);
        List<StudentExcelBen> stu = new ArrayList<>();// 创建list集合
        for (Map.Entry<String, Integer> entry : courseLists.entrySet()) {
            int classId = entry.getValue();
            //获取当前课程的学生信息
            List<ListRow_StudentIDAndStudentName> listItems = dbHelper.getStudentsByClassId(classId);
            for (ListRow_StudentIDAndStudentName item : listItems) {
                stu.add(new StudentExcelBen(item.getStudentID(), item.getStudentName(),Integer.toString(classId)));
            }
        }
        ExcelManager excelManager = new ExcelManager();
        @SuppressLint("SdCardPath") String path = "/data/user/0/com.example.teacherhelper/files/studentExport.xls";
        boolean success = excelManager.toExcel(path, stu);//导出数据
        long t2 = System.currentTimeMillis();
        double time = (t2 - t1) / 1000.0D;
        if (success) {
            System.out.print("导出成功：\n用时:" + time + "秒");
        } else {
            System.err.print("导出失败");
        }
    }


    private void creat_Excel(Integer teaId) throws Exception {

        //创建excel
        @SuppressLint("SdCardPath") String path = "/data/user/0/com.example.teacherhelper/files/studentExport.xls";
        File file = new File(path);
        WritableWorkbook workbook = Workbook.createWorkbook(file);

        //获取当前教师的课程信息
        HashMap<String, Integer> courseLists = dbHelper.getCourseClassRelationsByTeacherId(teaId);
        List<StudentExcelBen> stu = new ArrayList<>();// 创建list集合

        for (Map.Entry<String, Integer> entry : courseLists.entrySet()) {
            int classId = entry.getValue();
            String courseName = entry.getKey();
            int courseID = dbHelper.findCourseIdByName(courseName);
            //创建班级sheet
            WritableSheet sheet =workbook.createSheet(courseName , 0);
            //第一行设置列名:创建数组
            String[] title={"班级","姓名"};
            Label label =null ;
            //设置列名
            for (int i = 0; i < title.length; i++) {
                label = new Label(i, 0, title[i]);
                sheet.addCell(label);
            }
            // 获取点名次数、上机次数、平时作业次数
            int[] counts = dbHelper.getCourseStatistics(courseID, classId, teaId);
            int rollCall_count = counts[0];// 点名次数
            int normalHomework_count = counts[1];// 作业次数
            int operate_count = counts[2];// 上机次数

            //获取当前课程的学生信息
            int position =1;//记录位置
            List<Integer> studentIDs = dbHelper.getStudentIDs(classId, teaId, courseID);
            for (Integer id : studentIDs) {
                HashMap<Integer, String> studentInformation = dbHelper.getStudentIdAndNameById(id);
                for (Map.Entry<Integer, String> student : studentInformation.entrySet()) {
                    Integer studentID = student.getKey();
                    String studentName = student.getValue();
                    // 根据学生ID和课程ID获取学生成绩ID
                    int scoreid = dbHelper.getScoreRecordID(courseID, teaId, studentID);
//                System.out.println(id);
                    // 根据学生成绩ID获取学生成绩
                    ArrayList<ContentValues> courseScore = dbHelper.getCourseScoreById(scoreid);
                    ContentValues contentValues = courseScore.get(0);

                    //添加基本属性
                    label= new Label(0,position,Integer.toString(classId));
                    sheet.addCell(label);
                    label=new Label(1,position,studentName);
                    sheet.addCell(label);

                    for (int i = 0; i < rollCall_count; i++) {
                        label = new Label(i+2, 0, "考勤_" + (i+1));
                        sheet.addCell(label);
                        label = new Label(i+2, position, contentValues.getAsString("kqS_" + Integer.toString(i + 1)));
                        sheet.addCell(label);
                    }
                    for (int i = 0; i < normalHomework_count; i++) {
                        label = new Label(i+2+rollCall_count, 0, "平时作业_" + (i+1));
                        sheet.addCell(label);
                        label = new Label(i+2+rollCall_count, position, contentValues.getAsString("homeworkS_" + Integer.toString(i + 1)));
                        sheet.addCell(label);
                    }
                    for (int i = 0; i < operate_count; i++) {
                        label = new Label(i+2+rollCall_count+normalHomework_count, 0, "上机_" + (i+1));
                        sheet.addCell(label);
                        label = new Label(i+2+rollCall_count+normalHomework_count, position, contentValues.getAsString("sjS_" + Integer.toString(i + 1)));
                        sheet.addCell(label);
                    }

                    label = new Label(2+rollCall_count+normalHomework_count+operate_count, 0, "总分");
                    sheet.addCell(label);
                    label = new Label(2+rollCall_count+normalHomework_count+operate_count, position, contentValues.getAsString("FSCORE"));
                    sheet.addCell(label);
                    position= position+1;
                }
            }

//            List<ListRow_StudentIDAndStudentName> listItems = dbHelper.getStudentsByClassId(courseID);
//            for (ListRow_StudentIDAndStudentName item : listItems) {
//                stu.add(new StudentExcelBen(item.getStudentID(), item.getStudentName(),Integer.toString(classId)));
////                System.out.println(item.getStudentName());
//
//            }
        }
        workbook.write();
        workbook.close();
    }

    private void sendEMail(String teaMail) {
//        String path = context.getDataDir().getPath() + File.separator + "databases/TeacherHepler.db";
        String path = context.getFilesDir().getPath() + File.separator+"studentExport.xls";
        Mail_Manager.getInstance().sendMailWithFile("数据备份", "I't a back up mail!", path);
        Mail_Manager.RECEIVER_NAME =teaMail;
    }
}