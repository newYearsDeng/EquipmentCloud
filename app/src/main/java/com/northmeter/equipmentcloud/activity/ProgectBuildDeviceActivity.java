package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.IShowSMainMessage;
import com.northmeter.equipmentcloud.I.I_ShowBlueSend;
import com.northmeter.equipmentcloud.I.I_ShowBuildDevice;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.ProgectBuildDeviceResponse;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_UniqueInstance;
import com.northmeter.equipmentcloud.bluetooth.GetBlueEntity;
import com.northmeter.equipmentcloud.bluetooth.SendBlueMessage;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BluetoothChatService;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BtDeviceListActivity;
import com.northmeter.equipmentcloud.camera.activity.CaptureActivity;
import com.northmeter.equipmentcloud.presenter.ProgectBuildDevicePresenter;
import com.northmeter.equipmentcloud.utils.Udp_Help;
import com.northmeter.equipmentcloud.widget.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.DEVICE_NAME;
import static com.northmeter.equipmentcloud.bluetooth.bluetooth.blueActivity.DeviceListActivity.TOAST;

/**
 * Created by dyd on 2019/2/27.
 * 建筑内设备列表
 */

public class ProgectBuildDeviceActivity extends BaseActivity implements I_ShowBuildDevice, IShowSMainMessage,I_ShowBlueSend, TextWatcher {

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
    private CommonAdapter commonAdapter;
    private List<ProgectBuildDeviceResponse.PageList> datas = new ArrayList<>();
    private List<ProgectBuildDeviceResponse.PageList> datasBack = new ArrayList<>();
    private List<ProgectBuildDeviceResponse.PageList> searchDatas = new ArrayList<>();
    private int projectId, recordId;
    private String buildingName,projectName;
    private boolean showOrHide = false;
    private ProgectBuildDevicePresenter progectBuildDevicePresenter;
    private CommonDialog commonDialog;
    private String scanTableNum;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private SendBlueMessage sendBlueMessage;
    private GetBlueEntity getBlueEntity;

