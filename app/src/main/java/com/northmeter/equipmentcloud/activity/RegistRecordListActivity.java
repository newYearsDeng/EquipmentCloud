package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.northmeter.equipmentcloud.I.I_ShowReturnData;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.DBRegistBean;
import com.northmeter.equipmentcloud.presenter.RegistRecordListPresenter;
import com.northmeter.equipmentcloud.sqlite.DBRegistHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/8/13.
 * 设备注册任务列表
 */
public class RegistRecordListActivity extends BaseActivity implements TextWatcher, I_ShowReturnData {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.et_search_name)
    EditText etSearchName;

    private CommonAdapter commonAdapter;
    private List<DBRegistBean> datas,datasBack,searchDatas;
    private DBRegistHelper registHelper;
    private RegistRecordListPresenter registRecordListPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist_record_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("本地注册记录");
    }

    @Override
    public void initData() {
        super.initData();
        etSearchName.addTextChangedListener(this);
        registRecordListPresenter = new RegistRecordListPresenter(this);
        datas = new ArrayList<>();
        datasBack = new ArrayList<>();
        searchDatas  = new ArrayList<>();
        initListview();
        getUnLoadRegist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_tb_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
        }
    }

    private void getUnLoadRegist(){
        registHelper = new DBRegistHelper(this);
        //查询未完成的设备注册任务，isUpdata字段的值为0表示未完成
        List<DBRegistBean> registBeans =  registHelper.queryByCondit("isUpdata","0");
        datas.clear();
        datas.addAll(registBeans);
        datasBack.addAll(registBeans);
    }

    private void initListview(){
        commonAdapter = new CommonAdapter<DBRegistBean>(this,datas,R.layout.item_regist_record_list) {
            @Override
            public void convert(ViewHolder helper, DBRegistBean item) {
                helper.getTextViewSet(R.id.tv_regist_tablenum,item.getEquipmentNum());
                helper.getTextViewSet(R.id.tv_regist_type,item.getItemTypeId());
                helper.getTextViewSet(R.id.tv_regist_addr,item.getEquipmentAddress());

                helper.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(registHelper.delete(item.getRecordId())){
                            commonAdapter.notifyDataSetChanged();
                        }
                    }
                });

                helper.getView(R.id.btn_updata).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        registRecordListPresenter.registereEquipment(item);
                    }
                });

            }
        };
        listview.setAdapter(commonAdapter);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String searchName = etSearchName.getText().toString();
        if (!datasBack.isEmpty()) {
            searchDatas.clear();
            for (DBRegistBean item : datasBack) {
                if (item.getEquipmentNum().indexOf(searchName) >= 0) {
                    searchDatas.add(item);
                }
            }
            datas.clear();
            datas.addAll(searchDatas);
            commonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void returnSuccess(String message) {
        showMsg(message);
        commonAdapter.notifyDataSetChanged();
    }

    @Override
    public void returnFail(String message) {
        showMsg(message);
    }
}
