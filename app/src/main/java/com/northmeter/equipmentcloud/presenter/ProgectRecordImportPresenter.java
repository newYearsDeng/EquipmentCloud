package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectRecordImportPresenter;
import com.northmeter.equipmentcloud.I.I_ShowRecordImport;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/1/10.
 */

public class ProgectRecordImportPresenter implements I_ProgectRecordImportPresenter{
    Context context;
    private I_ShowRecordImport showRecordImport;

    public ProgectRecordImportPresenter(Context context){
        this.showRecordImport = (I_ShowRecordImport) context;
        this.context = context;
    }

    @Override
    public void getRecordImportBuildList(int projectId) {
        OkGo.<RecordImportResponse>get(API.getBuildList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("RecordImportList")
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectId",projectId)
                .execute(new DialogCallback<RecordImportResponse>((Activity) context,RecordImportResponse.class) {
                             @Override
                             public void onSuccess(Response<RecordImportResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showRecordImport.showData(response.body().getList());
                                 }else{
                                     showRecordImport.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onCacheSuccess(Response<RecordImportResponse> response) {
                                 super.onCacheSuccess(response);
                                 //showRecordImport.showData(response.body().getList());
                             }

                             @Override
                             public void onError(Response<RecordImportResponse> response) {
                                 super.onError(response);
                                 showRecordImport.returnFail(response.toString());
                             }
                         }
                );
    }


    @Override
    public void saveProgectRecord(int projectId,int recordId , String equipmentName, String equipmentId, String itemTypeId,
                                  String equipmentAddress) {
        Map mapList = new HashMap();
        mapList.put("recordId",recordId);//建筑设备维系表Id
        mapList.put("equipmentName",equipmentName);//设备名称
        mapList.put("equipmentId",equipmentId);//设备编号
        mapList.put("itemTypeId",itemTypeId);//产品型号ID
        mapList.put("equipmentAddress",equipmentAddress);//设备安装地址，是地址字符串的拼接
        mapList.put("projectId",projectId);//项目ID
        OkGo.<RecordImportResponse>post(API.saveEquipmentRecord)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<RecordImportResponse>((Activity) context,RecordImportResponse.class) {
                             @Override
                             public void onSuccess(Response<RecordImportResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showRecordImport.returnSuccess(response.body().getMsg());
                                 }else{
                                     showRecordImport.returnFail(response.body().getMsg());
                                 }
                             }


                             @Override
                             public void onError(Response<RecordImportResponse> response) {
                                 super.onError(response);
                                 showRecordImport.returnFail(response.toString());
                             }
                         }
                );
    }
}
