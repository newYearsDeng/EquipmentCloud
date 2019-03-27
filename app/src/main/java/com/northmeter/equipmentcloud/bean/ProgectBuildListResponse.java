package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 * 项目建筑列表
 */

public class ProgectBuildListResponse extends CommonResponse {

    private List<PageList> list;

    public List<PageList> getList() {
        return list;
    }

    public void setList(List<PageList> list) {
        this.list = list;
    }

    public class PageList implements Serializable{
        private int recordId;//建筑ID
        private String buildingName;//建筑名称
        private int type;//状态码 在设备子集代替buildingId,1：最后一级 0：不是最后一级
        private int parentId;//父建筑的id
        private int projectId;//项目id
        private int equipmentCount;//建筑下的设备的总数
        private int equipmentUnregistCount;//建筑下的未注册设备的总数
        private int equipmentUnactivateCount;//建筑下的未激活设备的总数

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getEquipmentCount() {
            return equipmentCount;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public void setEquipmentCount(int equipmentCount) {
            this.equipmentCount = equipmentCount;
        }

        public int getEquipmentUnregistCount() {
            return equipmentUnregistCount;
        }

        public void setEquipmentUnregistCount(int equipmentUnregistCount) {
            this.equipmentUnregistCount = equipmentUnregistCount;
        }

        public int getEquipmentUnactivateCount() {
            return equipmentUnactivateCount;
        }

        public void setEquipmentUnactivateCount(int equipmentUnactivateCount) {
            this.equipmentUnactivateCount = equipmentUnactivateCount;
        }
    }


}