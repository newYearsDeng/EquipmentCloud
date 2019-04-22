package com.northmeter.equipmentcloud.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.northmeter.equipmentcloud.activity.LocationSet_NBDevice;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothConnectionClient;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothScanClient;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.GattCode;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BluetoothChatService;
import com.northmeter.equipmentcloud.utils.Udp_Help;

/**
 * Created by dyd on 2019/3/11.
 */

public class BlueTooth_UniqueInstance{
    private static BlueTooth_UniqueInstance uniqueInstance=null;
    /**蓝牙对象*/
    private BluetoothConnectionClient connectionClient;

    /**bt蓝牙对象*/
    private static BluetoothChatService bluetoothChatService = null;

    /**表号*/
    private String tableNum;

    /**state判断是否结束进度条 state 0 ：表示安装测试界面的操作，1,2表示设置界面的操作，3：表示主从读取模式的拍照*/
    private int state = 0;



    public BlueTooth_UniqueInstance() {

    }

    public static BlueTooth_UniqueInstance getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new BlueTooth_UniqueInstance();
        }
        return uniqueInstance;
    }

    public BluetoothConnectionClient getConnectionClient() {

        return connectionClient;
    }
    public void setConnectionClient(BluetoothConnectionClient connectionClient) {
        this.connectionClient = connectionClient;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static BluetoothChatService getBluetoothChatService() {
        return bluetoothChatService;
    }

    public static void setBluetoothChatService(BluetoothChatService bluetoothChatService) {
        BlueTooth_UniqueInstance.bluetoothChatService = bluetoothChatService;
    }


    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

}
