package com.example.zhuffei.ffei.entity;

import android.util.Log;

import com.example.zhuffei.ffei.activity.MapActivity;
import com.example.zhuffei.ffei.service.Send_InputStream;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Shinelon on 2017/4/8.
 */

public class Send implements Runnable {
    private String Ip = UrlTool.host;
    private Integer port = 8080;
    private JSONObject jsonObject;
    private Socket socket;
    private OutputStream outputStream;
    private Send_InputStream inputStream;
    private MapActivity mapActivity;


    public Send(){}
    public Send(MapActivity mapActivity){
        this.mapActivity = mapActivity;
    }
    public String getIp() {
        return Ip;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private void startSocket(){
        try{
            socket = new Socket(Ip,port);
            Log.i("aaaaaa","连上了");
            outputStream = socket.getOutputStream();
            inputStream = new Send_InputStream(socket.getInputStream());
            inputStream.setMapActivity(mapActivity);
            new Thread(inputStream).start();
        }catch (Exception e){
            e.printStackTrace();
            Log.i("aaaaaa",e.getMessage()+"报错");
        }

    }
    @Override
    public void run() {
        try{
            startSocket();
            jsonObject = new JSONObject();
            while (socket != null){
                Thread.sleep(2000);
                jsonObject.put("lat",MapActivity.getLat());
                jsonObject.put("lng",MapActivity.getLng());
                outputStream.write(jsonObject.toString().getBytes("UTF-8"));
                outputStream.flush();
                Log.i("aaaaaaaaaaa",jsonObject.toString()+"上传的数据");
            }
        }catch (Exception e){
            Log.i("aaaaaaaa",e.getMessage()+"上传出错");
        }finally {
            if (outputStream != null){
                try {
                     outputStream.close();
                }catch (Exception e){
                      Log.i("aaaaaaaaaaaaaa",e.getMessage()+"关闭出错");
                }
            }
            if(socket != null){
                try{
                    socket.close();
                }catch (Exception e){
                    Log.i("aaaaaaaaaaaa",e.getMessage()+"关闭出错");
                }
            }
        }


    }
}
