package com.example.zhuffei.ffei.tool;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhuffei on 2020/3/15.
 */

public class HttpUtil {
    //发送Http请求类，每次发送Http请求调用该方法
    /*
    * 通过okhttp3发送请求(用来请求纯文本比较方便)
    * */
    /*
    * 不发送数据
    * */
    public static void sendHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
        //请求封装成任务，通过newCall()发出请求，构造函数中参数为你发送的请求
        //然后执行enqueue()方法，讲服务器处理后的结果返回
    }
    /*
    * 发送数据
    * */
    public static void sendHttpRequest(String address, RequestBody requestBody, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        //将要发送的数据用RequestBody对象保存，然后传入到该参数即可
        client.newCall(request).enqueue(callback);
    }

}