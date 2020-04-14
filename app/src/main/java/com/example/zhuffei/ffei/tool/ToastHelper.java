package com.example.zhuffei.ffei.tool;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by zhuffei on 2020/3/14.
 */


public class ToastHelper {
    public static Toast mToast = null;

    /**
     * 弹出Toast
     *
     * @param context  上下文对象
     * @param text     提示的文本
     * @param duration 持续时间（0：短；1：长）
     */
    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    /**
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    /**
     * 弹出Toast
     *
     * @param context  上下文对象
     * @param text     提示的文本
     * @param duration 持续时间（0：短；1：长）
     * @param gravity  位置（Gravity.CENTER;Gravity.TOP;...）
     */
    public static void showToast(Context context, String text, int duration, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    /**
     * 关闭Toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}

