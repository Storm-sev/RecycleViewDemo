package com.example.storm.recycleviewdemo.base;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.storm.recycleviewdemo.utils.LogUtils;

import rx.Subscriber;

/**
 * Created by Storm on 2017/5/29.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {


    public Context mContext;
    public ProgressDialog dialog;
    public BaseSubscriber(Context context) {
        this.mContext = context;
        dialog = new ProgressDialog(context);
        dialog.setMessage("loading.....");

    }

    @Override
    public void onStart() {
        super.onStart();
        dialog.show();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();

    }

    @Override
    public void onError(Throwable throwable) {
        LogUtils.d("网络请求错误" + throwable.toString());
        dialog.dismiss();
    }

    @Override
    public abstract void onNext(T t);
}
