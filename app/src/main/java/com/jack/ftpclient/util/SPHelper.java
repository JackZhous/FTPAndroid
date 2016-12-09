/*
 *         Copyright (C) 2016-2017 宙斯
 *         All rights reserved
 *
 *        filename :Class4
 *        description :
 *
 *         created by jackzhous at  11/07/2016 12:12:12
 *         http://blog.csdn.net/jackzhouyu
 */

package com.jack.ftpclient.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jack.ftpclient.bean.FTPConnectBean;

import java.util.Set;

/***********
 * author: jackzhous
 * file: SPHelper.java
 * create date: 2016/12/5 14:22
 * desc:
 ************/
public class SPHelper {

    private static final String FILE_NAME = "ftphelper";

    public static void putValue(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        if(key == null || "".equals(key)){
            return;
        }

        editor.putString(key, value);
        editor.apply();
    }

    public static void putValue(Context context, String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        if(key == null || "".equals(key)){
            return;
        }

        editor.putInt(key, value);
        editor.apply();
    }

    public static void putSet(Context context, String key, Set<String> value){
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit();
        if(key == null || "".equals(key)){
            return;
        }

        editor.putStringSet(key, value);
        editor.apply();
    }

    public static FTPConnectBean getConfig(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(Constant.PORT)){
            return null;
        }
        FTPConnectBean bean = new FTPConnectBean();
        bean.setIP(sharedPreferences.getString(Constant.IP, ""));
        bean.setPasswd(sharedPreferences.getString(Constant.PASSWD, ""));
        bean.setUsernmae(sharedPreferences.getString(Constant.USERNAME, ""));
        bean.setPort(sharedPreferences.getInt(Constant.PORT, 0));

        return bean;
    }
}
