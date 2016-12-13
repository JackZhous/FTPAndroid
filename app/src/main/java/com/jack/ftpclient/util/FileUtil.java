package com.jack.ftpclient.util;

import android.os.Environment;

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


    public File createLoaclFile(String filename){
        String file = defaultFilePath + File.separator + filename;
        File createFile = new File(file);

        return createFile;
    }


    public void deleteLocalFile(File file){
        if(file == null){
            return;
        }
        if(file.exists()){
            file.delete();
        }
    }
}
