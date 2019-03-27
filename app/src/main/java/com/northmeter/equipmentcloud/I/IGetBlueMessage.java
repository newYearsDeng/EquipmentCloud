package com.northmeter.equipmentcloud.I;


import com.northmeter.equipmentcloud.bluetooth.GetBuleModel;

/**
 * Created by dyd
 * 2017/5/15
 */
public interface IGetBlueMessage {
    void getBlueMessage(String str, GetBuleModel.DownloadMsgCallback downloadMsgCallback);
}
