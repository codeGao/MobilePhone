package cn.itcast.phonesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/19.
 */
public class ToastUtils {
    public static void show(Context cxt,String msg){
        Toast.makeText(cxt,msg,0).show();
    }
}
