package com.northmeter.equipmentcloud.bluetooth.bt_bluetooth;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.camera.activity.CaptureActivity;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BTMainActivity extends BaseActivity {

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CAMERARESULT = 201;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final String TAG = "BTMainActivity";
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    @BindView(R.id.button_test)
    Button buttonTest;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    public static final String TOAST = "toast";
    private String mConnectedDeviceName = null;
    public static final String DEVICE_NAME = "device_name";
    private String tableNum, tableMac;//水表编号,mac

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_view();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "蓝牙授权失败，请打开此设备的位置权限以发现您的设备", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_test)
    public void onViewClicked() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //method to get Images
                    Intent intent = new Intent();
                    intent.setClass(BTMainActivity.this, CaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(BTMainActivity.this, "Your Permission is needed to get access the camera or location", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CAMERARESULT);
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(BTMainActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        }
    }

    private void init_view() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                    if (data != null) {
                        if (data.hasExtra("result")) {//扫描到水表编号返回数据
                            tableMac = data.getStringExtra("result").toString();
                            System.out.println("tableMac::" + tableMac);

                            foundBTDevice(tableMac);

                            return;
                        }
                    }
                }
                break;
        }
    }


    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (true)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.e(TAG, "已连接：" + mConnectedDeviceName);
                            Toast.makeText(BTMainActivity.this, "成功连接:" + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                            //text_blue_flag.setText("已连接"+mConnectedDeviceName);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.e(TAG, "连接中");
                            //text_blue_flag.setText("连接中");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
                            //text_blue_flag.setText("连接失败");
                            Log.e(TAG, "连接失败");
                            break;
                        case BluetoothChatService.STATE_STOP:
                            //text_blue_flag.setText("关闭连接");
                            Log.e(TAG, "关闭连接");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_DEVICE_NAME:
                    Log.e(TAG, "Connected to " + mConnectedDeviceName);
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_TOAST://连接丢失
                    //text_blue_flag.setText("连接失败");
                    break;
                case MESSAGE_READ:
                    String receive_msg = (String) msg.obj;
                    int state = receive_msg.lastIndexOf("FEFEFEFE68");
                    String stateControl = receive_msg.substring(state + 24, state + 26);//控制字
                    String length = receive_msg.substring(state + 26, state + 28);//长度
                    String stateNum = receive_msg.substring(state + 28, state + 36);//标示符
                    break;
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        if (true)
            Log.e(TAG, "++ ON START ++");
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        } else {
            if (mChatService == null)
                setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (true)
            Log.e(TAG, "+ ON RESUME +");

        if (mChatService != null) {

            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {

                mChatService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatService.stop();

        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothChatService(this, mHandler);
        //BlueTooth_UniqueInstance.getInstance().setBluetoothChatService(mChatService);
    }

    private void foundBTDevice(String mac) {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //If there are paired devices, add each one to the ArrayAdapter
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac);
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.e(TAG, mac + " 蓝牙：" + device.getName() + "/" + device.getAddress());
                //mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                if (device.getAddress().equals(mac)) {
                    mChatService.connect(device);
                    return;
                }
            }
            mBluetoothAdapter.startDiscovery();
        } else {
            String noDevices = "没有蓝牙设备";
            mBluetoothAdapter.startDiscovery();
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG, "蓝牙搜索名字：" + device.getName() + "||" + device.getAddress());
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (device.getAddress().equals(tableMac)) {
                        mChatService.connect(device);
                    }

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                if (mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = getResources().getText(R.string.none_found).toString();
//                }
            }
        }
    };

}
