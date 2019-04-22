package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;

/**
 * Created by dyd on 2019/3/27.
 * 获取网络图片
 */

public class NetWorkPictureBean extends CommonResponse{
    private PictureData list;

    public PictureData getList() {
        return list;
    }

    public void setList(PictureData list) {
        this.list = list;
    }

    public class PictureData implements Serializable{
        private int recordId;
        private String equipNum;
        private String itemType;
        private String imageUrl;
        private double dataValue;
        private String createTime;

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getEquipNum() {
            return equipNum;
        }

        public void setEquipNum(String equipNum) {
            this.equipNum = equipNum;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
