package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.Random;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.db.BlankNumberDao;
import cn.itcast.phonesafe.db.BlankNumberInfo;
import cn.itcast.phonesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/12/6.
 */
public class BlankPhoneActivity extends Activity {

    private Button bt_add;
    private ListView lv_blankphone;
    private BlankNumberDao mDao;
    private List<BlankNumberInfo> mBlackNumberList;
    private MyAdapter myAdapter;
    private int mode = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myAdapter = new MyAdapter();
            lv_blankphone.setAdapter(myAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_phone);
        initUi();
        initData();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                mDao = BlankNumberDao.getInstance(getApplicationContext());
                mBlackNumberList = mDao.findAll();
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBlackNumberList.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlackNumberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = View.inflate(getApplicationContext(), R.layout.list_black_number, null);
            } else {
                view = convertView;
            }
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1、从数据库删除该条数据
                    mDao.delete(mBlackNumberList.get(position).phone);
                    //2、集合中删除
                    mBlackNumberList.remove(position);
                    //3、通知数据适配器刷新
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                }
            });
            tv_phone.setText(mBlackNumberList.get(position).phone);
            int mode = Integer.parseInt(mBlackNumberList.get(position).mode);
            switch (mode) {
                case 1:
                    tv_mode.setText("短信");
                    break;
                case 2:
                    tv_mode.setText("电话");
                    break;
                case 3:
                    tv_mode.setText("所有");
                    break;
            }
            Log.i("tag", "**********************************" + mBlackNumberList.size());
            return view;
        }
    }

    private void initUi() {
        bt_add = (Button) findViewById(R.id.bt_add);
        lv_blankphone = (ListView) findViewById(R.id.lv_blankphone);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBlankDialog();
//                insert();
            }
        });
    }

    public void insert() {
        BlankNumberDao dao = BlankNumberDao.getInstance(getApplicationContext());
        for (int i = 0; i < 20; i++) {
            if (i < 10) {
                dao.insert("1860000000" + i, 1 + new Random().nextInt(3) + "");
            } else {
                dao.insert("186000000" + i, 1 + new Random().nextInt(3) + "");
            }
        }
    }

    //展示添加黑名单号码
    private void showBlankDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.activity_add_blank_phone, null);
        dialog.setView(view);
        dialog.show();

        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        final RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        //监听rodiogroup的状态
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bg_sms:
                        mode = 1;
                        break;
                    case R.id.bg_phone:
                        mode = 2;
                        break;
                    case R.id.bg_all:
                        mode = 3;
                        break;
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            String phone = et_phone.getText().toString();//TODO  获取的phone值为空？
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phone)) {
                    //1、获取到填写的号码和类型插入到数据库中
                    mDao.insert(phone, mode + "");
                    //2、让数据和集合保持一致（1、数据库中的数据重新读一遍。2、手动向集合中添加一个对象）
                    BlankNumberInfo blankNumberInfo = new BlankNumberInfo();
                    blankNumberInfo.phone = phone;
                    blankNumberInfo.mode = mode + "";
                    //3、将对象插入到集合的最顶部
                    mBlackNumberList.add(0, blankNumberInfo);
                    //4、通知数据适配器刷新
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                } else {
                    ToastUtils.show(BlankPhoneActivity.this, "请输入拦截号码");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
