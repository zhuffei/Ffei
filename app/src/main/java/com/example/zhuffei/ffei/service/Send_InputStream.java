package com.example.zhuffei.ffei.service;

import android.util.Log;

import com.example.zhuffei.ffei.activity.MapActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Shinelon on 2017/4/8.
 */

public class Send_InputStream implements Runnable {

    private InputStream inputStream;
    private MapActivity mapActivity;
    private String text = "null";

    public Send_InputStream(){}
    public Send_InputStream(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public void setMapActivity(MapActivity MapActivity) {
        this.mapActivity = mapActivity;
    }

    @Override
    public void run() {
         int len = 0;
        byte[] buff = new byte[1024];
        try{
            while ((len = inputStream.read(buff)) != -1){
                text = new String(buff,0,len);
                Log.i("aaaaaaaaaaa",text+"返回值");
                JSONObject jsonObject = new JSONObject(text);
                int errorCode = jsonObject.getInt("errorCode");
                if (errorCode == 200){
                    JSONArray jsonArray = jsonObject.getJSONArray("date");
//                    mapActivity.allLatLng(jsonArray);
                }
            }
        }catch (Exception e){
            Log.i("aaaaaa",e.getMessage()+"接收返回值报错");
        }
    }
}
