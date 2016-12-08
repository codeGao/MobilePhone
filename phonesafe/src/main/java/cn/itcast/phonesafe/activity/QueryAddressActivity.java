package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.dao.AddressDao;

/**
 * Created by Administrator on 2016/11/29.
 */
public class QueryAddressActivity extends Activity {

    private EditText et_phone;
    private Button bu_query;
    private String address;
    private TextView query_result;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //控件查询的结果
            query_result.setText(address);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        initUi();
    }

    private void initUi() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bu_query = (Button) findViewById(R.id.bu_query);
        query_result = (TextView) findViewById(R.id.query_result);
        //1、点击按钮查询
        bu_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    //2、查询
                    query(phone);
                } else {
                    //抖动
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    et_phone.startAnimation(animation);
                }
            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString();
                query(phone);
            }
        });
    }

    //查询的是耗时操作故需要在子线程中执行
    private void query(final String phone) {
        new Thread() {
            @Override
            public void run() {
                address = AddressDao.getAddress(phone);
                //发送消息告诉主线程查询已近结束了
                mHander.sendEmptyMessage(0);
            }
        }.start();
    }
}

