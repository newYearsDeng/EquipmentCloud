package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/3/22.
 */

public interface I_ProgectSingleSelfCheckingResultPresenter {

    void getDeviceSelfCheckingResult(int recordId);
    void downloadFiles(String Url);
}
