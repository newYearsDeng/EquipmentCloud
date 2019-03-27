package com.northmeter.equipmentcloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowDownLoad;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseFragment;
import com.northmeter.equipmentcloud.bean.RecordDownLoadResponse;
import com.northmeter.equipmentcloud.presenter.ProgectRecordDownLoadPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dyd on 2019/1/2.
 * 档案列表
 */

public class Fragment_RecordDownLoadList extends BaseFragment implements I_ShowDownLoad {

    private static Fragment_RecordDownLoadList fragment;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_search_name)
    EditText etSearchName;

    private CommonAdapter commonAdapter;
    private List<RecordDownLoadResponse.ConcentratorList> datas = new ArrayList<>();
    private int getType;//请求的数据类型，已完成或未完成
    private ProgectRecordDownLoadPresenter progectRecordDownLoadPresenter;
    private int projectId;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_record_download_list;
    }

    public static Fragment_RecordDownLoadList newInstance(int getType, int projectId) {
        fragment = new Fragment_RecordDownLoadList();
        Bundle bundle = new Bundle();
        bundle.putInt("getType", getType);
        bundle.putInt("projectId", projectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void startGetArgument(Bundle savedInstanceState) {
        getType = getArguments().getInt("getType");
        projectId = getArguments().getInt("projectId");
    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        initListView();
        progectRecordDownLoadPresenter = new ProgectRecordDownLoadPresenter(getActivity(), this);
        progectRecordDownLoadPresenter.getRecordDownLoadList(projectId, "");
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
        commonAdapter = new CommonAdapter<RecordDownLoadResponse.ConcentratorList>(getActivity(), datas, R.layout.item_record_download_list) {
            @Override
            public void convert(ViewHolder helper, RecordDownLoadResponse.ConcentratorList item) {
                helper.getTextViewSet(R.id.et_record_name, item.getConcentratorName());
                helper.getCheckViewSet(R.id.checkbox_record, item.isCheck);
            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecordDownLoadResponse.ConcentratorList item = datas.get(position);
                if (item.isCheck) {
                    item.setCheck(false);
                } else {
                    item.setCheck(true);
                }
                commonAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void showData(List<RecordDownLoadResponse.ConcentratorList> datas) {
        datas.clear();
        datas.addAll(datas);
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

    @OnClick({R.id.btn_submit,R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_empty:
                progectRecordDownLoadPresenter.upRecordDownLoadList(projectId, datas);
                break;
            case R.id.tv_search://搜索
                progectRecordDownLoadPresenter.getRecordDownLoadList(projectId, etSearchName.getText().toString());
                break;
        }
    }
}
