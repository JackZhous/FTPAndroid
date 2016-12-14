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

package com.jack.ftpclient.inter;

import android.util.ArrayMap;

import java.util.HashMap;

/***********
 * author: jackzhous
 * file: FragmentListener.java
 * create date: 2016/12/5 11:53
 * desc:
 ************/
public interface FragmentListener {
    void fragCallback(int flag, HashMap<String, Object> map);


    void exitApp();
}
