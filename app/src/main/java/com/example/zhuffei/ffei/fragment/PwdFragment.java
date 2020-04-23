package com.example.zhuffei.ffei.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.BuyActivity;
import com.example.zhuffei.ffei.view.InputMethodView;
import com.example.zhuffei.ffei.view.PwdView;

/**
 * Created by chengww on 2017/5/13.
 */

public class PwdFragment extends DialogFragment implements View.OnClickListener {

    public static final String TYPE = "type";    //指纹 or pwd

    public int getType() {
        return type;
    }

    private int type = 0;

    private PwdView psw_input;
    private PwdView.InputCallBack inputCallBack;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定

        Bundle bundle = getArguments();

        if (bundle != null) {
            type = bundle.getInt(TYPE,0);
            switch (type){
                case 1:
//                    dialog.setContentView(R.layout.fragment_finger);
//                    initFingerView(dialog);
                    break;
                default:
                    dialog.setContentView(R.layout.fragment_pwd);
                    initPwdView(dialog);
                    break;
            }
        }else{
            dialog.setContentView(R.layout.fragment_pwd);
            initPwdView(dialog);
        }

        dialog.setCanceledOnTouchOutside(false); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);


        return dialog;
    }

    private void initPwdView(Dialog dialog) {
        psw_input = dialog.findViewById(R.id.pwdView);
        InputMethodView inputMethodView = dialog.findViewById(R.id.inputMethodView);
        psw_input.setInputMethodView(inputMethodView);
        psw_input.setInputCallBack(inputCallBack);

        dialog.findViewById(R.id.iv_close).setOnClickListener(this);
        dialog.findViewById(R.id.tv_miss_pwd).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.tv_miss_pwd:
                ((BuyActivity)getActivity()).showTintDialog("忘记密码","密码是123456",null);
                break;
        }
    }

    /**
     * 设置输入回调
     *
     * @param inputCallBack
     */
    public void setPaySuccessCallBack(PwdView.InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }


}

