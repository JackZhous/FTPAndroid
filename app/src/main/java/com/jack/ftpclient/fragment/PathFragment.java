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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jack.ftpclient.R;
import com.jack.ftpclient.bean.PathBean;
import com.jack.ftpclient.inter.FragmentListener;
import com.jack.ftpclient.util.Constant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import it.sauronsoftware.ftp4j.FTPFile;

/***********
 * author: jackzhous
 * file: PathFragment.java
 * create date: 2016/12/8 15:20
 * desc:
 * 显示ftp服务端的路径
 ************/
public class PathFragment extends BaseFragment {

    private FragmentListener            mListener;
    private Activity                    mActivity;
    private LinkedList<FTPFile>         mFTPFileList;
    private TextView                    mPathTitle;
    private ListAdapter                 mListAdapter;
    private StringBuilder               mCurrentDirectory;
    private int                         mDeletePosition = -1;

    private static Drawable             mFileDrawable;
    private static Drawable             mDirectoryDrawable;
    private static Drawable             mLinkDrawable;


    public PathFragment() {
        super();
        mCurrentDirectory = new StringBuilder();
        mFTPFileList = new LinkedList<>();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = getActivity();
        initFileDrawable();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_path, null);

        init(view);
        return view;
    }

    private void initFileDrawable(){
        if(mLinkDrawable != null){
            return;
        }
        Context context = mActivity.getApplicationContext();
        mFileDrawable = context.getResources().getDrawable(R.drawable.ic_file);
        mDirectoryDrawable = context.getResources().getDrawable(R.drawable.ic_folder);
        mLinkDrawable = context.getResources().getDrawable(R.drawable.ic_link);
    }

    private void init(View view){
        ListView listView = (ListView)view.findViewById(R.id.path_lv);
        mPathTitle = (TextView)view.findViewById(R.id.path_title);
        mListAdapter = new ListAdapter();
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new MyOnItemClickListener());
        listView.setOnItemLongClickListener(new ListItemOnLongClickListener());
    }

    private void listDirectory(String filename){
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constant.PATH, filename);
        mListener.fragCallback(Constant.FLAG_GET_LIST, map);
    }

    private class ListItemOnLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            FTPFile ftpFile = mFTPFileList.get(i);
            mDeletePosition = i;
            showAlertDialog(ftpFile, Constant.FLAG_DELETE_FILE);
            return false;
        }
    }


    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        public MyOnItemClickListener() {
            super();
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FTPFile ftpFile = mFTPFileList.get(i);
            switch (ftpFile.getType()){
                case FTPFile.TYPE_DIRECTORY:
                    listDirectory(ftpFile.getName());
                    break;

                case FTPFile.TYPE_FILE:
                    showAlertDialog(ftpFile, Constant.FLAG_DOWNLOAD);
                    break;
            }
        }



    }

    private void showAlertDialog(final FTPFile ftpFile, final int flag){
        StringBuilder sb = new StringBuilder();
        sb.append(mCurrentDirectory.toString());
        sb.append("\n");
        sb.append("文件名： " + ftpFile.getName());
        sb.append("\n");
        sb.append("文件大小: " + ftpFile.getSize());

        final String str = (flag == Constant.FLAG_DELETE_FILE) ? Constant.DELETE_FILE : Constant.DOWNLOAD_FILE;
        new AlertDialog.Builder(mActivity).setTitle("文件属性")
                .setMessage(sb.toString())
                .setNegativeButton(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> data = new HashMap<>();
                        String filename = mCurrentDirectory.toString().substring(1) + ftpFile.getName();
                        data.put(Constant.PATH, filename);
                        data.put(Constant.TYPE, ftpFile.getType());
                        data.put(Constant.SIZE, ftpFile.getSize());
                        mListener.fragCallback(flag, data);
                    }
                })
                .setPositiveButton("取消", null).show();
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    private void setmPathBean(PathBean bean) {
        FTPFile[] list = bean.getCurrentList();
        mFTPFileList.clear();

        for(FTPFile file : list){
            if(file.getName().startsWith(".")){
                continue;
            }
            Log.i("jackzhous", "code " + getEncoding(file.getName()));
            mFTPFileList.add(file);
        }
        mCurrentDirectory.append(bean.getCurrentPath());
        mCurrentDirectory.append("/");
        Log.i("jackzhous", "--" + mCurrentDirectory.toString());
        if(mPathTitle != null){
            Log.i("jackzhous", "directory view is null");
            mPathTitle.setText(mCurrentDirectory.toString());
        }
    }

    @Override
    public void setFragmentData(Object obj) {
        if(obj instanceof PathBean){
            PathBean bean = (PathBean)obj;
            setmPathBean(bean);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPathTitle != null){
            Log.i("jackzhous", "directory view is null");
            mPathTitle.setText(mCurrentDirectory.toString());
        }
    }

    @Override
    public void notifyData(boolean object) {
        if(object && mFTPFileList != null){
            mFTPFileList.remove(mDeletePosition);
            mDeletePosition = -1;
        }
        if(mListAdapter != null){
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setFragmentListener(FragmentListener listener) {
        mListener = listener;
    }


    private class ListAdapter extends BaseAdapter{


        public ListAdapter(){
        }

        @Override
        public int getCount() {
            return mFTPFileList.size();

        }

        @Override
        public Object getItem(int i) {
            return mFTPFileList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View containerView, ViewGroup viewGroup) {
            FTPFile ftpFile = mFTPFileList.get(i);
            ItemBean bean;
            if(containerView == null){
                containerView = LayoutInflater.from(mActivity).inflate(R.layout.path_list_item, null);
                bean = new ItemBean();
                bean.mImageView = (ImageView)containerView.findViewById(R.id.file_icon);
                bean.mTextView  = (TextView)containerView.findViewById(R.id.file_name);
                containerView.setTag(bean);
            }else{
                bean = (ItemBean)containerView.getTag();
            }
            String filename = null;
            try {
                filename = new String(ftpFile.getName().getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                filename = ftpFile.getName();
            }
            bean.mTextView.setText(filename);
            bean.mImageView.setImageDrawable(getDrawableWithType(ftpFile.getType()));
            return containerView;
        }
    }

    class ItemBean{
        ImageView               mImageView;
        TextView                mTextView;
    }


    private Drawable getDrawableWithType(int type){
        switch (type){
            case FTPFile.TYPE_DIRECTORY:
                return mDirectoryDrawable;
            case FTPFile.TYPE_FILE:
                return mFileDrawable;
            default:
                return mLinkDrawable;
        }
    }


    @Override
    public void onBackPressed() {

        if(mCurrentDirectory.length() <= 2){     //根目录
            mListener.exitApp();
            return;
        }
        mCurrentDirectory.deleteCharAt(mCurrentDirectory.lastIndexOf("/"));
        Log.i("jackzhous", "before " + mCurrentDirectory);
        String directory = mCurrentDirectory.substring(1, mCurrentDirectory.lastIndexOf("/"));
        mCurrentDirectory.delete(mCurrentDirectory.lastIndexOf("/"), mCurrentDirectory.length());
        mCurrentDirectory.delete(mCurrentDirectory.lastIndexOf("/"), mCurrentDirectory.length());
        Log.i("jackzhous", "after " + mCurrentDirectory + " " + directory);
        listDirectory(directory);
    }
}
