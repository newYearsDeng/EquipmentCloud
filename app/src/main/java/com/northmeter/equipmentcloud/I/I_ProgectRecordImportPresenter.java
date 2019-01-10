package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/1/10.
 */

public interface I_ProgectRecordImportPresenter {
    /**建筑列表下载*/
    void getRecordImportList(int projectId);

    /**上传档案*/
    void saveProgectRecord(String equipmentName,String equipmentId,int itemTypeId,String address,int projectId);
}
