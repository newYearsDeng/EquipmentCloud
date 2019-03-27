package com.northmeter.equipmentcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowBuildList;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ProgectBuildListRVAapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;
import com.northmeter.equipmentcloud.presenter.ProgectBuildListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/2/21.
 * 项目内建筑列表
 */

public class ProgectBuildListActivity extends BaseActivity implements I_ShowBuildList {
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.listview)
    RecyclerView listview;

    private int projectId,recordId;
    private String projectName,buildName;
    private Intent intent;
    private ProgectBuildListRVAapter adpter;
    private List<ProgectBuildListResponse.PageList> datas = new ArrayList<ProgectBuildListResponse.PageList>();
    private ProgectBuildListPresenter progectParkPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_buildlist;
    }

    @Override
    public void setTitle() {
        super.setTitle();
        String[] titleList = buildName.split("/");
        tvToolbarTitle.setText(titleList[titleList.length-1]);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        projectName = getIntent().getStringExtra("projectName");
        buildName = getIntent().getStringExtra("buildName");
        recordId  = getIntent().getIntExtra("recordId", 0);

        intent = new Intent();
        intent.putExtra("projectId", projectId);
    }

    @Override
    public void initData() {
        super.initData();
        progectParkPresenter = new ProgectBuildListPresenter(this);
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progectParkPresenter.getBuildList(projectId, recordId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initListView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listview.setLayoutManager(linearLayoutManager);
        listview.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));

        adpter = new ProgectBuildListRVAapter(datas);
        listview.setAdapter(adpter);

        adpter.setOnMyClickListener(new ProgectBuildListRVAapter.OnMyClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProgectBuildListResponse.PageList build = datas.get(position);
                Intent intent = new Intent();
                intent.putExtra("projectId", build.getProjectId());
                intent.putExtra("recordId", build.getRecordId());
                intent.putExtra("buildName", buildName+"/"+build.getBuildingName());
                intent.putExtra("projectName",projectName);
                switch(view.getId()){
                    case R.id.linear_buildlist://选择建筑后跳转
                        if(build.getType()==1){//状态码 1：最后一级  0：不是最后一级
                            goActivity(ProgectBuildDeviceActivity.class,intent);
                        }else{
                            goActivity(ProgectBuildListActivity.class,intent);
                        }
                        break;
                    case R.id.btn_testing://设备测试
                        progectParkPresenter.selfChecking(projectId,build.getRecordId());
                        break;
                    case R.id.btn_check://设备测试结果查询
                        goActivity(ProgectSelfCheckingResultActivity.class,intent);
                        break;

                }


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

    @Override
    public void showData(List<ProgectBuildListResponse.PageList> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        adpter.notifyDataSetChanged();
        if (datas.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void returnSuccess(String msg) {
        showMsg(msg);
    }

    @Override
    public void returnFail(String msg) {
        showMsg(msg);
    }
}
