package com.example.teacherhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

public class Activity_Login extends Activity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox cbSave;
    Context context = this;
    private DBHelper dbHelper;

    private EditText et_phoneCodes;
    private Bitmap bitmap;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("account_save", MODE_PRIVATE);
        // 获取存储数据
        String username = "";
        Integer name = sp.getInt("username", 0);
        if (name != 0) {
            username = name.toString();
        }
        String password = sp.getString("password", "");

        dbHelper = new DBHelper(Activity_Login.this, "TeacherHepler.db", null, 1);
        btnLogin = findViewById(R.id.activity_login_button_login);
        btnRegister = findViewById(R.id.activity_login_button_register);
        cbSave = findViewById(R.id.activity_login_save);
        etUsername = findViewById(R.id.activity_login_editText_userName);
        etPassword = findViewById(R.id.activity_login_editText_userPassword);
        et_phoneCodes = findViewById(R.id.et_inputCodes);

        //获取需要展示图片验证码的ImageView
        final ImageView image = (ImageView) findViewById(R.id.image);
        //获取工具类生成的图片验证码对象
        bitmap = CodeUtils.getInstance().createBitmap();
        //获取当前图片验证码的对应内容用于校验
        code = CodeUtils.getInstance().getCode();

        image.setImageBitmap(bitmap);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = CodeUtils.getInstance().createBitmap();
                code = CodeUtils.getInstance().getCode();
                image.setImageBitmap(bitmap);
            }
        });

        etUsername.setText(username);
        etPassword.setText(password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Susername = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String InputCode = et_phoneCodes.getText().toString();

                if (Susername.equals("") || password.equals("")) {
                    Toast.makeText(Activity_Login.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                } else if (!InputCode.equalsIgnoreCase(code)) {
                    Toast.makeText(Activity_Login.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                } else {
                    int username = Integer.parseInt(etUsername.getText().toString());
                    boolean isValidLogin = dbHelper.login(username, password);//验证账号信息

                    if (isValidLogin) {
                        //若选择记住账号
                        if (cbSave.isChecked()) {
                            SharedPreferences sp = getSharedPreferences("account_save", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("username", username);
                            editor.putString("password", password);
                            editor.apply();
                        }
                        // 登录成功的处理
                        Toast.makeText(Activity_Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        int userGravity = dbHelper.getUserGravity(username); // 调用根据账号查询用户权限的方法
                        if (userGravity == 0) {
                            //管理员
                            Toast.makeText(Activity_Login.this, "当前用户为管理员", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Login.this, Activity_ManaMain.class);
                            startActivity(intent);
                        } else if (userGravity == 1) {
                            teaInfo_Save(username);//保存信息
                            // 教师
                            Toast.makeText(Activity_Login.this, "当前用户为教师", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Login.this, ActivityTeaMain.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Activity_Login.this, "无法获取用户权限", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Activity_Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("login");
                Intent intent = new Intent(Activity_Login.this, ActivityRegister.class);
                startActivityForResult(intent, 0x01);
            }
        });
    }

    //处理注册返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01 && resultCode == 0x01) {//验证请求码和返回码
            etUsername = findViewById(R.id.activity_login_editText_userName);
            etPassword = findViewById(R.id.activity_login_editText_userPassword);
            Bundle bundle = data.getExtras();

            String username = String.valueOf(bundle.getInt("userid"));
            String password = bundle.getString("password");
            etUsername.setText(username);
            etPassword.setText(password);
        }
    }

    //个人信息存储
    void teaInfo_Save(int teaID) {
        String[] teacherDetails = dbHelper.getTeacherDetailsById(teaID);//查询教师数据
        SharedPreferences sp = getSharedPreferences("teaInfo_Save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("teacherId", teaID);
        editor.putString("teacherName", teacherDetails[0]);
        editor.putString("teacherEmail", teacherDetails[1]);
        editor.putString("emailPassword", teacherDetails[2]);
        editor.apply();
    }
}