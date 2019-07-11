package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectSelfCheckingPresenter;
import com.northmeter.equipmentcloud.I.I_ShowRecordImport;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/1/21.
 */

public class ProgectSelfCheckingPresenter implements I_ProgectSelfCheckingPresenter {
    Context context;
    private I_ShowRecordImport showRecordImport;

    public ProgectSelfCheckingPresenter(Context context){
        this.showRecordImport = (I_ShowRecordImport) context;
        this.context = context;
    }

    @Override
    public void getRecordImportBuildList(int projectId) {
        OkGo.<RecordImportResponse>get(API.getSharedUrl(context)+API.getBuildList)
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
                                 showRecordImport.returnFail(response.body().getMsg());
                             }
                         }
                );
    }

    @Override
    public void saveProgectSelfChecking(int projectId,int recodeId) {
        Map mapList = new HashMap();
        mapList.put("projectId",projectId);
        mapList.put("recodeId",recodeId);
        OkGo.<CommonResponse>post(API.getSharedUrl(context)+API.singleSelfChecking)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<CommonResponse>((Activity) context,CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showRecordImport.returnSuccess(response.body().getMsg());
                                 }else{
                                     showRecordImport.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                                 showRecordImport.returnFail(response.message());
                             }
                         }
                );
    }
}
