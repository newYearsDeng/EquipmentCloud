package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectBuildListPresenter;
import com.northmeter.equipmentcloud.I.I_ShowBuildList;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dyd on 2019/2/26.
 */

public class ProgectBuildListPresenter implements I_ProgectBuildListPresenter {
    public Context context;
    public I_ShowBuildList showBuildList;
    public ProgectBuildListPresenter(Context context){
        this.context = context;
        this.showBuildList = (I_ShowBuildList) context;
    }

    @Override
    public void getBuildList(int projectId,int parentId) {
        OkGo.<ProgectBuildListResponse>get(API.getBuildList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectId",projectId)
                .params("parentId",parentId)
                .execute(new DialogCallback<ProgectBuildListResponse>((Activity) context,ProgectBuildListResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildListResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildList.showData(response.body().getList());
                                 }else{
                                     showBuildList.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildListResponse> response) {
                                 super.onError(response);
                                 showBuildList.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    @Override
    public void selfChecking(int projectId, int recodeId) {
        Map mapList = new HashMap();
        mapList.put("projectId",projectId);
        mapList.put("recodeId",recodeId);
        OkGo.<CommonResponse>post(API.singleSelfChecking)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<CommonResponse>((Activity) context,CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildList.returnSuccess(response.body().getMsg());
                                 }else{
                                     showBuildList.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                                 showBuildList.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

}
