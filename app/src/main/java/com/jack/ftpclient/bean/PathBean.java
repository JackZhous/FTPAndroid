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

package com.jack.ftpclient.bean;

import it.sauronsoftware.ftp4j.FTPFile;

/***********
 * author: jackzhous
 * file: PathBean.java
 * create date: 2016/12/8 16:07
 * desc:
 ************/
public class PathBean {
    private String      currentPath;
    private FTPFile[]   currentList;


    public String getCurrentPath() {
        return currentPath;
    }

    public FTPFile[] getCurrentList() {
        return currentList;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public void setCurrentList(FTPFile[] currentList) {
        this.currentList = currentList;
    }
}
