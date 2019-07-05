package com.northmeter.equipmentcloud.bean;

import java.util.List;

/**
 * Created by dyd on 2019/3/12.
 * 通讯端口
 */

public class CommunicationPortResponse extends CommonResponse {

    public List<CommunicationPortResponse.PortBean> list;

    public List<CommunicationPortResponse.PortBean> getList() {
        return list;
    }

    public void setList(List<CommunicationPortResponse.PortBean> list) {
        this.list = list;
    }

    public static class PortBean{
        private String portName;
        private String portValue;

        public PortBean(String portName,String portValue){
            this.portName = portName;
            this.portValue = portValue;
        }

        public String getPortName() {
            return portName;
        }

        public void setPortName(String portName) {
            this.portName = portName;
        }

        public String getPortValue() {
            return portValue;
        }

        public void setPortValue(String portValue) {
            this.portValue = portValue;
        }
    }


}
