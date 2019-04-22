package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/3/22.
 * 单个设备自检结果查询
 */

public class SingleSelfCheckingResultResponse extends CommonResponse{

    public ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public class ResultBean implements Serializable{
        private String implementResult ;//	任务执行结果
        private String installAddress ;//	String 	设备的地址
        private String equipmentName ;//	String 	设备名称
        private String imageUrl ;//	String 	图片地址url
        private double dataValue ;//double 	识别的值
        private String endTime ;//	String 	任务结束时间

        public String getImplementResult() {
            return implementResult;
        }

        public void setImplementResult(String implementResult) {
            this.implementResult = implementResult;
        }

        public String getInstallAddress() {
            return installAddress;
        }

        public void setInstallAddress(String installAddress) {
            this.installAddress = installAddress;
        }

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public double getDataValue() {
            return dataValue;
        }

        public void setDataValue(double dataValue) {
            this.dataValue = dataValue;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

}
