package com.northmeter.equipmentcloud.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.northmeter.equipmentcloud.I.IShowSMainMessage;
import com.northmeter.equipmentcloud.base.MyApplication;
import com.northmeter.equipmentcloud.bean.EvenBusBean;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothConnectionClient;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BluetoothChatService;
import com.northmeter.equipmentcloud.enumBean.EvenBusEnum;
import com.northmeter.equipmentcloud.utils.Udp_Help;

import org.greenrobot.eventbus.EventBus;

import static com.northmeter.equipmentcloud.activity.LocationSet_NBDevice.DEVICE_NAME;
import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.TOAST;

/**
 * Created by dyd on 2019/3/11.
 */

public class BlueTooth_ConnectHelper implements IShowSMainMessage {
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Local Bluetooth adapter
    private static BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private static BluetoothChatService mChatService = null;

    private static BlueTooth_ConnectHelper uniqueInstance=null;

    public BlueTooth_ConnectHelper() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mChatService = new BluetoothChatService(MyApplication.getContext(), btHandler);
    }

    public static BlueTooth_ConnectHelper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new BlueTooth_ConnectHelper();
        }
        return uniqueInstance;
    }


    public void blueToothConnect(String address){
        BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        // Attempt to connect to the device
         mChatService.connect(device);
    }


    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
        this.mBluetoothAdapter = mBluetoothAdapter;
    }

    public BluetoothChatService getmChatService() {
        return mChatService;
    }

    public void setmChatService(BluetoothChatService mChatService) {
        this.mChatService = mChatService;
    }

    private Handler btHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (true)
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            System.out.println("连接成功");
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(true);
                            setmChatService(mChatService);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            System.out.println("连接中");
                            //dataChange.setDataChange("连接中...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                            System.out.println("连接失败");
                            //showMsg("连接失败");
                            //dataChange.setDataChange("连接失败");
                            break;
                        case BluetoothChatService.STATE_STOP:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                            //dataChange.setDataChange("断开连接");
                            System.out.println("断开连接");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String readBuf = (String) msg.obj;
                    System.out.println("接收到的数据转换后："+readBuf);
                    //getBlueEntity.transmitBlueMsg(readBuf);
                    new GetBlueEntity(new IShowSMainMessage() {
                        @Override
                        public void showMainMsg(String message) {
                            switch(BlueTooth_UniqueInstance.getInstance().getState()){
                                case 0:
                                    sendEventBus(EvenBusEnum.EvenBus_NBMeter_Install.getEvenName(), message);
                                    break;
                                case 1:
                                case 2:
                                    sendEventBus(EvenBusEnum.EvenBus_NBMeter_Setting.getEvenName(), message);
                                    break;
                                case 10:
                                    sendEventBus(EvenBusEnum.EvenBus_WaterMeterPicShow.getEvenName(), message);
                                    break;
                            }
                        }

                        @Override
                        public void showSettingMsg(String message) {
                            sendEventBus(EvenBusEnum.EvenBus_NBMeter_Setting.getEvenName(), message);
                        }
                    }).transmitBlueMsg(readBuf);
                    break;
                case MESSAGE_DEVICE_NAME:
                    //save the connected device's name
                    String  mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    System.out.println(mConnectedDeviceName);
                    break;
                case MESSAGE_TOAST://连接丢失
                    BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                    System.out.println("连接断开");
                    //dataChange.setDataChange("连接断开");
                    //showMsg(msg.getData().getString(TOAST));
                    break;
            }
        }
    };

    public void sendEventBus(String topic, String data) {
        EvenBusBean bean = new EvenBusBean();
        bean.setTopic(topic);
        bean.setData(data);
        EventBus.getDefault().post(bean);
    }

    @Override
    public void showMainMsg(String message) {

    }

    @Override
    public void showSettingMsg(String message) {

    }
}
