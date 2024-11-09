package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Activity_Mana_ImportStudent extends AppCompatActivity {
    private Button btManual,btInport;
    private DBHelper dbHelper;
    private List<StudentExcelBen> stu;
    //导入类
    private ChooseFile chooseFile;
    private final static String PATH = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mana_import_student);

        //动态授权
        if (isStoragePermissionGranted()) {
            File directory = new File(PATH);
            File[] files = directory.listFiles();
            Log.i("juno", PATH + " files : " + (files == null ? null : files.length));
        }
        chooseFile = new ChooseFile();
        chooseFile.setOnChooseFileBack(chooseFileBack);
        //
        dbHelper = new DBHelper(Activity_Mana_ImportStudent.this,"TeacherHepler.db",null,1);
        btManual = findViewById(R.id.activity_mana_import_button_manual);
        btInport = findViewById(R.id.activity_mana_import_button);
        //手工输入
        btManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Mana_ImportStudent.this, Activity_Mana_Import_Manual.class);
                startActivity(intent);
            }
        });
        //文件导入
        btInport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文件选择
                chooseFile.popupChoose(Activity_Mana_ImportStudent.this, v, getWindow(), getLayoutInflater(),true);
            }
        });
    }
    //回调
    ChooseFile.onChooseFileBack chooseFileBack = new ChooseFile.onChooseFileBack() {
        @Override
        public void onChooseBack(String path, String type) {
            Log.v(getComponentName().getClassName(),"选择文件：" + path);
            File file = new File(path);
            if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")){
                try {
                    stu = Send_Student.importStudent(path);
                    for (int i = 0; i < stu.size(); i++) {
                        StudentExcelBen student = stu.get(i); // 获取第i个student对象
                        System.out.println(Integer.parseInt(student.getStuId())+student.getStuName()+Integer.parseInt(student.getStuClass()));
                        dbHelper.insertStudentInfo(Integer.parseInt(student.getStuId()), student.getStuName(), Integer.parseInt(student.getStuClass()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(Activity_Mana_ImportStudent.this, "请选择excel文件" , Toast.LENGTH_SHORT).show();
            }

        }
    };

    //判断权限
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            final Context context = getApplicationContext();
            int readPermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int managerPermissionCheck = ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            if (readPermissionCheck == PackageManager.PERMISSION_GRANTED
                    && writePermissionCheck == PackageManager.PERMISSION_GRANTED
//                    && managerPermissionCheck == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("juno", "Permission is granted");
                return true;
            } else {
                Log.v("juno", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("juno", "Permission is granted");
            return true;
        }
    }
    //授权
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("juno","onRequestPermissionsResult requestCode ： " + requestCode
                        + " Permission: " + permissions[0] + " was " + grantResults[0]
                        + " Permission: " + permissions[1] + " was " + grantResults[1]
//                + " Permission: " + permissions[2] + " was " + grantResults[2]
        );
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            File directory = new File(PATH);
            File[] files = directory.listFiles();
            Log.i("juno", "After PERMISSION_GRANTED files : " + (files == null ? null : files.length));
        }
    }

}
