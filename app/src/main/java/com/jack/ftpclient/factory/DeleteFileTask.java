package com.jack.ftpclient.factory;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jack.ftpclient.util.Constant;

import java.io.IOException;
import java.util.HashMap;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/***********
 * author: jackzhous
 * file: DeleteFileTask.java
 * create date: 2016/12/14 16:33
 * desc:
 ************/
public class DeleteFileTask implements Runnable {

    private Handler mHandler;
    private HashMap mData;

    public DeleteFileTask(Handler handler, HashMap map){

        mData = map;
        mHandler = handler;
    }

    @Override
    public void run() {
        FTPClient client = FTPThreadPool.mClient;
        if(!client.isConnected()){
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.FLAG_FTP_CONNECT_LOGIN;
            msg.arg1 = Constant.FAILED;
            mHandler.sendMessage(msg);
            return;
        }
        boolean failed = false;
        try {
            String filename = mData.get(Constant.PATH).toString();
            Log.i("jackzhous", "delete " + filename);
            int type = (Integer)mData.get(Constant.TYPE);
            switch (type){
                case FTPFile.TYPE_DIRECTORY:
                    client.deleteDirectory(filename);
                    break;

                case FTPFile.TYPE_FILE:
                    client.deleteFile(filename);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            failed = true;
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            failed = true;
        } catch (FTPException e) {
            failed = true;
            e.printStackTrace();
        }
        Message message = mHandler.obtainMessage(Constant.FLAG_DELETE_FILE);
        if(failed){
            message.arg1 = Constant.FAILED;
            Log.i("jackzhous", "delete failed");
        }else{
            Log.i("jackzhous", "delete success");
            message.arg1 = Constant.SUCCESS;
        }
        mHandler.sendMessage(message);
        mHandler = null;
        mData = null;
    }
}
