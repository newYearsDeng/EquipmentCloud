package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectListResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ShowProgectList {
    void showData(List<ProgectListResponse.PageList> datas);
    void returnSuccess();
    void returnFail();
}
