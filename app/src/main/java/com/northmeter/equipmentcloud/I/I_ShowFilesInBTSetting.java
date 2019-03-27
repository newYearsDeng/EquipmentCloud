package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.LocalConfigurationPlanBean;

import java.util.List;

/**
 * Created by dyd on 2019/3/15.
 */

public interface I_ShowFilesInBTSetting {
    void showData(List<LocalConfigurationPlanBean.PlanBean> data);
    void returnMessage(String msg);
}
