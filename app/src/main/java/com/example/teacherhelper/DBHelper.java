package com.example.teacherhelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {// 数据库操作
    private static final String DATABASE_NAME = "TeacherHepler.db";
    private static final int DATABASE_VERSION = 1;

    //老师信息，teacherdetail表
    private static final String TABLE_TEACHER_NAME="teacherdetail";
    private static final String COLUMN_TEACHER_ID="TID";//老师工号、ID
    private static final String COLUMN_TEACHER_NAME="TNAME";
    private static final String COLUMN_TEACHER_EMAIL="EMAIL";
    private static final String COLUMN_TEACHER_EMAIL_PASS="EPASS";//邮箱密码
    private static final String COLUMN_USER_PASS="PASS";
    private static final String COLUMN_USER_GRAVITY="gravity";//权重0为管理员，1为老师
    //选课表，choosecourse表
    private static final String TABLE_CHOOSE_NAME="choosecourse";
    private static final String COLUMN_CNT_ID="ID";//自增id
    //    private static final String COLUMN_COURSE_ID="IDC";//课程id
//    private static final String COLUMN_TEACHER_ID="TID";//老师工号、ID
//    private static final String COLUMN_CLASS_ID="CID";//班级id
//    private static final String COLUMN_SCORE_ID="SCID";//成绩记录id
    private static final String COLUMN_SCORE_PER_KQ="Pkq";//点名成绩占比
    private static final String COLUMN_SCORE_PER_HOMEWORK="Phomework";//作业成绩占比
    private static final String COLUMN_SCORE_PER_SJ="Psj";//上机成绩占比
    private static final String COLUMN_SCORE_CNT_KQ="Ckq";//点名次数
    private static final String COLUMN_SCORE_CNT_HOMEWORK="Chomework";//作业次数
    private static final String COLUMN_SCORE_CNT_SJ="Csj";//上机次数

    //学生信息表，studnetinfo表
    private static final String TABLE_STUDENTINFO_NAME="studnetinfo";
    private static final String COLUMN_STUDENT_ID="SID";
    private static final String COLUMN_STUDENT_NAME="SNAME";//学生名字
//    private static final String  COLUMN_CLASS_ID="CID";//班级id

    //班级信息表，classinfo表
    private static final String TABLE_CLASSINFO_NAME="classinfo";
    private static final String COLUMN_CLASS_ID="CID";//班级id
    private static final String COLUMN_CLASS_ACADEMY="academy";//学院
    private static final String COLUMN_CLASS_MAJOR="major";//专业

    //课程信息表，courseinfo表
    private static final String TABLE_COURSEINFO_NAME="courseinfo";
    private static final String COLUMN_COURSE_ID="IDC";//课程id
    private static final String COLUMN_COURSE_NAME="CNAME";//课程名字

    //学生成绩表，coursescore表
    private static final String TABLE_COURSESCORE_NAME="coursescore";
    private static final String COLUMN_SCORE_ID="SCID";//成绩记录id
    //    private static final String COLUMN_STUDENT_ID="SID";
    private static final String COLUMN_KQ_SCORE="kqS";//点名成绩
    private static final String COLUMN_SJ_SCORE="sjS";//上机成绩
    private static final String COLUMN_HOMEWORK_SCORE="homeworkS";//作业成绩
    private static final String COLUMN_KQ_REMARK="kqR";//点名备注
    private static final String COLUMN_SJ_REMARK="sjR";//上机备注
    private static final String COLUMN_HOMEWORK_REMARK="homeworkR";//作业备注
    private static final String COLUMN_FINAL_SCORE="FSCORE";//最终成绩

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        System.out.println(1);
        // 创建老师信息，teacherdetail表
        String teacherdetail = "CREATE TABLE " + TABLE_TEACHER_NAME + "(" +
                COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_TEACHER_NAME + " TEXT," +
                COLUMN_TEACHER_EMAIL + " TEXT," +
                COLUMN_TEACHER_EMAIL_PASS + " TEXT," +
                COLUMN_USER_PASS +" TEXT," + // 使用 PASSWORD 函数
                COLUMN_USER_GRAVITY + " TEXT)";

        // 创建选课表，choosecourse表
        String choosecourse = "CREATE TABLE " + TABLE_CHOOSE_NAME + "(" +
                COLUMN_CNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_COURSE_ID + " INTEGER," +
                COLUMN_TEACHER_ID + " INTEGER," +
                COLUMN_CLASS_ID + " INTEGER," +
                COLUMN_SCORE_ID + " INTEGER," +
                COLUMN_SCORE_PER_KQ + " INTEGER," +
                COLUMN_SCORE_PER_HOMEWORK + " INTEGER," +
                COLUMN_SCORE_PER_SJ + " INTEGER," +
                COLUMN_SCORE_CNT_KQ + " INTEGER," +
                COLUMN_SCORE_CNT_HOMEWORK + " INTEGER," +
                COLUMN_SCORE_CNT_SJ + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_TEACHER_NAME + "(" + COLUMN_TEACHER_ID + ")," +
                "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSEINFO_NAME + "(" + COLUMN_COURSE_ID + ")," +
                "FOREIGN KEY(" + COLUMN_SCORE_ID + ") REFERENCES " + TABLE_COURSESCORE_NAME + "(" + COLUMN_SCORE_ID + ")," +
                "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASSINFO_NAME + "(" + COLUMN_CLASS_ID + "))"
                ;

        // 创建学生信息表，studentinfo表
        String studentinfo = "CREATE TABLE " + TABLE_STUDENTINFO_NAME + "(" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_STUDENT_NAME + " TEXT," +
                COLUMN_CLASS_ID + " INTEGER," +
                "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASSINFO_NAME + "(" + COLUMN_CLASS_ID + "))";

        // 创建班级信息表，classinfo表
        String classinfo = "CREATE TABLE " + TABLE_CLASSINFO_NAME + "(" +
                COLUMN_CLASS_ID + " INTEGER PRIMARY KEY," +
                COLUMN_CLASS_ACADEMY + " TEXT," +
                COLUMN_CLASS_MAJOR + " TEXT)";

        // 创建课程信息表，courseinfo表
        String courseinfo = "CREATE TABLE " + TABLE_COURSEINFO_NAME + "(" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_COURSE_NAME + " TEXT)";
        sqLiteDatabase.execSQL(teacherdetail);
        sqLiteDatabase.execSQL(choosecourse);
        sqLiteDatabase.execSQL(studentinfo);
        sqLiteDatabase.execSQL(classinfo);
        sqLiteDatabase.execSQL(courseinfo);
    }
    //检查课程是否冲突
    public boolean checkChooseCourseData(int teacherId, int courseId, int classId) {
        SQLiteDatabase db = getReadableDatabase();

        // 查询choosecourse表检查是否存在符合条件的数据
        String query = "SELECT * FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_TEACHER_ID + " = " + teacherId +
                " AND " + COLUMN_COURSE_ID + " = " + courseId +
                " AND " + COLUMN_CLASS_ID + " = " + classId;

        Cursor cursor = db.rawQuery(query, null);

        boolean dataExists = cursor != null && cursor.getCount() > 0;

        cursor.close();
        db.close();

        return dataExists;
    }

    //查找成绩id
    public int getScoreRecordID(int courseId, int teacherId, int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int scoreRecordId = -1; // 默认值为-1，表示未找到匹配的成绩记录ID

        // 查询choosecourse表以找到与课程ID和教师ID匹配的成绩记录ID
        String query = "SELECT " + COLUMN_SCORE_ID +
                " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_COURSE_ID + " = " + courseId +
                " AND " + COLUMN_TEACHER_ID + " = " + teacherId;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tempScoreRecordId = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_ID));

            // 根据学生ID进一步筛选
            query = "SELECT " + COLUMN_SCORE_ID +
                    " FROM " + TABLE_COURSESCORE_NAME +
                    " WHERE " + COLUMN_SCORE_ID + " = " + tempScoreRecordId +
                    " AND " + COLUMN_STUDENT_ID + " = " + studentId;

            Cursor tempCursor = db.rawQuery(query, null);
            if (tempCursor.moveToFirst()) {
                // 找到符合条件的成绩记录ID
                scoreRecordId = tempScoreRecordId;
                break;
            }
            tempCursor.close();
        }
        cursor.close();

        return scoreRecordId;
    }
    //查找对应班级选择该课程的学生
    public List<Integer> getStudentIDs(int classID, int teacherID, int courseID) {
        List<Integer> studentIDs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve the score record IDs based on class ID, teacher ID, and course ID
        String query = "SELECT " + COLUMN_SCORE_ID + " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_CLASS_ID + " = " + classID +
                " AND " + COLUMN_TEACHER_ID + " = " + teacherID +
                " AND " + COLUMN_COURSE_ID + " = " + courseID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int scoreID = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_ID));

                // Query to fetch student IDs from coursescore table based on score record ID
                String studentQuery = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_COURSESCORE_NAME +
                        " WHERE " + COLUMN_SCORE_ID + " = " + scoreID;

                Cursor studentCursor = db.rawQuery(studentQuery, null);

                if (studentCursor.moveToFirst()) {
                    @SuppressLint("Range") int studentID = studentCursor.getInt(studentCursor.getColumnIndex(COLUMN_STUDENT_ID));
                    studentIDs.add(studentID);
                }

                studentCursor.close();
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return studentIDs;
    }
    //查询所有班级信息
    public List<Map<String,Object>> getAllClassInfo() {
//        List<String> classInfoList = new ArrayList<>();
        ArrayList<Map<String, Object>> classInfoList = new ArrayList<Map<String, Object>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLASSINFO_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID));
                @SuppressLint("Range") String academy = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_ACADEMY));
                @SuppressLint("Range") String major = cursor.getString(cursor.getColumnIndex(COLUMN_CLASS_MAJOR));
                Map<String, Object> map = new HashMap<>();
                map.put("classId",classId);
                map.put("academy",academy);
                map.put("major",major);
                // 构建查询结果的字符串，并添加到列表中
