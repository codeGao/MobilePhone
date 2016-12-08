package cn.itcast.phonesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import cn.itcast.phonesafe.R;

/**
 * Created by Administrator on 2016/11/22.
 */
public class Setup1Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    protected void showPrePage() {

    }

    @Override
    protected void showNextPage() {
        Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
    }

}
