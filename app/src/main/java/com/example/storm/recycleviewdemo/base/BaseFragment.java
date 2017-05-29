package com.example.storm.recycleviewdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Storm on 2017/5/28.
 * fragment 的基类
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View rootView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = onCreateRootView(inflater, container);
        bindView();
        return rootView;
    }

    /**
     * 实例化布局
     */
    protected abstract void bindView();

    protected abstract View onCreateRootView(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
    }


}
