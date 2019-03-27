package com.northmeter.equipmentcloud.bluetooth;


import com.northmeter.equipmentcloud.I.ISendBlueMessage;
import com.northmeter.equipmentcloud.I.IShowSMainMessage;
import com.northmeter.equipmentcloud.I.I_ShowBlueSend;
import com.northmeter.equipmentcloud.bluetooth.bluetooth.tools.BluetoothConnectionClient;
import com.northmeter.equipmentcloud.bluetooth.bt_bluetooth.BluetoothChatService;
import com.northmeter.equipmentcloud.utils.Udp_Help;

/**
 * Created by dyd
 * 2017/5/18
 */
public class SendBlueMessage implements ISendBlueMessage {
    private BluetoothConnectionClient mConnectionClient;
    private BluetoothChatService bluetoothChatService;
    private I_ShowBlueSend showMessage;
    public SendBlueMessage(I_ShowBlueSend showMessage){
        this.showMessage = showMessage;
    }
    @Override
    public void sendBlueMessage(String para) {
        mConnectionClient = BlueTooth_UniqueInstance.getInstance().getConnectionClient();
        if(mConnectionClient==null){
            showMessage.showMessage("蓝牙未连接");
        }else{
            final String input = para;
            new Thread(){
                public void run(){
                    if(input.length()>20){
                        for(int i=0;i<input.length()/20;i++){
                            String send_1 = input.substring(i*20,i*20+20);
                            mConnectionClient.write(4, Udp_Help.strtoByteArray(send_1));
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        int state = input.length()/20;
                        String send_2 = input.substring(state*20,state*20+input.length()%20);
                        mConnectionClient.write(4,  Udp_Help.strtoByteArray(send_2));
                    }else{
                        mConnectionClient.write(4, Udp_Help.strtoByteArray( input));
                    }

                }
            }.start();
            showMessage.showMessage("发送成功");
        }
    }


    @Override
    public void sendBTblueMessage(String para , int state) {
        System.out.println("指令："+para);
        //bluetoothChatService = BlueTooth_UniqueInstance.getInstance().getBluetoothChatService();
        bluetoothChatService =  BlueTooth_ConnectHelper.getInstance().getmChatService();
        boolean flag = BlueTooth_UniqueInstance.getInstance().isBooleanConnected();
        BlueTooth_UniqueInstance.getInstance().setState(state);
        if(!flag){
            showMessage.showMessage("蓝牙未连接");
        }else{
            final String input = para;
            new Thread(){
                public void run(){
                    byte[] sendData = Udp_Help.strtoByteArray(input);
                    bluetoothChatService.write(sendData);
                }
            }.start();
          //showMessage.showMessage("发送成功");
        }

    }
}
