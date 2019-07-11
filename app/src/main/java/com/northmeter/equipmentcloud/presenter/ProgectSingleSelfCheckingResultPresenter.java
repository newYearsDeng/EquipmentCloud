package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectSingleSelfCheckingResultPresenter;
import com.northmeter.equipmentcloud.I.I_ShowSingleSelfChecking;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.SingleSelfCheckingResultResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;

/**
 * Created by dyd on 2019/1/21.
 */

public class ProgectSingleSelfCheckingResultPresenter implements I_ProgectSingleSelfCheckingResultPresenter {
    public I_ShowSingleSelfChecking showSelfCheckingResult;
    public Context context;

    public ProgectSingleSelfCheckingResultPresenter(Context context){
        this.context = context;
        this.showSelfCheckingResult = (I_ShowSingleSelfChecking) context;
    }


    /**查询单个设备自检结果*/
    @Override
    public void getDeviceSelfCheckingResult(int recordId) {
        OkGo.<SingleSelfCheckingResultResponse>get(API.getSharedUrl(context)+API.singlSeselfCheckingResult)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("recordId",recordId)
                .execute(new DialogCallback<SingleSelfCheckingResultResponse>((Activity) context,SingleSelfCheckingResultResponse.class) {
                             @Override
                             public void onSuccess(Response<SingleSelfCheckingResultResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showSelfCheckingResult.showData(response.body().getResult());
                                     downloadFiles(response.body().getResult().getImageUrl());
                                 }else{
                                     showSelfCheckingResult.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<SingleSelfCheckingResultResponse> response) {
                                 super.onError(response);
                                 showSelfCheckingResult.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }


    @Override
    public void downloadFiles(String Url) {
        if(Url!=null){
            OkGo.<Bitmap>get(Url)
                    .tag(this)
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Response<Bitmap> response) {
                            showSelfCheckingResult.showBitmap(response.body());
                        }
                    });
        }
    }
}
