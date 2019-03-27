package com.northmeter.equipmentcloud.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.northmeter.equipmentcloud.I.IShowSMainMessage;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_UniqueInstance;
import com.northmeter.equipmentcloud.bluetooth.GetBlueEntity;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothConnectionClient;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothScanClient;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.GattCode;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BluetoothChatService;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BtDeviceListActivity;
import com.northmeter.equipmentcloud.camera.activity.CaptureActivity;
import com.northmeter.equipmentcloud.fragment.Fragment_NBMeter_Install;
import com.northmeter.equipmentcloud.fragment.Fragment_NBMeter_Setting;
import com.northmeter.equipmentcloud.utils.Udp_Help;
import com.northmeter.equipmentcloud.widget.EmptyFragmentPagerAdapter;
import com.northmeter.equipmentcloud.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.DEVICE_NAME;
import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.TOAST;

/**
 * Created by dyd on 2019/3/11.
 * 本地设置——NB摄像水表
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class LocationSet_NBDevice extends BaseActivity implements IShowSMainMessage {
    private final static int FIND_BLUETOOTH_CODE = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    @BindView(R.id.vp_empty)
    NoScrollViewPager vpEmpty;
    @BindView(R.id.tl_empty)
    TabLayout tlEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    private String TAG = getClass().getSimpleName();
    public static final String DEVICE_NAME = "device_name";
    private BluetoothConnectionClient mConnectionClient;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    private final int FOUND_DEVICE = 0X01;
    private final int DISCONNECTED = 0X02;
    private final int FOUND_SERVICE = 0X03;
    private final int WRITE_SUCCESS = 0X04;
    private final int WRITE_FAILED = 0X05;
    private final int RECONNECT = 0x06;
    private final int RECEIVE = 0x07;
    private final int SEND = 0x08;
    private final int CONNECTED = 0X09;
    private final int BLUEM_ESSAGE = 0x0a;
    private String receive_msg = "";
    private String tableMac;//mac

    private DataChange dataChange;
    private List<Fragment> fragments = new ArrayList<>();
    private String[] mTitles;
    private EmptyFragmentPagerAdapter adapter;
    private GetBlueEntity getBlueEntity;
    private String mConnectedDeviceName = null;
    private boolean highBt = true;//高速蓝牙

    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_set_nb_device;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        type =  getIntent().getIntExtra("type",0);
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("本地配置");
        tvRightText.setText("蓝牙");
    }

    @Override
    public void initData() {
        super.initData();
        getBlueEntity = new GetBlueEntity(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(type == 0){
            fragments.add(Fragment_NBMeter_Install.newInstance(0));
            mTitles = new String[]{"安装测试"};
        }else{
            fragments.add(Fragment_NBMeter_Setting.newInstance(1));
            mTitles = new String[]{"参数设置"};
        }
        adapter = new EmptyFragmentPagerAdapter(getSupportFragmentManager(), fragments, mTitles);
        vpEmpty.setAdapter(adapter);
        tlEmpty.setupWithViewPager(vpEmpty);
        vpEmpty.setOffscreenPageLimit(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatService !=null){
            mChatService.stop();
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");
        mChatService = new BluetoothChatService(this, btHandler);
        BlueTooth_UniqueInstance.getInstance().setBluetoothChatService(mChatService);
    }

    @OnClick({R.id.tv_right_text,R.id.btn_tb_back})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.tv_right_text:
                if(highBt){//高速蓝牙
                    Intent serverIntent = new Intent(this, BtDeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }else{//低速蓝牙
                    startActivityForResult(new Intent(this, DeviceListActivity.class)
                        , DeviceListActivity.REQUEST_DEVICE);
                }

                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FIND_BLUETOOTH_CODE://低功耗蓝牙
                BluetoothConnectionClient c = new BluetoothConnectionClient(
                        (BluetoothDevice) data.getParcelableExtra(DeviceListActivity.DATA_DEVICE)
                        , this, mGattCallback);

                Message msg = mHandler.obtainMessage(FOUND_DEVICE);
                msg.obj = c;
                mHandler.sendMessage(msg);
                break;
            case REQUEST_CONNECT_DEVICE://高速蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            BtDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);

//                  SharedPreferences.Editor editor = sp.edit();
//                  editor.putString("BlueAddress",address);
//                  editor.commit();
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    Log.d(TAG, "BT not enabled");
                    showMsg("该功能需要打开手机蓝牙");
                }
                break;
        }
    }


    private final Handler btHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (true)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            dataChange.setDataChange("已连接"+mConnectedDeviceName);
                           showMsg("连接成功");
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(true);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            dataChange.setDataChange("连接中...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                            showMsg("连接失败");
                            dataChange.setDataChange("连接失败");
                            break;
                        case BluetoothChatService.STATE_STOP:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                            dataChange.setDataChange("断开连接");
                            System.out.println("断开连接");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String readBuf = (String) msg.obj;
                    System.out.println("接收到的数据转换后："+readBuf);
                    getBlueEntity.transmitBlueMsg(readBuf);
                    break;
                case MESSAGE_DEVICE_NAME:
                    //save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_TOAST://连接丢失
                    BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                    dataChange.setDataChange("连接断开");
                    showMsg(msg.getData().getString(TOAST));
                    break;
            }
        }
    };


    //-*-----------------------------------------------------------
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTED://连接成功
                    Toast.makeText(LocationSet_NBDevice.this, "连接成功", Toast.LENGTH_LONG).show();
                    dataChange.setDataChange("连接成功");
                    break;
                case DISCONNECTED://断开连接
                    Log.e(TAG, "连接断开...");
                    dataChange.setDataChange("连接断开");
                    Toast.makeText(LocationSet_NBDevice.this, "连接断开！", Toast.LENGTH_LONG).show();
                    break;
                case RECONNECT:
                    if (mConnectionClient != null) {
                        mConnectionClient.connect();
                    }/*else{
                        if(mScanClient!=null){
                            mScanClient.startScan();
                        }
                    }*/
                    break;
                case RECEIVE:
                    String data = (String) msg.obj;
                    break;
                case SEND:
                    break;
                case FOUND_DEVICE:
                    if (mConnectionClient != null) {
                        mConnectionClient.disconnect();
                    }
                    System.out.println("找到蓝牙并进行连接 ");
                    mConnectionClient = (BluetoothConnectionClient) msg.obj;
                    mConnectionClient.connect();

                    break;
                case BLUEM_ESSAGE:
                    String blueMsg = (String) msg.obj;
                    if (blueMsg.equals("success")) {
                        Toast.makeText(LocationSet_NBDevice.this, "设置成功", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (blueMsg.equals("fail")) {
                        Toast.makeText(LocationSet_NBDevice.this, "操作失败", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;

            }
        }
    };
    @SuppressLint("NewApi")
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    gatt.discoverServices();
                }
            }
            if (newState == BluetoothGatt.STATE_DISCONNECTED || status == 133) {
                Log.w(TAG, "onConnectionStateChange: disconnected");
                mHandler.sendEmptyMessage(DISCONNECTED);
                mHandler.sendEmptyMessage(RECONNECT);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            BluetoothGattService service = gatt.getService(GattCode.FFF_SERVICE);
            System.out.println("Uuid: " + service.getUuid());

            mConnectionClient.addCharacteristic(3, service.getCharacteristic(GattCode.FFF_3));
            mConnectionClient.addCharacteristic(4, service.getCharacteristic(GattCode.FFF_4));

            Log.w(TAG, "onServicesDiscovered: ");
            mConnectionClient.setCharacteristicNotification(GattCode.DESCRIPTOR,
                    3, true);

            mHandler.sendEmptyMessage(FOUND_SERVICE);

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.w(TAG, "onCharacteristicWrite: status=" + status);
            if (status == 0) {
                Message msg = mHandler.obtainMessage(SEND);
                msg.obj = characteristic.getValue();
                mHandler.sendMessage(msg);
            } else {
                mHandler.sendEmptyMessage(WRITE_FAILED);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();
            receive_msg = receive_msg + Udp_Help.bytesToHexString(value);
            Log.e(TAG, receive_msg);
            handleBtBlueMessage(receive_msg);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite: status=" + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                mHandler.sendEmptyMessage(CONNECTED);
            }
        }
    };




    /**
     * 处理蓝牙接收到的数据 FEFEFE6821000016200168 91 C300 343310EF 343D3E(图片编号，该包序号，总包数)
     */
    private String handleBtBlueMessage(String data) {//0080FEFEFE6821000016200168940000BC16C000FCF8
        int state = data.lastIndexOf("FEFEFE68");
        int state_lenth = state + 28;//从FEFEFE68到标识码的长度
        String ditle = data.substring(data.length() - 2, data.length());//检查最后的字节是否为16

        if (state >= 0 && data.length() > state_lenth && ditle.equals("16")) {
            //String stateNum = data.substring(state+28,state+36);//标识码
            String length_1 = data.substring(state + 24, state + 26);//长度 c3
            String length_2 = data.substring(state + 26, state + 28);//长度 00
            System.out.println(length_1 + "/" + length_2);
            int len = Integer.valueOf(length_2 + length_1, 16);//字符串内长度字节

            if (data.substring(state + 28, data.length() - 4).length() / 2 == len) {
                //mHandler.obtainMessage(BluetoothChat.MESSAGE_READ,data.substring(state,data.length())).sendToTarget();
                getBlueEntity.transmitBlueMsg(data.substring(state, data.length()));
                receive_msg = "";
            }
        }
        return null;
    }

    public void sendBroad(String action, String str) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("msg", str);
        sendBroadcast(intent);
    }

    @Override
    public void showMainMsg(String message) {
        sendBroad("Intent.NBMeter_Install", message);
    }

    @Override
    public void showSettingMsg(String message) {
        sendBroad("Intent.NBMeter_Setting", message);
    }



    public interface DataChange {
        void setDataChange(String receive);
    }

    public void setData(DataChange dataChange) {
        this.dataChange = dataChange;
    }

}
