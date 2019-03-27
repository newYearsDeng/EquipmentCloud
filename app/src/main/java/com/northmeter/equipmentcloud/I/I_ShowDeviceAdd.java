package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.ProjetTypeResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ShowDeviceAdd {
    void showplanData(List<ConfigurationPlanResponse.PlanBean> datas);
    void showTypeData(List<ProjetTypeResponse.PageList> datas);
    void returnSuccess(String msg);
    void returnFail(String msg);
}
