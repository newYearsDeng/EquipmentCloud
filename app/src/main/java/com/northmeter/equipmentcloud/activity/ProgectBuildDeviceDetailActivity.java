package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowDeviceDetail;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.EvenBusBean;
import com.northmeter.equipmentcloud.bean.ProgectDeviceDetailResponse;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_ConnectHelper;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BtDeviceListActivity;
import com.northmeter.equipmentcloud.enumBean.EvenBusEnum;
import com.northmeter.equipmentcloud.presenter.ProgectBuildDeviceDetailPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/3/1.
 * 设备详情
 */

public class ProgectBuildDeviceDetailActivity extends BaseActivity implements I_ShowDeviceDetail {
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_number)
    TextView tvDeviceNumber;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_big_cate_name)
    TextView tvBigCateName;
    @BindView(R.id.tv_small_cate_name)
    TextView tvSmallCateName;
    @BindView(R.id.tv_measure_category_name)
    TextView tvMeasureCategoryName;
    @BindView(R.id.tv_concentrator_name)
    TextView tvConcentratorName;
    @BindView(R.id.tv_collector_name)
    TextView tvCollectorName;
    @BindView(R.id.tv_activation_mode)
    TextView tvActivationMode;
    @BindView(R.id.tv_used_state)
    TextView tvUsedState;
    @BindView(R.id.btn_get_local_picture)
    Button btnGetLocalPicture;
    @BindView(R.id.btn_get_network_picture)
    Button btnGetNetworkPicture;
    @BindView(R.id.btn_set_xy_para)
    Button btnSetXyPara;
    @BindView(R.id.btn_set_local_para)
    Button btnSetLocalPara;

    private final static int FIND_BLUETOOTH_CODE = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter mBluetoothAdapter = null;
    private String TAG = getClass().getSimpleName();
    private ProgectBuildDeviceDetailPresenter progectBuildDeviceDetailPresenter;
    private int projectId,recordId;
    private String equipmentNum, itemTypeId, equipmentName;
    private Intent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_device_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        recordId = getIntent().getIntExtra("recordId", 0);
        equipmentNum = getIntent().getStringExtra("equipmentNum");
        itemTypeId = getIntent().getStringExtra("itemTypeId");
        equipmentName = getIntent().getStringExtra("equipmentName");

        intent = new Intent();
        intent.putExtra("projectId", projectId);
        intent.putExtra("equipmentNum", equipmentNum);
        intent.putExtra("itemTypeId", itemTypeId);
        intent.putExtra("equipmentName", equipmentName);
        intent.putExtra("recordId", recordId);
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("设备详情");
        if (equipmentNum == null) {
            btnGetLocalPicture.setVisibility(View.GONE);
            btnGetNetworkPicture.setVisibility(View.GONE);
            btnSetXyPara.setVisibility(View.GONE);
            btnSetLocalPara.setVisibility(View.GONE);
        } else {
            tvRightText.setText("蓝牙");
        }
    }

    @Override
    public void initData() {
        super.initData();
        progectBuildDeviceDetailPresenter = new ProgectBuildDeviceDetailPresenter(this);
        progectBuildDeviceDetailPresenter.getEquipmentDetails(recordId);

        mBluetoothAdapter = BlueTooth_ConnectHelper.getInstance().getmBluetoothAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()&&equipmentNum != null) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE://高速蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            BtDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BlueTooth_ConnectHelper.getInstance().blueToothConnect(address);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    // BlueTooth_ConnectHelper.getInstance().setupChat();
                } else {
                    Log.d(TAG, "BT not enabled");
                    showMsg("该功能需要打开手机蓝牙");
                }
                break;
        }
    }

    @OnClick({R.id.btn_tb_back, R.id.tv_right_text, R.id.btn_get_local_picture, R.id.btn_get_network_picture,
            R.id.btn_set_xy_para, R.id.btn_set_local_para})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.tv_right_text:
                Intent serverIntent = new Intent(this, BtDeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            case R.id.btn_get_local_picture://获取本地图片
                if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                    intent.putExtra("type", 0);
                    goActivity(WaterMeterPictureShow.class, intent);
                } else {
                    showMsg("未连接蓝牙");
                }
                break;
            case R.id.btn_get_network_picture://获取网络图片
                intent.putExtra("type", 1);
                goActivity(WaterMeterPictureShow.class, intent);
                break;
            case R.id.btn_set_xy_para://设置坐标轴参数
                if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                    intent.putExtra("type", 0);
                    goActivity(LocationSet_NBDevice.class, intent);
                } else {
                    showMsg("未连接蓝牙");
                }
                break;
            case R.id.btn_set_local_para://设置本地配置参数
                if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                    intent.putExtra("type", 1);
                    goActivity(LocationSet_NBDevice.class, intent);
                } else {
                    showMsg("未连接蓝牙");
                }
                break;
        }
    }

    @Override
    public void showData(ProgectDeviceDetailResponse.PageList deviceInfo) {
        tvDeviceType.setText(deviceInfo.getItemType());
        tvDeviceNumber.setText(deviceInfo.getEquipmentNum());
        tvDeviceName.setText(deviceInfo.getEquipmentName());
        tvBigCateName.setText(deviceInfo.getBigCategoryName());
        tvSmallCateName.setText(deviceInfo.getSmallCategoryName());
        tvMeasureCategoryName.setText(deviceInfo.getMeasureCategoryName());
        tvConcentratorName.setText(deviceInfo.getConcentratorName());
        tvCollectorName.setText(deviceInfo.getCollectorName());
        switch (deviceInfo.getUsedState()) {//注册状态 0-未注册，1-已经注册
            case 0:
                tvActivationMode.setText("未注册");
                break;
            case 1:
                tvActivationMode.setText("已注册");
                break;
        }
        switch (deviceInfo.getActivationMode()) {//激活状态 0-未激活，1—激活中，2-激活成功，3-激活失败，4，是否是可激活设备
            case 0:
                tvUsedState.setText("未激活");
                break;
            case 1:
                tvUsedState.setText("激活中");
                break;
            case 2:
                tvUsedState.setText("激活成功");
                break;
            case 3:
                tvUsedState.setText("激活失败");
                break;
            case 4:
                tvUsedState.setText("不可激活");
                break;
        }


    }

    @Override
    public void returnFail(String msg) {
        showMsg(msg);
    }


    /**
     * 事件订阅者处理事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EvenBusBean evenBusBean) {
        String topic = evenBusBean.getTopic();
        if (topic.equals(EvenBusEnum.EvenBus_BlueTooth_Connect.getEvenName())) {
            showMsg(evenBusBean.getData());
        }
    }


}
