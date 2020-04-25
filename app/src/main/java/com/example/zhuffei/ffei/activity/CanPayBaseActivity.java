package com.example.zhuffei.ffei.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.fragment.PwdFragment;
import com.example.zhuffei.ffei.view.PwdView;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/25 12:27
 */
public class CanPayBaseActivity extends BaseActivity implements PwdView.InputCallBack {
    public PwdFragment fragment;

    public void intoPwdFragment() {
        if (count <= 0)
            showPwdError();
        else {
            synchronized (MainActivity.class){
                if (fragment == null || fragment.getType() != 0 || !fragment.isResumed()){
                    createDialogFragment(0);
                }
            }
        }
    }
    public void createDialogFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(PwdFragment.TYPE, type);
        fragment = new PwdFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(CanPayBaseActivity.this);
        fragment.show(getSupportFragmentManager(), "Pwd");
    }

    //还可以输入密码次数
    public int count = 5;



    public void showPwdError() {
        count--;
        if (count <= 0){
            showTintDialog("提示：", "密码输入达到上限", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fragment.dismiss();
                }
            });
        }else {
            showTintDialog("密码错误","密码输入有误，你还可以输入" + count + "次",null);
        }
    }

    public void showTintDialog(String title,String msg,DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", listener);
        builder.create().show();
    }

    @Override
    public void onInputFinish(String result) {

    }
}
