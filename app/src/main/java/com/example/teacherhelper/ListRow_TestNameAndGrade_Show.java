package com.example.teacherhelper;

public class ListRow_TestNameAndGrade_Show {

    private String testName;// 成绩内容
    private String content;// 成绩

    public ListRow_TestNameAndGrade_Show(String testName, String content){
        this.testName = testName;
        this.content = content;
    }

    public String getTestName() {
        return testName;
    }

    public String getContent() {
        return content;
    }
}
