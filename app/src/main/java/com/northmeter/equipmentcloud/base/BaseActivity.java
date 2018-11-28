package com.northmeter.equipmentcloud.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd on 2018/11/28.
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseListener {
    protected Context mContext;
    protected Unbinder unbinder;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        BaseAppManager.getAppManager().addActivity(this);
        unbinder = ButterKnife.bind(this);
        mContext = this;
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        start();
    }

    @Override
    public void start() {
        initIntentData();
        setListener();
        initData();
        setTitle();
    }

    @Override
    public void start(View view) {

    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void setTitle() {

    }

    @Override
    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        BaseAppManager.getAppManager().removeAcitivity(this);
    }
}
