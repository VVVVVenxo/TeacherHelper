package com.example.teacherhelper;

public class ListRow_TestNameAndGrade {
    private String testName;// 成绩内容
    private Double grade;// 成绩

    public ListRow_TestNameAndGrade(String testName, Double grade){
        this.testName = testName;
        this.grade = grade;
    }

    public String getTestName() {
        return testName;
    }

    public Double getGrade() {
        return grade;
    }
}
