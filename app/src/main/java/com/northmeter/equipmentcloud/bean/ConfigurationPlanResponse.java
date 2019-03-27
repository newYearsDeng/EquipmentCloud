package com.northmeter.equipmentcloud.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyd on 2019/3/12.
 * 配置方案列表
 */

public class ConfigurationPlanResponse extends CommonResponse {

    public List<ConfigurationPlanResponse.PlanBean> list;

    public List<ConfigurationPlanResponse.PlanBean> getList() {
        return list;
    }

    public void setList(List<ConfigurationPlanResponse.PlanBean> list) {
        this.list = list;
    }

    public static class PlanBean{
        private String configurationPlanName;
        private String configurationPlanUrl;

        public String getConfigurationPlanName() {
            return configurationPlanName;
        }

        public void setConfigurationPlanName(String configurationPlanName) {
            this.configurationPlanName = configurationPlanName;
        }

        public String getConfigurationPlanUrl() {
            return configurationPlanUrl;
        }

        public void setConfigurationPlanUrl(String configurationPlanUrl) {
            this.configurationPlanUrl = configurationPlanUrl;
        }
    }


}
