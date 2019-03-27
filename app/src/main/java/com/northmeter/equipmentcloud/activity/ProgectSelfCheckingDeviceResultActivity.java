package com.northmeter.equipmentcloud.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowSingleSelfChecking;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.SingleSelfCheckingResultResponse;
import com.northmeter.equipmentcloud.presenter.ProgectSingleSelfCheckingResultPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 单个设备自检失败点明细
 */

public class ProgectSelfCheckingDeviceResultActivity extends BaseActivity implements I_ShowSingleSelfChecking {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.im_self_check_result)
    ImageView imSelfCheckResult;
    @BindView(R.id.tv_progrect_name)
    TextView tvProgrectName;
    @BindView(R.id.tv_progrect_address)
    TextView tvProgrectAddress;
    @BindView(R.id.tv_progrect_time)
    TextView tvProgrectTime;
    @BindView(R.id.image_camera_show)
    ImageView imageCameraShow;
    @BindView(R.id.data_value_title)
    TextView dataValueTitle;
    @BindView(R.id.data_value_show)
    TextView dataValueShow;

    private int projectId, recordId;
    private String equipmentName, equipmentId;

    private ProgectSingleSelfCheckingResultPresenter progectSingleSelfCheckingResultPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_selfchecking_result;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        recordId = getIntent().getIntExtra("recordId", 0);
        equipmentId = getIntent().getStringExtra("equipmentId");
        equipmentName = getIntent().getStringExtra("equipmentName");
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("设备自检结果");
    }

    @Override
    public void initData() {
        super.initData();
        progectSingleSelfCheckingResultPresenter = new ProgectSingleSelfCheckingResultPresenter(this);
        progectSingleSelfCheckingResultPresenter.getDeviceSelfCheckingResult(recordId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_tb_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
        }
    }


    @Override
    public void showData(SingleSelfCheckingResultResponse.ResultBean resultBean) {
        if (resultBean.getImplementResult() == 0) {
            imSelfCheckResult.setImageResource(R.drawable.self_device_result_true);
        } else {
            imSelfCheckResult.setImageResource(R.drawable.self_device_result_false);
        }
        tvProgrectName.setText(resultBean.getEquipmentName());
        tvProgrectAddress.setText(resultBean.getInstallAddress());
        tvProgrectTime.setText(resultBean.getEndTime());
        dataValueTitle.setText("识别结果");
        dataValueShow.setText(String.valueOf(resultBean.getDataValue()));

    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        imageCameraShow.setImageBitmap(bitmap);
    }

    @Override
    public void returnSuccess(String message) {
        showMsg(message);
    }

    @Override
    public void returnFail(String message) {
        showMsg(message);
        imSelfCheckResult.setImageResource(R.drawable.self_device_result_false);
    }
}
