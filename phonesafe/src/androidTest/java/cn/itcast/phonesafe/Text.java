package cn.itcast.phonesafe;

import java.util.List;
import java.util.Random;

import cn.itcast.phonesafe.db.BlankNumberDao;
import cn.itcast.phonesafe.db.BlankNumberInfo;

/**
 * Created by Administrator on 2016/12/8.
 */
public class Text extends ApplicationTest {
    public void insert(){
        BlankNumberDao dao = BlankNumberDao.getInstance(getContext());
        for(int i=0;i<100;i++){
            if(i<10){
                dao.insert("1860000000"+i, 1+new Random().nextInt(3)+"");
            }else{
                dao.insert("186000000"+i, 1+new Random().nextInt(3)+"");
            }
        }
    }

    public void delete(){
        BlankNumberDao dao = BlankNumberDao.getInstance(getContext());
        dao.delete("110");
    }

    public void findAll(){
        BlankNumberDao dao = BlankNumberDao.getInstance(getContext());
        List<BlankNumberInfo> blackNumberInfoList = dao.findAll();
    }
}
