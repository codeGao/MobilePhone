package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Handler;
import android.widget.TextView;

import cn.itcast.phonesafe.R;

/**
 * Created by Administrator on 2016/11/23.
 */
public class ContactActivity extends Activity {

    private ListView lv_contact;
    private ArrayList<HashMap<String,String>> contactList = new ArrayList<HashMap<String,String>>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           //填充数据适配器
            myAdapter = new MyAdapter();
            lv_contact.setAdapter(myAdapter);
        }
    };
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initUi();
        initData();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String,String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.contact_list, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            tv_name.setText(contactList.get(position).get("name"));
            tv_phone.setText(contactList.get(position).get("phone"));
            return view;
        }
    }

    private void initUi() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myAdapter != null){
                    HashMap<String, String> hashMap = myAdapter.getItem(position);
                    String phone = hashMap.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }

    //获取系统联系人
    private void initData() {
        //因为获取系统联系人可能是耗时的因此开启子线程访问
        new Thread() {
            @Override
            public void run() {
                //1、获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), new String[]{"contact_id"}, null, null, null);
                contactList.clear();
                //循环获取每一联系人的姓名和电话号码
                while(cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"), new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);
                    HashMap<String,String> hashMap = new HashMap<String, String>();
                    while(indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);

                        //6,区分类型去给hashMap填充数据
                        if(type.equals("vnd.android.cursor.item/phone_v2")){
                            //数据非空判断
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("phone", data);
                            }
                        }else if(type.equals("vnd.android.cursor.item/name")){
                            if(!TextUtils.isEmpty(data)){
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}
