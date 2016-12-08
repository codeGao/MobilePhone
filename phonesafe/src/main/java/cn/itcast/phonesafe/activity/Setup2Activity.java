package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import org.w3c.dom.Text;

import java.util.Set;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;
import cn.itcast.phonesafe.view.SettingItemView;

/**
 * Created by Administrator on 2016/11/22.
 */
public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView siv_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initUi();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(Setup2Activity.this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String sim_number = SpUtils.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(sim_number)) {
            Intent intent = new Intent(Setup2Activity.this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtils.show(getApplicationContext(), "请绑定sim卡");
        }
    }

    //初始化界面
    private void initUi() {
        siv_bound = (SettingItemView) findViewById(R.id.siv_bound);
        //1、回显（读取已有的绑定状态用作显示，读取sp中是否存储了sim卡的序列号）
        final String sim_number = SpUtils.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
        //2、判断sim卡号是否为空
        if (TextUtils.isEmpty(sim_number)) {
            siv_bound.setCheck(false);
        } else {
            siv_bound.setCheck(true);
        }
        //3、设置条目的点击事件
        siv_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4、获取原有的状态然后取反
                boolean check = siv_bound.isCheck();
                siv_bound.setCheck(!check);
                if (!check) {
                    //存储sim的序列号
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    //存储在sp中
                    SpUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else{
                    //从sp中删除节点
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }
}
