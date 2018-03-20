package com.evt.dmp.protocal;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by everitime5 on 2018-01-24.
 */

public class ConnectServer {
    private OkHttpClient client;
    private static ConnectServer instance = new ConnectServer();
    public static ConnectServer getInstance() {
        return instance;
    }

    private ConnectServer(){ this.client = new OkHttpClient(); }

    /** 웹 서버로 요청을 한다. */
    public void requestWebServer(String parameter, String parameter2, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("parameter", parameter)
                .build();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:1338/dpm/plan")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