//                String classInfo = "Class ID: " + classId + ", Academy: " + academy + ", Major: " + major;
//                classInfoList.add(classInfo);
                classInfoList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return classInfoList;
    }
    //输入课程id，班级id，老师id，查询choosecourse表，用int[]返回点名次数，上机次数，作业次数
    public int[] getCourseStatistics(int courseId, int classId, int teacherId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_SCORE_CNT_KQ,
                COLUMN_SCORE_CNT_SJ,
                COLUMN_SCORE_CNT_HOMEWORK
        };

        String selection = COLUMN_COURSE_ID + " = ? AND " +
                COLUMN_CLASS_ID + " = ? AND " +
                COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId), String.valueOf(classId), String.valueOf(teacherId)};

        Cursor cursor = db.query(
                TABLE_CHOOSE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int[] courseStatistics = new int[3];
        if (cursor.moveToFirst()) {
            courseStatistics[0] = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_CNT_KQ));
            courseStatistics[1] = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_CNT_SJ));
            courseStatistics[2] = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_CNT_HOMEWORK));
        }

        cursor.close();
        db.close();

        return courseStatistics;
    }
    //插入一条班级信息
    public void insertClassInfo(int classId, String academy, String major) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, classId);
        values.put(COLUMN_CLASS_ACADEMY, academy);
        values.put(COLUMN_CLASS_MAJOR, major);

        db.insert(TABLE_CLASSINFO_NAME, null, values);

        db.close();
    }

    //修改点名次数，上机次数，作业次数
    public void updateScoreCounts(int teacherId, int courseId, int classId, int newKqCount, int newSjCount, int newHomeworkCount) {
        SQLiteDatabase db = getWritableDatabase();

        // 更新choosecourse表中符合条件的数据行的点名次数、上机次数和作业次数
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_CNT_KQ, newKqCount);
        values.put(COLUMN_SCORE_CNT_SJ, newSjCount);
        values.put(COLUMN_SCORE_CNT_HOMEWORK, newHomeworkCount);

        String whereClause = COLUMN_TEACHER_ID + " = " + teacherId +
                " AND " + COLUMN_COURSE_ID + " = " + courseId +
                " AND " + COLUMN_CLASS_ID + " = " + classId;
        db.update(TABLE_CHOOSE_NAME, values, whereClause, null);

        db.close();
    }

    //插入一条学生id，姓名，班级id，到studentinfo表
    public void insertStudentInfo(int studentId, String studentName, int classId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_CLASS_ID, classId);
        db.insert(TABLE_STUDENTINFO_NAME, null, values);
        db.close();
    }

    //根据教师id 修改密码
    public void updateTeacherPasswordById(int teacherId, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASS, newPassword);

        String selection = COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};

        db.update(TABLE_TEACHER_NAME, values, selection, selectionArgs);

        db.close();
    }

    //返回coursescore表的成绩记录id    !!!!!!!!!!!废弃
