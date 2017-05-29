package com.example.storm.recycleviewdemo.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.storm.recycleviewdemo.R;
import com.example.storm.recycleviewdemo.base.BaseFragment;

/**
 * Created by Storm on 2017/5/29.
 *
 */

public class HomePager extends BaseFragment {
    @Override
    protected void bindView() {

    }

    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home_pager, container, false);
    }
}
