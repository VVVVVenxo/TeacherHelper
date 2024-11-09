package com.example.teacherhelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.teacherhelper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TeaHome extends Fragment {

    private GridView gridView;
    private Adapter_Home homeAdapter;
    private List<Map<String,Object>> list;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_tea_home,container,false);
        if(view!=null){
            init();
//            Toast.makeText(view.getContext(),"主页", Toast.LENGTH_SHORT).show();
            initValue();
        }

        return view;
    }
    private void initValue() {
        list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("imageId",R.drawable.chaxun);
        map.put("names","管理课程");
        map.put("nextActivity",Activity_Manager_Course.class);
        list.add(map);
        map=new HashMap<>();
        map.put("imageId",R.drawable.contact);
        map.put("names","成绩管理");
        map.put("nextActivity",Activity_Manager_Grade.class);
        list.add(map);
        map=new HashMap<>();
        map.put("imageId",R.drawable.addressbookadd);
        map.put("names","数据备份");
        map.put("nextActivity",Activity_BackUp.class);
        list.add(map);
        homeAdapter=new Adapter_Home(list,getContext(),getActivity());
        gridView.setAdapter(homeAdapter);
    }

    private void init() {
        gridView=(GridView)view.findViewById(R.id.tea_home_gv_home);
    }
}
