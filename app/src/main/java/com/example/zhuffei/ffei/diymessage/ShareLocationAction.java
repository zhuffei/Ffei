package com.example.zhuffei.ffei.diymessage;

import android.content.Intent;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.MapActivity;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class ShareLocationAction extends BaseAction {
    public ShareLocationAction(){
        super(R.drawable.nim_message_plus_location_selector, R.string.shareLocation);
    }
    @Override
    public void onClick() {
        ShareLocationAttachment attachment = new ShareLocationAttachment();
        IMMessage message = MessageBuilder.createCustomMessage(
                getAccount(), getSessionType(), attachment.getValue().getDesc(), attachment
        );
        sendMessage(message);
        FfeiApplication.context.startActivity(new Intent(FfeiApplication.context, MapActivity.class));
    }
}