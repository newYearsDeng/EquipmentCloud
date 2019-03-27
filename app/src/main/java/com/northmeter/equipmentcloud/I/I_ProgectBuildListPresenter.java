package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/2/26.
 */

public interface I_ProgectBuildListPresenter {
    /**获取建筑列表*/
    void getBuildList(int projectId,int parentId);
    /**项目自检*/
    void selfChecking(int projectId,int recodeId);
}
