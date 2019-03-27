package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/1/17.
 *集中器档案档案下发
 */

public class RecordDownLoadResponse extends CommonResponse{
    public ConcentratorPage page;

    public ConcentratorPage getPage() {
        return page;
    }

    public void setPage(ConcentratorPage page) {
        this.page = page;
    }

    public class ConcentratorPage implements Serializable{
        public String totalCount;
        public String pageSize;
        public String totalPage;
        public String currPage;
        private List<ConcentratorList> list;

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getCurrPage() {
            return currPage;
        }

        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        public List<ConcentratorList> getList() {
            return list;
        }

        public void setList(List<ConcentratorList> list) {
            this.list = list;
        }
    }


    public class ConcentratorList implements Serializable{
        public boolean isCheck = false;
        public int recordId;
        public String concentratorName;
        public String concentratorNum;
        public String itemType;
        public String localIp;
        public String stationIp;
        public String stationPort;
        public String isOnline;
        public String digitalSignal;
        public String signalPicUrl;
        public String signalDescribe;
        public String simNum;
        public String upCommunicate;
        public String usedState;
        public String installAddress;
        public String ipcId;
        public int projectId;
        public String etcetera;
        public String createTime;

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getConcentratorName() {
            return concentratorName;
        }

        public void setConcentratorName(String concentratorName) {
            this.concentratorName = concentratorName;
        }

        public String getConcentratorNum() {
            return concentratorNum;
        }

        public void setConcentratorNum(String concentratorNum) {
            this.concentratorNum = concentratorNum;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getLocalIp() {
            return localIp;
        }

        public void setLocalIp(String localIp) {
            this.localIp = localIp;
        }

        public String getStationIp() {
            return stationIp;
        }

        public void setStationIp(String stationIp) {
            this.stationIp = stationIp;
        }

        public String getStationPort() {
            return stationPort;
        }

        public void setStationPort(String stationPort) {
            this.stationPort = stationPort;
        }

        public String getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(String isOnline) {
            this.isOnline = isOnline;
        }

        public String getDigitalSignal() {
            return digitalSignal;
        }

        public void setDigitalSignal(String digitalSignal) {
            this.digitalSignal = digitalSignal;
        }

        public String getSignalPicUrl() {
            return signalPicUrl;
        }

        public void setSignalPicUrl(String signalPicUrl) {
            this.signalPicUrl = signalPicUrl;
        }

        public String getSignalDescribe() {
            return signalDescribe;
        }

        public void setSignalDescribe(String signalDescribe) {
            this.signalDescribe = signalDescribe;
        }

        public String getSimNum() {
            return simNum;
        }

        public void setSimNum(String simNum) {
            this.simNum = simNum;
        }

        public String getUpCommunicate() {
            return upCommunicate;
        }

        public void setUpCommunicate(String upCommunicate) {
            this.upCommunicate = upCommunicate;
        }

        public String getUsedState() {
            return usedState;
        }

        public void setUsedState(String usedState) {
            this.usedState = usedState;
        }

        public String getInstallAddress() {
            return installAddress;
        }

        public void setInstallAddress(String installAddress) {
            this.installAddress = installAddress;
        }

        public String getIpcId() {
            return ipcId;
        }

        public void setIpcId(String ipcId) {
            this.ipcId = ipcId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getEtcetera() {
            return etcetera;
        }

        public void setEtcetera(String etcetera) {
            this.etcetera = etcetera;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
