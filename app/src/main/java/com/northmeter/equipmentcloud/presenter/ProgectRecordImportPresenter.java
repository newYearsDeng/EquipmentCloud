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

    public ProgectRecordImportPresenter(I_ShowRecordImport showRecordImport){
        this.showRecordImport = showRecordImport;
        this.context = (Context) showRecordImport;
    }

    @Override
    public void getRecordImportList(int projectId) {
        OkGo.<RecordImportResponse>post(API.getBuildList)
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
                                     showRecordImport.returnFail();
                                 }
                             }

                             @Override
                             public void onCacheSuccess(Response<RecordImportResponse> response) {
                                 super.onCacheSuccess(response);
                                 showRecordImport.showData(response.body().getList());
                             }

                             @Override
                             public void onError(Response<RecordImportResponse> response) {
                                 super.onError(response);
                                 showRecordImport.returnFail();
                             }
                         }
                );
    }


    @Override
    public void saveProgectRecord(String equipmentName, String equipmentId, int itemTypeId,
                                  String address, int projectId) {
        Map mapList = new HashMap();
        mapList.put("equipmentName",equipmentName);
        mapList.put("equipmentId",equipmentId);
        mapList.put("itemTypeId",itemTypeId);
        mapList.put("itemTypeId",address);
        mapList.put("itemTypeId",projectId);

        OkGo.<RecordImportResponse>post(API.saveEquipmentRecord)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<RecordImportResponse>((Activity) context,RecordImportResponse.class) {
                             @Override
                             public void onSuccess(Response<RecordImportResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showRecordImport.showData(response.body().getList());
                                 }else{
                                     showRecordImport.returnFail();
                                 }
                             }

                             @Override
                             public void onCacheSuccess(Response<RecordImportResponse> response) {
                                 super.onCacheSuccess(response);
                                 showRecordImport.showData(response.body().getList());
                             }

                             @Override
                             public void onError(Response<RecordImportResponse> response) {
                                 super.onError(response);
                                 showRecordImport.returnFail();
                             }
                         }
                );
    }
}
