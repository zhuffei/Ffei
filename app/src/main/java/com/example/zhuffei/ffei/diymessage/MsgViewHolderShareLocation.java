package com.example.zhuffei.ffei.diymessage;

import android.content.Intent;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.MapActivity;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderShareLocation extends MsgViewHolderBase {
    public MsgViewHolderShareLocation(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

//    @Override
//    protected String getDisplayText() {
//        //实例化一个attachment
//ShareLocationAttachment attachment = (ShareLocationAttachment)message.getAttachment();
//        return "开启了位置共享";
//    }

    @Override
    public int getContentResId() {
        return R.layout.share_location_message;
    }

    @Override
    public void inflateContentView() {

    }

    @Override
    public void bindContentView() {
        ShareLocationAttachment attachment = (ShareLocationAttachment) message.getAttachment();
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    public void onItemClick() {
        FfeiApplication.context.startActivity(new Intent(FfeiApplication.context, MapActivity.class)
                .putExtra("accid1", message.getSessionId()).putExtra("accid2", message.getFromAccount()));

    }
}

