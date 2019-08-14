package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.DBRegistBean;
import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.sqlite.DBRegistHelper;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/8/13.
 */
public class RegistRecordListPresenter {
    private Context context;
    private I_ShowReturnData showReturnData;

    public RegistRecordListPresenter(Context context){
        this.context = context;
        showReturnData = (I_ShowReturnData) context;
    }

    public void registereEquipment(DBRegistBean registBean) {
        Map mapList = new HashMap();
        mapList.put("recordId",registBean.getRecordId());
        mapList.put("equipmentId",registBean.getEquipmentId());
        mapList.put("equipmentNum",registBean.getEquipmentNum());
        mapList.put("itemTypeId",registBean.getItemTypeId());
        mapList.put("equipmentAddress",registBean.getEquipmentAddress());
        OkGo.<ProgectBuildDeviceResponse>post(API.getSharedUrl(context)+API.registereEquipment)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     //注册成功，删除设备注册任务
                                     boolean isDel = new DBRegistHelper(context).delete(registBean.getRecordId());
                                     if(isDel){
                                         showReturnData.returnSuccess(response.body().getMsg());
                                     }
                                 }else{
                                     if(response.body().getMsg().indexOf("不存在")>=0){
                                         boolean isDel = new DBRegistHelper(context).delete(registBean.getRecordId());
                                         if(isDel){
                                             showReturnData.returnSuccess(response.body().getMsg());
                                         }
                                     }else{
                                         showReturnData.returnFail(response.body().getMsg());
                                     }
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showReturnData.returnFail("网络异常，联网成功后自动上传注册任务");
                             }
                         }
                );
    }

}
