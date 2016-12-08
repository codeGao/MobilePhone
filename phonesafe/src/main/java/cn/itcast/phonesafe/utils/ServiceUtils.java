package cn.itcast.phonesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ServiceUtils {
    public static boolean isRunning(Context ctx, String serviceName){
        //获取当前手机正在运行的所有服务
       ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo runningInfo:runningServices) {
            //获取每一个真正运行的服务的名称
            if(serviceName.equals(runningInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
