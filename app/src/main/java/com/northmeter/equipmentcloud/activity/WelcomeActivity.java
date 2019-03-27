package com.northmeter.equipmentcloud.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;

/**
 *欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_we;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	try{
            		startActivity(new Intent(WelcomeActivity.this,ProgectBuildDeviceDetailActivity.class));
                    finish();
            	}catch(Exception e){
            		startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            		e.printStackTrace();
            	}
               
            }
        }, 2000);
        
    }
    

}
