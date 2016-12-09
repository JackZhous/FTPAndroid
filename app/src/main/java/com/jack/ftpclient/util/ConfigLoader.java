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
import android.util.ArrayMap;

import com.jack.ftpclient.bean.FTPConnectBean;

import java.util.HashMap;

/***********
 * author: jackzhous
 * file: ConfigLoader.java
 * create date: 2016/12/5 15:31
 * desc:
 * 配置加载工具类，耳机缓存：文件和内存，优先从内存从获取，没有再去文件中去找
 ************/
public class ConfigLoader {

    private static ConfigLoader loader = new ConfigLoader();

    private FTPConnectBean bean;

    private ConfigLoader(){}

    public static ConfigLoader getLoader(){
        if(loader == null){
            loader = new ConfigLoader();
        }

        return loader;
    }

    public void saveConnectBeanToCache(HashMap<String,String> map){
        bean = new FTPConnectBean();
        String username = map.get(Constant.USERNAME);
        String passwd   = map.get(Constant.PASSWD);
        String ip       = map.get(Constant.IP);
        String port     = map.get(Constant.PORT);
        bean.setUsernmae(username);
        bean.setPasswd(passwd);
        bean.setIP(ip);

        int num_port = 0;
        try {
            num_port = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        bean.setPort(num_port);
    }

    public void saveConnectBeanToSP(Context context){
        SPHelper.putValue(context, Constant.USERNAME, bean.getUsernmae());
        SPHelper.putValue(context, Constant.PASSWD, bean.getPasswd());
        SPHelper.putValue(context, Constant.IP, bean.getIP());
        SPHelper.putValue(context, Constant.PORT, bean.getPort());
    }

    public FTPConnectBean getConnectBean(Context context){
        if(bean != null){
            return bean;
        }

        bean = SPHelper.getConfig(context);
        return bean;
    }

}
