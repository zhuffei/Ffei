package com.example.zhuffei.ffei.tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.activity.DetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhuffei on 2020/3/15.
 */

public class Tool {

    public static Bitmap bitmap ;


    private static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static void toDetail(Context context,Integer gid){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("gid",gid);
        context.startActivity(intent);
    }

    /**
     * 退出登录，清除用户数据
     */
    public static void logout() {
        SharedPreferences preferences = FfeiApplication.context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.apply();
    }

    public static String parseTime(String time) {

        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            Date now = new Date();
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            String s1 = "";
            if (day > 0) {
                if(day>10){
                    return time.substring(0,10);
                }else{
                s1 = day + "天前";
                return s1;}
            }
            if (hour > 0) {
                s1 = hour + "小时前";
                return s1;
            }
            if (min > 0){
                s1 = min + "分钟前";
                return s1;}else {
                return  "刚刚";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "未知时间";
        }

    }


}
