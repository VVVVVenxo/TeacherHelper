package com.example.teacherhelper;

public class Row_CourseAndClassID {
    private String course;  // 课程名称
    private String classID; // 班级ID
    public Row_CourseAndClassID(String course, String classID){
        this.course = course;
        this.classID = classID;
    }
    public String getCourse(){
        return course;
    }
    public String getClassID(){
        return classID;
    }
}
