package com.jerry.iotdashboard.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hjq.toast.ToastUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class http {


    private OkHttpClient client ;
    String val;
    public http(OkHttpClient client) {
        this.client = client;
    }

    public http() {
        this.client = new OkHttpClient();

    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                Bundle data = msg.getData();
                val = data.getString("value");
                ToastUtils.show( "请求结果:" + val);
                return true;
            }

        });
        new Thread( new Runnable() {
            @Override
            public void run() {
                try (Response response = client.newCall(request).execute()) {

                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("value",response.body().string());
                        msg.setData(data);
                        handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return val;
    }
}
