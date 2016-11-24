package com.example.admin.youhuo.utils;

/**
 * Created by admin on 2016/11/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

/**
 *本类为sharedpreferences保存类    xml本地保存类
 *	 sharedpreferences：android 五大存储方式之一，存储数据类型为： K  V  文件已xml形式保存
 *					项目应用：1.导航页是否第一次进入
 *						    2.用户信息，登陆信息
 * 好处：只要不卸载软件，或者不手动清除，基本上不会被清除
 * */
public class SharedUtils {
    private String name="fangjie";
    /**
     * 保存数据的方法
     * */
    public void saveShared(String key, String value, Context context){
        SharedPreferences shared=context.getSharedPreferences(name,0);
        SharedPreferences.Editor edit=shared.edit();
        edit.putString(key,value);
        edit.commit();
    }
    /**
     * 从本地获取数据
     * */
    public String getShared(String key,Context context){
        String str=null;
        SharedPreferences shared=context.getSharedPreferences(name,0);
        str=shared.getString(key,"");
        return str;
    }
}
