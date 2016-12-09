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

/***********
 * author: jackzhous
 * file: FTPConnectBean.java
 * create date: 2016/12/5 15:29
 * desc:
 ************/
public class FTPConnectBean {
    private String usernmae;
    private String passwd;
    private String IP;
    private int    port = 8745;

    public void setUsernmae(String usernmae) {
        this.usernmae = usernmae;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public String getUsernmae() {
        return usernmae;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }
}
