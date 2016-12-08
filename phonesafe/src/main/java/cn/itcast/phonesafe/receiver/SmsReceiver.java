package cn.itcast.phonesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.service.LocationService;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;

/**
 * Created by Administrator on 2016/11/27.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断是否开启的防盗保护
        boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            //获取短信内容
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            //循环遍历短信
            for (Object object: pdus ) {
                //获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                String messageBody = sms.getMessageBody();
                //判断短信中是否包含播放音乐的关键字
                if(messageBody.contains("#*alarm*#")){
                    //播放音乐
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                }
                //定位
                if(messageBody.contains("#*location*#")){
                    //开启服务的service
                    context.startService(new Intent(context, LocationService.class));
                }
                //锁屏
                if(messageBody.contains("#*lockscrenn*#")){

                }
                //远程清除数据
                if(messageBody.contains("#*wipedate*#")){

                }
            }
        }
    }
}
