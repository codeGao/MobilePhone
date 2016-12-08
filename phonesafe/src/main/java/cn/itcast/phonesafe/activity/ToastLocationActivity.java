package cn.itcast.phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/12/4.
 */
public class ToastLocationActivity extends Activity {

    private ImageView iv_drag;
    private Button bt_top;
    private Button bt_bottom;
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private long [] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        //初始化界面
        initUi();
    }

    private void initUi() {
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();

        int locationX = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int locationY = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        //将左上角的坐标作用在iv_drag上
        layoutParams.leftMargin = locationX;
        layoutParams.topMargin = locationY;
        //将以上规则作用在iv_drag上
        iv_drag.setLayoutParams(layoutParams);

        if(locationY >mScreenHeight/2){
            bt_bottom.setVisibility(View.INVISIBLE);
            bt_top.setVisibility(View.VISIBLE);
        }else{
            bt_bottom.setVisibility(View.VISIBLE);
            bt_top.setVisibility(View.INVISIBLE);
        }

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if(mHits[mHits.length-1]-mHits[0]<500){
                    //满足双击事件后，调用代码
                    int left = mScreenWidth / 2 - iv_drag.getWidth() / 2;
                    int top = mScreenHeight / 2 - iv_drag.getHeight() / 2;
                    int right = mScreenWidth/2+iv_drag.getWidth()/2;
                    int bottom = mScreenHeight/2+iv_drag.getHeight()/2;
                    iv_drag.layout(left,top,right,bottom);
                    //存储最终的位置
                    SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X,iv_drag.getLeft());
                    SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y,iv_drag.getTop());
                }
            }
        });
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            //Todo 拖拽相關的操作沒有實現
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    //移動
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX - startX;
                        int disY = moveY - startY;
                        //当前控件所在屏幕的（左上角）的位置
                        int left = iv_drag.getLeft() + disX;
                        int top = iv_drag.getTop() + disY;
                        int right = iv_drag.getRight() + disX;
                        int bottom = iv_drag.getBottom() + disY;
                        //容错处理（iv_drag不能拖出屏幕之外）
                        //左边缘不能超出屏幕
                        if(left<0){
                            return true;
                        }
                        //右边缘不能超出屏幕
                        if(right>mScreenWidth){
                            return true;
                        }
                        //上边缘不能超出屏幕高度
                        if(top<0){
                            return true;
                        }
                        //下边缘不能超出屏幕
                        if(bottom>mScreenHeight-20){
                            return true;
                        }
                        if(top>mScreenHeight/2){
                            bt_bottom.setVisibility(View.INVISIBLE);
                            bt_top.setVisibility(View.VISIBLE);
                        }else{
                            bt_bottom.setVisibility(View.VISIBLE);
                            bt_top.setVisibility(View.INVISIBLE);
                        }
                        //2、告知移动的控件，按计算出来的坐标进行展示
                        iv_drag.layout(left, top, right, bottom);
                        //3、重置一次开始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        //4、存储移动的位置
                        SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X,iv_drag.getLeft());
                        SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y,iv_drag.getTop());
                        break;

                }
                //在當前情況下返回fasle不響應事件需要返回true才行
                //既要响应拖拽又要响应点击事件
                return false;
            }
        });
    }
}
