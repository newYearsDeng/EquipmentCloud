package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 * 项目列表
 */

public class ProgectListResponse extends CommonResponse {

    private List<PageList> list;

    public List<PageList> getList() {
        return list;
    }

    public void setList(List<PageList> list) {
        this.list = list;
    }

    public class PageList implements Serializable{
        private int recordId;//项目id
        private String projectName;//项目名称
        private String createTime;//创建时间，格式：YYMMDDhhmm
        private int equipmentCount;//设备的总数
        private int equipmentUnregistCount;//未注册的设备
        private int equipmentUnactivateCount;//未激活的设备

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEquipmentCount() {
            return equipmentCount;
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




/**
 *     {
 "msg":"success",
 "code":0,
 "page":{
     "totalCount":6,//总记录数
     "pageSize":1,//页面条数
     "totalPage":6,//总的页数
     "currPage":1,//当前页
     "list":{
         "recordId":"1",//项目id
         "projectName":"北电",//项目名称
         "principalMsg":[//负责人信息
             {
             "userId":"1",
             "roleId":"1",
             "userName":"账号",
             "personName":"姓名"
             }
        ],
     "createEmpName":"1",
     "createTime":"2018-05-15 10:51:16"
     }
     }
 }*/