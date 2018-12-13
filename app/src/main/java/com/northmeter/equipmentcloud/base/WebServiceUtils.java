package com.northmeter.equipmentcloud.base;


import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by dyd on 2017/8/24.
 */
public class WebServiceUtils {
    public interface CallBack {
        String resultSuccess(String result);
        String resultFail(String result);
    }

    public static void getServiceInfo(final Context context , String odb, final CallBack callBack) {
//        Map jsonString = new OrderHttp().sendOrder(odb);
//        Gson gson = new Gson();
//        OkGo.<MeterControlResponse>post(API.sendMiddleware)
//                .tag(context)
//                .isSpliceUrl(true)//post请求的url上拼接上参数
//                .headers("username", SaveUserInfo.getLoginUser(context).getUsername())
//                .headers("userpwd", SaveUserInfo.getLoginUser(context).getUserpwd())
//                .params("ipcid", Constants.ipcid)
//                .upJson(gson.toJson(jsonString))
//                .execute(new DialogCallback<MeterControlResponse>((Activity) context,MeterControlResponse.class) {
//                             @Override
//                             public void onSuccess(Response<MeterControlResponse> response) {
//                                 callBack.resultSuccess(response.body());
//                             }
//
//                             @Override
//                             public void onError(Response<MeterControlResponse> response) {
//                                 super.onError(response);
//                                 callBack.resultFail("Error");
//                             }
//                         }
//                );
    }


}
