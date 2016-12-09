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

import com.jack.ftpclient.fragment.BaseFragment;
import com.jack.ftpclient.fragment.LoginFragment;
import com.jack.ftpclient.fragment.PathFragment;

/***********
 * author: jackzhous
 * file: FragmentFactory.java
 * create date: 2016/12/9 13:16
 * desc:
 * 创建Fragment的工厂类，设计的目的是遵从面向对象的设计原则：
 * 最小接口原则，依赖Fragment的基类，而不是组细节的子类
 ************/
public final class FragmentFactory {


    public static BaseFragment createPathFragment(){
        return new PathFragment();
    }



    public static BaseFragment createDialogFragment(){
        return new LoginFragment();
    }
}
