package com.northmeter.equipmentcloud.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dyd on 2019/4/8.
 */

public class DBHelper extends SQLiteOpenHelper{


    public DBHelper(Context context) {
        // 创建数据库
        super(context, DBStrings.DBName, null, DBStrings.DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * recordId 	是 	Int 	建筑设备配置表的Id、设备的recordId
         * equipmentId 	是 	String 	设备id
         * equipmentNum 	是 	String 	设备编号
         * itemTypeId 	是 	int 	产品型号
         * equipmentAddress 	是 	String 	设备安装地址，是地址字符串
         * isUpdata 是否上传 0 false  1 true
         * */
        System.out.println("创建表===============");
        db.execSQL("create table "+DBStrings.dbTableRegist+"(_id integer primary key autoincrement,recordId integer,equipmentId text,equipmentNum text,itemTypeId text,equipmentAddress text,isUpdata integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
