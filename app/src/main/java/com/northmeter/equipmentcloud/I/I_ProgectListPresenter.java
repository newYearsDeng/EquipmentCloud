package com.northmeter.equipmentcloud.I;

/**
 * Created by dyd on 2019/1/4.
 */

public interface I_ProgectListPresenter {
    //page 	页码  询全部，统一传0
    //limit 条数  查询全部，统一传0
    void getProgectList(int status,String page,String limit);
}
