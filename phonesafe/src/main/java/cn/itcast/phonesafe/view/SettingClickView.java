package cn.itcast.phonesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.itcast.phonesafe.R;

/**
 * Created by Administrator on 2016/12/1.
 */
public class SettingClickView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_des;

    public SettingClickView(Context context) {
        this(context,null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将设置界面的一个条目装换成一个view对象直接添加给当前的SettingItemView
        View.inflate(context, R.layout.setting_click_view,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    public void setDes(String des){
        tv_des.setText(des);
    }
}
