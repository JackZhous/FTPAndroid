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

import com.jack.ftpclient.util.ConfigLoader;
import com.jack.ftpclient.util.Constant;

import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/***********
 * author: jackzhous
 * file: DownloadTask.java
 * create date: 2016/12/9 18:14
 * desc:
 ************/
public class DownloadTask implements Runnable, FTPDataTransferListener {
    private Handler mHandler;
    private String  mFileName;

    public DownloadTask(String filename, Handler handler) {
        mHandler = handler;
        mFileName = filename;
    }


    @Override
    public void run() {
        boolean isFailed = false;
      /*  try {
            FTPClient client = FTPThreadPool.mClient;
            Message msg = mHandler.obtainMessage();
            if(!client.isConnected()){
                msg.what = Constant.FLAG_FTP_CONNECT_LOGIN;
                msg.arg1 = Constant.FAILED;
            }
            //client.download(mFileName, this);
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
        mHandler = null;*/
    }

    @Override
    public void started() {

    }

    @Override
    public void transferred(int i) {

    }

    @Override
    public void completed() {

    }

    @Override
    public void aborted() {

    }

    @Override
    public void failed() {

    }
}