//    public int getCourseScoreId(int courseId, int classId, int studentId) {
//        SQLiteDatabase db = getReadableDatabase();
//        String[] projection = {COLUMN_SCORE_ID};
//        String selection = COLUMN_COURSE_ID + " = ? AND " +
//                COLUMN_CLASS_ID + " = ? AND " +
//                COLUMN_STUDENT_ID + " = ?";
//        String[] selectionArgs = {String.valueOf(courseId), String.valueOf(classId), String.valueOf(studentId)};
//        Cursor cursor = db.query(
//                TABLE_COURSESCORE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );
//
//        int courseScoreId = -1;
//        if (cursor.moveToFirst()) {
//            courseScoreId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE_ID));
//        }
//        cursor.close();
//        db.close();
//
//        return courseScoreId;
//    }

    //根据教师id 修改邮箱、邮箱密码
    public void updateTeacherEmailCredentialsById(int teacherId, String newEmail, String newEmailPassword) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_EMAIL, newEmail);
        values.put(COLUMN_TEACHER_EMAIL_PASS, newEmailPassword);

        String selection = COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};

        db.update(TABLE_TEACHER_NAME, values, selection, selectionArgs);

        db.close();
    }
    //根据教师id，返回教师姓名、邮箱、密码
    public String[] getTeacherDetailsById(int teacherId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_TEACHER_NAME,
                COLUMN_TEACHER_EMAIL,
                COLUMN_TEACHER_EMAIL_PASS,
                COLUMN_USER_PASS
        };

        String selection = COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(teacherId)};

        Cursor cursor = db.query(
                TABLE_TEACHER_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        String[] teacherDetails = null;
        if (cursor.moveToFirst()) {
            String teacherName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME));
            String teacherEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_EMAIL));
            String emailPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_EMAIL_PASS));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASS));

            teacherDetails = new String[]{teacherName, teacherEmail, emailPassword, password};
        }

        cursor.close();
        db.close();

        return teacherDetails;
    }
    //修改Pkq点名占比，Psj上机占比,Phomework作业占比
    public void updateChooseCourseScores(int courseId, int teacherId, int classId, int pkq, int psj, int phomework) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE_PER_KQ, pkq);
        values.put(COLUMN_SCORE_PER_SJ, psj);
        values.put(COLUMN_SCORE_PER_HOMEWORK, phomework);

        String whereClause = COLUMN_COURSE_ID + " = ? AND " +
                COLUMN_TEACHER_ID + " = ? AND " +
                COLUMN_CLASS_ID + " = ?";
        String[] whereArgs = {String.valueOf(courseId), String.valueOf(teacherId), String.valueOf(classId)};
        db.update(TABLE_CHOOSE_NAME, values, whereClause, whereArgs);

        db.close();
    }


    //插入一个点名/上机/作业数据（"点名"/"上机"/"平时作业"）
    @SuppressLint("Range")
    public void updateCourseScore(int courseId, int classId, int studentId, String dataType, int position, double value, String message) {
        SQLiteDatabase db = getWritableDatabase();

        System.out.println(studentId);
        System.out.println(value);

        // 查询choosecourse表获取成绩记录ID列表
        String chooseCourseQuery = "SELECT " + COLUMN_SCORE_ID +
                " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_COURSE_ID + " = " + courseId +
                " AND " + COLUMN_CLASS_ID + " = " + classId;

        Cursor chooseCourseCursor = db.rawQuery(chooseCourseQuery, null);
        List<Integer> scoreIds = new ArrayList<>();

        while (chooseCourseCursor.moveToNext()) {
            int scoreId = chooseCourseCursor.getInt(chooseCourseCursor.getColumnIndex(COLUMN_SCORE_ID));
            scoreIds.add(scoreId);
        }

        chooseCourseCursor.close();

        if (!scoreIds.isEmpty()) {
            int cnt = position;
            String columnName = "";
            String columnName1 = "";
            if (dataType.equals("点名")) {
                columnName = "kqS_";
                columnName1 = "kqR_";
            } else if (dataType.equals("上机")) {
                columnName = "sjS_";
                columnName1 = "sjR_";
            } else if (dataType.equals("平时作业")) {
                columnName = "homeworkS_";
                columnName1 = "homeworkR_";
            }
            if (!columnName.isEmpty()) {
                columnName = columnName + cnt; // 对应的数据位置列名
                columnName1 = columnName1 + cnt;

                // 针对每个成绩记录ID，在coursescore表中匹配学生ID找到具体数据行，并更新对应位置的数据值
                for (int scoreId : scoreIds) {
                    String updateQuery = "UPDATE " + TABLE_COURSESCORE_NAME +
                            " SET " + columnName + " = " + value +
                            " WHERE " + COLUMN_SCORE_ID + " = " + scoreId +
                            " AND " + COLUMN_STUDENT_ID + " = " + studentId;

                    String updateQuery1 = "UPDATE " + TABLE_COURSESCORE_NAME +
                            " SET " + columnName1 + " = '" + message + "'" +
                            " WHERE " + COLUMN_SCORE_ID + " = " + scoreId +
                            " AND " + COLUMN_STUDENT_ID + " = " + studentId;

                    db.execSQL(updateQuery);
                    db.execSQL(updateQuery1);
                }
            }
        }
        db.close();
    }

    //根据成绩表id获取Pkq、Psj和Phomework的值
    public int[] getScorePercentages(int scoreId) {
        SQLiteDatabase db = getReadableDatabase();
        int[] percentages = new int[3];

        String query = "SELECT " + COLUMN_SCORE_PER_KQ + ", " + COLUMN_SCORE_PER_SJ + ", " + COLUMN_SCORE_PER_HOMEWORK +
                " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            percentages[0] = cursor.getInt(0); // Pkq
            percentages[1] = cursor.getInt(1); // Psj
            percentages[2] = cursor.getInt(2); // Phomework
        }
        cursor.close();
        db.close();
        return percentages;
    }

    //获取本节课是第几次操作   将会输入课程id，班级id以及（"点名"/"上机"/"平时作业"）
    public int findScoreValue(int courseId, int classId, String type) {
        SQLiteDatabase db = getReadableDatabase();
        int scoreId = -1;

        String query = "SELECT " + COLUMN_SCORE_ID +
                " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_COURSE_ID + " = " + courseId +
                " AND " + COLUMN_CLASS_ID + " = " + classId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            scoreId = cursor.getInt(0);
        }
        cursor.close();

        if (scoreId != -1) {
            int[] counts = getAttendanceAndAssignmentCounts(scoreId);
            int kqCount = counts[0];
            int homeworkCount = counts[1];
            int sjCount = counts[2];

            int cnt = 0;
            String columnName = "";
            if (type.equals("点名")) {
                columnName = "kqS_";
                cnt = kqCount;
            } else if (type.equals("上机")) {
                columnName = "sjS_";
                cnt = sjCount;
            } else if (type.equals("平时作业")) {
                columnName = "homeworkS_";
                cnt = homeworkCount;
            }

            if (!columnName.isEmpty()) {
                for (int i = 1; i <= cnt; i++) {
                    String columnToCheck = columnName + i;
                    String queryCheck = "SELECT " + columnToCheck +
                            " FROM " + TABLE_COURSESCORE_NAME +
                            " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;

                    Cursor cursorCheck = db.rawQuery(queryCheck, null);
                    if (cursorCheck.moveToFirst()) {
                        int columnIndex = cursorCheck.getColumnIndex(columnToCheck);
                        int value = cursorCheck.getInt(columnIndex);
                        if (value == 0) {
                            return i;
                        }
                    }
                    cursorCheck.close();
                }
            }
        }

        db.close();

        return -1; // 返回-1表示未找到符合条件的数据行或数据行中没有为0的值
    }

    //函数更新计算最终成绩
    public void calculateFinalScore(int scoreId) {

        // 获取出勤和作业计数
        int[] counts = getAttendanceAndAssignmentCounts(scoreId);
        int kqCount = counts[0];
        int homeworkCount = counts[1];
        int sjCount = counts[2];

        //获取Pkq、Psj和Phomework的值
        int[] count2 = getScorePercentages(scoreId);
        int Pkq = counts[0];
        int Phomework = counts[1];
        int Psj = counts[2];

        // 计算每个类别的总分
        double kqTotal = 0;
        double homeworkTotal = 0;
        double sjTotal = 0;

        for (int i = 1; i <= kqCount; i++) {
            String kqColumn = "kqS_" + i;
            String query = "SELECT SUM(" + kqColumn + ") FROM " + TABLE_COURSESCORE_NAME +
                    " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                kqTotal += cursor.getDouble(0);
            }
            cursor.close();
        }

        for (int i = 1; i <=sjCount; i++) {
            String sjColumn = "sjS_" + i;
            String query = "SELECT SUM(" + sjColumn + ") FROM " + TABLE_COURSESCORE_NAME +
                    " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                sjTotal += cursor.getDouble(0);
            }
            cursor.close();
        }

        for (int i = 1; i <= homeworkCount; i++) {
            String homeworkColumn = "homeworkS_" + i;
            String query = "SELECT SUM(" + homeworkColumn + ") FROM " + TABLE_COURSESCORE_NAME +
                    " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                homeworkTotal += cursor.getDouble(0);
            }
            cursor.close();
        }

        // 计算最终得分
        double kqPercentage = (kqTotal / (10 * kqCount)) * Pkq;
        double homeworkPercentage = (homeworkTotal / (10 * homeworkCount)) * Phomework;
        double sjPercentage = (sjTotal / (10 * sjCount)) * Psj;

        double finalScore = kqPercentage + homeworkPercentage + sjPercentage;

        // 更新coursescore表中的最终得分
        ContentValues values = new ContentValues();
        values.put(COLUMN_FINAL_SCORE, finalScore);

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_COURSESCORE_NAME, values, COLUMN_SCORE_ID + " = ?", new String[]{String.valueOf(scoreId)});
        db.close();
    }

    //根据课程名查找课程ID
    @SuppressLint("Range")
    public int findCourseIdByName(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int courseId = -1; // 默认值为-1，表示未找到对应的课程ID

        String query = "SELECT " + COLUMN_COURSE_ID + " FROM " + TABLE_COURSEINFO_NAME +
                " WHERE " + COLUMN_COURSE_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{courseName});

        if (cursor.moveToFirst()) {
            courseId = cursor.getInt(cursor.getColumnIndex(COLUMN_COURSE_ID));
        }

        cursor.close();
        db.close();

        return courseId;
    }
    //在指定的班级中模糊查找学生信息，返回学生id
    public ArrayList<Integer> getStudentIdsByClassIdAndKeyword(int classId, String keyword) {
        ArrayList<Integer> studentIds = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // 查询学生信息表，根据班级ID和关键字进行筛选
        String query = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTINFO_NAME +
                " WHERE " + COLUMN_CLASS_ID + " = " + classId +
                " AND (" + COLUMN_STUDENT_ID + " LIKE  '%" + keyword + "%'  OR " +
                COLUMN_STUDENT_NAME + " LIKE '%" + keyword + "%')";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
                studentIds.add(studentId);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return studentIds;
    }
    //已知coursescore表的自增主键ID，返回该id在表中对应的信息
    public ArrayList<ContentValues> getCourseScoreById(int courseScoreId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ContentValues> courseScores = new ArrayList<>();
        // 在coursescore表中根据自增主键ID查询对应的记录
        String query = "SELECT * FROM " + TABLE_COURSESCORE_NAME +
                " WHERE " + COLUMN_SCORE_ID + " = " + courseScoreId;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            // 获取记录的所有字段和对应的值
            ContentValues contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            // 将记录添加到列表中
            courseScores.add(contentValues);
        }
        cursor.close();
        return courseScores;
    }

    //输入学生id和课程id找到对应学生成绩id。如果找到匹配的记录，它将返回coursescore表的自增主键ID。如果未找到匹配的记录，函数将返回-1。
    @SuppressLint("Range")
    public int getCourseScoreIdByStudentAndCourseId(int studentId, int courseId) {
        SQLiteDatabase db = getReadableDatabase();
        int courseScoreId = -1;
        // 在coursescore表中根据学生ID查找所有一次id为外键的记录
        String query = "SELECT " + COLUMN_SCORE_ID + " FROM " + TABLE_COURSESCORE_NAME +
                " WHERE " + COLUMN_STUDENT_ID + " = " + studentId;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int scoreId = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_ID));
            // 在choosecourse表中根据自增主键ID和课程ID查找对应的记录
            String chooseQuery = "SELECT * FROM " + TABLE_CHOOSE_NAME +
                    " WHERE " + COLUMN_CNT_ID + " = " + scoreId +
                    " AND " + COLUMN_COURSE_ID + " = " + courseId;
            Cursor chooseCursor = db.rawQuery(chooseQuery, null);
            if (chooseCursor.moveToFirst()) {
                // 找到对应的记录，获取coursescore表的自增主键ID
                courseScoreId = chooseCursor.getInt(chooseCursor.getColumnIndex(COLUMN_CNT_ID));
                break;
            }
            chooseCursor.close();
        }
        cursor.close();
        return courseScoreId;
    }
    //根据输入的学生id返回学生id和对应的姓名
    public HashMap<Integer, String> getStudentIdAndNameById(int studentId) {
        HashMap<Integer, String> studentMap = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();

        // 查询学生信息表，根据学生ID进行筛选
        String query = "SELECT " + COLUMN_STUDENT_ID + ", " + COLUMN_STUDENT_NAME +
                " FROM " + TABLE_STUDENTINFO_NAME +
                " WHERE " + COLUMN_STUDENT_ID + " = " + studentId;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME));
                studentMap.put(id, name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentMap;
    }
    //根据班级id返回学生id和名字
    public List<ListRow_StudentIDAndStudentName> getStudentsByClassId(int classId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_STUDENT_ID + ", " + COLUMN_STUDENT_NAME +
                " FROM " + TABLE_STUDENTINFO_NAME +
                " WHERE " + COLUMN_CLASS_ID + " = " + classId;
        Cursor cursor = db.rawQuery(query, null);
        List<ListRow_StudentIDAndStudentName> students = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String studentID = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID));
                @SuppressLint("Range") String studentName = cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME));
                ListRow_StudentIDAndStudentName student = new ListRow_StudentIDAndStudentName(studentID, studentName);
                students.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }
    //返回指定老师的课程班级关系
    public HashMap<String, Integer> getCourseClassRelationsByTeacherId(int teacherId) {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String, Integer> courseClassRelations = new HashMap<>();

        String query = "SELECT DISTINCT " +
                TABLE_COURSEINFO_NAME + "." + COLUMN_COURSE_NAME + ", " +
                TABLE_CHOOSE_NAME + "." + COLUMN_CLASS_ID + " " +
                "FROM " + TABLE_CHOOSE_NAME + " " +
                "INNER JOIN " + TABLE_COURSEINFO_NAME + " " +
                "ON " + TABLE_CHOOSE_NAME + "." + COLUMN_COURSE_ID + " = " + TABLE_COURSEINFO_NAME + "." + COLUMN_COURSE_ID + " " +
                "WHERE " + TABLE_CHOOSE_NAME + "." + COLUMN_TEACHER_ID + " = " + teacherId;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME));
            @SuppressLint("Range") int classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID));
            courseClassRelations.put(courseName, classId);
        }
        cursor.close();
        return courseClassRelations;
    }


    //获取choosecourse表中的点名次数、上机次数和作业次数
    @SuppressLint("Range")
    public int[] getAttendanceAndAssignmentCounts(int scoreId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " +
                COLUMN_SCORE_CNT_KQ + ", " +
                COLUMN_SCORE_CNT_HOMEWORK + ", " +
                COLUMN_SCORE_CNT_SJ +
                " FROM " + TABLE_CHOOSE_NAME +
                " WHERE " + COLUMN_SCORE_ID + " = " + scoreId;

        Cursor cursor = db.rawQuery(query, null);
        int[] counts = new int[3];
        if (cursor.moveToFirst()) {
            counts[0] = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_CNT_KQ));
            counts[1] = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_CNT_HOMEWORK));
            counts[2] = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE_CNT_SJ));
        }
        cursor.close();
        return counts;
    }

    //    //选课小函数   Pkq点名占比，Psj上机占比,Phomework作业占比 cid班级id tid老师id course课程id
