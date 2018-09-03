package com.gy.android.flowmusic.http;

import com.gy.android.flowmusic.constants.Constant;
import com.gy.android.flowmusic.utils.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit2网络请求封装
 */
public class RetrofitHelper {
    //接口根地址
    private static final String BASE_URL = Constant.BASE_URL;
    //设置超时时间
    private static final long DEFAULT_TIMEOUT = 10_000L;

    private Retrofit retrofit;

    private static class SingletonHolder {
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    //私有化构造方法
    private RetrofitHelper() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                //添加请求头
                .addInterceptor(new LoggerInterceptor("===", true))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                //添加Gson解析
                .addConverterFactory(GsonConverterFactory.create())
                //添加rxjava
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RetrofitHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //这里返回一个泛型类，主要返回的是定义的接口类
    public <T> T createService(Class<T> clazz) {
        return retrofit.create(clazz);
    }

}
