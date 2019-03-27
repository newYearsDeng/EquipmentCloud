package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.bean.RecordDownLoadResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ShowDownLoad {
    void showData(List<RecordDownLoadResponse.ConcentratorList> datas);
    void returnSuccess(String msg);
    void returnFail(String msg);
}
