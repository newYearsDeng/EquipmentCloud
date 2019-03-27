package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/1/21.
 */

public interface I_ProgectSelfCheckingPresenter {
    /**建筑列表下载*/
    void getRecordImportBuildList(int projectId);
    /**提交设备自检信息*/
    void saveProgectSelfChecking(int projectId,int recodeId);
}
