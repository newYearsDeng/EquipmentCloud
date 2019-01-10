package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectListPresenter;
import com.northmeter.equipmentcloud.I.I_ShowProgectList;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyd on 2019/1/4.
 */

public class ProgectListPresenter implements I_ProgectListPresenter {
    Context context;
    I_ShowProgectList showProgectList;
    public ProgectListPresenter(I_ShowProgectList showProgectList){
        this.context = (Context) showProgectList;
        this.showProgectList = showProgectList;
    }

    @Override
    public void getProgectList(int status, String page, String limit) {
        Map mapList = new HashMap();
        mapList.put("status",status);
        mapList.put("page",page);
        mapList.put("limit",limit);

        OkGo.<ProgectListResponse>post(API.getProjectList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("projectList")
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<ProgectListResponse>((Activity) context,ProgectListResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectListResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showProgectList.showData(response.body().getPage().getList());
                                 }else{
                                     showProgectList.returnFail();
                                 }
                             }

                    @Override
                    public void onCacheSuccess(Response<ProgectListResponse> response) {
                        super.onCacheSuccess(response);
                        showProgectList.showData(response.body().getPage().getList());
                    }

                    @Override
                             public void onError(Response<ProgectListResponse> response) {
                                 super.onError(response);
                                 showProgectList.returnFail();
                             }
                         }
                );
    }
}
