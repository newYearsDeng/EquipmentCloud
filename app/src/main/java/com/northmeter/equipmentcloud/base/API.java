package com.northmeter.equipmentcloud.base;

/**
 * Created by dyd on 2018/12/13.
 */

public class API {
    public static String URL_BASE = "http://193.112.249.36:7655";

    /**--账户管理--------------------------*/


    /**--设备类型--------------------------*/
    /**获取设备类型信息*/
    public static String getDeviceTypeInfo = URL_BASE +"/dataBase/getDeviceTypeInfo";
    /**获取采集设备类型信息*/
    public static String getCollectorTypeInfo = URL_BASE +"/dataBase/getCollectorTypeInfo";
    /**获取采集中继设备类型信息*/
    public static String getRepeatersTypeInfo = URL_BASE +"/dataBase/getRepeatersTypeInfo";
    /**获取计量设备类型信息*/
    public static String getMeterTypeInfo = URL_BASE +"/dataBase/getMeterTypeInfo";


    /**--采集设备操作--------------------------*/
    /**添加采集设备post*/
    public static String addCollector = URL_BASE +"/dataBase/addCollector";
    /**删除采集设备post*/
    public static String deleteCollector = URL_BASE +"/dataBase/deleteCollector";
    /**删除采集设备及其下挂的计量设备post*/
    public static String forceDeleteCollector = URL_BASE +"/dataBase/forceDeleteCollector";
    /**更新采集设备及其相关的配表关系post*/
    public static String updateCollectorAndConfig = URL_BASE +"/dataBase/updateCollectorAndConfig";


    /**--计量设备操作--------------------------*/
    /**添加计量设备 post*/
    public static String addMeter = URL_BASE +"/dataBase/addMeter";
    /**删除计量设备 post*/
    public static String deleteMeter = URL_BASE +"/dataBase/deleteMeter";
    /**修改计量设备 post*/
    public static String updateMeter = URL_BASE +"/dataBase/updateMeter";


    /**--设备信息查询--------------------------*/
    /**获取采集设备总记录数*/
    public static String getCollectorInfoTotalRow = URL_BASE+"/dataBase/getCollectorInfoTotalRow";
    /**获取采集设备信息*/
    public static String getCollectorInfo = URL_BASE+"/dataBase/getCollectorInfo";
    /**获取采集中继总记录数*/
    public static String getRepeatersInfoTotalRow = URL_BASE+"/dataBase/getRepeatersInfoTotalRow";
    /**获取采集中继信息*/
    public static String getRepeatersInfo = URL_BASE+"/dataBase/getRepeatersInfo";
    /**获取计量设备总记录数*/
    public static String getMeterInfoTotalRow = URL_BASE+"/dataBase/getMeterInfoTotalRow";
    /**获取计量设备信息*/
    public static String getMeterInfo = URL_BASE+"/dataBase/getMeterInfo";



    /**--批量操作--------------------------*/
    /**批量添加采集设备 post*/
    public static String batchAddCollector = URL_BASE+"/dataBase/batchAddCollector";
    /**批量添加计量设备 post*/
    public static String batchAddMeter = URL_BASE+"/dataBase/batchAddMeter";
    /**批量获取组网关系*/
    public static String batchGetMeter = URL_BASE+"/dataBase/batchGetMeter";


    /**--本地数据--------------------------*/
    /**获取所有采集设备状态*/
    public static String getStatus = URL_BASE+"/connector/getStatus";
    /**获取当前用户采集设备状态*/
    public static String getStatusByUser = URL_BASE+"/connector/getStatusByUser";
    /**获取工控机本地数据总记录数*/
    public static String getHistoryRecordsTotalRow = URL_BASE+"/connector/getHistoryRecordsTotalRow";
    /**获取工控机本地数据*/
    public static String getHistoryRecords = URL_BASE+"/connector/getHistoryRecords";

    /**--通讯接口--post----------------------*/
    public static String sendMiddleware = URL_BASE+"/middleware";
}
