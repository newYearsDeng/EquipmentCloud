package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.RecordDownLoadResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/17.
 */

public interface I_RecordDownLoadPresenter {

    void getRecordDownLoadList(int projectId, String concentratorName);
    void upRecordDownLoadList(int projectId ,List<RecordDownLoadResponse.ConcentratorList> datas);
}
