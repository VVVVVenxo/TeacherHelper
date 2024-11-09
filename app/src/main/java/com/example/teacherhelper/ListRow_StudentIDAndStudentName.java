package com.example.teacherhelper;

public class ListRow_StudentIDAndStudentName {
    private String studentID, studentName;// 学生ID,学生姓名

    public String getStudentName() {
        return studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public ListRow_StudentIDAndStudentName(String studentID, String studentName){
        this.studentID = studentID;
        this.studentName = studentName;
    }
}
