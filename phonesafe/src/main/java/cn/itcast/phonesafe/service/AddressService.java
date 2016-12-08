package cn.itcast.phonesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.itcast.phonesafe.R;
import cn.itcast.phonesafe.dao.AddressDao;
import cn.itcast.phonesafe.utils.ConstantValue;
import cn.itcast.phonesafe.utils.SpUtils;
import cn.itcast.phonesafe.utils.ToastUtils;

/**
 * Created by Administrator on 2016/12/1.
 */
public class AddressService extends Service{
    public static String tag = "AddressService";
    private WindowManager wm;
    private MyPhoneListener myPhoneListener;
    private TelephonyManager tm;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View mViewToast;
    private TextView tv_toast;
    private String mAddress;
    private int[] mDrawableIds;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            tv_toast.setText(mAddress);
            super.handleMessage(msg);
        }
    };
    private int mScreenHeight;
    private int mScreenWidth;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //获取电话管理者对象
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //监听电话的状态
        myPhoneListener = new MyPhoneListener();
        tm.listen(myPhoneListener,PhoneStateListener.LISTEN_CALL_STATE);

        //获取窗体对象
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        super.onCreate();
    }

    class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                //空闲状态不做操作
                case TelephonyManager.CALL_STATE_IDLE:
                    //挂断电话时移出Toast
                    if(wm != null && mViewToast != null){
                        wm.removeView(mViewToast);
                    }
                    break;
                //响铃状态弹出Toast
                case TelephonyManager.CALL_STATE_RINGING:
//                    ToastUtils.show(getApplicationContext(),"CALL_STATE_RINGING + 电话铃声响了");
                    showToast(incomingNumber);
                    break;
                //摘机状态
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    //展示toast的窗口
    private void showToast(String incomingNumber) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE	默认能够被触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        //在响铃的时候显示吐司，和电话类型一直
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        //指定Toast所在的位置
        params.gravity = Gravity.LEFT + Gravity.TOP;
        //Toast显示的效果
        mViewToast = View.inflate(this, R.layout.toast_view,null);
        tv_toast = (TextView) mViewToast.findViewById(R.id.tv_toast);

        mViewToast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX-startX;
                        int disY = moveY-startY;

                        params.x = params.x+disX;
                        params.y = params.y+disY;

                        //容错处理
                        if(params.x<0){
                            params.x = 0;
                        }

                        if(params.y<0){
                            params.y=0;
                        }

                        if(params.x> mScreenWidth -mViewToast.getWidth()){
                            params.x = mScreenWidth -mViewToast.getWidth();
                        }

                        if(params.y> mScreenHeight -mViewToast.getHeight()-22){
                            params.y = mScreenHeight -mViewToast.getHeight()-22;
                        }

                        //告知窗体吐司需要按照手势的移动,去做位置的更新
                        wm.updateViewLayout(mViewToast, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_X, params.x);
                        SpUtils.putInt(getApplicationContext(),ConstantValue.LOCATION_Y, params.y);
                        break;
                }
                //true 响应拖拽触发的事件
                return true;
            }
        });

        params.x = SpUtils.getInt(getApplicationContext(),ConstantValue.LOCATION_X,0);
        params.y = SpUtils.getInt(getApplicationContext(),ConstantValue.LOCATION_Y,0);
        //从sp中获取对文字的索引，匹配图片用作显示
        mDrawableIds = new int[]{
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        int whichInt = SpUtils.getInt(getApplicationContext(), ConstantValue.TOAST_SYTLE, 0);
        tv_toast.setBackgroundResource(mDrawableIds[whichInt]);

        wm.addView(mViewToast,params);
        //获取来电的电话号码，用作查询
        query(incomingNumber);
    }

    private void query(final String incomingNumber) {
        new Thread(){
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(incomingNumber);
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        //取消電話的監聽
        if(tm != null && myPhoneListener != null){
            tm.listen(myPhoneListener,myPhoneListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
}
