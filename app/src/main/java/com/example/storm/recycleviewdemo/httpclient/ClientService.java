package com.example.storm.recycleviewdemo.httpclient;

import com.example.storm.recycleviewdemo.bean.JokeBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Storm on 2017/5/29.
 * <p>
 * 请求的连接
 */

public interface ClientService {


    @GET("0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8")
    Observable<JokeBean> getJokeService();
}
