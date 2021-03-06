package cn.itcast.phonesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.itcast.phonesafe.R;


/**
 * Created by Administrator on 2016/11/20.
 */
public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/cn.itcast.phonesafe";
    private static final String tag = "SettingItemView";
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //将界面的一个条目转换成view对象，直接添加到当前settigview对应的view中
        View.inflate(context, R.layout.setting_item_view, this);
        //自定义组合控件中的标题描述
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //获取自定义以及原生属性的操作，
        initAttrs(attrs);

        tv_title.setText(mDestitle);
    }

    /**
     * 返回属性集合中自定义属性属性值
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

        Log.i(tag, mDestitle);
        Log.i(tag, mDesoff);
        Log.i(tag, mDeson);

    }
    //判断是否开启的方法
    public boolean isCheck(){
        //又checkBox的选中结果，决定当前条目是否开启
        return cb_box.isChecked();
    }

    //是否开启作为变量在点击过程中进行传递
    public void setCheck(boolean isCheck){
    //当前条目在选择过程中cb_box选中状态也跟随ischeck的状态改变
        cb_box.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }
}
