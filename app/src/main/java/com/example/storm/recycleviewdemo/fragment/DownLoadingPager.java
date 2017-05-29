package com.example.storm.recycleviewdemo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.storm.recycleviewdemo.R;
import com.example.storm.recycleviewdemo.base.BaseFragment;

/**
 * Created by Storm on 2017/5/29.
 */

public class DownLoadingPager extends BaseFragment {
    @Override
    protected void bindView() {

    }

    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup conainer) {
        return inflater.inflate(R.layout.fragment_downloading_pager, conainer, false);
    }
}
