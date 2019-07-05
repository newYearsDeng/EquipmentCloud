package com.northmeter.equipmentcloud.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowBlueSend;
import com.northmeter.equipmentcloud.I.I_ShowFilesInBTSetting;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseFragment;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.EvenBusBean;
import com.northmeter.equipmentcloud.bean.LocalConfigurationPlanBean;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_UniqueInstance;
import com.northmeter.equipmentcloud.bluetooth.SendBlueMessage;
import com.northmeter.equipmentcloud.enumBean.EvenBusEnum;
import com.northmeter.equipmentcloud.presenter.Fragment_NBMeter_SettingPresenter;
import com.northmeter.equipmentcloud.utils.Udp_Help;
import com.northmeter.equipmentcloud.widget.PromptHelper;
import com.northmeter.equipmentcloud.widget.TimeDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dyd on 2017/8/9.
 * 无线摄像模组中 表端的参数设置
 */
public class Fragment_NBMeter_Setting extends BaseFragment implements I_ShowBlueSend,
        DialogInterface.OnDismissListener, I_ShowFilesInBTSetting {

    private static Fragment_NBMeter_Setting fragment;
    @BindView(R.id.et_exposure_time)
    EditText etExposureTime;
    @BindView(R.id.tv_exposure_setting)
    TextView tvExposureSetting;
    @BindView(R.id.et_contrast_ratio)
    EditText etContrastRatio;
    @BindView(R.id.et_compression_ratio)
    EditText etCompressionRatio;
    @BindView(R.id.tv_flash_state)
    TextView tvFlashState;
    @BindView(R.id.tv_reporting_type)
    TextView tvReportingType;
    @BindView(R.id.tv_reporting_date)
    TextView tvReportingDate;
    @BindView(R.id.tv_reporting_time)
    TextView tvReportingTime;
    @BindView(R.id.et_space_time)
    EditText etSpaceTime;
    @BindView(R.id.et_random_time)
    EditText etRandomTime;
    @BindView(R.id.tv_photo_time)
    TextView tvPhotoTime;
    Unbinder unbinder;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.tv_choose_plan)
    TextView tvChoosePlan;
    @BindView(R.id.et_coordinate_x)
    EditText etCoordinateX;
    @BindView(R.id.et_coordinate_y)
    EditText etCoordinateY;
    @BindView(R.id.et_coordinate_xlong)
    EditText etCoordinateXlong;
    @BindView(R.id.et_coordinate_ylong)
    EditText etCoordinateYlong;
    @BindView(R.id.et_postal_address)
    EditText etPostalAddress;
    @BindView(R.id.et_postal_address_new)
    EditText etPostalAddressNew;
    @BindView(R.id.btn_postal_address_select)
    Button btnPostalAddressSelect;
    @BindView(R.id.btn_postal_address_change)
    Button btnPostalAddressChange;
    @BindView(R.id.layout_postal_control)
    LinearLayout layoutPostalControl;

    private SendBlueMessage sendBlueMessage;
    private int projectId;
    private String equipmentNum;
    private String exposure_setting;//曝光设置，自动或者手动曝光
    private String str_Enable = "34";//使能位,参考起始时间上报或者忽略起始时间上报，禁能
    private Fragment_NBMeter_SettingPresenter settingPresenter;
    private ListView listview;//方案列表
    private CommonAdapter commonAdapter;
    private List<LocalConfigurationPlanBean.PlanBean> planDatas = new ArrayList();
    private boolean checkORselect = true;
    private boolean showOrHintPostal = false;

    public static Fragment_NBMeter_Setting newInstance(int getType) {
        fragment = new Fragment_NBMeter_Setting();
        Bundle bundle = new Bundle();
        bundle.putInt("getType", getType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_installing_setting;
    }

    @Override
    protected void startGetArgument(Bundle savedInstanceState) {
        projectId = getActivity().getIntent().getIntExtra("projectId",0);
        equipmentNum = getActivity().getIntent().getStringExtra("equipmentNum");
        BlueTooth_UniqueInstance.getInstance().setTableNum(equipmentNum);

        sendBlueMessage = new SendBlueMessage(this);

        settingPresenter = new Fragment_NBMeter_SettingPresenter(getActivity(), this);
        //获取文件列表
        settingPresenter.getFilesAllName(Constants.filePath+projectId);
    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_exposure_setting, R.id.tv_flash_state, R.id.tv_reporting_type, R.id.tv_reporting_date,
            R.id.tv_reporting_time, R.id.btn_sure, R.id.tv_choose_plan, R.id.tv_photo_time, R.id.btn_check,
            R.id.btn_postal_address_select, R.id.btn_postal_address_change,R.id.layout_postal_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_postal_show://显示或隐藏修改通讯地址的操作面板
                if(showOrHintPostal){
                    showOrHintPostal = false;
                    layoutPostalControl.setVisibility(View.GONE);
                }else{
                    showOrHintPostal = true;
                    layoutPostalControl.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.tv_exposure_setting://曝光设置
                dialog_show(1);
                break;
            case R.id.tv_flash_state://补光灯状态
                dialog_show(2);
                break;
            case R.id.tv_reporting_type://上报设置（参考或忽略起始时间上报）
                dialog_show(3);
                break;
            case R.id.tv_reporting_date://上报起始日期
                PromptHelper.createTimePicker(getContext(), true, true, true, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String year_ = year + "";
                        String startDate = year_.substring(year_.length() - 2, year_.length()) + "-" + String.format("%02d", monthOfYear + 1) + "-"
                                + String.format("%02d", dayOfMonth);

                        tvReportingDate.setText(startDate);
                    }
                }, this);
                break;
            case R.id.tv_reporting_time://上报起始时间
                new TimeDialog(getActivity()).showTimeDialog(new TimeDialog.SetTimeData() {
                    @Override
                    public void setTimeData(String timeData) {
                        tvReportingTime.setText(timeData);
                    }
                });
                break;
            case R.id.tv_photo_time://拍照时间
                new TimeDialog(getActivity()).showTimeDialog(new TimeDialog.SetTimeData() {
                    @Override
                    public void setTimeData(String timeData) {
                        tvPhotoTime.setText(timeData.substring(0, timeData.length() - 3));
                    }
                });
                break;
            case R.id.btn_check:
                startLoadingDialog();
                checkORselect = false;
                getSelectSetting("4c3310ef", 1);
                break;
            case R.id.btn_sure:
                startLoadingDialog();
                checkORselect = true;
                sendBlueMessage.sendBTblueMessage(get_setting(), 1);
                break;
            case R.id.tv_choose_plan:
                dialog_show(0);
                break;
            case R.id.btn_postal_address_select://查询通讯地址
                startLoadingDialog();
                String para = "68AAAAAAAAAAAA68130000DF16";
                sendBlueMessage.sendBTblueMessage(para, 2);
                break;
            case R.id.btn_postal_address_change://修改通讯地址
                startLoadingDialog();
                String postalAdd = Udp_Help.reverseRst(etPostalAddress.getText().toString());
                String newPostalAdd = Udp_Help.reverseRst(etPostalAddressNew.getText().toString());

                String para_change = "68" + postalAdd +
                        "68150600" + Udp_Help.create_645ToHex(newPostalAdd);
                String cs = Udp_Help.get_sum(para_change).toUpperCase() + "16";
                String sendMsg = "FEFEFEFE" + para_change + cs;
                sendBlueMessage.sendBTblueMessage(sendMsg, 2);
                break;
        }
    }

    @Override
    public void showData(List<LocalConfigurationPlanBean.PlanBean> data) {
        planDatas.clear();
        planDatas.addAll(data);
    }

    @Override
    public void returnMessage(String msg) {
        showMsg(msg);
    }


    public void dialog_show(int showState) {
        final AlertDialog dialogSex = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialog)).create();
        dialogSex.show();
        Window window = dialogSex.getWindow();
        window.setContentView(R.layout.dialog_device_add);

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);

        dialogSex.setCanceledOnTouchOutside(true);
        dialogSex.setCancelable(true);
        window.setWindowAnimations(R.style.AnimBottom_Dialog);

        TextView tv_toolbar_title = window.findViewById(R.id.tv_toolbar_title);

        window.findViewById(R.id.btn_dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSex.cancel();
            }
        });

        window.findViewById(R.id.dialog_layout).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogSex.cancel();
            }
        });

        window.findViewById(R.id.btn_dialog_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSex.cancel();
            }
        });

        listview = window.findViewById(R.id.listview);

        switch (showState) {
            case 0://配置方案
                tv_toolbar_title.setText("配置方案");
                commonAdapter = new CommonAdapter<LocalConfigurationPlanBean.PlanBean>(getActivity(), planDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, LocalConfigurationPlanBean.PlanBean item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item.getName());
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvChoosePlan.setText(planDatas.get(position).getName());
                        dialogSex.cancel();
                        setConfigure(settingPresenter.getFilesInfo(projectId,planDatas.get(position).getName()));
                    }
                });
                break;
            case 1://曝光设置
                tv_toolbar_title.setText("曝光设置");
                final List<String> exposureDatas = new ArrayList();
                exposureDatas.add("自动曝光");
                exposureDatas.add("手动曝光");
                commonAdapter = new CommonAdapter<String>(getActivity(), exposureDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, String item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item);
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvExposureSetting.setText(exposureDatas.get(position));
                        dialogSex.cancel();
                    }
                });
                break;
            case 2://补光灯状态
                tv_toolbar_title.setText("补光灯状态");
                final List<String> flashStateDatas = new ArrayList();
                flashStateDatas.add("开启自动补光");
                flashStateDatas.add("关闭自动补光");
                flashStateDatas.add("开启手动补光");
                flashStateDatas.add("关闭手动补光");
                commonAdapter = new CommonAdapter<String>(getActivity(), flashStateDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, String item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item);
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvFlashState.setText(flashStateDatas.get(position));
                        dialogSex.cancel();
                    }
                });
                break;
            case 3://上报类型
                tv_toolbar_title.setText("上报类型");
                final List<String[]> reportingDatas = new ArrayList();
                reportingDatas.add(new String[]{"参考起始时间上报", "34"});
                reportingDatas.add(new String[]{"忽略起始时间上报", "35"});
                reportingDatas.add(new String[]{"禁能", "EE"});
                commonAdapter = new CommonAdapter<String[]>(getActivity(), reportingDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, String[] item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item[0]);
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvReportingType.setText(reportingDatas.get(position)[0]);
                        str_Enable = reportingDatas.get(position)[1];
                        dialogSex.cancel();
                    }
                });
                break;
        }
    }

    private void setConfigure(List<String> paraList){
        if(paraList.size()>=15){
            etExposureTime.setText(paraList.get(0));
            tvExposureSetting.setText(paraList.get(1));
            etCoordinateX.setText(paraList.get(2));
            etCoordinateY.setText(paraList.get(3));
            etCoordinateXlong.setText(paraList.get(4));
            etCoordinateYlong.setText(paraList.get(5));
            etContrastRatio.setText(paraList.get(6));
            etCompressionRatio.setText(paraList.get(7));
            tvFlashState.setText(paraList.get(8));
            tvReportingType.setText(paraList.get(9));
            tvReportingDate.setText(paraList.get(10));
            tvReportingTime.setText(paraList.get(11));
            etSpaceTime.setText(paraList.get(12));
            etRandomTime.setText(paraList.get(13));
            tvPhotoTime.setText(paraList.get(14));
        }
    }

    private void getSelectSetting(String status, int state) {
        String firstpara = "68" + Udp_Help.reverseRst(BlueTooth_UniqueInstance.getInstance().getTableNum()) +
                "68110400" + status;
        String cs = Udp_Help.get_sum(firstpara).toUpperCase() + "16";
        String last = "FEFEFEFE" + firstpara + cs;
        sendBlueMessage.sendBTblueMessage(last, state);
    }


    private String get_setting() {//4c3310ef 现场参数设置
        String para_flash = "DDDD";
        if (tvExposureSetting.getText().toString().equals("自动曝光")) {
            exposure_setting = "DD";//自动曝光
        } else {
            exposure_setting = "EE";//手动曝光
        }

        switch (tvFlashState.getText().toString()) {
            case "开启自动补光":
                para_flash = "DDDD";//开启
                break;
            case "关闭自动补光":
                para_flash = "DD88";//关闭
                break;
            case "开启手动补光":
                para_flash = "EEDD";//开启
                break;
            case "关闭手动补光":
                para_flash = "EE88";//关闭
                break;
        }

        String para_x = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645
                (etCoordinateX.getText().toString()));
        String para_y = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645
                (etCoordinateY.getText().toString()));
        String para_xl = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645
                (etCoordinateXlong.getText().toString()));
        String para_yl = Udp_Help.reverseRst(Udp_Help.get_came_hexTo645
                (etCoordinateYlong.getText().toString()));

        String para_str = para_x + para_y + para_xl + para_yl;


        String para_set = "68" + Udp_Help.reverseRst(BlueTooth_UniqueInstance.getInstance().getTableNum()) +
                "68141B004c3310ef" + Constants.HandlerKey +
                Udp_Help.get_Stting_HexTo645(etExposureTime.getText().toString()) +
                exposure_setting +
                para_str +
                Udp_Help.get_Stting_HexTo645(etContrastRatio.getText().toString()) +
                "DD" + Udp_Help.get_Stting_HexTo645(etCompressionRatio.getText().toString()) +
                para_flash;

        return getSendString(para_set);
    }

    private String get_TimeSet() {//38343337
        String para_Time = "68" + Udp_Help.reverseRst(BlueTooth_UniqueInstance.getInstance().getTableNum()) +
                "68141B0038343337" + Constants.HandlerKey +
                str_Enable +
                Udp_Help.get_NBTimeTo645(tvReportingDate.getText().toString(), 0) + "33" +
                Udp_Help.get_NBTimeTo645(tvReportingTime.getText().toString(), 1) +
                Udp_Help.reverseRst(Udp_Help.get_NBMinTo645(etSpaceTime.getText().toString())) +
                Udp_Help.get_Stting_HexTo645(etRandomTime.getText().toString()) +
                Udp_Help.get_NBTimeTo645(tvPhotoTime.getText().toString(), 1);

        return getSendString(para_Time);
    }

    private String getSendString(String prar) {
        String cs = Udp_Help.get_sum(prar).toUpperCase() + "16";
        String sendMsg = "FEFEFEFE" + prar + cs;
        return sendMsg;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void showMessage(String message) {
        showMsg(message);
        if (message.equals("蓝牙未连接")) {
            stopLoadingDialog();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String blueMsg = (String) msg.obj;
            int control_state = BlueTooth_UniqueInstance.getInstance().getState();
            switch (control_state) {
                case 1:
                    if (blueMsg.equals("success")) {
                        if (checkORselect) {
                            sendBlueMessage.sendBTblueMessage(get_TimeSet(), 2);
                        }
                    } else if (blueMsg.equals("fail")) {
                        stopLoadingDialog();
                        showMsg("操作失败");
                    }
                    break;
                case 2:
                    stopLoadingDialog();
                    if (blueMsg.equals("success")) {
                        showMsg("操作成功");
                        return;
                    } else if (blueMsg.equals("fail")) {
                        showMsg("操作失败");
                        return;
                    }
                    break;
            }

            if (blueMsg.length() >= 24) {
                String control = blueMsg.substring(22, 24).toUpperCase();
                switch (control) {//3DDD33333333733423336FDD6FDDDD
                    case "91":
                        String msgflag = blueMsg.substring(28, 36).toUpperCase();
                        String data = blueMsg.substring(36, blueMsg.length() - 4);
                        switch (msgflag) {
                            case "4C3310EF":
                                getSelectSetting("38343337", 2);//第二条查询命令
                                //曝光时间
                                String exposureTime = data.substring(0, 2);
                                etExposureTime.setText(String.valueOf(Integer.valueOf(Udp_Help.get_645ToHex(exposureTime), 16)));
                                //曝光设置
                                if (data.substring(2, 4).equals("DD")) {
                                    tvExposureSetting.setText("自动曝光");
                                } else {
                                    tvExposureSetting.setText("手动曝光");
                                }
                                //取景坐标
                                String et_coordinate_xy = data.substring(4, 20);
                                String x = ("" + Integer.parseInt(Udp_Help.get_645ToHex(
                                        Udp_Help.reverseRst(et_coordinate_xy.substring(0, 4))), 16));
                                String y = ("" + Integer.parseInt(Udp_Help.get_645ToHex(
                                        Udp_Help.reverseRst(et_coordinate_xy.substring(4, 8))), 16));
                                String xLong = ("" + Integer.parseInt(Udp_Help.get_645ToHex(
                                        Udp_Help.reverseRst(et_coordinate_xy.substring(8, 12))), 16));
                                String yLong = ("" + Integer.parseInt(Udp_Help.get_645ToHex(
                                        Udp_Help.reverseRst(et_coordinate_xy.substring(12, 16))), 16));
                                etCoordinateX.setText(x);
                                etCoordinateY.setText(y);
                                etCoordinateXlong.setText(xLong);
                                etCoordinateYlong.setText(yLong);

                                //对比度
                                String et_contrast_ratio = data.substring(20, 22);
                                etContrastRatio.setText(String.valueOf(Integer.valueOf(Udp_Help.get_645ToHex(et_contrast_ratio), 16)));

                                //压缩率
                                String et_compression_ratio = data.substring(22, 26);
                                String data_ys = et_compression_ratio.substring(et_compression_ratio.length() - 2, et_compression_ratio.length());
                                etCompressionRatio.setText(String.valueOf(Integer.valueOf(Udp_Help.get_645ToHex(data_ys), 16)));

                                //补光灯状态
                                String tv_flash_state = data.substring(26, 30);
                                String state = Udp_Help.get_645ToHex(tv_flash_state).toUpperCase();
                                switch (state) {
                                    case "AAAA"://0xaa：自动补光模式  0xaa：开启补光灯
                                        tvFlashState.setText("自动补光已开启");
                                        break;
                                    case "BBAA":
                                        tvFlashState.setText("手动补光已开启");
                                        break;
                                    case "AA55":
                                        tvFlashState.setText("自动补光已关闭");
                                        break;
                                    case "BB55"://0xbb：手动补光模式 0x55：关闭补光灯
                                        tvFlashState.setText("手动补光已关闭");
                                        break;
                                    default:
                                        tvFlashState.setText(state);
                                        break;
                                }

                                break;
                            case "38343337":
                                //使能位
                                String resultTime = Udp_Help.get_645ToHex(data.substring(0, 24));
                                switch (resultTime.substring(0, 2)) {
                                    case "01":
                                        tvReportingType.setText("参考起始时间上报");
                                        str_Enable = "34";
                                        break;
                                    case "02":
                                        tvReportingType.setText("忽略起始时间上报");
                                        str_Enable = "35";
                                        break;
                                    default:
                                        tvReportingType.setText("禁能");
                                        str_Enable = "EE";
                                        break;
                                }

                                //日期
                                tvReportingDate.setText(Udp_Help.get_NBTimeToStr(resultTime.substring(2, 8), "-"));
                                tvReportingTime.setText(Udp_Help.get_NBTimeToStr(resultTime.substring(10, 16), ":"));
                                etSpaceTime.setText(String.valueOf(Integer.valueOf(Udp_Help.reverseRst(resultTime.substring(16, 24)), 16)));

                                //随机退避时间
                                String et_random_time = data.substring(24, 26);
                                etRandomTime.setText(String.valueOf(Integer.valueOf(Udp_Help.get_645ToHex(et_random_time), 16)));
                                //拍照时间
                                String et_photo_time = Udp_Help.get_645ToHex(data.substring(26, 30));
                                tvPhotoTime.setText(Udp_Help.get_NBPhotoTimeToStr(et_photo_time, ":"));
                                break;

                        }
                        break;
                    case "93"://FEFEFE6873090818200368930600A63C3B4B53361916
                        String dataPostal = blueMsg.substring(28, blueMsg.length() - 4);
                        String tableNum = Udp_Help.reverseRst(Udp_Help.get_645ToHex(dataPostal));
                        etPostalAddress.setText(tableNum);
                        etPostalAddressNew.setText(tableNum);
                        break;
                }

            }

        }
    };


    /**
     * 4.事件订阅者处理事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EvenBusBean evenBusBean) {
        String topic = evenBusBean.getTopic();
        if(topic.equals(EvenBusEnum.EvenBus_NBMeter_Setting.getEvenName())){
            String data = evenBusBean.getData();

            Message msg = handler.obtainMessage(1);
            msg.obj = data;
            Fragment_NBMeter_Setting.this.handler.sendMessage(msg);
        }

    }


}
