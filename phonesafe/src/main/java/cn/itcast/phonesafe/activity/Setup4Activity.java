package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/11/22.
 */
public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        initUi();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(Setup4Activity.this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        boolean open_security = SpUtils.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        if(open_security){
            Intent intent = new Intent(Setup4Activity.this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(getApplicationContext(),ConstantValue.SETUP_OVER,true);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }else{
            ToastUtils.show(getApplicationContext(),"请开启防盗保护");
        }
    }

    private void initUi() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //1、是否选中的状态回显
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //2、根据状态修改checkBox的后续文字显示
        cb_box.setChecked(open_security);
        if(open_security){
            cb_box.setText("安全设置已开启");
        }else{
            cb_box.setText("安全设置已关闭");
        }
        //3、点击过程监听状态的变化
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //4、存储点击后的状态
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_SECURITY,isChecked);
                //5、根据开启关闭的状态去修改显示的文字
                if(isChecked){
                    cb_box.setText("安全设置已开启");
                }else{
                    cb_box.setText("安全设置已关闭");
                }
            }
        });
    }
}
