package com.example.teacherhelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragment_TeaUser extends Fragment implements View.OnClickListener{

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_tea_user,container,false);
        if(view!=null){
//            Toast.makeText(view.getContext(),"个人中心", Toast.LENGTH_SHORT).show();
            init();
        }
        return view;
    }
    private void init() {
        LinearLayout teaInfo = (LinearLayout) view.findViewById(R.id.fragment_tea_user_info);
        LinearLayout changeMail = (LinearLayout) view.findViewById(R.id.fragment_tea_user_update_mail);
        LinearLayout changePwd = (LinearLayout) view.findViewById(R.id.fragment_tea_user_update_pwd);
        TextView tvTeaName = (TextView) view.findViewById(R.id.fragment_tea_user_name);
//        设置监听器
        teaInfo.setOnClickListener(this);
        changeMail.setOnClickListener(this);
        changePwd.setOnClickListener(this);

        SharedPreferences sharedPreferences=view.getContext().getSharedPreferences("teaInfo_Save", Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("teacherName","username");
        tvTeaName.setText(name);

    }
    //条目点击
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_tea_user_info:
                enterTeaInfo();
                break;
            case R.id.fragment_tea_user_update_mail:
                enterChangeMail();
                break;
            case R.id.fragment_tea_user_update_pwd:
                enterChangePwd();
                break;
        }
    }
    //dialog显示信息
    private void enterTeaInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("当前用户");
        View view_info = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_tea_info, null);
        builder.setView(view_info); // 设置对话框的自定义View
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
       // 获取存储数据
        TextView tvTeaId = view_info.findViewById(R.id.dialog_tea_info_id);
        TextView tvTeaName = view_info.findViewById(R.id.dialog_tea_info_name);
        TextView tvTeaMail = view_info.findViewById(R.id.dialog_tea_info_mail);
        SharedPreferences sp = this.getContext().getSharedPreferences("teaInfo_Save",Context.MODE_PRIVATE);
        tvTeaId.setText(Integer.toString(sp.getInt("teacherId",0)));
        tvTeaName.setText(sp.getString("teacherName", ""));
        tvTeaMail.setText(sp.getString("teacherEmail",""));

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //修改邮箱及密码
    private void enterChangeMail() {
        DBHelper dbHelper = new DBHelper(view.getContext(), "TeacherHepler.db", null, 1);
        SharedPreferences sp = this.getContext().getSharedPreferences("teaInfo_Save",Context.MODE_PRIVATE);
        //显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("当前邮箱为：");
        View view_upate_mail = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_tea_update_mail, null);
        builder.setView(view_upate_mail); // 设置对话框的自定义View

        ///获取原邮箱并显示
        TextView pre_Mail = view_upate_mail.findViewById(R.id.dialog_tea_update_preMail);
        String preMail = sp.getString("teacherEmail","");
        pre_Mail.setText(preMail);

        //按钮监听器
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取新邮箱
                EditText etCrrentMail = view_upate_mail.findViewById(R.id.dialog_tea_update_currentMail);
                EditText etMailPwd = view_upate_mail.findViewById(R.id.dialog_tea_update_MailPwd);
                String currentMail = etCrrentMail.getText().toString();
                String mailPwd = etMailPwd.getText().toString();
                //非空判断
                if(!currentMail.equals("") && !mailPwd.equals("")){
                    //创建一个正则表达式字符串
                    String regex = "^[a-zA-Z0-9]+([-.][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([-.][a-zA-Z0-9]+)*\\.[a-zA-Z0-9]+([-.][a-zA-Z0-9]+)*$";
                    //根据正则表达式字符串创建一个Pattern对象
                    Pattern pattern = Pattern.compile(regex);
                    //根据字符串创建一个Matcher对象
                    Matcher matcher = pattern.matcher(currentMail);
                    if (matcher.matches()) {
                        //数据库操作
                        dbHelper.updateTeacherEmailCredentialsById(sp.getInt("teacherId", 0), currentMail, mailPwd);
                        //sp 文件更新
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("teacherEmail", currentMail);
                        editor.putString("emailPassword", mailPwd);
                        editor.apply();
                    }
                }else {
                    Toast.makeText(view.getContext(),"请输入有效数据", Toast.LENGTH_SHORT).show();
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

    }
    //修改密码
    private void enterChangePwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("修改密码");
        View view_update_pwd = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_tea_updata_pwd, null);
        builder.setView(view_update_pwd); // 设置对话框的自定义View
        //按钮监听器
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //输入验证
                EditText etCurrentPwd = view_update_pwd.findViewById(R.id.dialog_tea_update_currentPwd);
                EditText etCrrentPwd1 = view_update_pwd.findViewById(R.id.dialog_tea_update_currentPwd1);
                String newpwd = etCurrentPwd.getText().toString();
                String newpwd1 = etCrrentPwd1.getText().toString();
                //验证是否不空且相等
                if ((!newpwd.equals(""))&&(!newpwd1.equals(""))&&(newpwd1.equals(newpwd))){
                    DBHelper dbHelper = new DBHelper(view.getContext(), "TeacherHepler.db", null, 1);
                    SharedPreferences sp = Fragment_TeaUser.this.getContext().getSharedPreferences("teaInfo_Save",Context.MODE_PRIVATE);
                    dbHelper.updateTeacherPasswordById(sp.getInt("teacherId",0),newpwd);
                    Toast.makeText(view.getContext(),"修改成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Toast.makeText(view.getContext(),"两次密码不一致", Toast.LENGTH_SHORT).show();
                }

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
    }
}
