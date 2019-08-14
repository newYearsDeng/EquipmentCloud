package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;

/**
 * Created by dyd on 2019/8/2.
 * 设备注册任务存储
 */
public class DBRegistBean implements Serializable {
    /**
     * recordId 	是 	Int 	建筑设备配置表的Id、设备的recordId
     * equipmentId 	是 	String 	设备id
     * equipmentNum 	是 	String 	设备编号
     * itemTypeId 	是 	int 	产品型号
     * equipmentAddress 	是 	String 	设备安装地址，是地址字符串
     * */
    public int recordId;
    public String equipmentId;
    public String equipmentNum;
    public String itemTypeId;
    public String equipmentAddress;
    public int isUpdata; //是否上传 0 false  1true

    public DBRegistBean(int recordId,String equipmentId,String equipmentNum,String itemTypeId,
                        String equipmentAddress,int isUpdata){
        this.recordId = recordId;
        this.equipmentId = equipmentId;
        this.equipmentNum = equipmentNum;
        this.itemTypeId = itemTypeId;
        this.equipmentAddress = equipmentAddress;
        this.isUpdata = isUpdata;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentNum() {
        return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
        this.equipmentNum = equipmentNum;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getEquipmentAddress() {
        return equipmentAddress;
    }

    public void setEquipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
    }

    public int getIsUpdata() {
        return isUpdata;
    }

    public void setIsUpdata(int isUpdata) {
        this.isUpdata = isUpdata;
    }
}
