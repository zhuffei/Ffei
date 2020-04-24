package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.fragment.FXFragment;
import com.example.zhuffei.ffei.fragment.GZFragment;
import com.example.zhuffei.ffei.fragment.LTFragment;
import com.example.zhuffei.ffei.fragment.WDFragment;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/20 15:21
 */
public class HomeActivity extends UI implements View.OnClickListener {
    //聊天页面常量
    private final int LT = 3;
    //发现页面常量
    private final int FX = 1;
    //关注页面常量
    private final int GZ = 2;
    //我的页面常量
    private final int WD = 4;

    //发现页面布局控件
    private LinearLayout mLinFX;
    //聊天页面布局控件
    private RelativeLayout mLinLT;
    //关注页面布局控件
    private LinearLayout mLinGZ;
    //我的页面布局控件
    private LinearLayout mLinWD;

    //聊天Fragment
    private LTFragment mLTFragment;
    //发现Fragment
    private FXFragment mFXFragment;
    //我的Fragment
    private WDFragment mWDFragment;
    //关注Fragment
    private GZFragment mGZFragment;

    private ImageView btnPlus;

    //关注ImageButton控件
    private ImageButton mGZIBtn;
    //发现ImageButton控件
    private ImageButton mFXIBtn;
    //聊天ImageButton控件
    private ImageButton mLTIBtn;
    //我的ImageButton控件
    private ImageButton mWDIBtn;

    //关注文字控件
    private TextView mGZTv;
    //发现文字控件
    private TextView mFXTv;
    //聊天文字控件
    private TextView mLTTv;
    //我的文字控件
    private TextView mWDTv;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView bubble;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        event();
        addFragment(FX);
        new Thread() {
            @Override
            public void run() {
                while (FfeiApplication.isLogin) {
                    int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (unreadNum == 0) {
                                bubble.setVisibility(View.GONE);
                            } else {
                                bubble.setVisibility(View.VISIBLE);
                                bubble.setText(unreadNum + "");
                            }
                        }
                    });
                    try {
                        Thread.sleep(5001);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    /**
     * 初始化所有控件
     */
    private void initView() {
        btnPlus = findViewById(R.id.mBtnPlus);
        mLinFX = findViewById(R.id.mLinFX);
        mLinLT = findViewById(R.id.mLinLiaoTian);
        mLinGZ = findViewById(R.id.mLinGuanZhu);
        mLinWD = findViewById(R.id.mLinUser);
        mGZIBtn = findViewById(R.id.mBtnGZ);
        mFXIBtn = findViewById(R.id.mBtnFX);
        mLTIBtn = findViewById(R.id.mBtnLT);
        mWDIBtn = findViewById(R.id.mBtnWD);
        mGZTv = findViewById(R.id.mTvGuanZhu);
        mFXTv = findViewById(R.id.mTvFaXian);
        mLTTv = findViewById(R.id.mTvLiaoTian);
        mWDTv = findViewById(R.id.mTvUser);
        bubble = findViewById(R.id.bubble);
        manager = getSupportFragmentManager();


    }

    /**
     * 初始化事件
     */
    private void event() {
        mLinLT.setOnClickListener(this);
        mLinGZ.setOnClickListener(this);
        mLinWD.setOnClickListener(this);
        mLinFX.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
    }

    /**
     * 初始化所有控件状态为原始状态
     */
    private void initColor() {
        mGZTv.setTextColor(getResources().getColor(R.color.normal));
        mFXTv.setTextColor(getResources().getColor(R.color.normal));
        mLTTv.setTextColor(getResources().getColor(R.color.normal));
        mWDTv.setTextColor(getResources().getColor(R.color.normal));
        mGZIBtn.setBackgroundResource(R.mipmap.focus_normal);
        mFXIBtn.setBackgroundResource(R.mipmap.faxian_normal);
        mLTIBtn.setBackgroundResource(R.mipmap.chat_normal);
        mWDIBtn.setBackgroundResource(R.mipmap.user_normal);
    }

    /**
     * 隐藏所有已经显示了Fragment
     *
     * @param transaction
     */
    private void hint(FragmentTransaction transaction) {
        if (mFXFragment != null) {
            transaction.hide(mFXFragment);
        }
        if (mLTFragment != null) {
            transaction.hide(mLTFragment);
        }
        if (mWDFragment != null) {
            transaction.hide(mWDFragment);
        }
        if (mGZFragment != null) {
            transaction.hide(mGZFragment);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mLinLiaoTian:
                //添加聊天布局的Fragment
                addFragment(LT);
                break;
            case R.id.mLinGuanZhu:
                //添加关注布局的Fragment
                addFragment(GZ);
                break;
            case R.id.mLinUser:
                //添加我的布局的Fragment
                addFragment(WD);
                break;
            case R.id.mLinFX:
                //添加发现布局的Fragment
                addFragment(FX);
                break;
            case R.id.mBtnPlus:
                Intent intent = new Intent(this, PlusActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 添加对应的布局，根据传来的ID进行添加
     *
     * @param index
     */
    private void addFragment(int index) {
        initColor();
        //获取事物
        transaction = manager.beginTransaction();
        //每次先隐藏所有的Fragment，然后加载当前要显示的Fragment
        hint(transaction);
        switch (index) {
            case LT:
                mLTTv.setTextColor(getResources().getColor(R.color.pressed));
                mLTIBtn.setBackgroundResource(R.mipmap.chat_pressed);
                //初次加载Fragment先判断是否为空
                if (mLTFragment == null) {
                    mLTFragment = new LTFragment();
                    //第一次添加到Fragment中
                    transaction.add(R.id.mFrame, mLTFragment);
                } else {
                    //以后的每一次只需要显示即可
                    transaction.show(mLTFragment);
                }
                break;
            case GZ:
                mGZTv.setTextColor(getResources().getColor(R.color.pressed));
                mGZIBtn.setBackgroundResource(R.mipmap.focus_pressed);
                if (mGZFragment == null) {
                    mGZFragment = new GZFragment();
                    transaction.add(R.id.mFrame, mGZFragment);
                } else {
                    transaction.show(mGZFragment);
                    mGZFragment.refresh();
                }
                break;
            case WD:
                mWDTv.setTextColor(getResources().getColor(R.color.pressed));
                mWDIBtn.setBackgroundResource(R.mipmap.user_pressed);
                if (mWDFragment == null) {
                    mWDFragment = new WDFragment();
                    transaction.add(R.id.mFrame, mWDFragment);
                } else {
                    transaction.show(mWDFragment);
                }
                break;
            case FX:
                mFXTv.setTextColor(getResources().getColor(R.color.pressed));
                mFXIBtn.setBackgroundResource(R.mipmap.faxian_pressed);
                if (mFXFragment == null) {
                    mFXFragment = new FXFragment();
                    transaction.add(R.id.mFrame, mFXFragment);
                } else {
                    transaction.show(mFXFragment);
                }
                break;
        }
        //最后记得提交
        transaction.commit();
    }


}
