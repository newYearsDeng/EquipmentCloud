package com.northmeter.equipmentcloud.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.northmeter.equipmentcloud.I.I_ShowDeviceAdd;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.CommunicationPortResponse;
import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.ProjetTypeResponse;
import com.northmeter.equipmentcloud.enumBean.CommunicationEnum;
import com.northmeter.equipmentcloud.presenter.ProgectDeviceAddPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/2/28.
 * 新增设备
 */

public class ProgectDeviceAddActivity extends BaseActivity implements I_ShowDeviceAdd {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.tv_item_type_id)
    TextView tvItemTypeID;
    @BindView(R.id.tv_ipc_number)
    EditText tvIpcNumber;
    @BindView(R.id.tv_concentrator_name)
    EditText tvConcentratorName;
    @BindView(R.id.tv_collector_name)
    EditText tvCollectorName;
    @BindView(R.id.tv_device_plan)
    TextView tvDevicePlan;
    @BindView(R.id.tv_device_secret_key)
    EditText tvDeviceSecretKey;
    @BindView(R.id.tv_device_port)
    TextView tvDevicePort;
    @BindView(R.id.tv_device_needdown)
    TextView tvDeviceNeeddown;

    private int projectId, recordId;
    private String projectName;
    private ProgectDeviceAddPresenter progectDeviceAddPresenter;
    private ListView listview;
    private CommonAdapter commonAdapter;
    private List<ConfigurationPlanResponse.PlanBean> planDatas = new ArrayList();
    private List<ProjetTypeResponse.PageList> typeDatas = new ArrayList();
    List<CommunicationPortResponse.PortBean> portDatas = new ArrayList<>();
    private int isNeeddown=0;
    private String checkPort;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_device_add;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        projectName = getIntent().getStringExtra("projectName");
        recordId = getIntent().getIntExtra("recordId", 0);
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("新增设备");
    }

    @Override
    public void initData() {
        super.initData();
        progectDeviceAddPresenter = new ProgectDeviceAddPresenter(this);
        progectDeviceAddPresenter.getConfigurationPlan(projectName);
        progectDeviceAddPresenter.getProjetType();

        for (CommunicationEnum item : CommunicationEnum.values()) {
            portDatas.add(new CommunicationPortResponse.PortBean(item.getPortName(), item.getPortValue()));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_tb_back, R.id.btn_device_add_sure, R.id.tv_device_plan, R.id.tv_item_type_id,
            R.id.tv_device_port,R.id.tv_device_needdown})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.tv_device_plan://配置方案
                dialog_show(0);
                break;
            case R.id.tv_item_type_id://产品型号
                dialog_show(1);
                break;
            case R.id.tv_device_port://端口
                dialog_show(2);
                break;
            case R.id.tv_device_needdown://是否下载档案
                dialog_show(3);
                break;
            case R.id.btn_device_add_sure:
                progectDeviceAddPresenter.addBuildingequipment(tvDeviceName.getText().toString(),
                        tvItemTypeID.getText().toString(), recordId, projectId,
                        tvDeviceSecretKey.getText().toString(), checkPort,
                        tvIpcNumber.getText().toString(), tvConcentratorName.getText().toString(),
                        tvCollectorName.getText().toString(), tvDevicePlan.getText().toString(),isNeeddown);
                break;
        }
    }


    public void dialog_show(int planOrTypeChoice) {
        final AlertDialog dialogSex = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialog)).create();
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

        switch (planOrTypeChoice) {
            case 0:
                tv_toolbar_title.setText("配置方案");
                commonAdapter = new CommonAdapter<ConfigurationPlanResponse.PlanBean>(this, planDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, ConfigurationPlanResponse.PlanBean item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item.getConfigurationPlanName());
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvDevicePlan.setText(planDatas.get(position).getConfigurationPlanName());
                        dialogSex.cancel();
                    }
                });
                break;
            case 1:
                tv_toolbar_title.setText("产品型号");
                commonAdapter = new CommonAdapter<ProjetTypeResponse.PageList>(this, typeDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, ProjetTypeResponse.PageList item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item.getItemType() + "    " + item.getSmallCategoryName());
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvItemTypeID.setText(typeDatas.get(position).getItemType());
                        dialogSex.cancel();
                    }
                });
                break;
            case 2:
                tv_toolbar_title.setText("通讯端口");
                commonAdapter = new CommonAdapter<CommunicationPortResponse.PortBean>(this, portDatas, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, CommunicationPortResponse.PortBean item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item.getPortName());
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvDevicePort.setText(portDatas.get(position).getPortName());
                        checkPort = portDatas.get(position).getPortValue();
                        dialogSex.cancel();
                    }
                });
                break;
            case 3:
                final List<String[]> needdownList = new ArrayList();
                needdownList.add(new String[]{"N", "0"});
                needdownList.add(new String[]{"Y", "1"});

                tv_toolbar_title.setText("是否下载档案");
                commonAdapter = new CommonAdapter<String[]>(this, needdownList, R.layout.item_dialog_add_device) {
                    @Override
                    public void convert(ViewHolder helper, String[] item) {
                        helper.getTextViewSet(R.id.tv_add_plan_name, item[0]);
                    }
                };
                listview.setAdapter(commonAdapter);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvDeviceNeeddown.setText(needdownList.get(position)[0]);
                        isNeeddown = Integer.parseInt(needdownList.get(position)[1]);
                        dialogSex.cancel();
                    }
                });
                break;
        }
    }


    @Override
    public void showplanData(List<ConfigurationPlanResponse.PlanBean> datas) {
        if (!datas.isEmpty()) {
            this.planDatas.clear();
            this.planDatas.addAll(datas);
        }
    }

    @Override
    public void showTypeData(List<ProjetTypeResponse.PageList> datas) {
        if (!datas.isEmpty()) {
            this.typeDatas.clear();
            this.typeDatas.addAll(datas);
        }
    }

    @Override
    public void returnSuccess(String msg) {
        showMsg(msg);
        this.finish();
    }

    @Override
    public void returnFail(String msg) {
        showMsg(msg);
    }
}
