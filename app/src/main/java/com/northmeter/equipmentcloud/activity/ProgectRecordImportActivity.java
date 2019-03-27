package com.northmeter.equipmentcloud.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.northmeter.equipmentcloud.I.I_ShowRecordImport;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.adapter.CommonAdapter;
import com.northmeter.equipmentcloud.adapter.ViewHolder;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.ProgectListResponse;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
import com.northmeter.equipmentcloud.camera.activity.CaptureActivity;
import com.northmeter.equipmentcloud.camera.camera.CameraManager;
import com.northmeter.equipmentcloud.presenter.ProgectRecordImportPresenter;
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
 * 档案导入
 */

public class ProgectRecordImportActivity extends BaseActivity implements I_ShowRecordImport {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.linear_contain)
    LinearLayout linearContain;

    private ListView listview;//点击建筑按钮是显示的列表
    private int projectId;
    private View view;
    private PopupWindow popupWindow;
    private Gson gson = new Gson();;
    private JsonParser parser = new JsonParser() ;
    private Map btnAndListMap;//存储每个建筑按钮的建筑child列表
    private Map<Object,RecordImportResponse.BuildList> btnSelectMap;//存储选择的建筑信息
    private Map<Integer,Integer> typeList;//判断当前btnSelectMap相同key值的建筑信息的下级建筑是设备还是建筑
    private CommonAdapter commonAdapter;
    private List<RecordImportResponse.BuildList> buildDatas;

    private static final int REQUEST_CAMERARESULT=201;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private List<View> viewList;
    private List<ViewHolder> viewHolderList;
    private int mark=0;
    private static int checkViewHolder=0;
    private String scanTableNum;

    private ProgectRecordImportPresenter progectRecordImportPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_record_import;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        projectId = getIntent().getIntExtra("projectId",0);
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("档案导入");
    }

    @Override
    public void initData() {
        super.initData();
        progectRecordImportPresenter = new ProgectRecordImportPresenter(this);
        progectRecordImportPresenter.getRecordImportBuildList(projectId);

        viewList = new ArrayList<>();
        viewHolderList = new ArrayList<>();


        buildDatas = new ArrayList<>();
        btnAndListMap = new HashMap<>();//存储默认加载的PopupWindow的建筑列表
        btnSelectMap = new HashMap();//选择建筑后存储该建筑信息
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                    if (data != null) {
                        if (data.hasExtra("result")) {//扫描到水表编号返回数据
                            scanTableNum = data.getStringExtra("result").toString();
                            if(typeList.get(mark-1)==1){
                                String oldText = btnSelectMap.get(mark-1).getEquipmentName();
                                viewHolderList.get(mark-1).tv_progect_addr.setText(oldText+"\n"+scanTableNum);
                            }else{
                                showMsg("请依次选择完整的设备地址");
                            }
                            return;
                        }
                    }
                }
                break;
        }
    }

    @OnClick({R.id.btn_tb_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
//            case R.id.iv_right_icon://扫码
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        showMsg("请在权限设置中允许打开相机或录像");
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                        intent.setData(uri);
//                        startActivityForResult(intent, 10);
//                        return;
//                    }
//                }
//                Intent intent = new Intent(this,CaptureActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent,SCANNIN_GREQUEST_CODE);
//                break;
            case R.id.btn_submit://提交
                StringBuffer equipmentAdd = new StringBuffer();
                for(int i=0;i<btnSelectMap.size();i++){
                    equipmentAdd.append(btnSelectMap.get(i).getBuildingName()+"/");
                }
                String equipmentAddress = equipmentAdd.toString().substring(0,equipmentAdd.length()-1);
                RecordImportResponse.BuildList bean= btnSelectMap.get(mark-1);
                progectRecordImportPresenter.saveProgectRecord(projectId,bean.getRecordId() , bean.getEquipmentName(),
                    scanTableNum, bean.getBuildingName(), equipmentAddress.toString());
                break;
        }
    }


    private void initpopupWindow(){
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

    private void initAddView(int mark,String buildName){
        View addView = LayoutInflater.from(ProgectRecordImportActivity.this).inflate(R.layout.item_progect_import_view, null);
        addView.setId(mark);
        linearContain.addView(addView, mark);
        getViewInstance(addView,buildName);
    }

    private void getViewInstance(View view ,String buildName){
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
        @Override public void onClick(View v) {
            View view = (View) v.getParent();
            for (int i = 0; i < linearContain.getChildCount(); i++) {
                ViewHolder viewHolder = viewHolderList.get(i);
                Log.v("Import", "view.getId()==" + view.getId() + "  viewHolder.id==" + viewHolder.id);
                if (view.getId() == viewHolder.id) {
                    removeViews(i);
                    checkViewHolder = i;
                    System.out.println(view.getId()+"/"+viewHolder.id);
                    popupWindow.showAsDropDown(v,0,0,Gravity.CENTER);
                    List<RecordImportResponse.BuildList> data = (List<RecordImportResponse.BuildList>) btnAndListMap.get(view.getId());
                    buildDatas.clear();
                    buildDatas.addAll(data);
                    commonAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     *删除排在被点击按钮后面按钮*/
    private void removeViews(int index){
        int childNum = linearContain.getChildCount();
        for(int i = childNum-1; i >= 0; i--){
            if(i > index){
                linearContain.removeViewAt(i);
                viewHolderList.remove(i);
                viewList.remove(i);
                typeList.remove(i);
            }
        }
        mark = index+1;
    }


    @Override
    public void showData(List<RecordImportResponse.BuildList> data) {
        if(!data.isEmpty()){
            initAddView(mark,data.get(0).getBuildingName());
            btnAndListMap.put(mark,data);
            typeList.put(mark,data.get(0).getType());
            btnSelectMap.put(mark,data.get(0));
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







//        JsonObject jsonObject_0 = parser.parse(gson.toJson(datas.get(0))).getAsJsonObject();
//        JsonArray jsonArray_0 = parser.parse(gson.toJson(jsonObject_0.get("child"))).getAsJsonArray();
//        System.out.println(jsonObject_0.get("child"));
//
//        JsonObject jsonObject_1 = (JsonObject) parser.parse(gson.toJson(jsonArray_0.get(0)));
//        JsonArray jsonArray_1 = parser.parse(gson.toJson(jsonObject_1.get("child"))).getAsJsonArray();
//        System.out.println(jsonObject_1.get("child"));
//
//        JsonObject jsonObject_2  = parser.parse(gson.toJson(jsonArray_1.get(0))).getAsJsonObject();
//        JsonArray jsonArray_2 = parser.parse(gson.toJson(jsonObject_2.get("child"))).getAsJsonArray();
//        System.out.println(jsonObject_2.get("child"));
//
//
//        RecordImportResponse.BuildList  buildList =  gson.fromJson(new JsonParser().parse(gson.toJson(datas.get(0).getChild())),RecordImportResponse.BuildList.class);
//        JsonArray jsonArray0 = parser.parse(gson.toJson(buildList.getChild().get(0))).getAsJsonArray();
//
//
//        JsonObject jsonObject1 = new JsonParser().parse(gson.toJson(datas.get(0))).getAsJsonObject();
//        //再转JsonArray 加上数据头
//        JsonArray jsonArray = jsonObject1.getAsJsonArray("child");
//        for (JsonElement user : jsonArray) {
//            //通过反射 得到UserBean.class
//            gson.fromJson(user, getClass());
//
//        }
