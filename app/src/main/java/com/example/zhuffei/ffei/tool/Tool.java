package com.example.zhuffei.ffei.tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.example.zhuffei.ffei.activity.DetailActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuffei on 2020/3/15.
 */

public class Tool {

    public static Bitmap bitmap ;
    /**
     * 检测字符串是否为手机号
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

        Matcher m = p.matcher(phone);

        return m.matches();
    }

    /**
     * 检查密码格式
     * @param password
     * @return
     */
    public static boolean TestPassword(String password) {
        Pattern p = Pattern.compile("^[\\da-zA-Z!@#$.%^&*]*$");

        Matcher m = p.matcher(password);

        return m.matches();
    }
    public static int getScreenWidth(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static void toDetail(Context context){
        Intent intent = new Intent(context, DetailActivity.class);
        context.startActivity(intent);
    }

    /**
     * 退出登录，清除用户数据
     * @param context
     */
    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.apply();
    }


}
