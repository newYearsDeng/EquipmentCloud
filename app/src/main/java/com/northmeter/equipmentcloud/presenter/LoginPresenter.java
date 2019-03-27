package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_LoginPresenter;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.activity.ProgectListActivity;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.LoginResponse;
import com.northmeter.equipmentcloud.bean.UserInfo;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/1/4.
 */

public class LoginPresenter implements I_LoginPresenter {
    Context context;
    I_ShowReturnData showReturnData;
    public LoginPresenter(Context context,I_ShowReturnData showReturnData){
        this.context = context;
        this.showReturnData = showReturnData;
    }

    @Override
    public void toLogin(final String userName, final String passWord) {
        Map mapList = new HashMap();
        mapList.put("userName",userName);
        mapList.put("passWord",passWord);
        OkGo.<LoginResponse>post(API.login)
                .tag(this)
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<LoginResponse>((Activity) context,LoginResponse.class) {
                             @Override
                             public void onSuccess(Response<LoginResponse> response) {
                                 int code = response.body().getCode();
                                 if(code == 0){
                                     UserInfo userInfo = new UserInfo();
                                     userInfo.setUserName(userName);
                                     userInfo.setPassWord(passWord);
                                     userInfo.setExpire(response.body().getExpire());
                                     userInfo.setToken(response.body().getToken());
                                     SaveUserInfo.saveLoginUser(context,userInfo);
                                     showReturnData.returnSuccess(response.body().getMsg());
                                 }else{
                                     showReturnData.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<LoginResponse> response) {
                                 super.onError(response);
                                 showReturnData.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }
}
