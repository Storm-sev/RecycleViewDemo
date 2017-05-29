package com.example.storm.recycleviewdemo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.storm.recycleviewdemo.R;
import com.example.storm.recycleviewdemo.adapter.JokeAdapter;
import com.example.storm.recycleviewdemo.base.BaseFragment;
import com.example.storm.recycleviewdemo.base.BaseSubscriber;
import com.example.storm.recycleviewdemo.bean.JokeBean;
import com.example.storm.recycleviewdemo.httpclient.CommonTransformer;
import com.example.storm.recycleviewdemo.httpclient.RetrofitClienttManager;
import com.example.storm.recycleviewdemo.utils.LogUtils;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by Storm on 2017/5/29.
 */

public class HomePager extends BaseFragment {

    @BindView(R.id.rv_home)
    RecyclerView rvHome;

    private JokeAdapter mAdapter;


    @Override
    protected void bindView() {

    }

    @Override
    protected View onCreateRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_home_pager, container, false);
    }

    @Override
    protected void initData() {

        mAdapter = new JokeAdapter(mContext, false);

        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.item_empty_view, (ViewGroup) rvHome.getParent(), false);
        mAdapter.setEmptyView(emptyView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHome.setLayoutManager(layoutManager);
        rvHome.setAdapter(mAdapter);

        getNetData();
    }




    /**
     * 加载网络数据
     */
    public void getNetData() {

        Observable<JokeBean> jokeService = RetrofitClienttManager.getClientService().getJokeService();

        jokeService.compose(new CommonTransformer<JokeBean>())
                .subscribe(new BaseSubscriber<JokeBean>(mContext) {
                    @Override
                    public void onNext(JokeBean jokeBean) {
                        LogUtils.d("请求成功" + jokeBean.getInfo().getCount());
                        mAdapter.refreshData(jokeBean.getList());

                    }
                });
    }




}
