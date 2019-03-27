package com.northmeter.equipmentcloud.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.northmeter.equipmentcloud.I.I_ProgectBuildDevicePresenter;
import com.northmeter.equipmentcloud.I.I_ShowBuildDevice;
import com.northmeter.equipmentcloud.base.API;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.LocalConfigurationPlanBean;
import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;
import com.northmeter.equipmentcloud.http.DialogCallback;
import com.northmeter.equipmentcloud.utils.SaveUserInfo;
import com.northmeter.equipmentcloud.utils.Udp_Help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dyd on 2019/2/28.
 * 设备列表查询
 */

public class ProgectBuildDevicePresenter implements I_ProgectBuildDevicePresenter {

    private Context context;
    private I_ShowBuildDevice showBuildDevice;

    public ProgectBuildDevicePresenter(Context context){
        this.context = context;
        this.showBuildDevice = (I_ShowBuildDevice) context;
    }


    /**获取设备列表*/
    @Override
    public void getEquipList(int projectId, int buildingId ) {
        OkGo.<ProgectBuildDeviceResponse>get(API.getEquipList)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("projectId",projectId)
                .params("buildingId",buildingId)
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildDevice.showData(response.body().getList());
                                 }else{
                                     showBuildDevice.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showBuildDevice.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    /**设备注册*/
    @Override
    public void registereEquipment(int recordId, String equipmentId, String equipmentNum, String itemTypeId, String equipmentAddress, final int state) {
        Map mapList = new HashMap();
        mapList.put("recordId",recordId);
        mapList.put("equipmentId",equipmentId);
        mapList.put("equipmentNum",equipmentNum);
        mapList.put("itemTypeId",itemTypeId);
        mapList.put("equipmentAddress",equipmentAddress);
        OkGo.<ProgectBuildDeviceResponse>post(API.registereEquipment)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(mapList))
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildDevice.returnSuccess(response.body().getMsg(),state);
                                 }else{
                                     showBuildDevice.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showBuildDevice.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    /**设备激活*/
    @Override
    public void doactiveEquipment(int recordId, final int state) {
        Map mapList = new HashMap();
        mapList.put("recordId",recordId);
        OkGo.<ProgectBuildDeviceResponse>get(API.doactiveEquipment)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("recordId",recordId)
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildDevice.returnSuccess(response.body().getMsg(),state);
                                 }else{
                                     showBuildDevice.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showBuildDevice.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }

    /**设备自检*/
    @Override
    public void doTestEquipment( int recordId, String equipmentId, String equipmentNum, String itemTypeId, String equipmentName, final int state) {
        Map mapList = new HashMap();
        mapList.put("recordId",recordId);
        mapList.put("equipmentId",equipmentId);
        mapList.put("equipmentNum",equipmentNum);
        mapList.put("itemTypeId",itemTypeId);
        mapList.put("equipmentName",equipmentName);

        OkGo.<ProgectBuildDeviceResponse>get(API.singleSelfChecking)
                .tag(this)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .params("recordId",recordId)
                .params("equipmentId",equipmentId)
                .params("equipmentNum",equipmentNum)
                .params("itemTypeId",itemTypeId)
                .params("equipmentName",equipmentName)
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildDevice.returnSuccess(response.body().getMsg(),state);
                                 }else{
                                     showBuildDevice.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showBuildDevice.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }


    /**删除设备*/
    @Override
    public void deleteEquipment(List<ProgectBuildDeviceResponse.PageList> equipList, final int state) {
        List delList = new ArrayList();
        for(ProgectBuildDeviceResponse.PageList checkList : equipList){
            if(checkList.isCheck()){
                delList.add(checkList.getRecordId());
            }
        }
        OkGo.<ProgectBuildDeviceResponse>post(API.deleteEquipment)
                .tag(this)
                .headers("token", SaveUserInfo.getLoginUser(context).getToken())
                .upJson(new Gson().toJson(delList))
                .execute(new DialogCallback<ProgectBuildDeviceResponse>((Activity) context,ProgectBuildDeviceResponse.class) {
                             @Override
                             public void onSuccess(Response<ProgectBuildDeviceResponse> response) {
                                 if(response.body().getCode() == 0){
                                     showBuildDevice.returnSuccess(response.body().getMsg(),state);
                                 }else{
                                     showBuildDevice.returnFail(response.body().getMsg());
                                 }
                             }

                             @Override
                             public void onError(Response<ProgectBuildDeviceResponse> response) {
                                 super.onError(response);
                                 showBuildDevice.returnFail("连接失败，请稍后重试");
                             }
                         }
                );
    }



    /**读取文件
     * Environment.getExternalStorageDirectory().getAbsolutePath()+"/northmeter/"+"progectID"*/
    public String loadFromSDFile(String path,String fileName) {
        String result = "";
        try {
            File file=new File(path+fileName);
            if(!file.exists()){
                result = null;
            }else{
                FileInputStream fin = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(fin,"GBK");
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    result = result+line+"\n";
                    System.out.println("======"+line);
                }
                br.close();
                reader.close();
                fin.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            showBuildDevice.returnFail("没有找到指定文件");
        }
        return result;
    }


    /**
     * 曝光时间=30
     曝光设置=自动曝光
     取景坐标x轴原点=0
     取景坐标y轴原点=0
     取景坐标x轴长度=320
     取景坐标y轴长度=240
     对比度=60
     压缩率=60
     补光灯状态=开启自动补光
     上报类型=参考起始时间上报
     上报起始日期=2019-03-21
     上报起始时间=12:00:00
     上报间隔分钟=5
     随机退避时间=5
     拍照时间=12:00*/
    public String getFilesInfo(String equipmentId ,String fileName,int state){
        String result = loadFromSDFile(Constants.filePath,fileName);
        if(result==null){
            showBuildDevice.returnFail("文件不存在，请返回项目列表重新加载");
        }else{
            String[] infoList = result.split("\n");
            if(infoList.length>=15){
                if(state == 0 ){
                    String etExposureTime = infoList[0].split("=")[1];
                    String tvExposureSetting = infoList[1].split("=")[1];
                    String etCoordinateX = infoList[2].split("=")[1];
                    String etCoordinateY = infoList[3].split("=")[1];
                    String etCoordinateXlong = infoList[4].split("=")[1];
                    String etCoordinateYlong = infoList[5].split("=")[1];
                    String etContrastRatio = infoList[6].split("=")[1];
                    String etCompressionRatio = infoList[7].split("=")[1];
                    String tvFlashState = infoList[8].split("=")[1];
                    String sendStr = get_setting(equipmentId,etExposureTime,tvExposureSetting ,  tvFlashState , etCoordinateX, etCoordinateY,
                            etCoordinateXlong, etCoordinateYlong, etContrastRatio, etCompressionRatio);
                    return sendStr;
                }else{
                    String reportingDatas = infoList[8].split("=")[1];
                    String tvReportingDate = infoList[10].split("=")[1];
                    String tvReportingTime = infoList[11].split("=")[1];
                    String etSpaceTime = infoList[12].split("=")[1];
                    String etRandomTime = infoList[13].split("=")[1];
                    String tvPhotoTime = infoList[14].split("=")[1];

                    String sendStr = get_TimeSet(equipmentId ,  reportingDatas, tvReportingDate, tvReportingTime,
                            etSpaceTime, etRandomTime, tvPhotoTime);
                    return sendStr;
                }
            }
        }
        return null;
    }

    private String get_setting(String equipmentId,String etExposureTime,String tvExposureSetting , String tvFlashState ,String etCoordinateX,String etCoordinateY,
                               String etCoordinateXlong,String etCoordinateYlong,String etContrastRatio,String etCompressionRatio) {//4c3310ef 现场参数设置
        String exposureSetting  = "DD";
        if (tvExposureSetting.equals("自动曝光")) {
            exposureSetting = "DD";//自动曝光
        } else {
            exposureSetting = "EE";//手动曝光
        }

        String para_flash = "DDDD";
        switch (tvFlashState){
            case "开启自动补光":
                para_flash = "DDDD";//开启
                break;
            case "关闭自动补光":
                para_flash = "DD88";//关闭
                break;
            case "开启手动补光":
                para_flash = "EEDD";//开启
                break;
            case "关闭手动补光":
                para_flash = "EE88";//关闭
                break;
            default:
                para_flash = "DDDD";//开启
                break;
        }

        String para_x = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645(etCoordinateX));
        String para_y = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645(etCoordinateY));
        String para_xl = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645(etCoordinateXlong));
        String para_yl = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645(etCoordinateYlong));

        String para_str = para_x + para_y + para_xl + para_yl;


        String para_set = "68" + Udp_Help.reverseRst(equipmentId) +
                "68141B004c3310ef" + Constants.HandlerKey +
                Udp_Help.get_Stting_HexTo645(etExposureTime) +
                exposureSetting +
                para_str +
                Udp_Help.get_Stting_HexTo645(etContrastRatio) +
                "DD" + Udp_Help.get_Stting_HexTo645(etCompressionRatio) +
                para_flash;

        return getSendString(para_set);
    }

    private String get_TimeSet(String equipmentId , String reportingDatas,String tvReportingDate,String tvReportingTime,
                               String etSpaceTime,String etRandomTime,String tvPhotoTime) {//38343337
        String str_Enable = "34";//使能位,参考起始时间上报或者忽略起始时间上报，禁能
        switch(reportingDatas){
            case "参考起始时间上报":
                str_Enable = "34";
                break;
            case "忽略起始时间上报":
                str_Enable = "35";
                break;
            case "禁能":
                str_Enable = "EE";
                break;
            default:
                str_Enable = "34";
                break;
        }

        String para_Time = "68" + Udp_Help.reverseRst(equipmentId) +
                "68141B0038343337" + Constants.HandlerKey +
                str_Enable +
                Udp_Help.get_NBTimeTo645(tvReportingDate, 0) + "33" +
                Udp_Help.get_NBTimeTo645(tvReportingTime, 1) +
                Udp_Help.reverseRst(Udp_Help.get_NBMinTo645(etSpaceTime)) +
                Udp_Help.get_Stting_HexTo645(etRandomTime) +
                Udp_Help.get_NBTimeTo645(tvPhotoTime, 1);

        return getSendString(para_Time);
    }

    private String getSendString(String prar) {
        String cs = Udp_Help.get_sum(prar).toUpperCase() + "16";
        String sendMsg = "FEFEFEFE" + prar + cs;
        return sendMsg;
    }

    /**
     * nb摄像水表激活或者设备测试
     */
    public String sendNBActiveORResiger(String equipmentNum) {
        String para = "68" + Udp_Help.reverseRst(equipmentNum) +
                "68140E00363310EF" + Constants.HandlerKey + "34F4";
        String cs = Udp_Help.get_sum(para).toUpperCase() + "16";
        String sendMsg = "FEFEFEFE" + para + cs;
        System.out.println("指令："+sendMsg);
        return sendMsg;
    }
}
