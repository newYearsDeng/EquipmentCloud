package com.northmeter.equipmentcloud.I;


import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;

import java.util.List;

/**
 * Created by dyd on 2019/2/26.
 */

public interface I_ShowBuildList {
   void showData(List<ProgectBuildListResponse.PageList> datas);
    void returnSuccess(String msg);
    void returnFail(String msg);
}
