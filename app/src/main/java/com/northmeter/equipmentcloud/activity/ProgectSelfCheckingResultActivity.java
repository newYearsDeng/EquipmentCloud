package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.CommonResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 设备自检失败点明细
 */

public class ProgectSelfCheckingResultActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_selfchecking_total)
    TextView tvSelfcheckingTotal;

    private CommonAdapter commonAdapter;
    private List<CommonResponse> datas = new ArrayList<CommonResponse>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_selfchecking_result;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("失败点明细");
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initListView() {
        commonAdapter = new CommonAdapter<CommonResponse>(this, datas, R.layout.item_progect_selfchecking_result) {
            @Override
            public void convert(ViewHolder helper, CommonResponse item) {
                helper.getTextViewSet(R.id.tv_progrect_name, item.getMsg());
            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
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
}
