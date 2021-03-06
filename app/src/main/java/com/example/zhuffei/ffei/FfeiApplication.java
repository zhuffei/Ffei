package com.example.zhuffei.ffei;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.zhuffei.ffei.diymessage.CustomAttachParser;
import com.example.zhuffei.ffei.diymessage.ShareLocationAttachment;
import com.example.zhuffei.ffei.diymessage.MsgViewHolderShareLocation;
import com.example.zhuffei.ffei.entity.User;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.util.NIMUtil;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/6 19:22
 */
public class FfeiApplication extends Application {

    public static Context context;

    public static boolean isLogin = false;

    public static User user;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        NIMClient.init(context,null,null);
        if (NIMUtil.isMainProcess(this)) {

            Log.d("aaaaaaaaaa", "初始化");
            NimUIKit.init(this);

            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

            NimUIKit.registerMsgItemViewHolder(ShareLocationAttachment.class, MsgViewHolderShareLocation.class);
        } else {
            Log.d("aaaaaaaaaa", "不是主线程？？？");
        }

    }
}
