package com.northmeter.equipmentcloud.bluetooth;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.BlueToothDeviceBean;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothScanClient;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.view.DeviceListItemView;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class AllScanDeviceListActivity extends BaseActivity implements View.OnClickListener, BluetoothAdapter.LeScanCallback {
    public static final String DATA_DEVICE = "DEVICE";
    public static final String DATA_TYPE = "TYPE";
    String TAG = getClass().getSimpleName();

    public static final int REQUEST_DEVICE = 0X01;
    private ListView lv;
    private TextView confirm;
    private ImageView cancel;

    private DeviceAdapter adapter;
    private ArrayList<BlueToothDeviceBean> devices;
    private BluetoothDevice checkedDevice;
    private int type;
    private int prevCheckedPosition = -1;

    private BluetoothScanClient mScanClient;

    private static final int ENABLE_BT_REQUEST_ID = 1;
    public static int REQUEST_CODE_DEVICE_NAME = 11;
    public static String DEVICE_NAME = "DeviceListActivity.DEVICE_NAME";
    public static final String TOAST = "toast";
    private BluetoothAdapter mBtAdapter;

    private Handler mHandler = new Handler(){
        @Override
        public synchronized void handleMessage(Message msg) {
            adapter.add((BlueToothDeviceBean) msg.obj);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_device_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScanClient = BluetoothScanClient.getInstance(this, this);
        if(!mScanClient.isBluetoothOpen()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST_ID);
        }else{
            mScanClient.startScan();
            doDiscovery();
        }
        initView();
        initDatas();
        initListener();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // user didn't want to turn on BT
    	System.out.println("requestCode="+requestCode+" RESULT_OK "+RESULT_OK);
        if (requestCode == ENABLE_BT_REQUEST_ID) {
        	if(resultCode == RESULT_OK) {
        		mScanClient.startScan();
                doDiscovery();
		        return;
		    }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanClient.destroy();

        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    public void initDatas() {
        devices = new ArrayList<BlueToothDeviceBean>();
        adapter = new DeviceAdapter(this, devices);
        lv.setAdapter(adapter);
    }

    private void initView() {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        lv = (ListView) findViewById(R.id.device_list);
        confirm = (TextView) findViewById(R.id.confirm);
        cancel = (ImageView) findViewById(R.id.cancel);
        lv.setEmptyView(findViewById(R.id.empty_view));
    }

    private void initListener() {
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "on ItemClick : position =" + position + ", id=" + id);
                if (position == prevCheckedPosition) return;
                prevCheckedPosition = position;
                checkedDevice = adapter.getItem(position).getBlueDevice();
                type = adapter.getItem(position).getType();

                adapter.notifyDataSetChanged();
            }
        });

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (checkedDevice == null) return;
                BlueTooth_UniqueInstance.getInstance().setBlueType(type);
                Intent i = new Intent();
                i.putExtra(DATA_DEVICE,checkedDevice);
                i.putExtra(DATA_TYPE,type);
                setResult(RESULT_OK,i);
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (device == null || device.getName() == null) return;
        Log.w(TAG, "onLeScan: bleDevice == " + device.getAddress() + " == " + device.getName());
        BlueToothDeviceBean deviceBean = new BlueToothDeviceBean(device,1,false);
        if (checkDeviceExist(deviceBean,1)) {
            Message msg = mHandler.obtainMessage(1);
            msg.obj = deviceBean;
            mHandler.sendMessage(msg);
        }
    }

    @SuppressLint("NewApi")
	@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BluetoothScanClient.REQUEST_LOCATIONARESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.
                    if (mScanClient != null) {
                        mScanClient.startScan();
                    }
                    doDiscovery();
                } else {
                    // The user disallowed the requested permission.
                    Toast.makeText(this, R.string.permission_failed, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    /**BT经典蓝牙搜索，使用BluetoothAdapter的startDiscovery()*/
    private void doDiscovery() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BlueToothDeviceBean deviceBean = new BlueToothDeviceBean(device,0,false);

                Log.w(TAG, "onLeScan: btDevice == " + device.getAddress() + " == " + device.getName());
                if(checkDeviceExist(deviceBean,0)){
                    Message msg = mHandler.obtainMessage(1);
                    msg.obj = deviceBean;
                    mHandler.sendMessage(msg);
                }
            }
        }
    };







    /**Adapter*/
    private class DeviceAdapter extends ArrayAdapter<BlueToothDeviceBean> {
        Context context;

        public DeviceAdapter(Context context, ArrayList<BlueToothDeviceBean> objects) {
            super(context, 0, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BluetoothDevice device = getItem(position).getBlueDevice();
            ALLDeviceListItemView v;
            if (convertView == null) {
                v = new ALLDeviceListItemView(context, device);

            } else {
                v = (ALLDeviceListItemView) convertView;
                v.setDevice(device);
                v.initData();
            }
            v.setCheckState(position == prevCheckedPosition);

            return v;
        }

    }

    /**检查蓝牙是否重复添加*/
    private synchronized boolean checkDeviceExist(BlueToothDeviceBean device,int type) {
        System.out.println("======"+device.getBlueDevice().getAddress()+"===="+device.getType());
        boolean isExist = true;
        if (devices == null) {
            return false;
        }
        if(type==0){//BT经典蓝牙
            for (BlueToothDeviceBean d : devices) {
                if (d.getBlueDevice().getAddress().equals(device.getBlueDevice().getAddress())){
                    isExist =  false;
                }
            }
        }else{//ble蓝牙
            for (int i = 0;i<devices.size();i++) {
                if (devices.get(i).getBlueDevice().getAddress().equals(device.getBlueDevice().getAddress())) {
                    if(devices.get(i).getType()==0){
                        devices.remove(i);
                        adapter.notifyDataSetChanged();
                        isExist =  false;
                    }else{
                        isExist =  false;
                    }
                }
            }
        }

        return isExist;
    }

}
