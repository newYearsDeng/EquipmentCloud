package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.base.WebServiceUtils;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.presenter.LoginPresenter;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        etLoginName.setText("");
        etLoginPasswd.setText("");
        loginPresenter = new LoginPresenter(this);
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
    public void returnSuccess(int code, String message) {
        showMsg(message);
        if (code == 0){
            goActivity(ProgectListActivity.class);
        }
    }

    @Override
    public void returnFail(int code, String message) {
        showMsg(message);
    }
}
