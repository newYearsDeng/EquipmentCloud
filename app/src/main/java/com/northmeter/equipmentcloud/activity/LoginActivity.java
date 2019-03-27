package com.northmeter.equipmentcloud.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.base.WebServiceUtils;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.Blue_MainActivity;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BTMainActivity;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.presenter.LoginPresenter;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;
import com.northmeter.equipmentcloud.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2018/12/13.
 */

public class LoginActivity extends BaseActivity implements I_ShowReturnData{

    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.et_login_passwd)
    EditText etLoginPasswd;

    private boolean showOrHide = false;
    private LoginPresenter loginPresenter;
    private static final int REQUEST_CAMERARESULT=201;
    private static final int REQUEST_LOCATIONARESULT=2010;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED||
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                checkLocationAndOpenCamer();
            }else{
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CAMERARESULT);
            }
        }
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
                    showMsg("该功能需要您授权打开相机和定位");
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 10);
                }else{
                    checkLocationAndOpenCamer();
                }
                break;
            case REQUEST_LOCATIONARESULT:
                break;
            default:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 10);
                break;
        }

    }

    /**检查是否打开了定位服务，再打开相机扫描*/
    private void checkLocationAndOpenCamer(){
        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        boolean locationISOK = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!locationISOK){
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_LOCATIONARESULT);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        etLoginName.setText(SaveUserInfo.getLoginUser(this).getUserName());
        etLoginPasswd.setText(SaveUserInfo.getLoginUser(this).getPassWord());
        loginPresenter = new LoginPresenter(this,this);
//        if(SharedPreferencesUtil.getPrefBoolean(this,"is_login",false)){
//            goActivity(ProgectListActivity.class);
//        }
    }

    @OnClick({R.id.btn_login, R.id.iv_delete,R.id.iv_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_show://密码显示或隐藏
                if (showOrHide){
                    etLoginPasswd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//密码
                    showOrHide = false;
                }else{
                    etLoginPasswd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//明文
                    showOrHide = true;
                }
                break;
            case R.id.btn_login://登录
                loginPresenter.toLogin(etLoginName.getText().toString(),etLoginPasswd.getText().toString());
                break;
            case R.id.iv_delete://输入框清空
                etLoginName.setText("");
                etLoginPasswd.setText("");
                break;
        }
    }

    @Override
    public void returnSuccess(String message) {
            goActivity(ProgectListActivity.class);
            this.finish();

    }

    @Override
    public void returnFail(String message) {
        showMsg(message);
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
