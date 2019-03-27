package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 * 设备详情
 */

public class ProgectDeviceDetailResponse extends CommonResponse {

    private PageList list;

    public PageList getList() {
        return list;
    }

    public void setList(PageList list) {
        this.list = list;
    }

    public class PageList implements Serializable{
        private String itemType;//产品型号
        private String equipmentNum;//设备编号
        private String equipmentName;//设备名称
        private String bigCategoryName;//产品大类
        private String smallCategoryName;//产品小类
        private String measureCategoryName;//能源类型
        private String concentratorName;//集中器名称
        private String collectorName;//采集器名称
        private int activationMode;//注册状态
        private int usedState;//激活状态
        private String configurationPlanUrl;//设备配置方案的url

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
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

        public String getBigCategoryName() {
            return bigCategoryName;
        }

        public void setBigCategoryName(String bigCategoryName) {
            this.bigCategoryName = bigCategoryName;
        }

        public String getSmallCategoryName() {
            return smallCategoryName;
        }

        public void setSmallCategoryName(String smallCategoryName) {
            this.smallCategoryName = smallCategoryName;
        }

        public String getMeasureCategoryName() {
            return measureCategoryName;
        }

        public void setMeasureCategoryName(String measureCategoryName) {
            this.measureCategoryName = measureCategoryName;
        }

        public String getConcentratorName() {
            return concentratorName;
        }

        public void setConcentratorName(String concentratorName) {
            this.concentratorName = concentratorName;
        }

        public String getCollectorName() {
            return collectorName;
        }

        public void setCollectorName(String collectorName) {
            this.collectorName = collectorName;
        }

        public int getActivationMode() {
            return activationMode;
        }

        public void setActivationMode(int activationMode) {
            this.activationMode = activationMode;
        }

        public int getUsedState() {
            return usedState;
        }

        public void setUsedState(int usedState) {
            this.usedState = usedState;
        }

        public String getConfigurationPlanUrl() {
            return configurationPlanUrl;
        }

        public void setConfigurationPlanUrl(String configurationPlanUrl) {
            this.configurationPlanUrl = configurationPlanUrl;
        }
    }


}


