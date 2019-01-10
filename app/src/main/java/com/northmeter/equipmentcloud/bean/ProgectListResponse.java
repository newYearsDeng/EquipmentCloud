package com.northmeter.equipmentcloud.bean;

import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 * 项目列表
 */

public class ProgectListResponse extends CommonResponse{

    public PageInfo page;

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public class PageInfo{
        private long totalCount;
        private long pageSize;
        private long totalPage;
        private long currPage;
        private List<PageList> list;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public long getPageSize() {
            return pageSize;
        }

        public void setPageSize(long pageSize) {
            this.pageSize = pageSize;
        }

        public long getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(long totalPage) {
            this.totalPage = totalPage;
        }

        public long getCurrPage() {
            return currPage;
        }

        public void setCurrPage(long currPage) {
            this.currPage = currPage;
        }

        public List<PageList> getList() {
            return list;
        }

        public void setList(List<PageList> list) {
            this.list = list;
        }
    }

    public class PageList{
        private int recordId;
        private String projectName;
        private String createEmpName;
        private String createTime;
        private List<PrincipalMsgList> principalMsg;

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

        public String getCreateEmpName() {
            return createEmpName;
        }

        public void setCreateEmpName(String createEmpName) {
            this.createEmpName = createEmpName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<PrincipalMsgList> getPrincipalMsg() {
            return principalMsg;
        }

        public void setPrincipalMsg(List<PrincipalMsgList> principalMsg) {
            this.principalMsg = principalMsg;
        }
    }


    public class PrincipalMsgList{
        private String userId;
        private String roleId;
        private String userName;
        private String personName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
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