//    public boolean chooseCourseL(SQLiteDatabase sqLiteDatabase,  int cid, int tid, int Pkq, int Psj, int Phomework, int course) {
//        // 查询学生ID列表
//        List<Integer> studentIds = new ArrayList<>();
//        String query = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTINFO_NAME + " WHERE " + COLUMN_CLASS_ID + " = " + cid;
//        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
//                studentIds.add(studentId);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        ContentValues values = new ContentValues();
//
//        int[] counts = getAttendanceAndAssignmentCounts();
//        int m = counts[0];
//        int n = counts[1];
//        int k = counts[2];
//
//        for (int studentId : studentIds) {
//            values.clear();
//            values.put(COLUMN_STUDENT_ID, studentId);
//            long courseId = sqLiteDatabase.insert(TABLE_COURSESCORE_NAME, null, values);
//            values.clear();
//            values.put(COLUMN_COURSE_ID, course);
//            values.put(COLUMN_TEACHER_ID, tid);
//            values.put(COLUMN_CLASS_ID, cid);
//            values.put(COLUMN_SCORE_ID, courseId);
//            values.put(COLUMN_SCORE_PER_KQ, Pkq);
//            values.put(COLUMN_SCORE_PER_HOMEWORK, Phomework);
//            values.put(COLUMN_SCORE_PER_SJ, Psj);
//            values.put(COLUMN_SCORE_CNT_KQ, m);
//            values.put(COLUMN_SCORE_CNT_HOMEWORK, k);
//            values.put(COLUMN_SCORE_CNT_SJ, n);
//            sqLiteDatabase.insert(TABLE_CHOOSE_NAME, null, values);
//        }
//        return true;
//    }
    //选课大函数   m点名次数，n上机次数，k作业次数，Pkq点名占比，Psj上机占比,Phomework作业占比 cid班级id tid老师id course课程id
    public boolean chooseCourse(SQLiteDatabase sqLiteDatabase, int m, int n, int k, int cid, int tid, int Pkq, int Psj, int Phomework, int course,List<Integer> student) {
        // 查询学生ID列表
        List<Integer> studentIds = student;
//        String query = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTINFO_NAME + " WHERE " + COLUMN_CLASS_ID + " = " + cid;
//        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDENT_ID));
//                studentIds.add(studentId);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();

        // 动态创建coursescore表
        if(!isTableExists(TABLE_COURSESCORE_NAME)){
            createCoursescoreTable(sqLiteDatabase, 10,10,10,Pkq,Psj,Phomework);
        }

        // 绑定coursescore的自增ID到choosecourse表的记录
//        System.out.println(studentIds);

        ContentValues values = new ContentValues();
        for (int studentId : studentIds) {
            values.clear();
            values.put(COLUMN_STUDENT_ID, studentId);
            long courseId = sqLiteDatabase.insert(TABLE_COURSESCORE_NAME, null, values);
            values.clear();
            values.put(COLUMN_COURSE_ID, course);
            values.put(COLUMN_TEACHER_ID, tid);
            values.put(COLUMN_CLASS_ID, cid);
            values.put(COLUMN_SCORE_ID, courseId);
            values.put(COLUMN_SCORE_PER_KQ, Pkq);
            values.put(COLUMN_SCORE_PER_HOMEWORK, Phomework);
            values.put(COLUMN_SCORE_PER_SJ, Psj);
            values.put(COLUMN_SCORE_CNT_KQ, m);
            values.put(COLUMN_SCORE_CNT_HOMEWORK, k);
            values.put(COLUMN_SCORE_CNT_SJ, n);
            sqLiteDatabase.insert(TABLE_CHOOSE_NAME, null, values);
        }
        return true;
    }
    //判断表格是否存在
    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean tableExists = false;
        if (cursor != null && cursor.moveToFirst()) {
            tableExists = true;
            cursor.close();
        }
        return tableExists;
    }

    //查询所有课程名字
    public List<String> getAllCourseNames() {
        List<String> courseNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSEINFO_NAME, new String[]{COLUMN_COURSE_NAME}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME));
            courseNames.add(courseName);
        }

        cursor.close();
        db.close();
        return courseNames;
    }
    //根据传入的课程名字返回课程id
    @SuppressLint("Range")
    public int getCourseIdByName(String courseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int courseId = -1;
        // 构建查询语句
        String query = "SELECT " + COLUMN_COURSE_ID + " FROM " + TABLE_COURSEINFO_NAME +
                " WHERE " + COLUMN_COURSE_NAME + " = ?";
        String[] selectionArgs = {courseName};
        // 执行查询
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            // 从结果中获取课程ID
            courseId = cursor.getInt(cursor.getColumnIndex(COLUMN_COURSE_ID));
        }
        // 关闭游标和数据库连接
        cursor.close();
        db.close();
        return courseId;
    }
    //查询所有班级id
    public List<Integer> getAllClassIds() {
        List<Integer> classIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLASSINFO_NAME, new String[]{COLUMN_CLASS_ID}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int classId = cursor.getInt(cursor.getColumnIndex(COLUMN_CLASS_ID));
            classIds.add(classId);
        }

        cursor.close();
        db.close();
        return classIds;
    }
    //动态创建coursescore表
    public void createCoursescoreTable(SQLiteDatabase sqLiteDatabase, int m, int n, int k,int Pkq,int Psj,int Phomework) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_COURSESCORE_NAME);
        sb.append("(");
        sb.append(COLUMN_SCORE_ID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLUMN_STUDENT_ID);
        sb.append(" INTEGER,");

        // 创建点名列和备注列
        for (int i = 1; i <= m; i++) {
            sb.append("kqS_");
            sb.append(i);
            sb.append(" REAL DEFAULT 0,"); // 设置默认值为0，数据类型改为REAL
            sb.append("kqR_");
            sb.append(i);
            sb.append(" TEXT,");
        }

