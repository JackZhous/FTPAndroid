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

package com.jack.ftpclient.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.jack.ftpclient.inter.FragmentListener;

/***********
 * author: jackzhous
 * file: BaseFragment.java
 * create date: 2016/12/8 15:25
 * desc:
 ************/
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
        super();
    }


    public abstract void setFragmentListener(FragmentListener listener);

    public abstract void setFragmentData(Object obj);

    public abstract void notifyData(boolean delete);

    public abstract void onBackPressed();
}
