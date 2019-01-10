package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ShowReturnData {
    void returnSuccess(int code,String message);
    void returnFail(int code,String message);
}
