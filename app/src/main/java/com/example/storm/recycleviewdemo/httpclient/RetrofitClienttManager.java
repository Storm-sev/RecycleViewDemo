package com.example.storm.recycleviewdemo.httpclient;

import com.example.storm.recycleviewdemo.utils.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Storm on 2017/5/28.
 *
 */

public class RetrofitClienttManager {

    public static OkHttpClient httpClient;
    public static volatile Retrofit mRetrofit;
    public static ClientService mClientService;


    public static Retrofit initRetrofit() {

        if (mRetrofit == null) {
            synchronized (RetrofitClienttManager.class) {
                if (mRetrofit == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(httpLoggingInterceptor)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .build();

                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(Api.BASE_URL)
                            .client(httpClient)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                }
            }
        }
        return mRetrofit;
    }


    /**
     *  获取连接请求
     * @return
     */
    public static ClientService getClientService(){
        if (mClientService == null) {
            mClientService = initRetrofit().create(ClientService.class);
        }
        return mClientService;
    }


}
