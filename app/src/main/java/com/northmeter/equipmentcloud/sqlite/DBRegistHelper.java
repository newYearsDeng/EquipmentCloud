package com.northmeter.equipmentcloud.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.northmeter.equipmentcloud.bean.DBRegistBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyd on 2019/4/8.
 * 设备注册任务存储
 */

public class DBRegistHelper {

    private DBHelper dbHelper;

    public DBRegistHelper(Context context) {
        super();
        this.dbHelper = new DBHelper(context);
    }

    /**
     * recordId 	是 	Int 	建筑设备配置表的Id、设备的recordId
     * equipmentId 	是 	String 	设备id
     * equipmentNum 	是 	String 	设备编号
     * itemTypeId 	是 	int 	产品型号
     * equipmentAddress 	是 	String 	设备安装地址，是地址字符串
     * isUpdata 是否上传了
     * */

    public void insert(DBRegistBean registBean){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            //deleteIn(db,registBean.getRecordId());
            //实例化常量值
            ContentValues cValue = new ContentValues();
            cValue.put("recordId",registBean.getRecordId());
            cValue.put("equipmentId",registBean.getEquipmentId());
            cValue.put("equipmentNum",registBean.getEquipmentNum());
            cValue.put("itemTypeId",registBean.getItemTypeId());
            cValue.put("equipmentAddress",registBean.getEquipmentAddress());
            cValue.put("isUpdata",registBean.getIsUpdata());
            //调用insert()方法插入数据
            db.insert(DBStrings.dbTableRegist,null,cValue);

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public boolean deleteIn(SQLiteDatabase db,int recordId) {
        boolean result = false;
        try{
            //删除条件
            String whereClause = "recordId=?";
            //删除条件参数
            String[] whereArgs = {String.valueOf(recordId)};
            //执行删除
            db.delete(DBStrings.dbTableRegist,whereClause,whereArgs);
            result = true;
        }catch (Exception e){
            result = false;
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }


    public boolean delete(int recordId) {
        boolean result = false;
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            //删除条件
            String whereClause = "recordId=?";
            //删除条件参数
            String[] whereArgs = {String.valueOf(recordId)};
            //执行删除
            db.delete(DBStrings.dbTableRegist,whereClause,whereArgs);
            result = true;
        }catch (Exception e){
            result = false;
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }

    public int update(DBRegistBean registBean) {
        int result = 0;
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            ContentValues cValue = new ContentValues();
            //在values中添加内容
            cValue.put("recordId",registBean.getRecordId());
            cValue.put("equipmentId",registBean.getEquipmentId());
            cValue.put("equipmentNum",registBean.getEquipmentNum());
            cValue.put("itemTypeId",registBean.getItemTypeId());
            cValue.put("equipmentAddress",registBean.getEquipmentAddress());
            cValue.put("isUpdata",registBean.getIsUpdata());
            //where 子句 "?"是占位符号，对应后面的"1",
            String whereClause="recordId=?";
            String [] whereArgs = {String.valueOf(registBean.getRecordId())};
            //参数1 是要更新的表名
            //参数2 是一个ContentValeus对象
            //参数3 是where子句
            result = db.update(DBStrings.dbTableRegist, cValue, whereClause, whereArgs);
            if(result==0){
                db.insert(DBStrings.dbTableRegist,null,cValue);
            }

            //实例化内容值
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }

    public List<DBRegistBean> query() {
        Cursor cursor = null;
        List<DBRegistBean> blueList = new ArrayList<>();
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            //查询获得游标
            cursor = db.query (DBStrings.dbTableRegist,null,null,null,null,null,null);
            //判断游标是否为空
            if(cursor.moveToFirst()){
                //遍历游标
                for(int i=0;i<cursor.getCount();i++){
                    cursor.move(i);
                    //获得ID
                    int id = cursor.getInt(0);
                    int projectId =cursor.getInt(1);
                    String equipmentId =cursor.getString(2);
                    String equipmentNum =cursor.getString(3);
                    String itemTypeId =cursor.getString(4);
                    String equipmentAddress =cursor.getString(5);
                    int isUpdata =cursor.getInt(6);
                    blueList.add(new DBRegistBean(projectId, equipmentId, equipmentNum,
                            itemTypeId, equipmentAddress , isUpdata));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally {
            cursor.close();
            db.close();
        }
        return blueList;
    }

    /**根据类型  isUpdata=0*/
    public List<DBRegistBean> queryByCondit(String selectClause,String selectionArgs) {
        Cursor cursor = null;
        List<DBRegistBean> blueList = new ArrayList<>();
        SQLiteDatabase db = null;
        //projectId=? and fatherRecordId=?
        String whereClause = selectClause+"=?";
        String [] whereArgs = {selectionArgs};
        try{
            db = dbHelper.getWritableDatabase();
            //查询获得游标
            cursor = db.query (DBStrings.dbTableRegist,null,whereClause,whereArgs,null,null,null);
            //判断游标是否为空
            if(cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    //获得ID
                    int id = cursor.getInt(0);
                    int projectId =cursor.getInt(1);
                    String equipmentId =cursor.getString(2);
                    String equipmentNum =cursor.getString(3);
                    String itemTypeId =cursor.getString(4);
                    String equipmentAddress =cursor.getString(5);
                    int isUpdata =cursor.getInt(6);
                    blueList.add(new DBRegistBean(projectId, equipmentId, equipmentNum,
                            itemTypeId, equipmentAddress , isUpdata));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return blueList;
        }finally {
            cursor.close();
            db.close();
        }
        return blueList;
    }

}
