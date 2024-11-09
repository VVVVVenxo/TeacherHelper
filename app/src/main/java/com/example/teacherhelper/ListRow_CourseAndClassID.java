package com.example.teacherhelper;

public class ListRow_CourseAndClassID {// 包含课程名称和班级ID的自定义list类型
    private String course, classID;  // 课程名称, 班级ID
    public ListRow_CourseAndClassID(String course, String classID){
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
