package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_RecordDownLoadPresenter;
import com.northmeter.equipmentcloud.I.I_ShowDownLoad;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.RecordDownLoadResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyd on 2019/1/17.
 */

public class ProgectRecordDownLoadPresenter implements I_RecordDownLoadPresenter{
    private Context context;
    private I_ShowDownLoad showDownLoad;

    public ProgectRecordDownLoadPresenter(Context context,I_ShowDownLoad showDownLoad){
        this.context = context;
        this.showDownLoad = showDownLoad;
    }


    @Override
    public void getRecordDownLoadList(int projectId, String concentratorName) {
        OkGo.<RecordDownLoadResponse>get(API.getConcentratorList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("RecordDownLoadList")
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectId",projectId)
                .params("concentratorName",concentratorName)
                .execute(new DialogCallback<RecordDownLoadResponse>((Activity) context,RecordDownLoadResponse.class) {
                             @Override
                             public void onSuccess(Response<RecordDownLoadResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDownLoad.showData(response.body().getPage().getList());
                                 }else{
                                     showDownLoad.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onCacheSuccess(Response<RecordDownLoadResponse> response) {
                                 super.onCacheSuccess(response);
                                 //showRecordImport.showData(response.body().getList());
                             }

                             @Override
                             public void onError(Response<RecordDownLoadResponse> response) {
                                 super.onError(response);
                                 showDownLoad.returnFail(response.body().getMsg());
                             }
                         }
                );
    }

    @Override
    public void upRecordDownLoadList(int projectId ,List<RecordDownLoadResponse.ConcentratorList> datas) {
        Map mapObject = new HashMap();
        mapObject.put("projectId",projectId);
        List recordList = new ArrayList();
        for(RecordDownLoadResponse.ConcentratorList data :datas){
            if(data.isCheck()){
                recordList.add(data.getRecordId());
            }
        }
        mapObject.put("list",recordList);
        OkGo.<CommonResponse>post(API.saveConcentratorList)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapObject))
                .execute(new DialogCallback<CommonResponse>((Activity) context,CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showDownLoad.returnSuccess(response.body().getMsg());
                                 }else{
                                     showDownLoad.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                                 showDownLoad.returnFail(response.body().getMsg());
                             }
                         }
                );

    }
}
