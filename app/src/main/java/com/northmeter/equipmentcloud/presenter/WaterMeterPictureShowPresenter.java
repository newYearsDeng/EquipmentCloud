package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ShowNetWorkPicture;
import com.northmeter.equipmentcloud.I.I_WaterMeterPictureShowPresenter;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.bean.NetWorkPictureBean;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dyd on 2019/3/27.
 */

public class WaterMeterPictureShowPresenter implements I_WaterMeterPictureShowPresenter {
    private Context context;
    private I_ShowNetWorkPicture i_showNetWorkPicture;
    private LoadingDialog mLoadingDialog;

    public WaterMeterPictureShowPresenter(Context context){
        this.context = context;
        i_showNetWorkPicture = (I_ShowNetWorkPicture) context;

    }

    @Override
    public void getNetWorkPicture(String equipmentNum,String itemTypeId) {
        OkGo.<NetWorkPictureBean>get(API.getSharedUrl(context)+API.getMeterNetWorkPic)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("equipNum",equipmentNum)
                .params("itemType",itemTypeId)
                .execute(new DialogCallback<NetWorkPictureBean>((Activity) context,NetWorkPictureBean.class) {
                             @Override
                             public void onSuccess(Response<NetWorkPictureBean> response) {
                                 if(response.body().getCode() == 0 ){
                                     if(response.body().getList()==null){
                                         i_showNetWorkPicture.returnMessage("没有查询到图片");
                                     }else{
                                         i_showNetWorkPicture.showData(response.body().getList());
                                         downloadFiles(response.body().getList().getImageUrl());
                                     }
                                 }else{
                                     i_showNetWorkPicture.returnMessage(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<NetWorkPictureBean> response) {
                                 super.onError(response);
                                 i_showNetWorkPicture.returnMessage("连接失败，请稍后重试");
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
                            i_showNetWorkPicture.showBitmap(response.body());
                        }
                    });
        }
    }


    public void startLoadingDialog(){
        mLoadingDialog = new LoadingDialog(context);
        mLoadingDialog.setLoadingText("加载中,请稍后...");
        mLoadingDialog.setInterceptBack(false);
        mLoadingDialog.show();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handeler.sendEmptyMessage(0);
            }
        };
        new Timer().schedule(timerTask,20000);
    }

    Handler handeler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLoadingDialog.close();
        }
    };

    public void stopLoadingDialog(){
        if(mLoadingDialog==null){
            mLoadingDialog = new LoadingDialog(context);
        }
        mLoadingDialog.close();
    }
}
