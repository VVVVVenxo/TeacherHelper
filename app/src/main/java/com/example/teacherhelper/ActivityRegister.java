package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class ActivityRegister extends AppCompatActivity {

    private EditText etUserid;
    private EditText etPassword;
    private EditText etUserName;
    private Button btnRegister;
    private ImageButton ImageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        DBHelper dbHelper=new DBHelper(ActivityRegister.this,"TeacherHepler.db",null,1);

        btnRegister = findViewById(R.id.activity_register_button_register);
        ImageBack = findViewById(R.id.activity_register_image_back);
        ImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserid = findViewById(R.id.activity_register_editText_userid);
                etPassword = findViewById(R.id.activity_register_editText_userPassword);
                etUserName = findViewById(R.id.activity_register_editText_name);
                String userIdString = etUserid.getText().toString();
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                int userId = 0;
                if (userIdString.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ActivityRegister.this,"请输入账号、姓名和密码",Toast.LENGTH_SHORT).show();
                } else
                {
                    userId = Integer.parseInt(userIdString);
                    Boolean ret =dbHelper.register(userId,username,password);//插入记录，返回是否成功执行
                    if(ret){//注册成功
                        Toast.makeText(ActivityRegister.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("userid",userId);
                        bundle.putString("password",password);
                        intent.putExtras(bundle);
                        setResult(0x01,intent);
                        finish();
                    }else {
                        Toast.makeText(ActivityRegister.this,"用户已存在",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}