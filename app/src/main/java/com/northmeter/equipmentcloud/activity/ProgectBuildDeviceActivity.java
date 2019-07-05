package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.andview.refreshview.XRefreshView;
import com.northmeter.equipmentcloud.I.I_ShowBlueSend;
import com.northmeter.equipmentcloud.I.I_ShowBuildDevice;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.EvenBusBean;
import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.bluetooth.AllScanDeviceListActivity;
import com.northmeter.equipmentcloud.bluetooth.BleBlue_ConnectHelper;
import com.northmeter.equipmentcloud.bluetooth.BleConnect_InstanceHelper;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_ConnectHelper;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_UniqueInstance;
import com.northmeter.equipmentcloud.bluetooth.SendBlueMessage;
import com.northmeter.equipmentcloud.camera.activity.CaptureActivity;
import com.northmeter.equipmentcloud.enumBean.EvenBusEnum;
import com.northmeter.equipmentcloud.presenter.ProgectBuildDevicePresenter;
import com.northmeter.equipmentcloud.widget.CommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.northmeter.equipmentcloud.bluetooth.AllScanDeviceListActivity.DATA_TYPE;
import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.DATA_DEVICE;

/**
 * Created by dyd on 2019/2/27.
 * 建筑内设备列表
 */

public class ProgectBuildDeviceActivity extends BaseActivity implements XRefreshView.XRefreshViewListener, I_ShowBuildDevice, I_ShowBlueSend, TextWatcher
{

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.iv_device_add)
    ImageView ivDeviceAdd;
    @BindView(R.id.iv_device_del)
    ImageView ivDeviceDelete;
    @BindView(R.id.et_search_name)
    EditText etSearchName;
    @BindView(R.id.btn_device_del_sure)
    Button btnDeviceDelSure;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;
    @BindView(R.id.x_refresh_view)
    XRefreshView xRefreshView;
    private CommonAdapter commonAdapter;
    private List<ProgectBuildDeviceResponse.PageList> datas = new ArrayList<>();
    private List<ProgectBuildDeviceResponse.PageList> datasBack = new ArrayList<>();
    private List<ProgectBuildDeviceResponse.PageList> searchDatas = new ArrayList<>();
    private int projectId, recordId;
    private String buildingName, projectName;
    private boolean showOrHide = false;//显示或隐藏设备的s
    private ProgectBuildDevicePresenter progectBuildDevicePresenter;
    private CommonDialog commonDialog;
    private String scanTableNum;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private BluetoothAdapter mBluetoothAdapter = null;
    private SendBlueMessage sendBlueMessage;


    private int doRecordId;
    private String doItemTypeId, doEquipmentId, doEquipmentNum, fileName, doEquipmentName;

    private Vibrator vibrator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_build_device;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        progectBuildDevicePresenter.getEquipList(projectId, recordId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        projectName = getIntent().getStringExtra("projectName");
        buildingName = getIntent().getStringExtra("buildName");
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvRightText.setText("取消");
        tvRightText.setVisibility(View.GONE);
        String[] titleList = buildingName.split("/");
        tvToolbarTitle.setText(titleList[titleList.length - 1]);
        ivDeviceAdd.setVisibility(View.VISIBLE);
        ivDeviceDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        etSearchName.addTextChangedListener(this);
        initRefresh();
        initListView();
        progectBuildDevicePresenter = new ProgectBuildDevicePresenter(this);
        mBluetoothAdapter = BlueTooth_ConnectHelper.getInstance().getmBluetoothAdapter();
        sendBlueMessage = new SendBlueMessage(this);
    }

    private void initRefresh() {
        // 设置是否可以下拉刷新
        xRefreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        xRefreshView.setPullLoadEnable(false);
        // 设置上次刷新的时间
        xRefreshView.restoreLastRefreshTime(xRefreshView.getLastRefreshTime());
        // 设置时候可以自动刷新
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setXRefreshViewListener(this);
        //xRefreshView.startRefresh();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRefresh(boolean isPullDown) {
        progectBuildDevicePresenter.getEquipList(projectId, recordId);
    }

    @Override
    public void onLoadMore(boolean isSilence) {

    }

    @Override
    public void onRelease(float direction) {

    }

    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                    if (data != null) {
                        if (data.hasExtra("result")) {//扫描到水表编号返回数据
                            scanTableNum = data.getStringExtra("result").toString();
                            //设备注册
                            progectBuildDevicePresenter.registereEquipment(doRecordId, doEquipmentId,
                                    scanTableNum, doItemTypeId, buildingName, 1);
                            return;
                        }
                    }
                }
                break;
            case REQUEST_CONNECT_DEVICE://高速蓝牙