// 创建上机列和备注列
        for (int i = 1; i <= n; i++) {
            sb.append("sjS_");
            sb.append(i);
            sb.append(" REAL DEFAULT 0,"); // 设置默认值为0，数据类型改为REAL
            sb.append("sjR_");
            sb.append(i);
            sb.append(" TEXT,");
        }

// 创建作业列和备注列
        for (int i = 1; i <= k; i++) {
            sb.append("homeworkS_");
            sb.append(i);
            sb.append(" REAL DEFAULT 0,"); // 设置默认值为0，数据类型改为REAL
            sb.append("homeworkR_");
            sb.append(i);
            sb.append(" TEXT,");
        }

        sb.append(COLUMN_FINAL_SCORE);
        sb.append(" REAL DEFAULT 0,"); // 设置默认值为0
        sb.append("FOREIGN KEY(");
        sb.append(COLUMN_STUDENT_ID);
        sb.append(") REFERENCES ");
        sb.append(TABLE_STUDENTINFO_NAME);
        sb.append("(");
        sb.append(COLUMN_STUDENT_ID);
        sb.append("))");
        sqLiteDatabase.execSQL(sb.toString());
    }
    //登录验证
    public boolean login(int account, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TEACHER_ID + " = ? AND " + COLUMN_USER_PASS + " = ?";
        String[] selectionArgs = {String.valueOf(account), password};
        Cursor cursor = db.query(TABLE_TEACHER_NAME, null, selection, selectionArgs, null, null, null);

        boolean isValidLogin = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return isValidLogin;
    }
    //查询登录用户身份
    @SuppressLint("Range")
    public int getUserGravity(int account) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TEACHER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(account)};
        Cursor cursor = db.query(TABLE_TEACHER_NAME, new String[]{COLUMN_USER_GRAVITY}, selection, selectionArgs, null, null, null);

        int userGravity = 0; // 默认权限为 0

        if (cursor.moveToFirst()) {
            userGravity = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_GRAVITY));
        }

        cursor.close();
        db.close();
        return userGravity;
    }
    //注册
    public boolean register(int id, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_ID, id);
        values.put(COLUMN_TEACHER_NAME, name);
        values.put(COLUMN_USER_PASS, password);
        values.put(COLUMN_USER_GRAVITY, 1);
        db.insert(TABLE_TEACHER_NAME, null, values);
        db.close();
        return true;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}