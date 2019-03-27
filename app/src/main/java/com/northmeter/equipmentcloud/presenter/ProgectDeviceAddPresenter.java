package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectDeviceAddPresenter;
import com.northmeter.equipmentcloud.I.I_ShowDeviceAdd;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.ProjetTypeResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/3/1.
 */

public class ProgectDeviceAddPresenter implements I_ProgectDeviceAddPresenter {
    private Context context;
    private I_ShowDeviceAdd showDeviceAdd;
    public ProgectDeviceAddPresenter(Context context){
        this.context = context;
        showDeviceAdd = (I_ShowDeviceAdd) context;
    }

    @Override
    public void addBuildingequipment(String equipmentName,String itemTypeId,int buildingId,int projectId,String key,
                                     String terminalPort,String ipcNum,String concentratorName,String collectorName,
                                     String configurationPlanName) {
        Map mapList = new HashMap();
        mapList.put("equipmentName",equipmentName);
        mapList.put("itemTypeId",itemTypeId);
        mapList.put("buildingId",buildingId);
        mapList.put("projectId",projectId);
        mapList.put("key",key);
        mapList.put("terminalPort",terminalPort);
        mapList.put("ipcNum",ipcNum);
        mapList.put("concentratorName",concentratorName);
        mapList.put("collectorName",collectorName);
        mapList.put("configurationPlanName",configurationPlanName);

        OkGo.<CommonResponse>post(API.addBuildingequipment)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<CommonResponse>((Activity) context,CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDeviceAdd.returnSuccess(response.body().getMsg());
                                 }else{
                                     showDeviceAdd.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                                 showDeviceAdd.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    /**获取配置方案*/
    @Override
    public void getConfigurationPlan(String projectName) {
        OkGo.<ConfigurationPlanResponse>get(API.getConfigurationPlan)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectName",projectName)
                .execute(new DialogCallback<ConfigurationPlanResponse>((Activity) context,ConfigurationPlanResponse.class) {
                             @Override
                             public void onSuccess(Response<ConfigurationPlanResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDeviceAdd.showplanData(response.body().getList());
                                 }else{
                                     showDeviceAdd.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ConfigurationPlanResponse> response) {
                                 super.onError(response);
                                 showDeviceAdd.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }


    /**获取产品型号*/
    @Override
    public void getProjetType() {
        OkGo.<ProjetTypeResponse>post(API.getProjetType)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .execute(new DialogCallback<ProjetTypeResponse>((Activity) context,ProjetTypeResponse.class) {
                             @Override
                             public void onSuccess(Response<ProjetTypeResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDeviceAdd.showTypeData(response.body().getList());
                                 }else{
                                     showDeviceAdd.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProjetTypeResponse> response) {
                                 super.onError(response);
                                 showDeviceAdd.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }


}
