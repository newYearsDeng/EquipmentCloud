package com.northmeter.equipmentcloud.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyd on 2019/4/8.
 */

public class DBEquipmentBuildHelper {

    private DBHelper dbHelper;

    public DBEquipmentBuildHelper(Context context) {
        super();
        this.dbHelper = new DBHelper(context);
    }

    /**
     * int projectId;//项目id
     * int recordId;//建筑ID
     * String buildingName;//建筑名称
     * int type;//状态码 在设备子集代替buildingId,1：最后一级 0：不是最后一级
     * int parentId;//父建筑的id
     * int equipmentCount;//建筑下的设备的总数
     * int equipmentUnregistCount;//建筑下的未注册设备的总数
     * int equipmentUnactivateCount;//建筑下的未激活设备的总数
     * */

    public void insert(List<ProgectBuildListResponse.PageList> blueList,int fatherRecordId){
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            for(ProgectBuildListResponse.PageList blueItem:blueList){
                //实例化常量值
                ContentValues cValue = new ContentValues();
                cValue.put("projectId",blueItem.getProjectId());
                cValue.put("recordId",blueItem.getRecordId());
                cValue.put("buildingName",blueItem.getBuildingName());
                cValue.put("type",blueItem.getType());
                cValue.put("parentId",blueItem.getParentId());
                cValue.put("equipmentCount",blueItem.getEquipmentCount());
                cValue.put("equipmentUnregistCount",blueItem.getEquipmentUnregistCount());
                cValue.put("equipmentUnactivateCount",blueItem.getEquipmentUnactivateCount());
                cValue.put("fatherRecordId",fatherRecordId);
                //调用insert()方法插入数据
                db.insert(DBStrings.tableName,null,cValue);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    public boolean delete(String deleteName,String Mac) {
        boolean result = false;
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            //删除条件
            String whereClause = deleteName+"=?";
            //删除条件参数
            String[] whereArgs = {Mac};
            //执行删除
            db.delete(DBStrings.tableName,whereClause,whereArgs);
            result = true;
        }catch (Exception e){
            result = false;
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }

    public int update(List<ProgectBuildListResponse.PageList> blueList,int fatherRecordId) {
        int result = 0;
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            for(ProgectBuildListResponse.PageList blueItem:blueList){
                ContentValues values = new ContentValues();
                //在values中添加内容
                values.put("projectId",blueItem.getProjectId());
                values.put("recordId",blueItem.getRecordId());
                values.put("buildingName",blueItem.getBuildingName());
                values.put("type",blueItem.getType());
                values.put("parentId",blueItem.getParentId());
                values.put("equipmentCount",blueItem.getEquipmentCount());
                values.put("equipmentUnregistCount",blueItem.getEquipmentUnregistCount());
                values.put("equipmentUnactivateCount",blueItem.getEquipmentUnactivateCount());
                values.put("fatherRecordId",fatherRecordId);
                //where 子句 "?"是占位符号，对应后面的"1",
                String whereClause="projectId=? and fatherRecordId=?";
                String [] whereArgs = {String.valueOf(blueItem.getProjectId()),String.valueOf(fatherRecordId)};
                //参数1 是要更新的表名
                //参数2 是一个ContentValeus对象
                //参数3 是where子句
                result = db.update(DBStrings.tableName, values, whereClause, whereArgs);
            }
            //实例化内容值
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return result;
    }

    public List<ProgectBuildListResponse.PageList> query() {
        Cursor cursor = null;
        List<ProgectBuildListResponse.PageList> blueList = new ArrayList<>();
        SQLiteDatabase db = null;
        try{
            db = dbHelper.getWritableDatabase();
            //查询获得游标
            cursor = db.query (DBStrings.tableName,null,null,null,null,null,null);
            //判断游标是否为空
            if(cursor.moveToFirst()){
                //遍历游标
                for(int i=0;i<cursor.getCount();i++){
                    cursor.move(i);
                    //获得ID
                    int id = cursor.getInt(0);
                    //获得projectId
                    int projectId =cursor.getInt(1);
                    //获得recordId
                    int recordId =cursor.getInt(2);
                    //获得buildingName
                    String buildingName =cursor.getString(3);
                    //获得type
                    int type =cursor.getInt(4);
                    //获得parentId
                    int parentId =cursor.getInt(5);
                    //获得equipmentCount
                    int equipmentCount =cursor.getInt(6);
                    //获得equipmentUnregistCount
                    int equipmentUnregistCount =cursor.getInt(7);
                    //获得equipmentUnactivateCount
                    int equipmentUnactivateCount=cursor.getInt(8);

                    int fatherRecordId = cursor.getInt(9);

                    blueList.add(new ProgectBuildListResponse().new PageList( projectId, recordId, buildingName,
                            type, parentId , equipmentCount , equipmentUnregistCount , equipmentUnactivateCount,fatherRecordId));
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

    /**根据projectId,fatherRecordId(0：第一个建筑)类型  */
    public List<ProgectBuildListResponse.PageList> queryByCondit(String selectClause,String selectOut,String selectionArgs1,String selectionArgs2) {
        Cursor cursor = null;
        List<ProgectBuildListResponse.PageList> blueList = new ArrayList<>();
        SQLiteDatabase db = null;
        //projectId=? and fatherRecordId=?
        String whereClause = selectClause+"=? and " + selectOut+"?";
        String [] whereArgs = {selectionArgs1,selectionArgs2};
        try{
            db = dbHelper.getWritableDatabase();
            //查询获得游标
            cursor = db.query (DBStrings.tableName,null,whereClause,whereArgs,null,null,null);
            //判断游标是否为空
            if(cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    //获得ID
                    int id = cursor.getInt(0);
                    //获得projectId
                    int projectId =cursor.getInt(1);
                    //获得recordId
                    int recordId =cursor.getInt(2);
                    //获得buildingName
                    String buildingName =cursor.getString(3);
                    //获得type
                    int type =cursor.getInt(4);
                    //获得parentId
                    int parentId =cursor.getInt(5);
                    //获得equipmentCount
                    int equipmentCount =cursor.getInt(6);
                    //获得equipmentUnregistCount
                    int equipmentUnregistCount =cursor.getInt(7);
                    //获得equipmentUnactivateCount
                    int equipmentUnactivateCount=cursor.getInt(8);
                    int fatherRecordId = cursor.getInt(9);

                    blueList.add(new ProgectBuildListResponse().new PageList( projectId, recordId, buildingName,
                            type, parentId , equipmentCount , equipmentUnregistCount , equipmentUnactivateCount,fatherRecordId));
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


    /**多条件类型查询*/
    public List<ProgectBuildListResponse.PageList> queryByConditMore(String selectClause,String[] selectionArgs) {
        Cursor cursor = null;
        List<ProgectBuildListResponse.PageList> blueList = new ArrayList<>();
        SQLiteDatabase db = null;
        String whereClause = selectClause;
        try{
            db = dbHelper.getWritableDatabase();
            //查询获得游标
            cursor = db.query (DBStrings.tableName,null,whereClause,selectionArgs,null,null,null);
            //判断游标是否为空
            if(cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    //获得ID
                    int id = cursor.getInt(0);
                    //获得projectId
                    int projectId =cursor.getInt(1);
                    //获得recordId
                    int recordId =cursor.getInt(2);
                    //获得buildingName
                    String buildingName =cursor.getString(3);
                    //获得type
                    int type =cursor.getInt(4);
                    //获得parentId
                    int parentId =cursor.getInt(5);
                    //获得equipmentCount
                    int equipmentCount =cursor.getInt(6);
                    //获得equipmentUnregistCount
                    int equipmentUnregistCount =cursor.getInt(7);
                    //获得equipmentUnactivateCount
                    int equipmentUnactivateCount=cursor.getInt(8);
                    int fatherRecordId = cursor.getInt(9);

                    blueList.add(new ProgectBuildListResponse().new PageList( projectId, recordId, buildingName,
                            type, parentId , equipmentCount , equipmentUnregistCount , equipmentUnactivateCount,fatherRecordId));
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
