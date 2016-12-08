package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.SubMenu;
import android.view.View;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.service.AddressService;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.ServiceUtils;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.view.SettingClickView;
import cn.itcast.phonesafe.view.SettingItemView;


/**
 * Created by Administrator on 2016/11/20.
 */
public class SettingActivity extends Activity {

    private SettingItemView siv_phoneShow;
    private SettingClickView scv_toast_tyle;
    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_location;
    private SettingItemView siv_blank_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUpdate();
        initShowPhone();
        initToastStyle();
        initLocation();
        initBlankPhone();
    }


    //归属地提示框展示
    private void initLocation() {
        scv_toast_location = (SettingClickView) findViewById(R.id.scv_toast_location);
        scv_toast_location.setTitle("归属地提示框的位置");
        scv_toast_location.setDes("设置归属地提示框的位置");
        scv_toast_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
            }
        });
    }

    //Toast顯示框的style
    private void initToastStyle() {
        scv_toast_tyle = (SettingClickView) findViewById(R.id.scv_toast_tyle);
        scv_toast_tyle.setTitle("设置归属地显示风格");
        //描述文字所在的字符数组
        mToastStyleDes = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        //通过sp获取toast的索引值
        mToastStyle = SpUtils.getInt(this, ConstantValue.TOAST_SYTLE, 0);
        //通过索引，获取字符串数组中的文字，显示给描述内容控件
        scv_toast_tyle.setDes(mToastStyleDes[mToastStyle]);
        //4、监听点击事件
        scv_toast_tyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //5、显示吐司样式的弹出框
                showToastStyleDialog();
            }
        });
    }

    //点击选择toast的提示框
    private void showToastStyleDialog() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.home_apps);
        builder.setTitle("请选择归属地样式");
        builder.setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1、记录选中的索引值，2、关闭对话框，3、显示选中设置的文字
                SpUtils.putInt(getApplicationContext(),ConstantValue.TOAST_SYTLE,which);
                dialog.dismiss();
                scv_toast_tyle.setDes(mToastStyleDes[which]);
            }
        });
        //取消的消极按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void initShowPhone() {
        siv_phoneShow = (SettingItemView) findViewById(R.id.siv_phoneShow);
        //获取已有的开关状态用作显示
//        boolean phone_show = SpUtils.getBoolean(this, ConstantValue.PHONE_SHOW, false);
        boolean phone_show = ServiceUtils.isRunning(this, "cn.itcast.phonesafe.service.AddressService");
        //是否选中根据上一次的存储结果做决定
        siv_phoneShow.setCheck(phone_show);
        siv_phoneShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_phoneShow.isCheck();
                siv_phoneShow.setCheck(!isCheck);
                if(!isCheck){
                    //开启服务管理Toast
                    startService(new Intent(getApplicationContext(),AddressService.class));
                }else{
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                }
            }
        });
    }

    //版本更新的开关
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的开关状态用作显示
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);
                //讲取反后的结果存储在sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }


    //黑名单拦截设置
    private void initBlankPhone() {
        siv_blank_phone = (SettingItemView) findViewById(R.id.siv_blank_phone);
        //获取已有的开关状态用作显示
        Boolean blank_phone = SpUtils.getBoolean(getApplicationContext(), ConstantValue.BLANK_PHONE, false);
        //是否选中根据上次存储的结果做显示
        siv_blank_phone.setCheck(blank_phone);
        siv_blank_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = siv_blank_phone.isCheck();
                siv_blank_phone.setCheck(!check);
                //取反后存储到sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.BLANK_PHONE,!check);
            }
        });
    }
}
