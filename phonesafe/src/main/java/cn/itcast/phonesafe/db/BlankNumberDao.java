package cn.itcast.phonesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */
public class BlankNumberDao {
    private BlankNumberOpenHelper blankNumberOpenHelper ;
    //BlankNumberDao的单例模式
    //1、私有化构造方法
    private BlankNumberDao(Context context){
        //创建数据库及表结构
        blankNumberOpenHelper = new BlankNumberOpenHelper(context);
    }
    //2、声明一个当前类对象
    public static BlankNumberDao blankNumberDao = null;
    //3、提供一个静态方法，如果当前类对象为空则创建一个新的
    public static BlankNumberDao getInstance(Context context){
        if(blankNumberDao == null){
            blankNumberDao = new BlankNumberDao(context);
        }
        return blankNumberDao;
    }

    //insert
    public void insert(String phone,String mode){
        SQLiteDatabase db = blankNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }
    //delete
    public void delete(String phone){
        SQLiteDatabase db = blankNumberOpenHelper.getWritableDatabase();
        db.delete("blacknumber","phone=?",new String[]{phone});
        db.close();
    }

    public List<BlankNumberInfo> findAll(){
        SQLiteDatabase db = blankNumberOpenHelper.getWritableDatabase();

        Cursor cursor = db.query("blacknumber", new String[]{"phone","mode"}, null, null, null, null, "_id desc");
        List<BlankNumberInfo> blackNumberList = new ArrayList<BlankNumberInfo>();
        while(cursor.moveToNext()){
            BlankNumberInfo blankNumberInfo = new BlankNumberInfo();
            blankNumberInfo.phone = cursor.getString(0);
            blankNumberInfo.mode = cursor.getString(1);
            blackNumberList.add(blankNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberList;
    }
}
