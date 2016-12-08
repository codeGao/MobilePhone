package cn.itcast.phonesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;

/**
 * Created by Administrator on 2016/11/27.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String tag = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //监听手机重启的广播
        Log.i(tag,"呵呵哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或或");
        Log.i(tag,"手机重启了");

        //获取手机重启的sim卡序列号
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber() + "xxx";
        //读取sp中存储的sim卡序列号
        String sim_number = SpUtils.getString(context, ConstantValue.SIM_NUMBER, "");
        //比对sim卡的序列号
        if(!simSerialNumber.equals(sim_number)){
            //发送短信给设置的电话号码
            SmsManager sms = SmsManager.getDefault();
            //直接获取sp中存储的联系人电话号码
            String context_phone = SpUtils.getString(context, ConstantValue.CONTACT_PHONE, "");
            sms.sendTextMessage(context_phone,null,"sim card is change",null,null);
        }
    }
}
