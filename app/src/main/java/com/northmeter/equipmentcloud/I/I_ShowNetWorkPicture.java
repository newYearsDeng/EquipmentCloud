package com.northmeter.equipmentcloud.I;

import android.graphics.Bitmap;

import com.northmeter.equipmentcloud.bean.NetWorkPictureBean;

/**
 * Created by dyd on 2019/3/27.
 */

public interface I_ShowNetWorkPicture {

    void showData(NetWorkPictureBean.PictureData pictureData);
    void showBitmap(Bitmap bitmap);
    void returnMessage(String message);
}
