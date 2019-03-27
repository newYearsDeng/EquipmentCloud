package com.northmeter.equipmentcloud.I;


import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;

import java.util.List;

/**
 * Created by dyd on 2019/2/26.
 */

public interface I_ShowBuildDevice {
   void showData(List<ProgectBuildDeviceResponse.PageList> datas);
    void returnSuccess(String msg ,int state);
    void returnFail(String msg);
}
