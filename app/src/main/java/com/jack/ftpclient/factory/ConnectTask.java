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

package com.jack.ftpclient.factory;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.jack.ftpclient.bean.FTPConnectBean;
import com.jack.ftpclient.util.ConfigLoader;
import com.jack.ftpclient.util.Constant;

import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/***********
 * author: jackzhous
 * file: ConnectTask.java
 * create date: 2016/12/5 14:20
 * desc:
 ************/
public class ConnectTask implements Runnable {

    private FTPConnectBean   bean;
    private Handler          mHandler;

    public ConnectTask(Activity activity, Handler handler) {
        bean = ConfigLoader.getLoader().getConnectBean(activity);
        mHandler = handler;
    }


    @Override
    public void run() {
        boolean isFailed = false;
        try {
            FTPClient client = FTPThreadPool.mClient;
            Message msg = mHandler.obtainMessage(Constant.FLAG_FTP_CONNECT_LOGIN);
            if(bean != null){
                if(!client.isConnected()){
                    client.connect(bean.getIP(), bean.getPort());
                }
                client.login(bean.getUsernmae(), bean.getPasswd());
                msg.arg1 = Constant.SUCCESS;
            }else{
                msg.arg1 = Constant.FAILED;
            }
            mHandler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
            isFailed = true;
        } catch (FTPIllegalReplyException e) {
            isFailed = true;
            e.printStackTrace();
        } catch (FTPException e) {
            isFailed = true;
            e.printStackTrace();
        }
        if(isFailed){
            Message msg = mHandler.obtainMessage(Constant.FLAG_FTP_CONNECT_LOGIN);
            msg.arg1 = Constant.FAILED;
            mHandler.sendMessage(msg);
        }
        bean = null;
        mHandler = null;
    }
}
