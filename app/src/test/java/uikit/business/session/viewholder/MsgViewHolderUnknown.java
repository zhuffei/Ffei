package com.netease.nim.uikit.business.session.viewholder;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderUnknown extends MsgViewHolderBase {

    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.nim_message_item_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        return message.getSessionType() != SessionTypeEnum.ChatRoom;
    }

    @Override
    public void inflateContentView() {
    }

    @Override
    public void bindContentView() {
    }
}
