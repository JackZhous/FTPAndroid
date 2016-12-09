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

import java.util.HashMap;

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
    private FTPFile[]                   mFTPFileList;
    private TextView                    mPathTitle;
    private ListAdapter                 mListAdapter;
    private StringBuilder               mCurrentDirectory;

    private static Drawable             mFileDrawable;
    private static Drawable             mDirectoryDrawable;
    private static Drawable             mLinkDrawable;


    public PathFragment() {
        super();
        mCurrentDirectory = new StringBuilder();
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
        mPathTitle.setText("/");
        mListAdapter = new ListAdapter();
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(new MyOnItemClickListener());
    }


    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        public MyOnItemClickListener() {
            super();
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            FTPFile ftpFile = mFTPFileList[i];
            switch (ftpFile.getType()){
                case FTPFile.TYPE_DIRECTORY:
                    listDirectory(ftpFile);
                    break;

                case FTPFile.TYPE_FILE:
                    showFilePropert(ftpFile);
                    break;
            }
        }


        private void listDirectory(FTPFile ftpFile){
            HashMap<String, Object> map = new HashMap<>();
            map.put(Constant.PATH, ftpFile.getName());
            mListener.fragCallback(Constant.FLAG_GET_LIST, map);
        }

        private void showFilePropert(final FTPFile ftpFile){
            StringBuilder sb = new StringBuilder();
            sb.append(mCurrentDirectory.toString());
            sb.append("\n");
            sb.append("文件名： " + ftpFile.getName());
            sb.append("\n");
            sb.append("文件大小: " + ftpFile.getSize());

            new AlertDialog.Builder(mActivity).setTitle("文件属性")
                            .setMessage(sb.toString())
                            .setNegativeButton("下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put(Constant.DOWNLOAD_FILE, mCurrentDirectory.toString().substring(1) + ftpFile.getName());
                                    mListener.fragCallback(Constant.FLAG_DOWNLOAD, data);
                                }
                            })
                            .setPositiveButton("取消", null).show();
        }
    }

    private void setmPathBean(PathBean bean) {
        mFTPFileList = bean.getCurrentList();
        mCurrentDirectory.append(bean.getCurrentPath());
        mCurrentDirectory.append("/");
        if(mPathTitle != null){
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
    public void notifyData() {
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
            return mFTPFileList.length;

        }

        @Override
        public Object getItem(int i) {
            return mFTPFileList[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View containerView, ViewGroup viewGroup) {
            FTPFile ftpFile = mFTPFileList[i];
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
            bean.mTextView.setText(ftpFile.getName());
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


}
