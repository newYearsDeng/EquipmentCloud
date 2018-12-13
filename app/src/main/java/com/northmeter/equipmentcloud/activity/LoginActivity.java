package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2018/12/13.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.et_login_passwd)
    EditText etLoginPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        super.initData();
        etLoginName.setText("");
        etLoginPasswd.setText("");
    }

    @OnClick({R.id.et_login_passwd, R.id.btn_login, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_login_passwd:
                break;
            case R.id.btn_login:
                getData();
                break;
            case R.id.iv_delete:
                etLoginName.setText("");
                etLoginPasswd.setText("");
                break;
        }
    }

    private void getData() {

    }
}
