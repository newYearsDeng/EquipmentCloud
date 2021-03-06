package com.northmeter.equipmentcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.fragment.Fragment_ProgectList;
import com.northmeter.equipmentcloud.widget.EmptyFragmentPagerAdapter;
import com.northmeter.equipmentcloud.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/2.
 * 项目列表
 */

public class ProgectListActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    @BindView(R.id.tl_empty)
    TabLayout tlEmpty;
    @BindView(R.id.vp_empty)
    NoScrollViewPager vpEmpty;
    @BindView(R.id.btn_tb_back)
    ImageView btnTbBack;

    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTitles;
    private EmptyFragmentPagerAdapter adapter;
    private long exitTime;

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
        tvToolbarTitle.setText("项目列表");
        btnTbBack.setVisibility(View.GONE);
        tvRightText.setText("注册记录");
    }

    @Override
    public void initData() {
        super.initData();
        fragments.add(Fragment_ProgectList.newInstance(0));//加载未完成项目列表
        fragments.add(Fragment_ProgectList.newInstance(1));//加载已完成项目列表
        mTitles = new String[]{"未完成", "已完成"};
        adapter = new EmptyFragmentPagerAdapter(getSupportFragmentManager(), fragments, mTitles);
        vpEmpty.setAdapter(adapter);
        tlEmpty.setupWithViewPager(vpEmpty);
        vpEmpty.setOffscreenPageLimit(2);
        //启动service，上传未完成的设备注册任务
        startService(new Intent(this,RegistService.class));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_tb_back, R.id.tv_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.tv_right_text:
                goActivity(RegistRecordListActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showMsg("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
