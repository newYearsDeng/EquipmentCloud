package com.northmeter.equipmentcloud.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.northmeter.equipmentcloud.I.I_ShowBuildList;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.ProgectBuildListRVAapter;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;
import com.northmeter.equipmentcloud.presenter.ProgectBuildListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/2/21.
 * 项目内建筑列表
 */

public class ProgectBuildListActivity extends BaseActivity implements XRefreshView.XRefreshViewListener, I_ShowBuildList {
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.x_refresh_view)
    XRefreshView xRefreshView;
    @BindView(R.id.tv_progect_build_address)
    TextView tvProgectBuildAddress;

    private int projectId, recordId;
    private String projectName, buildName;
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
        tvToolbarTitle.setText(projectName);
        tvProgectBuildAddress.setText(buildName);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        projectName = getIntent().getStringExtra("projectName");
        buildName = getIntent().getStringExtra("buildName");
        recordId = getIntent().getIntExtra("recordId", 0);

        intent = new Intent();
        intent.putExtra("projectId", projectId);
    }

    @Override
    public void initData() {
        super.initData();
        progectParkPresenter = new ProgectBuildListPresenter(this);
        initRefresh();
        initListView();
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
        progectParkPresenter.getBuildList(projectId, recordId);
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
                intent.putExtra("projectId", projectId);
                intent.putExtra("recordId", build.getRecordId());
                intent.putExtra("buildName", buildName + "/" + build.getBuildingName());
                intent.putExtra("projectName", projectName);
                switch (view.getId()) {
                    case R.id.linear_buildlist://选择建筑后跳转
                        if (build.getType() == 1) {//状态码 1：最后一级  0：不是最后一级
                            goActivity(ProgectBuildDeviceActivity.class, intent);
                        } else {
                            goActivity(ProgectBuildListActivity.class, intent);
                        }
                        break;
                    case R.id.btn_testing://设备测试
                        showMsg("权限不足，暂未开放");
                        //progectParkPresenter.selfChecking(projectId, build.getRecordId());
                        break;
                    case R.id.btn_check://设备测试结果查询
                        showMsg("权限不足，暂未开放");
                        //goActivity(ProgectSelfCheckingResultActivity.class, intent);
                        break;

                }
            }
        });
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
        xRefreshView.stopRefresh();
        this.datas.clear();
        this.datas.addAll(datas);
        adpter.notifyDataSetChanged();
        if (datas.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnSuccess(String msg) {
        xRefreshView.stopRefresh();
        showMsg(msg);
    }

    @Override
    public void returnFail(String msg) {
        xRefreshView.stopRefresh();
        showMsg(msg);
    }

}
