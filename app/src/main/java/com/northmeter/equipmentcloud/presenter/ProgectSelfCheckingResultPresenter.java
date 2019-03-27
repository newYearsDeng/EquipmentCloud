package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectSelfCheckingResultPresenter;
import com.northmeter.equipmentcloud.I.I_ShowSelfCheckingResult;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.bean.SingleSelfCheckingResultResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/1/21.
 */

public class ProgectSelfCheckingResultPresenter implements I_ProgectSelfCheckingResultPresenter {
    public I_ShowSelfCheckingResult showSelfCheckingResult;
    public Context context;

    public ProgectSelfCheckingResultPresenter(Context context){
        this.context = context;
        this.showSelfCheckingResult = (I_ShowSelfCheckingResult) context;
    }


    /**查询项目设备自检结果*/
    @Override
    public void getProgectSelfCheckingResult() {
        Map mapList = new HashMap();
        mapList.put("recordId","");
        OkGo.<CommonResponse>post(API.doactiveEquipment)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<CommonResponse>((Activity) context,CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showSelfCheckingResult.returnSuccess(response.body().getMsg());
                                 }else{
                                     showSelfCheckingResult.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                                 showSelfCheckingResult.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

}
