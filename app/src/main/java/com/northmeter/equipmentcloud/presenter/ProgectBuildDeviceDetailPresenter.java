package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectBuildDeviceDetail;
import com.northmeter.equipmentcloud.I.I_ShowDeviceDetail;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.ProgectDeviceDetailResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

/**
 * Created by dyd on 2019/3/11.
 * 获取设备详情
 */

public class ProgectBuildDeviceDetailPresenter implements I_ProgectBuildDeviceDetail {
    private Context context;
    private I_ShowDeviceDetail showDeviceDetail;
    public ProgectBuildDeviceDetailPresenter(Context context){
        this.context = context;
        this.showDeviceDetail = (I_ShowDeviceDetail)context;
    }

    @Override
    public void getEquipmentDetails(int recordId) {
        OkGo.<ProgectDeviceDetailResponse>get(API.getEquipmentDetails)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("recordId",recordId)
                .execute(new DialogCallback<ProgectDeviceDetailResponse>((Activity) context,ProgectDeviceDetailResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectDeviceDetailResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDeviceDetail.showData(response.body().getList());
                                 }else{
                                     showDeviceDetail.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectDeviceDetailResponse> response) {
                                 super.onError(response);
                                 showDeviceDetail.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }
}