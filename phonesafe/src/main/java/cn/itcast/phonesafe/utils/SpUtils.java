package cn.itcast.phonesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/11/20.
 */
public class SpUtils {
    private static SharedPreferences sp;

    //写入变量至sp中
    public static void putBoolean(Context cxt, String key, boolean value) {
        if (sp == null) {
            sp = cxt.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    //从sp中读取boolean变量
    public static boolean getBoolean(Context ctx, String key, boolean value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, value);
    }

    //写入字符串到sp中
    public static void putString(Context ctx, String key, String deautValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, deautValue).commit();
    }

    //读取sp中的字符串
    public static String getString(Context cxt, String key, String deautValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = cxt.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, deautValue);
    }

    //删除sp中的数据
    public static void remove(Context cxt, String key) {
        if (sp == null) {
            sp = cxt.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }

    public static void putInt(Context ctx,String key,int value){
        if(sp == null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context ctx,String key,int detValue){
        if(sp == null){
            sp = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getInt(key,detValue);
    }

}
