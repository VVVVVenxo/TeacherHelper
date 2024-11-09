package com.example.teacherhelper;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Send_Student {
    public static void main(String path) throws Exception {
//        exportStudent();
        importStudent(path);
    }

    static void exportStudent() throws Exception {
        long t1 = System.currentTimeMillis();
        List<StudentExcelBen> stu = new ArrayList<>();
        for (int i = 1; i <=3; i++) {
            StudentExcelBen s = new StudentExcelBen();
            s.setStuId("6"+i);
            s.setStuName("小明" + i);
            s.setStuClass("2102");
            stu.add(s);
        }
        ExcelManager excelManager = new ExcelManager();
        @SuppressLint("SdCardPath") String path = "/data/user/0/com.example.teacherhelper/files/studentExport.xls";
//        File file = new File(path);
//        OutputStream excelStream = new FileOutputStream(path,true);
        boolean success = excelManager.toExcel(path, stu);
        long t2 = System.currentTimeMillis();

        double time = (t2 - t1) / 1000.0D;
        if (success) {
            System.out.print("导出成功：\n用时:" + time + "秒");
        } else {
            System.err.print("导出失败");
        }
    }

    static List<StudentExcelBen> importStudent(String path) throws Exception {
        long t1 = System.currentTimeMillis();
//        @SuppressLint("SdCardPath") String path = "/data/user/0/com.example.teacherhelper/files/studentExport.xls";
        InputStream excelStream = new FileInputStream(path);
        ExcelManager excelManager = new ExcelManager();
        List<StudentExcelBen> stu = excelManager.fromExcel(excelStream, StudentExcelBen.class);
        long t2 = System.currentTimeMillis();
        double time = (t2 - t1) / 1000.0D;
        System.out.print("读到Student个数:" + stu.size() + "\n用时:" + time + "秒");
        return stu;
    }

}
