package com.northmeter.equipmentcloud.I;

import com.northmeter.equipmentcloud.bean.SingleSelfCheckingResultResponse;

import java.util.List;

/**
 * Created by dyd on 2019/1/21.
 */

public interface I_ShowSelfCheckingResult {
    void showData();
    void returnSuccess(String message);
    void returnFail(String message);
}
