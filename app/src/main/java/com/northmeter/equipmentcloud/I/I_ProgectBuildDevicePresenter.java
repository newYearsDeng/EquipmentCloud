package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;

import java.util.List;

/**
 * Created by dyd on 2019/2/28.
 */

public interface I_ProgectBuildDevicePresenter {
    /**查询设备列表*/
    void getEquipList(int projectId , int buildingId );
    /**设备注册*/
    void registereEquipment(int recordId,String equipmentId,String equipmentNum,String itemTypeId ,String equipmentAddress,int state);
    /**设备激活*/
    void doactiveEquipment(int recordId,int state);
    /**设备测试*/
    void doTestEquipment(int recordId,String equipmentId,String equipmentNum,String itemTypeId,String equipmentName,int state);
    /**设备删除*/
    void deleteEquipment(List<ProgectBuildDeviceResponse.PageList> equipList,int state);
}
