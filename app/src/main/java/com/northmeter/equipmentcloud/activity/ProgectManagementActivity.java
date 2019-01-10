package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 项目管理
 */

public class ProgectManagementActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_management;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("项目管理");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_tb_back, R.id.ll_pro_manage_import, R.id.ll_pro_manage_download,
            R.id.ll_pro_manage_selfchecking})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.ll_pro_manage_import:
                goActivity(ProgectRecordImportActivity.class);
                break;
            case R.id.ll_pro_manage_download:
                goActivity(ProgectRecordDownActivity.class);
                break;
            case R.id.ll_pro_manage_selfchecking:
                goActivity(ProgectSelfCheckingActivity.class);
                break;
        }
    }
}
