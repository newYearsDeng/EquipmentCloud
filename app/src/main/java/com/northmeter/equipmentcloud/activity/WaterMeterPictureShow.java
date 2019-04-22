package com.northmeter.equipmentcloud.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.equipmentcloud.I.IShowAnalysisPic;
import com.northmeter.equipmentcloud.I.I_ShowBlueSend;
import com.northmeter.equipmentcloud.I.I_ShowNetWorkPicture;
import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.base.BaseActivity;
import com.northmeter.equipmentcloud.base.Constants;
import com.northmeter.equipmentcloud.bean.EvenBusBean;
import com.northmeter.equipmentcloud.bean.NetWorkPictureBean;
import com.northmeter.equipmentcloud.bluetooth.BlueTooth_ConnectHelper;
import com.northmeter.equipmentcloud.bluetooth.SendBlueMessage;
import com.northmeter.equipmentcloud.enumBean.EvenBusEnum;
import com.northmeter.equipmentcloud.presenter.OKhttpRequest;
import com.northmeter.equipmentcloud.presenter.WaterMeterPictureShowPresenter;
import com.northmeter.equipmentcloud.utils.Udp_Help;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2019/3/26.
 * 获取摄像水表本地或者网络图片
 */

public class WaterMeterPictureShow extends BaseActivity implements I_ShowBlueSend, IShowAnalysisPic, I_ShowNetWorkPicture {
    @BindView(R.id.connect_flag)
    EditText connectFlag;
    @BindView(R.id.edittext_tablenum)
    EditText edittextTablenum;
    @BindView(R.id.tv_show_analysis)
    TextView tvShowAnalysis;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.image_camera_show)
    ImageView imageCameraShow;
    @BindView(R.id.layout_local)
    LinearLayout layoutLocal;
    @BindView(R.id.button_get_network_picture)
    Button buttonGetNetworkPicture;

    private SendBlueMessage sendBlueMessage;
    private OKhttpRequest oKhttpRequest;
    private Map<Integer, String> map_count = new HashMap();
    private Bitmap bmp = null;//读取到的水表照片
    private int type;
    private String equipmentNum, equipmentName,itemTypeId;
    private WaterMeterPictureShowPresenter waterMeterPictureShowPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_meter_picture;
    }

    @Override
    public void initIntentData() {
        super.initIntentData();
        type = getIntent().getIntExtra("type", 0);
        equipmentNum = getIntent().getStringExtra("equipmentNum");
        itemTypeId = getIntent().getStringExtra("itemTypeId");
        equipmentName = getIntent().getStringExtra("equipmentName");
        if(equipmentNum==null){
            equipmentNum = "000000000000";
        }
    }

    @Override
    public void setTitle() {
        super.setTitle();
        tvToolbarTitle.setText("获取图片");
    }

    @Override
    public void initData() {
        edittextTablenum.setText(equipmentNum);
        oKhttpRequest = new OKhttpRequest(this);
        sendBlueMessage = new SendBlueMessage(this);
        waterMeterPictureShowPresenter = new WaterMeterPictureShowPresenter(this);
        if (type == 0) {
            connectFlag.setText(BlueTooth_ConnectHelper.getInstance().getmConnectedDeviceName());
            layoutLocal.setVisibility(View.VISIBLE);
            photo();
        } else {
            connectFlag.setVisibility(View.GONE);
            buttonGetNetworkPicture.setVisibility(View.VISIBLE);
            waterMeterPictureShowPresenter.getNetWorkPicture(edittextTablenum.getText().toString(),itemTypeId);
        }
        super.initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.button_get_picture, R.id.button_analysis_picture, R.id.btn_tb_back,R.id.button_get_network_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_back:
                this.finish();
                break;
            case R.id.button_get_picture://本地拍照
                photo();
                break;
            case R.id.button_analysis_picture://解析图片
                waterMeterPictureShowPresenter.startLoadingDialog();
                oKhttpRequest.okhttpRequest(bmp, edittextTablenum.getText().toString());
                break;
            case R.id.button_get_network_picture://获取网络图片
                waterMeterPictureShowPresenter.getNetWorkPicture(edittextTablenum.getText().toString(),itemTypeId);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            waterMeterPictureShowPresenter.stopLoadingDialog();
        }
    };

    @Override
    public void showAnalysisPic(String Code, String Message, String Value) {
        tvShowAnalysis.setText(Value.split(",")[0].split("\\.")[0]);
        handler.sendEmptyMessage(0);

    }

    @Override
    public void showAnalysisPicException(String exception) {//图片解析数据异常
        tvShowAnalysis.setText(exception);
        handler.sendEmptyMessage(0);
    }

    private void photo() {//34EE主动上报第一张图片；  34DD手动读取第一张图片
        waterMeterPictureShowPresenter.startLoadingDialog();
        cleanDrawable();
        String para_0 = "68" + Udp_Help.reverseRst(edittextTablenum.getText().toString()) +
                "68140E00363310EF" + Constants.HandlerKey + "34EE";
        String cs_0 = Udp_Help.get_sum(para_0).toUpperCase() + "16";
        String last_0 = "FEFEFEFE" + para_0 + cs_0;
        System.out.println(last_0);
        sendBlueMessage.sendBTblueMessage(last_0, 10);//state 0 ：表示安装测试界面的操作，1,2表示设置界面的操作，10：表示本地拍照读取
    }

    /**
     * 清除imageview里面的图片
     */
    private void cleanDrawable() {
        Drawable drawable = imageCameraShow.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        imageCameraShow.setImageDrawable(null);
    }

    @Override
    public void showMessage(String message) {
        showMsg(message);
    }

    /**
     * 事件订阅者处理事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(EvenBusBean evenBusBean) {
        String topic = evenBusBean.getTopic();
        if (topic.equals(EvenBusEnum.EvenBus_WaterMeterPicShow.getEvenName())) {
            handleMessage(evenBusBean.getData());
        }
    }

    public void handleMessage(String blueMsg) {
        if (blueMsg.equals("success")) {//设置窗口参数成功
            showMsg("操作成功");
            return;
        } else if (blueMsg.equals("fail")) {
            waterMeterPictureShowPresenter.stopLoadingDialog();
            showMsg("操作失败");
            return;
        }
        String control = blueMsg.substring(22, 24).toUpperCase();
        if (control.equals("91")) {
            String msgflag = blueMsg.substring(28, 36).toUpperCase();
            switch (msgflag) {
                case "343310EF"://照片fefefe6821000016200168910802343310ef 343c3e 2cd0261e28dfde750a6c9c8
                    String img_data = blueMsg.substring(36, blueMsg.length() - 4);
                    String page_code_1 = Udp_Help.get_645ToHex(img_data.substring(0, 2));//图片序号

                    //get_645ToHex获取到的数据只为减去33后的16进制数据，得到最终的参数需要转换为10进制参数
                    int page_count_1 = Integer.parseInt(Udp_Help.get_645ToHex(img_data.substring(4, 6)), 16);//包总个数
                    int page_count_2 = Integer.parseInt(Udp_Help.get_645ToHex(img_data.substring(2, 4)), 16);//当前包序号
                    map_count.put(page_count_2, img_data.substring(6, img_data.length()));

                    System.out.println(page_count_1 + "--*-*-**-*-*-" + page_count_2);
                    if (page_count_1 - page_count_2 == 1) {
                        try {
                            String result = "";
                            for (int i = 0; i < page_count_1; i++) {
                                String datas = map_count.get(i);
                                result = result + datas;
                            }

                            String img_datas = Udp_Help.get_645ToHex(result);
                            byte[] bytes = Udp_Help.strtoByteArray(img_datas);

                            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageCameraShow.setImageBitmap(bmp);
                            waterMeterPictureShowPresenter.stopLoadingDialog();

                            map_count.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMsg("读取照片失败");
                            waterMeterPictureShowPresenter.stopLoadingDialog();
                        }

                    }
                    break;
            }
        }

    }


    @Override
    public void showData(NetWorkPictureBean.PictureData pictureData) {
        tvShowAnalysis.setText(String.valueOf(pictureData.getDataValue()));
    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        cleanDrawable();
        imageCameraShow.setImageBitmap(bitmap);
    }

    @Override
    public void returnMessage(String message) {
        showMsg(message);
    }
}
