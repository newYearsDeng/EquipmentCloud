package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.ProgectDeviceDetailResponse;

import java.util.List;

/**
 * Created by dyd on 2019/3/11.
 */

public interface I_ShowDeviceDetail {
    void showData(ProgectDeviceDetailResponse.PageList deviceInfo);
    void returnFail(String msg);
}
