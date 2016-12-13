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

/***********
 * author: jackzhous
 * file: Constant.java
 * create date: 2016/12/5 13:25
 * desc:
 ************/
public class Constant {

    public static final String                    AUTHOR = "jackzhous";

    public static final String                    TAG_FRAGMENT_LOGIN = "login";
    public static final String                    TAG_FRAGMENT_PATH = "path";

    public static final String                    PATH = "path";
    public static final String                    DOWNLOAD_FILE = "file";

    public static final String                    USERNAME="username";
    public static final String                    PASSWD = "passwd";
    public static final String                    IP ="IP";
    public static final String                    PORT = "PORT";
    public static final String                    FRAG_INSTANCE = "fragment";

    public static final int                       FLAG_FTP_INPUT = 0x01;
    public static final int                       FLAG_FTP_CONNECT_LOGIN = 0x02;
    public static final int                       FLAG_GET_LIST = 0x03;
    public static final int                       FLAG_DOWNLOAD = 0x04;
    public static final int                       FLAG_DOWNLOAD_PROGRESS = 0x05;

    public static final int                       SUCCESS = 0x01;
    public static final int                       FAILED  = 0x00;
}
