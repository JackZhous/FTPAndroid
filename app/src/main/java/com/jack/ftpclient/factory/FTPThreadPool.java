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

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/***********
 * author: jackzhous
 * file: FTPThreadPool.java
 * create date: 2016/12/5 13:47
 * desc:
 ************/
public class FTPThreadPool {
    private static FTPThreadPool                         mInstance = new FTPThreadPool();

    private static ExecutorService                       mThreadPool;

    public static FTPClient                              mClient   = new FTPClient();

    private static final int                             FIX_NUMBER = 5;

    private FTPThreadPool(){
        mThreadPool = Executors.newFixedThreadPool(FIX_NUMBER);
    }


    public static FTPThreadPool getmInstance(){
        if(mInstance == null){
            mInstance = new FTPThreadPool();
        }

        return mInstance;
    }


    public void executeTask(Runnable task){
        mThreadPool.submit(task);
    }


    public void recyleSelf(){
        try {
            mClient.disconnect(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mClient = null;
        mThreadPool.shutdownNow();
        mInstance = null;
        mThreadPool = null;
    }

}
