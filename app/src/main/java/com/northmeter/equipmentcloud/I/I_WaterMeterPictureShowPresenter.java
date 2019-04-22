package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/3/27.
 */

public interface I_WaterMeterPictureShowPresenter {

    /**获取网络图片*/
    void getNetWorkPicture(String equipmentNum,String itemTypeId);

    void downloadFiles(String Url);
}