//                if (resultCode == Activity.RESULT_OK) {
//                    progectBuildDevicePresenter.startLoadingDialog();
//                    String address = data.getExtras().getString(
//                            BtDeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    BlueTooth_ConnectHelper.getInstance().blueToothConnect(address);
//                }
                if (resultCode == Activity.RESULT_OK) {
                    //断开蓝牙
                    BlueTooth_ConnectHelper.getInstance().stopBlueToothConnect();
                    BleConnect_InstanceHelper.getInstance().cancelConnect();
                    BleBlue_ConnectHelper.getInstance().cancelConnect();

                    int type = data.getExtras().getInt(DATA_TYPE);
                    BluetoothDevice checkedDevice = data.getExtras().getParcelable(DATA_DEVICE);
                    if(type == 0){//BT
                        BlueTooth_ConnectHelper.getInstance().blueToothConnect(checkedDevice.getAddress());
                    }else{
                        if(Build.VERSION.SDK_INT > 23){
                            BleConnect_InstanceHelper bleConnect = BleConnect_InstanceHelper.getInstance();
                            bleConnect.setMacStr(checkedDevice.getAddress());
                            bleConnect.connecedDevice();
                        }else{
                            BleBlue_ConnectHelper.getInstance().blueToothConnect(checkedDevice);
                        }
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode != Activity.RESULT_OK) {
                    showMsg("该功能需要打开手机蓝牙");
                }
                break;
        }
    }


    private void initListView() {
        commonAdapter = new CommonAdapter<ProgectBuildDeviceResponse.PageList>(this, datas, R.layout.item_progrect_build_device) {
            @Override
            public void convert(ViewHolder helper, final ProgectBuildDeviceResponse.PageList item) {

                helper.getView(R.id.btn_device_active).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (item.getDevtestMode() == 2 || item.getActivationMode() == 2) {//1-远端激活，2-近端激活
                            doRecordId = item.getRecordId();
                            doEquipmentId = item.getEquipmentId();
                            doEquipmentNum = item.getEquipmentNum();
                            doItemTypeId = item.getItemTypeId();
                            doEquipmentName = item.getEquipmentName();
                            fileName = item.getConfigurationPlanName();
                            //打开蓝牙，发送设备激活和测试命令
                            if (!mBluetoothAdapter.isEnabled()) {
                                Intent enableIntent = new Intent(
                                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                            } else {
                                switch (item.getActiveStatus()) {
                                    case 2://设备测试
                                        if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                                            progectBuildDevicePresenter.startLoadingDialog();
                                            sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 25);
                                        } else {
                                            BlueTooth_UniqueInstance.getInstance().setState(24);
                                            //Intent serverIntent = new Intent(ProgectBuildDeviceActivity.this, BtDeviceListActivity.class);
                                            //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                                            Intent intent = new Intent(ProgectBuildDeviceActivity.this, AllScanDeviceListActivity.class);
                                            startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                                        }
                                        break;
                                    default://设备激活
                                        if(fileName==null||fileName==""||fileName.equals("")){//如果设备配置文件为空，则直接发送本地激活命令
                                            if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                                                progectBuildDevicePresenter.startLoadingDialog();
                                                sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 23);
                                            } else {
                                                BlueTooth_UniqueInstance.getInstance().setState(26);
                                                //Intent serverIntent = new Intent(ProgectBuildDeviceActivity.this, BtDeviceListActivity.class);
                                                //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                                                Intent intent = new Intent(ProgectBuildDeviceActivity.this, AllScanDeviceListActivity.class);
                                                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                                            }
                                        }else{
                                            if (BlueTooth_ConnectHelper.getInstance().isBooleanConnected()) {
                                                String para_1 = progectBuildDevicePresenter.getFilesInfo(projectId,doEquipmentNum, fileName, 0);
                                                if (para_1 != null) {//设置现场参数
                                                    progectBuildDevicePresenter.startLoadingDialog();
                                                    sendBlueMessage.sendBTblueMessage(para_1, 21);
                                                }
                                            } else {
                                                BlueTooth_UniqueInstance.getInstance().setState(20);
                                                //Intent serverIntent = new Intent(ProgectBuildDeviceActivity.this, BtDeviceListActivity.class);
                                                //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                                                Intent intent = new Intent(ProgectBuildDeviceActivity.this, AllScanDeviceListActivity.class);
                                                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                                            }
                                        }
                                        break;
                                }

                            }
                        } else {
                            if (item.getActiveStatus() == 2) {
                                progectBuildDevicePresenter.doTestEquipment(item.getRecordId(), item.getEquipmentId(), item.getEquipmentNum(),
                                        item.getItemTypeId(), item.getEquipmentName(), 0);
                            } else {
                                progectBuildDevicePresenter.doactiveEquipment(item.getRecordId(), 1);
                            }
                        }
                    }
                });

                if (showOrHide) {
                    helper.getView(R.id.btn_device_check).setVisibility(View.VISIBLE);
                    helper.getView(R.id.btn_device_register).setVisibility(View.GONE);
                    helper.getView(R.id.btn_device_active).setVisibility(View.GONE);
                    helper.getView(R.id.btn_device_info).setVisibility(View.GONE);
                    helper.getView(R.id.btn_get_result).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.btn_device_check).setVisibility(View.GONE);
                    helper.getView(R.id.btn_device_register).setVisibility(View.VISIBLE);
                    helper.getView(R.id.btn_device_active).setVisibility(View.VISIBLE);
                    helper.getView(R.id.btn_device_info).setVisibility(View.VISIBLE);
                    helper.getView(R.id.btn_get_result).setVisibility(View.VISIBLE);
                }
                helper.getTextViewSet(R.id.tv_build_name, item.getEquipmentName());
                helper.getCheckViewSet(R.id.btn_device_check, item.isCheck());

                int registerStatus = item.getRegisterStatus();//0-未注册，1-已经注册
                int activeStatus = item.getActiveStatus();//0-未激活，1—激活中，2-激活成功，3-激活失败，4，是否是可激活设备
                switch (registerStatus) {
                    case 0://未注册
                        helper.getView(R.id.btn_device_register).setVisibility(View.VISIBLE);
                        helper.getView(R.id.btn_device_active).setVisibility(View.GONE);
                        break;
                    case 1://已注册
                        helper.getView(R.id.btn_device_register).setVisibility(View.GONE);
                        helper.getView(R.id.btn_device_active).setVisibility(View.VISIBLE);
                        if (activeStatus == 4) {//不可激活,直接进入设备测试
                            helper.getView(R.id.btn_device_active).setClickable(false);
                            helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.no_selector_bg));
                            helper.getTextViewSet(R.id.btn_device_active, "未激活");
                        } else {
                            switch (activeStatus) {
                                case 0://未激活，点此重新激活
                                    helper.getTextViewSet(R.id.btn_device_active, "未激活");
                                    helper.getView(R.id.btn_device_active).setClickable(true);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.selector_sure_button_bg));
                                    break;
                                case 1://激活中
                                    helper.getTextViewSet(R.id.btn_device_active, "激活中");
                                    helper.getView(R.id.btn_device_active).setClickable(false);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.no_selector_bg));
                                    break;
                                case 2://激活成功
                                    if (item.getImplementStatus() == 0) {//测试状态 0-未执行完成 1-已执行完毕 2—未测试过
                                        helper.getTextViewSet(R.id.btn_device_active, "测试中");
                                        helper.getView(R.id.btn_device_active).setClickable(false);
                                        helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.no_selector_bg));
                                    }else{
                                        if(item.getImplementResult()==1){//0-测试成功 1-测试失败
                                            helper.getTextViewSet(R.id.btn_device_active, "测试失败");
                                        }else{
                                            helper.getTextViewSet(R.id.btn_device_active, "设备测试");
                                        }
                                        helper.getView(R.id.btn_device_active).setClickable(true);
                                        helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.selector_sure_button_bg));
                                    }
                                    break;
                                case 3://激活失败，点此重新激活
                                    helper.getTextViewSet(R.id.btn_device_active, "激活失败");
                                    helper.getView(R.id.btn_device_active).setClickable(true);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.selector_sure_button_bg));
                                    break;
                                default:
                                    helper.getView(R.id.btn_device_active).setClickable(false);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.drawable.no_selector_bg));
                                    break;
                            }
                        }
                        break;
                    default:
                        helper.getView(R.id.btn_device_register).setVisibility(View.VISIBLE);
                        helper.getView(R.id.btn_device_active).setVisibility(View.GONE);
                        break;
                }


                helper.getView(R.id.btn_device_register).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProgectBuildDeviceActivity.this, CaptureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                        doRecordId = item.getRecordId();
                        doEquipmentId = item.getEquipmentId();
                        doItemTypeId = item.getItemTypeId();
                    }
                });

                helper.getView(R.id.btn_device_info).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_detail = new Intent();
                        intent_detail.putExtra("projectId", projectId);
                        intent_detail.putExtra("equipmentNum", item.getEquipmentNum());
                        intent_detail.putExtra("itemTypeId", item.getItemTypeId());
                        intent_detail.putExtra("equipmentName", item.getEquipmentName());
                        intent_detail.putExtra("recordId", item.getRecordId());
                        goActivity(ProgectBuildDeviceDetailActivity.class, intent_detail);
                    }
                });

                helper.getView(R.id.btn_get_result).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_detail = new Intent();
                        intent_detail.putExtra("equipmentName", item.getEquipmentName());
                        intent_detail.putExtra("recordId", item.getRecordId());
                        goActivity(ProgectSelfCheckingDeviceResultActivity.class, intent_detail);
                    }
                });

                helper.getView(R.id.btn_device_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.isCheck()) {
                            item.setCheck(false);
                        } else {
                            item.setCheck(true);
                        }
                        commonAdapter.notifyDataSetChanged();
                    }
                });

                helper.getView(R.id.linear_buildlist).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showOrHide) {
                            if (item.isCheck()) {
                                item.setCheck(false);
                            } else {
                                item.setCheck(true);
                            }
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                });

                helper.getView(R.id.linear_buildlist).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(vibrator.hasVibrator()) {
                            vibrator.vibrate(20); //参数标识  震动持续毫秒
                        }
                        tvRightText.setVisibility(View.VISIBLE);
                        btnDeviceDelSure.setVisibility(View.VISIBLE);
                        ivDeviceDelete.setVisibility(View.GONE);
                        ivDeviceAdd.setVisibility(View.GONE);
                        showOrHide = true;
                        commonAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @OnClick({R.id.btn_tb_back, R.id.iv_device_add, R.id.iv_device_del, R.id.btn_device_del_sure,
            R.id.tv_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back://返回
                this.finish();
                break;
            case R.id.iv_device_add://增加设备
                Intent intent_add = new Intent();
                intent_add.putExtra("projectId", projectId);
                intent_add.putExtra("projectName", projectName);
                intent_add.putExtra("recordId", recordId);
                goActivity(ProgectDeviceAddActivity.class, intent_add);
                break;
            case R.id.iv_device_del://进入删除设备功能
                tvRightText.setVisibility(View.VISIBLE);
                btnDeviceDelSure.setVisibility(View.VISIBLE);
                ivDeviceDelete.setVisibility(View.GONE);
                ivDeviceAdd.setVisibility(View.GONE);
                showOrHide = true;
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_right_text://退出删除功能
                tvRightText.setVisibility(View.GONE);
                btnDeviceDelSure.setVisibility(View.GONE);
                ivDeviceDelete.setVisibility(View.VISIBLE);
                ivDeviceAdd.setVisibility(View.VISIBLE);
                showOrHide = false;
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_device_del_sure://确认删除按钮
                commonDialog = new CommonDialog(this,
                        R.layout.dialog_devie_delete, "是否删除设备？",new CommonDialog.CallBack() {
                    @Override
                    public void onConfirm() {
                        progectBuildDevicePresenter.deleteEquipment(datas, 1);
                        commonDialog.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;
        }
    }


    @Override
    public void showData(List<ProgectBuildDeviceResponse.PageList> datas) {
        xRefreshView.stopRefresh();
        this.datas.clear();
        this.datas.addAll(datas);
        this.datasBack.clear();
        this.datasBack.addAll(datas);
        commonAdapter.notifyDataSetChanged();
        if (datas.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnSuccess(String msg, int state) {
        xRefreshView.stopRefresh();
        showMsg(msg);
        if (state == 1) {
            progectBuildDevicePresenter.getEquipList(projectId, recordId);
        }
    }

    @Override
    public void returnFail(String msg) {
        xRefreshView.stopRefresh();
        showMsg(msg);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String searchName = etSearchName.getText().toString();
        if (!datasBack.isEmpty()) {
            searchDatas.clear();
            for (ProgectBuildDeviceResponse.PageList item : datasBack) {
                if (item.getEquipmentName().indexOf(searchName) >= 0) {
                    searchDatas.add(item);
                }
            }
            datas.clear();
            datas.addAll(searchDatas);
            commonAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 事件订阅者处理事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EvenBusBean evenBusBean) {
        String topic = evenBusBean.getTopic();
        int state = BlueTooth_UniqueInstance.getInstance().getState();
        if (topic.equals(EvenBusEnum.EvenBus_BuildDevice.getEvenName())) {
            String message = evenBusBean.getData();
            if (message.equals("success")) {
                switch (state) {
                    case 20://连接蓝牙后本地发送配置参数
                        progectBuildDevicePresenter.startLoadingDialog();
                        String para_1 = progectBuildDevicePresenter.getFilesInfo(projectId,doEquipmentNum, fileName, 0);
                        if (para_1 != null) {//设置现场参数
                            sendBlueMessage.sendBTblueMessage(para_1, 21);
                        } else {
                            progectBuildDevicePresenter.stopLoadingDialog();
                        }
                        break;
                    case 21://设置现场参数 时间
                        String para_2 = progectBuildDevicePresenter.getFilesInfo(projectId,doEquipmentNum, fileName, 1);
                        if (para_2 != null) {
                            sendBlueMessage.sendBTblueMessage(para_2, 22);
                        } else {
                            progectBuildDevicePresenter.stopLoadingDialog();
                        }
                        break;
                    case 22://唤醒激活
                        sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 23);
                        break;
                    case 26://无配置文件时激活
                        progectBuildDevicePresenter.startLoadingDialog();
                        sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 23);
                        break;
                    case 23://通知后台激活
                        progectBuildDevicePresenter.stopLoadingDialog();
                        progectBuildDevicePresenter.doactiveEquipment(doRecordId, 1);
                        break;
                    case 24://连接蓝牙后本地发送测试命令
                        progectBuildDevicePresenter.startLoadingDialog();
                        sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 25);
                        break;
                    case 25://通知后台测试
                        progectBuildDevicePresenter.stopLoadingDialog();
                        progectBuildDevicePresenter.doTestEquipment(doRecordId, doEquipmentId, doEquipmentNum, doItemTypeId, doEquipmentName, 1);
                        break;
                }
            } else if (message.equals("fail")) {
                progectBuildDevicePresenter.stopLoadingDialog();
                showMsg("配置失败,请重试");
            }
        } else if (topic.equals(EvenBusEnum.EvenBus_BlueTooth_Connect.getEvenName())) {
            showMsg(evenBusBean.getData());
            if (state == 20 || state == 24 || state==26) {
                progectBuildDevicePresenter.stopLoadingDialog();
            }
        }
    }

    @Override
    public void showMessage(String message) {

    }

}


/**
 * 1.根据获取注册状态（0-未注册，1-已经注册），判断设备是否注册，未注册时按钮显示为“未注册”，点击按钮进行注册流程；
 2.注册状态为 已经注册时，根据激活状态（0-未激活，1—激活中，2-激活成功，3-激活失败，4，不可激活设备）进行分类处理；
 a.激活状态为4（不可激活设备），显示此设备不可激活，等待主节点设备的测试。
 b.激活状态为0（未激活），按钮显示未激活，可点击激活；
 c.激活状态为1（激活中），按钮显示激活中，不可点击；
 d.激活状态为2（激活成功），查询测试状态implementStatus（0-执行中 1-已执行完毕 2—未测试过），测试状态为0时按钮显示为灰色（不可点击）“测试中”；测试状态为1或者2时，根据测试结果ImplementResult（0-测试成功 1-测试失败）确定按钮的显示，此时按钮可再次点击进行测试。

 e.激活状态为3（激活失败），按钮显示激活失败，点击可重新进入激活流程；
 */