package com.northmeter.equipmentcloud.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.fragment.Fragment_NBMeter_Install;
import com.northmeter.equipmentcloud.fragment.Fragment_NBMeter_Setting;
import com.northmeter.equipmentcloud.widget.EmptyFragmentPagerAdapter;
import com.northmeter.equipmentcloud.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/3/11.
 * 本地设置——NB摄像水表
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LocationSet_NBDevice extends BaseActivity{
    @BindView(R.id.vp_empty)
    NoScrollViewPager vpEmpty;
    @BindView(R.id.tl_empty)
    TabLayout tlEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    private String TAG = getClass().getSimpleName();
    public static final String DEVICE_NAME = "device_name";

    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTitles;
    private EmptyFragmentPagerAdapter adapter;

    private int type;
    private String equipmentNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_set_nb_device;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        equipmentNum = getIntent().getStringExtra("equipmentNum");
        type =  getIntent().getIntExtra("type",0);
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("本地配置");
    }

    @Override
    public void initData() {
        super.initData();
        if(type == 0){
            fragments.add(Fragment_NBMeter_Install.newInstance(0));
            mTitles = new String[]{"安装测试"};
        }else{
            fragments.add(Fragment_NBMeter_Setting.newInstance(1));
            mTitles = new String[]{"参数设置"};
        }
        adapter = new EmptyFragmentPagerAdapter(getSupportFragmentManager(), fragments, mTitles);
        vpEmpty.setAdapter(adapter);
        tlEmpty.setupWithViewPager(vpEmpty);
        vpEmpty.setOffscreenPageLimit(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.btn_tb_back})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.btn_tb_back:
                this.finish();
                break;
        }

    }


}
