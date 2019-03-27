package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.northmeter.equipmentcloud.I.I_ShowRecordImport;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
import com.northmeter.equipmentcloud.presenter.ProgectSelfCheckingPresenter;
import com.northmeter.equipmentcloud.widget.PopupHelper;
import com.northmeter.equipmentcloud.widget.WidgetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 设备自检
 */

public class ProgectSelfCheckingActivity extends BaseActivity implements I_ShowRecordImport {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.linear_contain)
    LinearLayout linearContain;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;

    private ListView listview;//点击建筑按钮是显示的列表
    private int projectId;
    private String projectName;
    private View view;
    private PopupWindow popupWindow;
    private Gson gson;
    private JsonParser parser;
    private Map btnAndListMap;
    private Map<Object,RecordImportResponse.BuildList> btnSelectMap;//存储选择的建筑信息
    private Map<Integer,Integer> typeList;//判断当前btnSelectMap相同key值的建筑信息的下级建筑是设备还是建筑
    private CommonAdapter commonAdapter;
    private List<RecordImportResponse.BuildList> buildDatas;

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private List<View> viewList;
    private List<ViewHolder> viewHolderList;
    private int mark = 0;
    private static int checkViewHolder = 0;

    private ProgectSelfCheckingPresenter progectSelfCheckingPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_record_import;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId", 0);
        projectName = getIntent().getStringExtra("projectName");
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("设备自检");
        tvRightText.setText("查询");
        btnSubmit.setText("立即自检");
    }

    @Override
    public void initData() {
        super.initData();
        progectSelfCheckingPresenter = new ProgectSelfCheckingPresenter(this);
        progectSelfCheckingPresenter.getRecordImportBuildList(projectId);

        viewList = new ArrayList<>();
        viewHolderList = new ArrayList<>();

        gson = new Gson();
        parser = new JsonParser();
        buildDatas = new ArrayList<>();
        btnAndListMap = new HashMap<>();
        btnSelectMap = new HashMap();
        typeList = new HashMap<>();

        initpopupWindow();

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

    @OnClick({R.id.btn_tb_back, R.id.btn_submit,R.id.tv_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.btn_submit://立即自检
                progectSelfCheckingPresenter.saveProgectSelfChecking(projectId,btnSelectMap.get(mark-1).getRecordId());
                break;
            case R.id.tv_right_text://结果查询
                Intent intent = new Intent();
                intent.putExtra("projectId",projectId);
                intent.putExtra("projectName",projectName);
                goActivity(ProgectSelfCheckingResultActivity.class,intent);
                break;
        }
    }

    private void initpopupWindow() {
        view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.item_progect_import_poput, null);
        popupWindow = new PopupHelper().getWindow_ALLWRAP(view, getApplicationContext());
        popupWindow.setWidth((int) (WidgetHelper.getWindowWidth(this)));

        listview = view.findViewById(R.id.listview);
        commonAdapter = new CommonAdapter<RecordImportResponse.BuildList>(this, buildDatas, R.layout.item_progect_import_poput_view) {
            @Override
            public void convert(com.northmeter.equipmentcloud.adapter.ViewHolder helper, RecordImportResponse.BuildList item) {
                if(typeList.get(checkViewHolder)==0){
                    helper.getTextViewSet(R.id.tv_build_addr, item.getBuildingName());
                }else{
                    helper.getTextViewSet(R.id.tv_build_addr, item.getEquipmentName());
                }
            }
        };

        listview.setAdapter(commonAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(typeList.get(checkViewHolder)==0){
                    viewHolderList.get(checkViewHolder).tv_progect_addr.setText(buildDatas.get(position).getBuildingName());
                }else{
                    viewHolderList.get(checkViewHolder).tv_progect_addr.setText(buildDatas.get(position).getEquipmentName());
                }
                btnSelectMap.put(checkViewHolder,buildDatas.get(position));
                //获取点击的建筑列表，列表也是带child的json数据
                RecordImportResponse.BuildList listData = gson.fromJson(gson.toJson(buildDatas.get(position)), RecordImportResponse.BuildList.class);
                //解析child中的json数据，并默认选择第一个建筑的名字写入按钮中
                if(listData.getChild()!=null&&listData.getChild().size()>0){
                    if(listData.getType()==0){//建筑级别(type：1表示下一级是设备，0表示是建筑)
                        initAddView(mark, gson.fromJson(gson.toJson(listData.getChild().get(0)), RecordImportResponse.BuildList.class).getBuildingName());
                        btnAndListMap.put(mark, listData.getChild());
                        typeList.put(mark,listData.getType());
                        btnSelectMap.put(mark, gson.fromJson(gson.toJson(listData.getChild().get(0)), RecordImportResponse.BuildList.class));
                        mark++;
                    }else if(listData.getType()==1){//设备级别
                        initAddView(mark, gson.fromJson(gson.toJson(listData.getChild().get(0)), RecordImportResponse.BuildList.class).getEquipmentName());
                        btnAndListMap.put(mark, listData.getChild());
                        typeList.put(mark,listData.getType());
                        btnSelectMap.put(mark, gson.fromJson(gson.toJson(listData.getChild().get(0)), RecordImportResponse.BuildList.class));
                        mark++;
                    }

                }
                popupWindow.dismiss();
            }
        });
    }

    private void initAddView(int mark, String buildName) {
        View addView = LayoutInflater.from(ProgectSelfCheckingActivity.this).inflate(R.layout.item_progect_import_view, null);
        addView.setId(mark);
        linearContain.addView(addView, mark);
        getViewInstance(addView, buildName);
    }

    private void getViewInstance(View view, String buildName) {
        ViewHolder vh = new ViewHolder();
        vh.id = view.getId();
        vh.relative_content = (RelativeLayout) view.findViewById(R.id.relative_content);
        vh.tv_progect_addr = (TextView) view.findViewById(R.id.tv_progect_addr);
        vh.tv_progect_addr.setText(buildName);
        // 设置监听
        vh.relative_content.setOnClickListener(selectProListener);
        viewHolderList.add(vh);
        viewList.add(view);

    }

    public class ViewHolder {
        private int id = -1;
        private RelativeLayout relative_content;
        private TextView tv_progect_addr;
    }

    View.OnClickListener selectProListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View view = (View) v.getParent();
            for (int i = 0; i < linearContain.getChildCount(); i++) {

                ViewHolder viewHolder = viewHolderList.get(i);
                Log.v("Import", "view.getId()==" + view.getId() + "  viewHolder.id==" + viewHolder.id);
                if (view.getId() == viewHolder.id) {
                    removeViews(i);
                    checkViewHolder = i;
                    System.out.println(view.getId() + "/" + viewHolder.id);
                    popupWindow.showAsDropDown(v, 0, 0, Gravity.CENTER);
                    List<RecordImportResponse.BuildList> data = (List<RecordImportResponse.BuildList>) btnAndListMap.get(view.getId());
                    buildDatas.clear();
                    buildDatas.addAll(data);
                    commonAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void removeViews(int index){
        int childNum = linearContain.getChildCount();
        for(int i=childNum-1;i>=0;i--){
            if(i > index){
                linearContain.removeViewAt(i);
                viewHolderList.remove(i);
                viewList.remove(i);
            }
        }
        mark = index+1;
    }


    @Override
    public void showData(List<RecordImportResponse.BuildList> data) {
        if (!data.isEmpty()) {
            initAddView(mark, data.get(0).getBuildingName());
            btnAndListMap.put(mark, data);
            btnSelectMap.put(mark,data.get(0));
            typeList.put(mark,data.get(0).getType());
            mark++;
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