    private int doRecordId;
    private String doItemTypeId,doEquipmentId,doEquipmentNum,doEquipmentName,fileName;
    private int activeStatus;//激活状态

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_build_device;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initListView();
        progectBuildDevicePresenter = new ProgectBuildDevicePresenter(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sendBlueMessage = new SendBlueMessage(this);
        getBlueEntity = new GetBlueEntity(this);
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
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            BtDeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setupChat();
                } else {
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

                        if (item.getDevtestMode()==2) {//1-远端激活，2-近端激活
                            activeStatus = item.getActiveStatus();
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
                                if (mChatService == null) {
                                    setupChat();
                                }
                                if (BlueTooth_UniqueInstance.getInstance().isBooleanConnected()) {
                                    String para_1 = progectBuildDevicePresenter.getFilesInfo(doEquipmentNum,fileName,0);
                                    if(para_1 == null){//设置现场参数
                                        showMsg("文件不存在，请返回项目列表重新加载");
                                    }else{
                                        sendBlueMessage.sendBTblueMessage(para_1,0);
                                    }
                                } else {
                                    Intent serverIntent = new Intent(ProgectBuildDeviceActivity.this, BtDeviceListActivity.class);
                                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                                }
                            }
                        } else {
                            if (item.getActiveStatus() == 2) {
                                progectBuildDevicePresenter.doTestEquipment(item.getRecordId(),item.getEquipmentId(),item.getEquipmentNum(),item.getItemTypeId(),item.getEquipmentName(), 0);
                            } else {
                                progectBuildDevicePresenter.doactiveEquipment(item.getRecordId(), 1);
                            }
                        }
                    }
                });

                if (item.isDelShow()) {
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
                            helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.color.color_little_gray));
                            helper.getTextViewSet(R.id.btn_device_active, "未激活");
                        } else {
                            switch (activeStatus) {
                                case 0://未激活，点此重新激活
                                    helper.getTextViewSet(R.id.btn_device_active, "未激活");
                                    break;
                                case 1://激活中
                                    helper.getTextViewSet(R.id.btn_device_active, "激活中");
                                    helper.getView(R.id.btn_device_active).setClickable(false);
                                    break;
                                case 2://激活成功
                                    if(item.getImplementStatus()==0){//测试状态 0-未执行完成 1-已执行完毕 2—未测试过
                                        helper.getView(R.id.btn_device_active).setClickable(false);
                                        helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.color.color_little_gray));
                                    }
                                    helper.getTextViewSet(R.id.btn_device_active, "设备测试");
                                    break;
                                case 3://激活失败，点此重新激活
                                    helper.getTextViewSet(R.id.btn_device_active, "激活失败");
                                    helper.getView(R.id.btn_device_active).setClickable(false);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.color.color_little_gray));
                                    break;
                                default:
                                    helper.getView(R.id.btn_device_active).setClickable(false);
                                    helper.getView(R.id.btn_device_active).setBackground(getResources().getDrawable(R.color.color_little_gray));
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
                        intent_detail.putExtra("equipmentId", item.getEquipmentId());
                        intent_detail.putExtra("equipmentName", item.getEquipmentName());
                        intent_detail.putExtra("recordId", item.getRecordId());
                        goActivity(ProgectBuildDeviceDetailActivity.class, intent_detail);
                    }
                });

                helper.getView(R.id.btn_get_result).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_detail = new Intent();
                        intent_detail.putExtra("projectId", projectId);
                        intent_detail.putExtra("equipmentId", item.getEquipmentId());
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
                        if(showOrHide){
                            if (item.isCheck()) {
                                item.setCheck(false);
                            } else {
                                item.setCheck(true);
                            }
                            commonAdapter.notifyDataSetChanged();
                        }
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
                intent_add.putExtra("projectName",projectName);
                intent_add.putExtra("recordId", recordId);
                goActivity(ProgectDeviceAddActivity.class, intent_add);
                break;
            case R.id.iv_device_del://进入删除设备功能
                tvRightText.setVisibility(View.VISIBLE);
                btnDeviceDelSure.setVisibility(View.VISIBLE);
                ivDeviceDelete.setVisibility(View.GONE);
                ivDeviceAdd.setVisibility(View.GONE);
                showOrHide = true;
                for (ProgectBuildDeviceResponse.PageList data : datas) {
                    data.setDelShow(showOrHide);
                }
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_right_text://退出删除功能
                tvRightText.setVisibility(View.GONE);
                btnDeviceDelSure.setVisibility(View.GONE);
                ivDeviceDelete.setVisibility(View.VISIBLE);
                ivDeviceAdd.setVisibility(View.VISIBLE);
                showOrHide = false;
                for (ProgectBuildDeviceResponse.PageList data : datas) {
                    data.setDelShow(showOrHide);
                }
                commonAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_device_del_sure://确认删除按钮
                commonDialog = new CommonDialog(this,
                        R.layout.dialog_devie_delete, new CommonDialog.CallBack() {
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
        this.datas.clear();
        this.datas.addAll(datas);
        this.datasBack.clear();
        this.datasBack.addAll(datas);
        commonAdapter.notifyDataSetChanged();
        if (datas.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void returnSuccess(String msg, int state) {
        showMsg(msg);
        if (state == 1) {
            progectBuildDevicePresenter.getEquipList(projectId, recordId);
        }
    }

    @Override
    public void returnFail(String msg) {
        showMsg(msg);
    }

    @OnClick(R.id.btn_device_del_sure)
    public void onViewClicked() {
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        progectBuildDevicePresenter.getEquipList(projectId, recordId);
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    private void setupChat() {
        mChatService = new BluetoothChatService(this, btHandler);
        BlueTooth_UniqueInstance.getInstance().setBluetoothChatService(mChatService);
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

    @Override
    public void showMainMsg(String message) {
        int state = BlueTooth_UniqueInstance.getInstance().getState();
        if (message.equals("success")) {
            switch (state){
                case 0://设置现场参数 时间
                    String para_2  = progectBuildDevicePresenter.getFilesInfo(doEquipmentNum,fileName,1);
                    if(para_2 == null){
                        showMsg("文件不存在，请返回项目列表重新加载");
                    }else{
                        sendBlueMessage.sendBTblueMessage(para_2,1);
                    }
                    break;
                case 1://唤醒激活
                    sendBlueMessage.sendBTblueMessage(progectBuildDevicePresenter.sendNBActiveORResiger(doEquipmentNum), 2);
                    break;
                case 2:
                    if (activeStatus == 2) {
                        progectBuildDevicePresenter.doTestEquipment(doRecordId,doEquipmentId,doEquipmentNum,doItemTypeId,doEquipmentName, 1);
                    } else {
                        progectBuildDevicePresenter.doactiveEquipment(doRecordId, 1);
                    }
                    showMsg("配置成功，等待激活完成");
                    break;
            }
        } else if (message.equals("fail")) {
            showMsg("配置失败,请重试");
        }

    }

    @Override
    public void showSettingMsg(String message) {

    }


    private final Handler btHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            showMsg("连接成功");
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(true);
                            String para_1 = progectBuildDevicePresenter.getFilesInfo(doEquipmentNum,fileName,0);
                            if(para_1 == null){//设置现场参数
                                showMsg("文件不存在，请返回项目列表重新加载");
                            }else{
                                sendBlueMessage.sendBTblueMessage(para_1,0);
                            }
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            showMsg("连接中...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            break;
                        case BluetoothChatService.STATE_NONE:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                            showMsg("连接失败");
                            break;
                        case BluetoothChatService.STATE_STOP:
                            BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
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
                    String mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    break;
                case MESSAGE_TOAST://连接丢失
                    BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                    showMsg("连接断开");
                    showMsg(msg.getData().getString(TOAST));
                    break;
            }
        }
    };

    @Override
    public void showMessage(String message) {

    }
}
