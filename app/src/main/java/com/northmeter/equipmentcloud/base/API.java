package com.northmeter.equipmentcloud.base;

/**
 * Created by dyd on 2018/12/13.
 */

public class API {
    public static String URL_BASE = "http://10.168.1.50:801/machine-cloud";

    /**--账户管理--------------------------GET*/
    public static String login = URL_BASE +"/app/customers/login";

    /**--项目列表查询--------------------------POST*/
    public static String getProjectList = URL_BASE +"/app/project/list";

    /**档案的导入*/
    public static String saveEquipmentRecord = URL_BASE +"app/equipment/save";

    /**项目建筑信息的获取GET*/
    public static String getBuildList = URL_BASE +"/app/project/buildList";

}
