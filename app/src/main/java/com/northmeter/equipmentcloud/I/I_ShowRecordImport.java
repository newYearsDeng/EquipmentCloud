package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ShowRecordImport {
    void showData(List<RecordImportResponse.BuildList> datas);
    void returnSuccess(String msg);
    void returnFail(String msg);
}
