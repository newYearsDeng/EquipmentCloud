package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.fragment.Fragment_ProgectList;
import com.northmeter.equipmentcloud.fragment.Fragment_RecordDownLoadList;
import com.northmeter.equipmentcloud.widget.EmptyFragmentPagerAdapter;
import com.northmeter.equipmentcloud.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 档案下发
 */

public class ProgectRecordDownActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    @BindView(R.id.tl_empty)
    TabLayout tlEmpty;
    @BindView(R.id.vp_empty)
    NoScrollViewPager vpEmpty;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTitles;
    private EmptyFragmentPagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_list;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("档案下载");
    }

    @Override
    public void initData() {
        super.initData();
        fragments.add(Fragment_RecordDownLoadList.newInstance(0));//加载已完成项目列表
        fragments.add(Fragment_RecordDownLoadList.newInstance(1));//加载未完成项目列表
        mTitles = new String[]{"已完成","未完成"};
        adapter = new EmptyFragmentPagerAdapter(getSupportFragmentManager(),fragments,mTitles);
        vpEmpty.setAdapter(adapter);
        tlEmpty.setupWithViewPager(vpEmpty);
        vpEmpty.setOffscreenPageLimit(2);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_tb_back)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_tb_back:
                this.finish();
                break;
        }
    }
}