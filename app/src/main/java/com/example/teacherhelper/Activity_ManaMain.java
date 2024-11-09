package com.example.teacherhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Activity_ManaMain extends AppCompatActivity implements View.OnClickListener {

    LinearLayout homeLinear;
    Fragment_ManaHome fragmentHome;

    private FragmentManager mfragmentManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mana_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        initView();
    }

    private void initView() {
        homeLinear= (LinearLayout) findViewById(R.id.activity_mana_main_home);
        homeLinear.setOnClickListener(this);
        mfragmentManger = getSupportFragmentManager();
        homeLinear.performClick();

    }

    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = mfragmentManger.beginTransaction();//只能是局部变量，不能为全局变量，否则不能重复commit
        //FragmentTransaction只能使用一次
        hideAllFragment(fragmentTransaction);
        switch (view.getId()){
            case R.id.activity_mana_main_home:
                setAllFalse();
                homeLinear.setSelected(true);
                if (fragmentHome==null){
                    fragmentHome=new Fragment_ManaHome();
                    fragmentTransaction.add(R.id.activity_mana_fragment,fragmentHome);
                }else{
                    fragmentTransaction.show(fragmentHome);
                }
                break;
//            case R.id.activity_tea_main_user:
//                setAllFalse();
//                userLinear.setSelected(true);
//                if(fragmentUser==null){
//                    fragmentUser=new Fragment_TeaUser();
//                    fragmentTransaction.add(R.id.activity_tea_fragment,fragmentUser);
//                }else {
//                    fragmentTransaction.show(fragmentUser);
//                }
//                break;
        }
        fragmentTransaction.commit();//记得必须要commit,否则没有效果
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(fragmentHome!=null){
            fragmentTransaction.hide(fragmentHome);
        }
    }

    private void setAllFalse() {
        homeLinear.setSelected(false);
    }
}