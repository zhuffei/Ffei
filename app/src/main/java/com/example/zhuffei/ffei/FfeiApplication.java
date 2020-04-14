package com.example.zhuffei.ffei;

import android.app.Application;
import android.content.Context;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.util.NIMUtil;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/6 19:22
 */
public class FfeiApplication extends Application {

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        NIMClient.init(this,null,null);
        if(NIMUtil.isMainProcess(this)){
            NimUIKit.init(this);
        }
    }
}
