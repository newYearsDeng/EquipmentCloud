package com.northmeter.equipmentcloud.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseFragment;
import com.northmeter.equipmentcloud.bean.CommonResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd on 2019/1/2.
 * 档案列表
 */

public class Fragment_RecordDownLoadList extends BaseFragment {

    private static Fragment_RecordDownLoadList fragment;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    Unbinder unbinder;

    private CommonAdapter commonAdapter;
    private List<CommonResponse> datas = new ArrayList<CommonResponse>();
    private int getType;//请求的数据类型，已完成或未完成

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_record_download_list;
    }

    public static Fragment_RecordDownLoadList newInstance(int getType) {
        fragment = new Fragment_RecordDownLoadList();
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
        commonAdapter = new CommonAdapter<CommonResponse>(getActivity(), datas, R.layout.item_record_download_list) {
            @Override
            public void convert(ViewHolder helper, CommonResponse item) {
                helper.getTextViewSet(R.id.et_record_name, item.getMsg());
            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
