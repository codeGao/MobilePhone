package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/11/27.
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建手势管理的对象，用作管理在onTouchEvent传递过来的手势动作
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //监听手势的移动
                if(e1.getX() - e2.getX() > 0){
                    showNextPage();
                }
                if(e1.getX() - e2.getX() < 0){
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //上一页的抽象类
    protected abstract void showPrePage();

    //下一页的抽象类的方法
    protected abstract void showNextPage();
    //监听屏幕的响应事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过手势处理类，接受多种类型的事件用作处理
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //点击下一页按钮的时候,根据子类的showNextPage方法做相应跳转
    public void nextPage(View view){
        showNextPage();
    }
    //点击上一页按钮的时候,根据子类的showNextPage方法做相应跳转
    public void prePage(View view){
        showPrePage();
    }
}
