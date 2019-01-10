package com.northmeter.equipmentcloud.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/1/3.
 * 设备自检
 */

public class ProgectSelfCheckingActivity extends BaseActivity {
    private static String log = "ProgectSelfCheckingActivity";
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.linear_contain)
    LinearLayout linearContain;
    @BindView(R.id.tv_right_text)
    TextView tvRightText;


    private List<View> viewList;
    private List<ViewHolder> viewHolderList;
    private int mark = 0;

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
        tvToolbarTitle.setText("设备自检");
        tvRightText.setText("查询");
    }

    @Override
    public void initData() {
        super.initData();
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

    @OnClick({R.id.btn_tb_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back://返回
                this.finish();
                break;
            case R.id.btn_submit://立即自检
                break;
            case R.id.tv_right_text://查询
                goActivity(ProgectSelfCheckingResultActivity.class);
                break;
        }
    }


    private void initView(){
        viewList = new ArrayList<>();
        viewHolderList = new ArrayList<>();
        View addView = LayoutInflater.from(ProgectSelfCheckingActivity.this).inflate(R.layout.item_progect_import_view, null);
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

    View.OnClickListener selectProListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            View view = (View) v.getParent();
            for (int i = 0; i < linearContain.getChildCount(); i++) {
                ViewHolder viewHolder = viewHolderList.get(i);
                Log.v(log, "view.getId()==" + view.getId() + "  viewHolder.id==" + viewHolder.id);
                if (view.getId() == viewHolder.id) {
                    viewHolder.tv_progect_addr.getText();
                    //viewHolder.id;
                }
            }
        }
    };


    public class ViewHolder {
        private int id = -1;
        private RelativeLayout relative_content;
        private TextView tv_progect_addr;
    }






    private void addView() {
        RelativeLayout linear_detailed_child = new RelativeLayout(this);
        linear_detailed_child.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                48, 1.0f);
        llp.setMargins(15, 43, 15, 0);
        linear_detailed_child.setLayoutParams(llp);

        //设置权重
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

        TextView text_1 = new TextView(this);
        int id = View.generateViewId();
        text_1.setId(id);
        lp.gravity = Gravity.CENTER;
        text_1.setLayoutParams(lp);
        text_1.setText("小区名称12345");
        text_1.setTextSize(17);
        text_1.setTextColor(getResources().getColor(R.color.color_build_color));
        text_1.setPadding(30, 30, 10, 30);
        linear_detailed_child.addView(text_1);

        ImageView image_weather = new ImageView(this);
        lp.gravity = Gravity.RIGHT;
        image_weather.setLayoutParams(lp);
        image_weather.setImageDrawable(getResources().getDrawable(R.drawable.record_import_down_select));
        image_weather.setPadding(30, 30, 10, 30);
        linear_detailed_child.addView(image_weather);

        linearContain.addView(linear_detailed_child);
    }
}
