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
import android.util.Log;

import com.jack.ftpclient.util.ConfigLoader;
import com.jack.ftpclient.util.Constant;
import com.jack.ftpclient.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/***********
 * author: jackzhous
 * file: DownloadTask.java
 * create date: 2016/12/9 18:14
 * desc:
 ************/
public class DownloadTask implements Runnable{

    private static final String             TAG = Constant.AUTHOR + "-DownloadTask";
    private Handler                         mHandler;
    private HashMap                         mData;
    private FTPDataTransferListener         mListener;
    private long fileSize                   = 0;
    private long remoteSize                 = 0;
    private File                            mLocalFile;

    public DownloadTask(HashMap data, Handler handler) {
        mHandler = handler;
        mData = data;
        mListener = new DownloadFileListener();
        String path = mData.get(Constant.PATH).toString();
        String localFileName = path.substring(path.lastIndexOf("/") + 1);
        remoteSize = (Long)mData.get(Constant.SIZE);
        mLocalFile = FileUtil.getInstance().createLoaclFile(localFileName);
    }


    @Override
    public void run() {
            try {
            FTPClient client = FTPThreadPool.mClient;

            if(!client.isConnected()){
                Message msg = mHandler.obtainMessage();
                msg.what = Constant.FLAG_FTP_CONNECT_LOGIN;
                msg.arg1 = Constant.FAILED;
                mHandler.sendMessage(msg);
                return;
            }
            client.download(mData.get(Constant.PATH).toString(), mLocalFile, mListener);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            e.printStackTrace();
        }
    }



    private class DownloadFileListener implements FTPDataTransferListener{
        @Override
        public void started() {
            Log.i(TAG, "started");
        }

        @Override
        public void transferred(int i) {
            fileSize = fileSize + i;
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.FLAG_DOWNLOAD_PROGRESS;
            msg.arg1 = (int)(fileSize * Constant.PROGRESS_MAX / remoteSize);
            mHandler.sendMessage(msg);

        }

        @Override
        public void completed() {
            Log.i(TAG, "completed " + fileSize);
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.FLAG_DOWNLOAD;
            msg.arg1 = Constant.SUCCESS;
            mHandler.sendMessage(msg);
            recycle();
        }

        @Override
        public void aborted() {
            Log.i(TAG, "aborted");
            FileUtil.getInstance().deleteLocalFile(mLocalFile);
            recycle();
        }

        @Override
        public void failed() {
            Log.i(TAG, "failed");
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.FLAG_DOWNLOAD;
            msg.arg1 = Constant.FAILED;
            mHandler.sendMessage(msg);
            recycle();
        }
    }

    public void recycle(){
        mHandler = null;
        mLocalFile = null;
    }
}
