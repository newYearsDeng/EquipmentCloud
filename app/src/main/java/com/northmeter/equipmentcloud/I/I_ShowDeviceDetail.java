package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectDeviceDetailResponse;

/**
 * Created by dyd on 2019/3/11.
 */

public interface I_ShowDeviceDetail {
    void showData(ProgectDeviceDetailResponse.PageList deviceInfo);
    void returnMessage(String msg);
}
