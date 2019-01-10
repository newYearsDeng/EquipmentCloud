package com.northmeter.equipmentcloud.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.I_ShowRecordImport;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.bean.RecordImportResponse;
import com.northmeter.equipmentcloud.camera.camera.CameraManager;
import com.northmeter.equipmentcloud.presenter.ProgectRecordImportPresenter;
import com.northmeter.equipmentcloud.widget.PopupHelper;
import com.northmeter.equipmentcloud.widget.WidgetHelper;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;

    private View view;
    private PopupWindow popupWindow;

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private List<View> viewList;
    private List<ViewHolder> viewHolderList;
    private int mark = 0;

    private ProgectRecordImportPresenter progectRecordImportPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progect_record_import;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("档案导入");
        ivRightIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        progectRecordImportPresenter = new ProgectRecordImportPresenter(this);
        progectRecordImportPresenter.getRecordImportList(0);

        viewList = new ArrayList<>();
        viewHolderList = new ArrayList<>();

        view = LayoutInflater.from(getApplicationContext()).inflate(
                0, null);
        popupWindow = new PopupHelper().getWindow_ALLWRAP(view, getApplicationContext());
        popupWindow.setWidth((int) (WidgetHelper.getWindowWidth(this) * 0.8));

        //popupWindow.showAtLocation(v, Gravity.TOP|Gravity.RIGHT, 0, 0);
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

    @OnClick({R.id.btn_tb_back, R.id.btn_submit,R.id.iv_right_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.iv_right_icon://扫码
                Intent intent = new Intent(this,CameraManager.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,SCANNIN_GREQUEST_CODE);
                break;
            case R.id.btn_submit://提交
                break;
        }
    }

    private void initAddView(){
        View addView = LayoutInflater.from(ProgectRecordImportActivity.this).inflate(R.layout.item_progect_import_view, null);
        addView.setId(mark);
        linearContain.addView(addView, mark);
        getViewInstance(addView);
    }

    private void getViewInstance(View view){
        ViewHolder vh = new ViewHolder();
        vh.id = view.getId();
        vh.relative_content = (RelativeLayout) view.findViewById(R.id.relative_content);
        vh.tv_progect_addr = (TextView) view.findViewById(R.id.tv_progect_addr);
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
                    viewHolder.tv_progect_addr.getText();
                    //viewHolder.id;
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                    if (data != null) {
                        if (data.hasExtra("result")) {//扫描到水表编号返回数据
                            String result = data.getStringExtra("result").toString();
                            System.out.println("result:"+result);
                            return;
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void showData(List<RecordImportResponse.BuildList> datas) {

    }

    @Override
    public void returnSuccess() {

    }

    @Override
    public void returnFail() {

    }
}
