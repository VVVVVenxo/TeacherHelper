package com.example.teacherhelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Mana_Manager_Class extends AppCompatActivity {
    private SimpleAdapter adapterClass;// 获取适配器
    private List<Map<String, Object>> ClassInfolist = new ArrayList<Map<String, Object>>();
//    private List<ListRow_CourseAndClassID> listItems = new ArrayList<>();// 创建list集合
    private ListView listView;// listview组件对象
    private DBHelper dbHelper;// DBHelper对象
    private static final String DATABASE_NAME = "TeacherHepler.db";// 数据库名
    private static final int version = 1;// 数据库版本
    private Button button_increase;// 增加按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mana_manager_class);
        dbHelper = new DBHelper(this, DATABASE_NAME, null, version);
        listView = findViewById(R.id.activity_mana_manager_class_listview);

        ClassInfolist = dbHelper.getAllClassInfo();

        adapterClass = new SimpleAdapter(this, ClassInfolist,
                R.layout.listview_class_item_layout, new String[]{"classId", "academy","major"}
                , new int[]{R.id.listview_class_item_classId
                ,R.id.listview_class_item_academy,R.id.listview_class_item_major
        }); // 创建SimpleAdapter
        listView.setAdapter(adapterClass);//设置适配器

        button_increase = findViewById(R.id.activity_mana_manager_class_increase);
        button_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog显示
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Mana_Manager_Class.this);
                builder.setTitle("添加课程信息");
                View view_classInfo = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_mana_manager_class, null);
                builder.setView(view_classInfo); //设置样式
                //按钮监听器
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer classId = 0;
                        String academy,major;
                        //课程信息
                        EditText etClassId = view_classInfo.findViewById(R.id.dialog_mana_manager_class_classId);
                        EditText etAcademy = view_classInfo.findViewById(R.id.dialog_mana_manager_class_academy);
                        EditText etMajor = view_classInfo.findViewById(R.id.dialog_mana_manager_class_major);
                        //非空验证
                        if (!etClassId.getText().toString().equals("")) {
                            classId = Integer.parseInt(etClassId.getText().toString());
                        }
                        academy = etAcademy.getText().toString();
                        major = etAcademy.getText().toString();
                        //非空判断
                        if(!academy.equals("") && !major.equals("") && classId.equals(0)){
                            //数据库操作
                            dbHelper.insertClassInfo(classId, academy, major);
                        }else {
                            Toast.makeText(v.getContext(),"请输入有效数据", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                showClassInfo();
            }
        });
    }
    //更新信息
    void showClassInfo(){
        ClassInfolist = dbHelper.getAllClassInfo();
        adapterClass.notifyDataSetChanged();
    }
}