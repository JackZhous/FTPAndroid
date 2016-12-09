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
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.jack.ftpclient.MainActivity;
import com.jack.ftpclient.bean.FTPConnectBean;
import com.jack.ftpclient.bean.PathBean;
import com.jack.ftpclient.util.ConfigLoader;
import com.jack.ftpclient.util.Constant;

import java.io.IOException;
import java.lang.ref.WeakReference;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/***********
 * author: jackzhous
 * file: GetPathTask.java
 * create date: 2016/12/5 13:20
 * desc:
 ************/
public class GetPathTask implements Runnable {

    private static final String TAG = Constant.AUTHOR + "-" + "GetPathTask";
    private Handler             handler;
    private String              mPath;

    public GetPathTask(Handler handler, String path) {
        this.handler = handler;
        if(TextUtils.isEmpty(path)){
            path = "/";
        }
        this.mPath = path;
    }


    @Override
    public void run() {
        FTPClient client;
        boolean isFailed = false;
            try {
                client = FTPThreadPool.mClient;
                if(!client.isConnected()){
                    Message msg = handler.obtainMessage(Constant.FLAG_FTP_CONNECT_LOGIN);
                    msg.arg1 = Constant.FAILED;
                    handler.sendMessage(msg);
                }
                client.changeDirectory(mPath);
                FTPFile[] fileList = client.list();
                PathBean bean = new PathBean();
                bean.setCurrentPath(mPath);
                bean.setCurrentList(fileList);


                Message msg = handler.obtainMessage(Constant.FLAG_GET_LIST);
                msg.arg1 = Constant.SUCCESS;
                msg.obj = bean;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                isFailed = true;
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                isFailed = true;
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                isFailed = true;
                e.printStackTrace();
            } catch (FTPListParseException e) {
                isFailed = true;
                e.printStackTrace();
            } catch (FTPDataTransferException e) {
                isFailed = true;
                e.printStackTrace();
            }

        if(isFailed){
            Message msg = handler.obtainMessage(Constant.FLAG_GET_LIST);
            msg.arg1 = Constant.FAILED;
            handler.sendMessage(msg);
        }
        handler = null;
    }
}
