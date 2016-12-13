package com.jack.ftpclient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jack.ftpclient.bean.PathBean;
import com.jack.ftpclient.factory.ConnectTask;
import com.jack.ftpclient.factory.DownloadTask;
import com.jack.ftpclient.factory.FTPThreadPool;
import com.jack.ftpclient.factory.FragmentFactory;
import com.jack.ftpclient.factory.GetPathTask;
import com.jack.ftpclient.fragment.BaseFragment;
import com.jack.ftpclient.inter.FragmentListener;
import com.jack.ftpclient.util.ConfigLoader;
import com.jack.ftpclient.util.Constant;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;


public class MainActivity extends FragmentActivity {
    private static final String       TAG = Constant.AUTHOR + " --- " + "MainActivity";
    private static final int          MAX = 1000;
    private FragmentListener          listener;
    private FragmentManager           mManager;
    private FTPThreadPool             mThreadPool;
    private Handler                   mHandler;
    private ProgressDialog            mDownloadFileDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerListener();

        mThreadPool = FTPThreadPool.getmInstance();
        mHandler = new MyHandler(this);
        mManager = getSupportFragmentManager();
        autoConnectFTPServer();
    }


    public void showFragDialog(){
        BaseFragment dialogFragment = FragmentFactory.createDialogFragment();
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.replace(R.id.container, dialogFragment, Constant.TAG_FRAGMENT_LOGIN);
        dialogFragment.setFragmentListener(listener);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }


    private void autoConnectFTPServer(){
        Runnable connect = new ConnectTask(this, mHandler);
        mThreadPool.executeTask(connect);
    }

    private void getServerPath(String directory){
        Runnable path = new GetPathTask(mHandler, directory);
        mThreadPool.executeTask(path);
    }

    /**
     * 下载文件
     * @param file
     */
    private void downLoadFile(Object file){
        FTPFile ftpFile = (FTPFile)file;
        Runnable download = new DownloadTask(ftpFile, mHandler);
        mThreadPool.executeTask(download);
        createProgressBar(ftpFile.getName());
    }


    private void createProgressBar(String fileName){
        mDownloadFileDialog = new ProgressDialog(this);
        mDownloadFileDialog.setTitle("下载文件中....");
        mDownloadFileDialog.setMessage(fileName);
        mDownloadFileDialog.setMax(MAX);
        mDownloadFileDialog.setProgress(0);
        mDownloadFileDialog.setCanceledOnTouchOutside(false);
        mDownloadFileDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadFileDialog.setCancelable(false);
        mDownloadFileDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mThreadPool.mClient.abortCurrentDataTransfer(true);
                    mDownloadFileDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FTPIllegalReplyException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showProgressBar(int percent){
        if(mDownloadFileDialog == null){
            return;
        }

        Log.i(TAG, "progress " + percent);
        mDownloadFileDialog.setProgress(percent);
        mDownloadFileDialog.show();
    }


    private void registerListener(){
        listener = new FragmentListener() {
            @Override
            public void fragCallback(int flag, HashMap code) {
                switch (flag){
                    case Constant.FLAG_FTP_INPUT:
                        ConfigLoader.getLoader().saveConnectBeanToCache(code);
                        Runnable connect = new ConnectTask(MainActivity.this, mHandler);
                        mThreadPool.executeTask(connect);
                        break;

                    case Constant.FLAG_GET_LIST:
                        String path = (String) code.get(Constant.PATH);
                        getServerPath(path);
                        break;

                    case Constant.FLAG_DOWNLOAD:
                        Object ftpFile = code.get(Constant.DOWNLOAD_FILE);
                        downLoadFile(ftpFile);
                        break;



                }
            }
        };
    }



    public static class MyHandler extends Handler{

        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = weakReference.get();
            if(activity != null){
                handleMyMessage(activity, msg);
            }
        }

        private void handleMyMessage(MainActivity activity, Message message){
            int resFlag = message.what;
            switch (resFlag){
                case Constant.FLAG_FTP_CONNECT_LOGIN:
                    doFTPLoginResult(message, activity);
                    break;

                case Constant.FLAG_GET_LIST:
                    doFTPFileList(message, activity);
                    break;

                case Constant.FLAG_DOWNLOAD:
                    activity.mDownloadFileDialog.dismiss();
                    activity.mDownloadFileDialog = null;
                    Log.i(TAG, "下载成功");
                    break;

                case Constant.FLAG_DOWNLOAD_PROGRESS:
                    activity.showProgressBar(message.arg1);
                    break;
            }
        }

        /**
         * 是否有dialog显示了
         * @param fragmentManager
         * @return
         */
        private boolean isShowDialog(FragmentManager fragmentManager){
            BaseFragment dialogFragment = (BaseFragment)fragmentManager.findFragmentByTag(Constant.TAG_FRAGMENT_LOGIN);
            if(dialogFragment == null){
                return false;
            }
            return true;
        }

        /**
         * 处理获取路径结果
         * @param message
         * @param activity
         */
        private void doFTPFileList(Message message, MainActivity activity){
            if(Constant.SUCCESS == message.arg1){
                PathBean bean = (PathBean)message.obj;
                BaseFragment fragment = (BaseFragment)activity.mManager.findFragmentByTag(Constant.TAG_FRAGMENT_PATH);
                if(fragment == null){
                    fragment = FragmentFactory.createPathFragment();
                    FragmentTransaction transaction = activity.mManager.beginTransaction();
                    transaction.replace(R.id.container, fragment, Constant.TAG_FRAGMENT_PATH);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    fragment.setFragmentListener(activity.listener);
                }
                fragment.setFragmentData(bean);
                fragment.notifyData();
            }else{
                Toast.makeText(activity, "获取路径失败", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 处理FTP登录结果
         */
        private void doFTPLoginResult(Message message, MainActivity activity){
            if(Constant.SUCCESS != message.arg1){
                Toast.makeText(activity, "连接登录失败", Toast.LENGTH_SHORT).show();
                if(!isShowDialog(activity.mManager)){
                    activity.showFragDialog();
                }
            }else{
                Toast.makeText(activity, "连接登录成功", Toast.LENGTH_SHORT).show();
                ConfigLoader.getLoader().saveConnectBeanToSP(activity);
                if(isShowDialog(activity.mManager)){
                    activity.mManager.popBackStack();
                }
                activity.getServerPath("/");
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThreadPool.recyleSelf();
    }
}
