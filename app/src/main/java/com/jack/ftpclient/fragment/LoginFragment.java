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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jack.ftpclient.R;
import com.jack.ftpclient.inter.FragmentListener;
import com.jack.ftpclient.util.Constant;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/***********
 * author: jackzhous
 * file: LoginFragment.java
 * create date: 2016/12/5 10:47
 * desc:
 ************/
public class LoginFragment extends BaseFragment {

    public static final String              R_ID = "RESOURCE_ID";
    private int                             resourceId;
    private View                            view;
    private FragmentListener                mListener;
    private Activity                        mActivity;
    private ConnectedListener               connectedListener;

    public LoginFragment(){
        super();
        connectedListener = new ConnectedListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_login, null);
        Button connect = (Button)view.findViewById(R.id.btn_ok);
        connect.setOnClickListener(connectedListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setFragmentListener(FragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void setFragmentData(Object obj) {

    }

    private static class ConnectedListener implements View.OnClickListener{

        WeakReference<LoginFragment> weakReference;

        public ConnectedListener(LoginFragment fragment){
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onClick(View view) {
            LoginFragment fragmentUtil = weakReference.get();
            if(fragmentUtil != null){
                HashMap<String, Object> map = fragmentUtil.isInputEmpty();
                if(map == null){
                    Toast.makeText(fragmentUtil.mActivity, "输入信息不完整...", Toast.LENGTH_SHORT).show();
                }else{
                    fragmentUtil.mListener.fragCallback(Constant.FLAG_FTP_INPUT, map);
                }
            }
        }
    }

    private HashMap<String,Object> isInputEmpty(){
        HashMap<String,Object> map = null;
        if(view == null){
            return map;
        }
        EditText text = (EditText)view.findViewById(R.id.ftp_user);
        String username = null;
        if(text != null){
            username = text.getText().toString();
        }
        if(TextUtils.isEmpty(username)){
            return null;
        }

        EditText text1 = (EditText)view.findViewById(R.id.ftp_passwd);
        String passwd = null;
        if(text1 != null){
            passwd = text1.getText().toString();
        }
        if(TextUtils.isEmpty(passwd)){
            return null;
        }

        EditText text2 = (EditText)view.findViewById(R.id.ftp_server);
        String ip = null;
        if(text2 != null){
            ip = text2.getText().toString();
        }
        if(TextUtils.isEmpty(ip)){
            return null;
        }

        EditText text3 = (EditText)view.findViewById(R.id.ftp_port);
        String port = null;
        if(text3 != null){
            port = text3.getText().toString();
        }
        if(TextUtils.isEmpty(port)){
            return null;
        }

        map = new HashMap<>();
//        map.put(Constant.USERNAME, username);
//        map.put(Constant.PASSWD, passwd);
//        map.put(Constant.IP, ip);
//        map.put(Constant.PORT, port);

        map.put(Constant.USERNAME, "ftp");
        map.put(Constant.PASSWD, "ftp");
        map.put(Constant.IP, "10.8.240.25");
        map.put(Constant.PORT, "2121");
        return map;
    }

    @Override
    public void onBackPressed() {
        mListener.exitApp();
    }

    @Override
    public void notifyData(boolean delete) {

    }
}
