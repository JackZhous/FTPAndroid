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

import android.os.Environment;
import android.util.Log;

import java.io.File;

/***********
 * author: jackzhous
 * file: FileUtil.java
 * create date: 2016/12/9 18:24
 * desc:
 ************/
public class FileUtil {

    private static FileUtil instance;

    private static String defaultFilePath;

    private FileUtil(){
        init();
    }

    private void init(){
        defaultFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "ftppath";
        Log.i("jackzhous", "path " + defaultFilePath);
        File defaultDiectory = new File(defaultFilePath);
        if(!defaultDiectory.exists()){
            defaultDiectory.mkdirs();
        }
    }

    public static FileUtil getInstance(){
        if (instance == null){
            instance = new FileUtil();
        }

        return instance;
    }

}
