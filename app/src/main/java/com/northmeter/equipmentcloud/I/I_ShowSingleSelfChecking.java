package com.northmeter.equipmentcloud.I;

import android.graphics.Bitmap;

import com.northmeter.equipmentcloud.bean.SingleSelfCheckingResultResponse;

/**
 * Created by dyd on 2019/3/22.
 */

public interface I_ShowSingleSelfChecking {
    void showData(SingleSelfCheckingResultResponse.ResultBean resultBean);
    void showBitmap(Bitmap bitmap);
    void returnSuccess(String message);
    void returnFail(String message);
}
