package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ActivityTeaMain extends AppCompatActivity implements View.OnClickListener{

    LinearLayout homeLinear;
    LinearLayout userLinear;
    Fragment_TeaHome fragmentHome;
    Fragment_TeaUser fragmentUser;

    private FragmentManager mfragmentManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_main);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        initView();
    }

    private void initView() {
        homeLinear= (LinearLayout) findViewById(R.id.activity_tea_main_home);
        userLinear= (LinearLayout) findViewById(R.id.activity_tea_main_user);
        homeLinear.setOnClickListener(this);
        userLinear.setOnClickListener(this);
        mfragmentManger = getSupportFragmentManager();
        homeLinear.performClick();

    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = mfragmentManger.beginTransaction();//只能是局部变量，不能为全局变量，否则不能重复commit
        //FragmentTransaction只能使用一次
        hideAllFragment(fragmentTransaction);
        switch (view.getId()){
            case R.id.activity_tea_main_home:
                setAllFalse();
                homeLinear.setSelected(true);
                if (fragmentHome==null){
                    fragmentHome=new Fragment_TeaHome();
                    fragmentTransaction.add(R.id.activity_tea_fragment,fragmentHome);
                }else{
                    fragmentTransaction.show(fragmentHome);
                }
                break;
            case R.id.activity_tea_main_user:
                setAllFalse();
                userLinear.setSelected(true);
                if(fragmentUser==null){
                    fragmentUser=new Fragment_TeaUser();
                    fragmentTransaction.add(R.id.activity_tea_fragment,fragmentUser);
                }else {
                    fragmentTransaction.show(fragmentUser);
                }
                break;
        }
        fragmentTransaction.commit();//记得必须要commit,否则没有效果
    }
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(fragmentHome!=null){
            fragmentTransaction.hide(fragmentHome);
        }
        if(fragmentUser!=null){
            fragmentTransaction.hide(fragmentUser);
        }
    }

    private void setAllFalse() {
        homeLinear.setSelected(false);
        userLinear.setSelected(false);
    }
}