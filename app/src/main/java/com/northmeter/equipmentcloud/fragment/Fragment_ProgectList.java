package com.northmeter.equipmentcloud.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowProgectList;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.activity.ProgectBuildListActivity;
import com.northmeter.equipmentcloud.activity.ProgectManagementActivity;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseFragment;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.CommonResponse;
import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.presenter.ProgectListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd on 2019/1/2.
 * 项目列表
 */

public class Fragment_ProgectList extends BaseFragment implements I_ShowProgectList {

    private static Fragment_ProgectList fragment;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder;

    private CommonAdapter commonAdapter;
    private List<ProgectListResponse.PageList> datas = new ArrayList<ProgectListResponse.PageList>();
    private int getType;//请求的数据类型，已完成或未完成
    private ProgectListPresenter progectListPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_progect_list;
    }

    public static Fragment_ProgectList newInstance(int getType) {
        fragment = new Fragment_ProgectList();
        Bundle bundle = new Bundle();
        bundle.putInt("getType", getType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void startGetArgument(Bundle savedInstanceState) {
        getType = getArguments().getInt("getType");
    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        progectListPresenter = new ProgectListPresenter(getActivity(),this);
        progectListPresenter.getProgectList(getType);
        initListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initListView() {
        commonAdapter = new CommonAdapter<ProgectListResponse.PageList>(getActivity(), datas, R.layout.item_progrect_list) {
            @Override
            public void convert(ViewHolder helper, ProgectListResponse.PageList item) {
                if (getType == 0){
                    helper.getImageViewSet(R.id.iv_left_item,R.color.color_noalready);
                }else{
                    helper.getImageViewSet(R.id.iv_left_item,R.color.color_already);
                }
                helper.getTextViewSet(R.id.tv_progrect_name, item.getProjectName());
                helper.getTextViewSet(R.id.tv_progrect_unregister, String.valueOf(item.getEquipmentUnregistCount()));
                helper.getTextViewSet(R.id.tv_progrect_unactivate, String.valueOf(item.getEquipmentUnactivateCount()));
                helper.getTextViewSet(R.id.tv_progrect_equipment_count_1, "/"+item.getEquipmentCount()+"个");
                helper.getTextViewSet(R.id.tv_progrect_equipment_count_2, "/"+item.getEquipmentCount()+"个");
                helper.getTextViewSet(R.id.tv_progrect_time,item.getCreateTime());
            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProgectListResponse.PageList pageList = datas.get(position);
                progectListPresenter.getConfigurationPlan(pageList.getProjectName());

                Intent intent = new Intent(getActivity(),ProgectBuildListActivity.class);
                intent.putExtra("projectId",pageList.getRecordId());
                intent.putExtra("projectName",pageList.getProjectName());
                intent.putExtra("buildName",pageList.getProjectName());
                intent.putExtra("recordId",0);
                startActivity(intent);


            }
        });
    }

    @Override
    public void showData(List<ProgectListResponse.PageList> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        commonAdapter.notifyDataSetChanged();
        if(datas.isEmpty()){
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
