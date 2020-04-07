package com.example.zhuffei.ffei;

import android.app.Application;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/6 19:22
 */
public class FfeiApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        NIMClient.init(this,null,null);

        if(NIMUtil.isMainProcess(this)){
            NimUIKit.init(this);
        }
    }
}
