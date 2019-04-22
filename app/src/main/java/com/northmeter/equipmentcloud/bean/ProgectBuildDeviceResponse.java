package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 * 设备列表
 */

public class ProgectBuildDeviceResponse extends CommonResponse {

    private List<PageList> list;

    public List<PageList> getList() {
        return list;
    }

    public void setList(List<PageList> list) {
        this.list = list;
    }

    public class PageList implements Serializable{
        private boolean isDelShow;
        private boolean isCheck;
        private int recordId;
        private String equipmentId;
        private String equipmentNum;
        private String itemTypeId;
        private String equipmentName;
        private String configurationPlanName;//配置方案的名称
        private int implementStatus;//测试状态 	0-未执行完成 1-已执行完毕
        private int implementResult;//测试状态 	0-测试成功 1-测试失败
        private int activeStatus;//激活状态  0-未激活，1—激活中，2-激活成功，3-激活失败，4，是否是可激活设备
        private int registerStatus;//注册状态  0-未注册，1-已经注册
        private int activationMode;// 激活模式 	1-远端激活，2-近端激活
        private int devtestMode;//测试模式 	1-远端测试，2-近端测试

        public boolean isDelShow() {
            return isDelShow;
        }

        public void setDelShow(boolean delShow) {
            isDelShow = delShow;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
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

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public int getActiveStatus() {
            return activeStatus;
        }

        public void setActiveStatus(int activeStatus) {
            this.activeStatus = activeStatus;
        }

        public int getRegisterStatus() {
            return registerStatus;
        }

        public void setRegisterStatus(int registerStatus) {
            this.registerStatus = registerStatus;
        }

        public String getItemTypeId() {
            return itemTypeId;
        }

        public void setItemTypeId(String itemTypeId) {
            this.itemTypeId = itemTypeId;
        }

        public String getConfigurationPlanName() {
            return configurationPlanName;
        }

        public void setConfigurationPlanName(String configurationPlanName) {
            this.configurationPlanName = configurationPlanName;
        }
        public int getImplementStatus() {
            return implementStatus;
        }

        public void setImplementStatus(int implementStatus) {
            this.implementStatus = implementStatus;
        }

        public int getImplementResult() {
            return implementResult;
        }

        public void setImplementResult(int implementResult) {
            this.implementResult = implementResult;
        }

        public int getActivationMode() {
            return activationMode;
        }

        public void setActivationMode(int activationMode) {
            this.activationMode = activationMode;
        }

        public int getDevtestMode() {
            return devtestMode;
        }

        public void setDevtestMode(int devtestMode) {
            this.devtestMode = devtestMode;
        }
    }


}