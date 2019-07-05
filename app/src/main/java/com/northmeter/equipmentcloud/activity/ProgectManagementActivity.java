package com.northmeter.equipmentcloud.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

    private int projectId;
    private String projectName;
    private Intent intent;
    private static final int REQUEST_CAMERARESULT=201;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_management;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showMsg("该功能需要您授权打开相机");
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CAMERARESULT);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERARESULT:
                boolean isAllGranted = true;
                for(int result : grantResults){
                    if(result == PackageManager.PERMISSION_DENIED){
                        isAllGranted = false;
                        break;
                    }
                }
                if(!isAllGranted){
                    //权限有缺失
                    showMsg("该功能需要您授权打开相机");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 10);
                }
                break;
        }

    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId",0);
        projectName = getIntent().getStringExtra("projectName");
        intent = new Intent();
        intent.putExtra("projectId",projectId);
        intent.putExtra("projectName",projectName);
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
                goActivity(ProgectRecordImportActivity.class,intent);
                break;
            case R.id.ll_pro_manage_download:
                goActivity(ProgectRecordDownActivity.class,intent);
                break;
            case R.id.ll_pro_manage_selfchecking:
                goActivity(ProgectSelfCheckingActivity.class,intent);
                break;
        }
    }
}
