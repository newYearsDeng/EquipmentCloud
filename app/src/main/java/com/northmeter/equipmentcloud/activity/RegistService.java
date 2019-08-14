package com.northmeter.equipmentcloud.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.DBRegistBean;
import com.northmeter.equipmentcloud.http.JsonCallback;
import com.northmeter.equipmentcloud.sqlite.DBRegistHelper;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyd on 2019/8/5.
 * 注册任务上传
 */
public class RegistService extends Service {
    private DBRegistHelper registHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getUnLoadRegist();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    private void getUnLoadRegist(){
        registHelper = new DBRegistHelper(this);
        //查询未完成的设备注册任务，isUpdata字段的值为0表示未完成
        List<DBRegistBean> registBeans =  registHelper.queryByCondit("isUpdata","0");
        for(DBRegistBean registItem:registBeans){
            registereEquipment(registItem);
        }
    }

    private void registereEquipment(DBRegistBean registBean) {
        Map mapList = new HashMap();
        mapList.put("recordId",registBean.getRecordId());
        mapList.put("equipmentId",registBean.getEquipmentId());
        mapList.put("equipmentNum",registBean.getEquipmentNum());
        mapList.put("itemTypeId",registBean.getItemTypeId());
        mapList.put("equipmentAddress",registBean.getEquipmentAddress());
        OkGo.<CommonResponse>post(API.getSharedUrl(this)+API.registereEquipment)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(this).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new JsonCallback<CommonResponse>(CommonResponse.class) {
                             @Override
                             public void onSuccess(Response<CommonResponse> response) {
                                 if(response.body().getCode() == 0){
                                     //注册完成，在数据库中删除设备注册任务
                                     registHelper.delete(registBean.getRecordId());
                                 }
                             }
                             @Override
                             public void onError(Response<CommonResponse> response) {
                                 super.onError(response);
                             }
                         }
                );
    }

}
