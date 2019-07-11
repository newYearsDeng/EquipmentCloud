package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.base.CheckPermissionsActivity;
import com.northmeter.equipmentcloud.presenter.LoginPresenter;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;
import com.northmeter.equipmentcloud.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2018/12/13.
 */

public class LoginActivity extends CheckPermissionsActivity implements I_ShowReturnData{

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

    @OnClick({R.id.btn_login, R.id.iv_delete,R.id.iv_show,R.id.iv_network})
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
            case R.id.iv_network://网络设置
                showNetWorkDialog();
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

    public void showNetWorkDialog() {
        final AlertDialog dialogSex = new AlertDialog.Builder(new ContextThemeWrapper(LoginActivity.this, R.style.AlertDialog)).create();
        dialogSex.show();
        Window window = dialogSex.getWindow();
        window.setContentView(R.layout.dialog_network);
        dialogSex.setCanceledOnTouchOutside(true);
        dialogSex.setCancelable(true);
        // 在此设置显示动画
        window.setWindowAnimations(R.style.AnimBottom_Dialog);

        EditText netInput = window.findViewById(R.id.et_net_input);
        netInput.setText(SharedPreferencesUtil.getPrefString(LoginActivity.this,"BASE_URL",API.URL_BASE));

        window.findViewById(R.id.btn_net_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                netInput.setText(API.URL_BASE);
                SharedPreferencesUtil.setPrefString(LoginActivity.this,"BASE_URL",API.URL_BASE);
                dialogSex.cancel();
            }
        });
        window.findViewById(R.id.btn_net_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtil.setPrefString(LoginActivity.this,"BASE_URL",netInput.getText().toString());
                dialogSex.cancel();
            }
        });
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogSex.show();
    }
}
