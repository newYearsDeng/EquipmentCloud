package com.northmeter.equipmentcloud.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowDeviceAdd;
import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.ConfigurationPlanResponse;
import com.northmeter.equipmentcloud.bean.ProjetTypeResponse;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
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
    EditText tvDevicePort;

    private int projectId,recordId;
    private String projectName;
    private ProgectDeviceAddPresenter progectDeviceAddPresenter;
    private ListView listview;//方案列表
    private CommonAdapter commonAdapter;
    private List<ConfigurationPlanResponse.PlanBean> planDatas = new ArrayList();
    private List<ProjetTypeResponse.PageList> typeDatas = new ArrayList();

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_tb_back, R.id.btn_device_add_sure,R.id.tv_device_plan,R.id.tv_item_type_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.tv_device_plan://配置方案
                dialog_show(true);
                break;
            case R.id.tv_item_type_id://产品型号
                dialog_show(false);
                break;
            case R.id.btn_device_add_sure:
                progectDeviceAddPresenter.addBuildingequipment(tvDeviceName.getText().toString(),
                        tvItemTypeID.getText().toString(),recordId,projectId,
                        tvDeviceSecretKey.getText().toString(),tvDevicePort.getText().toString(),
                        tvIpcNumber.getText().toString(),tvConcentratorName.getText().toString(),
                        tvCollectorName.getText().toString(),tvDevicePlan.getText().toString());
                break;
        }
    }


    public void dialog_show(boolean planOrTypeChoice) {
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
        if(planOrTypeChoice){
            tv_toolbar_title.setText("配置方案");
            commonAdapter = new CommonAdapter<ConfigurationPlanResponse.PlanBean>(this, planDatas, R.layout.item_dialog_add_device) {
                @Override
                public void convert(com.northmeter.equipmentcloud.adapter.ViewHolder helper, ConfigurationPlanResponse.PlanBean item) {
                    helper.getTextViewSet(R.id.tv_add_plan_name,item.getConfigurationPlanName());
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
        }else{
            tv_toolbar_title.setText("产品型号");
            commonAdapter = new CommonAdapter<ProjetTypeResponse.PageList>(this, typeDatas, R.layout.item_dialog_add_device) {
                @Override
                public void convert(com.northmeter.equipmentcloud.adapter.ViewHolder helper, ProjetTypeResponse.PageList item) {
                    helper.getTextViewSet(R.id.tv_add_plan_name,item.getItemType()+"    "+item.getSmallCategoryName());
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
        }


    }


    @Override
    public void showplanData(List<ConfigurationPlanResponse.PlanBean> datas) {
        if (!datas.isEmpty()){
            this.planDatas.clear();
            this.planDatas.addAll(datas);
        }
    }

    @Override
    public void showTypeData(List<ProjetTypeResponse.PageList> datas) {
        if (!datas.isEmpty()){
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
