package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/3/1.
 */

public interface I_ProgectDeviceAddPresenter {
    void addBuildingequipment(String equipmentName,String itemTypeId,int buildingId,int projectId,String key,
    String terminalPort,String ipcNum,String concentratorName,String collectorName,String configurationPlanName);

    void getConfigurationPlan(String projectName);

    void getProjetType();
}
