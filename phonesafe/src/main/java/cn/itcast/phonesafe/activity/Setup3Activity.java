package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/11/22.
 */
public class Setup3Activity extends BaseSetupActivity {

    private Button bt_contact;
    private EditText et_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUi();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(Setup3Activity.this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
    }

    @Override
    protected void showNextPage() {
        String phone = et_contact.getText().toString().trim();
        if(!TextUtils.isEmpty(phone)){
            Intent intent = new Intent(Setup3Activity.this, Setup4Activity.class);
            startActivity(intent);
            finish();
            //存储输入的电话号码
            SpUtils.putString(getApplicationContext(),ConstantValue.CONTACT_PHONE,phone);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }else {
            ToastUtils.show(getApplicationContext(),"请输入电话号码");
        }
    }

    private void initUi() {
        et_contact = (EditText) findViewById(R.id.et_contact);
        //电话号码的回显过程
        String phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        et_contact.setText(phone);
        bt_contact = (Button) findViewById(R.id.bt_contact);
        bt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setup3Activity.this, ContactActivity.class);
                startActivityForResult(intent,0);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            String phone = data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_contact.setText(phone);
            SpUtils.putString(getApplicationContext(),ConstantValue.CONTACT_PHONE,phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
