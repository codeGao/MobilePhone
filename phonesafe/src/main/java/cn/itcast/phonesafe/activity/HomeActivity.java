package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;


/**
 * Created by Administrator on 2016/11/20.
 */
public class HomeActivity extends Activity {

    private GridView gv_home;
    private String[] mTitleStrs;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
        initData();
    }

    private void initData() {
        mTitleStrs = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        mDrawableIds = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
        };
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //开启提示框
                        showDialog();
                        break;
                    case 1:
                        //通信卫士
                        startActivity(new Intent(getApplicationContext(),BlankPhoneActivity.class));
                        break;
                    case 7:
                        //跳转到高级工具设置页面
                        startActivity(new Intent(getApplicationContext(),AToolActivity.class));
                        finish();
                        break;
                    case 8:
                        //设置中心
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });
    }

    //开启密码提示框
    private void showDialog() {
        //判断本地是否存有密码
        String pwd = SpUtils.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(pwd)) {
            //设置初始化密码的提示框
            showSetPwdDialog();
        } else {
            //确认密码提示框
            showConfirmPwd();
        }
    }

    //确认密码提示框
    private void showConfirmPwd() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(HomeActivity.this, R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);
        dialog.show();
        Button bt_commit = (Button) view.findViewById(R.id.bt_commit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confim_pwd = (EditText) view.findViewById(R.id.et_confim_pwd);
                String confirmPwd = et_confim_pwd.getText().toString();
                if(!TextUtils.isEmpty(confirmPwd)){
                    String pwd = SpUtils.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PSD, "");
                    if(pwd.equals(confirmPwd)){
                        Intent intent = new Intent(HomeActivity.this,Setup1Activity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else{
                        ToastUtils.show(getApplicationContext(), "确认密码错误");
                    }
                }else{
                    ToastUtils.show(HomeActivity.this,"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //初始化设置密码的提示框
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(HomeActivity.this, R.layout.dialog_set_psd, null);
        //让对话框显示自己定义的对话款界面
        dialog.setView(view);
        dialog.show();
        Button bt_commit = (Button) view.findViewById(R.id.bt_commit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                EditText et_confim_pwd = (EditText) view.findViewById(R.id.et_confim_pwd);
                String pwd = et_set_pwd.getText().toString();
                String commitPwd = et_confim_pwd.getText().toString();

                if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(commitPwd)) {
                    if (pwd.equals(commitPwd)) {
                        //密码相同进入下一个页面
                        Intent intent = new Intent(HomeActivity.this, Setup1Activity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                        //将密码存储在sp中
                        SpUtils.putString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PSD, pwd);
                    } else {
                        ToastUtils.show(HomeActivity.this, "确认密码错误");
                    }
                } else {
                    ToastUtils.show(HomeActivity.this, "请输入密码");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStrs[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return view;
        }
    }

    private void initUi() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
