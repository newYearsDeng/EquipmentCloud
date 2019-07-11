package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.northmeter.equipmentcloud.I.I_ProgectListPresenter;
import com.northmeter.equipmentcloud.I.I_ShowProgectList;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.LocalConfigurationPlanBean;
import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyd on 2019/1/4.
 */

public class ProgectListPresenter implements I_ProgectListPresenter {
    Context context;
    I_ShowProgectList showProgectList;
    public ProgectListPresenter(Context context,I_ShowProgectList showProgectList){
        this.context = context;
        this.showProgectList = showProgectList;
    }

    @Override
    public void getProgectList(int status) {
        Map mapList = new HashMap();
        mapList.put("status",status);
        OkGo.<ProgectListResponse>get(API.getSharedUrl(context)+API.getProjectList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("status",status)
                .execute(new DialogCallback<ProgectListResponse>((Activity) context,ProgectListResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectListResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showProgectList.showData(response.body().getList());
                                 }else{
                                     showProgectList.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectListResponse> response) {
                                 super.onError(response);
                                 showProgectList.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    /**获取配置方案*/
    @Override
    public void getConfigurationPlan(final int projectId, String projectName) {
        OkGo.<ConfigurationPlanResponse>get(API.getSharedUrl(context)+API.getConfigurationPlan)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectName",projectName)
                .execute(new DialogCallback<ConfigurationPlanResponse>((Activity) context,ConfigurationPlanResponse.class) {
                             @Override
                             public void onSuccess(Response<ConfigurationPlanResponse> response) {
                                 if(response.body().getCode() == 0){
                                    List<ConfigurationPlanResponse.PlanBean> planBeans = response.body().getList();
                                    for(ConfigurationPlanResponse.PlanBean planItem : planBeans ){
                                        downloadFiles(projectId,planItem.getConfigurationPlanUrl(),planItem.getConfigurationPlanName());
                                    }

                                 }else{
                                     showProgectList.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ConfigurationPlanResponse> response) {
                                 super.onError(response);
                                 showProgectList.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    @Override
    public void downloadFiles(int projectId,String url,String name) {
        OkGo.<File>get(url)
                .tag(this)
                .params("token", SaveUserInfo.getLoginUser(context).getToken())
                .execute(new FileCallback(Constants.filePath+projectId,name) {

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        //pd.dismiss();
                    }

                    @Override
                    public void onError(Response<File> response) {
                        //pd.dismiss();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        System.out.println(progress);
                        //handler.sendEmptyMessage((int) (progress.fraction*100));
                    }
                });
    }


    /**获取文件列表*/
    public void getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if(!file.exists()){
            file.mkdirs();
        }
        if (files == null){
            showProgectList.returnFail("error");
        }else{
            List<LocalConfigurationPlanBean.PlanBean> fileList = new ArrayList<>();
            LocalConfigurationPlanBean.PlanBean planBean = new LocalConfigurationPlanBean.PlanBean();
            for(int i = 0;i<files.length;i++){
                System.out.println("path======"+files[i].getAbsolutePath());
                System.out.println("name======"+files[i].getName());
                planBean.setAddress(files[i].getAbsolutePath());
                planBean.setName(files[i].getName());
                fileList.add(planBean);
            }

        }
    }


}
