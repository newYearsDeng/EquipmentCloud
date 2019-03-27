package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/10.
 * 档案导入时查询建筑信息
 */

public class RecordImportResponse extends CommonResponse{
    public List<BuildList> list;

    public List<BuildList> getList() {
        return list;
    }

    public void setList(List<BuildList> list) {
        this.list = list;
    }

    public class BuildList implements Serializable{
        private int recordId;
        private String buildingName;
        private int type;
        private int equipmentId;
        private int projectId;
        private String equipmentName;
        private List<BuildList> child;

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

        public int getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(int equipmentId) {
            this.equipmentId = equipmentId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getEquipmentName() {
            return equipmentName;
        }

        public void setEquipmentName(String equipmentName) {
            this.equipmentName = equipmentName;
        }

        public List getChild() {
            return child;
        }

        public void setChild(List<BuildList> child) {
            this.child = child;
        }
    }



}

/**
 recordId 	是 	Integer 	建筑id
 buildingName 	是 	String 	建筑名称 在设备子集代替itemTypeId
 type 	是 	integer 	状态码 在设备子集代替buildingId
 equipmentId 	是 	integer 	设备id
 projectId 	是 	integer 	项目id
 equipmentName 	是 	String 	设备名称
 child 	是 	List 	子集建筑或者设备
 */
