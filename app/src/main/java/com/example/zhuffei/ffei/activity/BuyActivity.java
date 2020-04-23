package com.example.zhuffei.ffei.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.fragment.PayDetailFragment;
import com.example.zhuffei.ffei.fragment.PwdFragment;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.PwdView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 12:49
 */
public class BuyActivity extends AppCompatActivity implements PwdView.InputCallBack {

    private ImageView goodsImg;

    private TextView goodsName;

    private TextView goodsPrice;

    private TextView price;

    private TextView confirm;

    private Goods data;

    private int gid;

    private ImageView back;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    initViews();

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        findViews();
        gid = getIntent().getIntExtra("gid", 0);
        getData();
        setListener();
    }

    private void setListener() {
        confirm.setOnClickListener(v -> {
                    PayDetailFragment payDetailFragment = new PayDetailFragment(data.getPrice(),this);
                    payDetailFragment.show(getSupportFragmentManager(), "payDetailFragment");
                }
        );
        back.setOnClickListener(v -> finish());
    }

    private void getData() {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.GETGOODSBYID, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                data = JSON.parseObject(String.valueOf(map.get("data")), Goods.class);
                handler.sendEmptyMessage(0);
            }
        });
    }

    private void findViews() {
        goodsImg = findViewById(R.id.goodsImg);
        goodsName = findViewById(R.id.goodsName);
        goodsPrice = findViewById(R.id.goodsPrice);
        price = findViewById(R.id.price);
        confirm = findViewById(R.id.confirm);
        back = findViewById(R.id.back);
    }

    private void initViews() {
        if (data.getType() == 1) {
            AsyncImageLoader asyncImageLoader = new AsyncImageLoader(this);
            asyncImageLoader.asyncloadImage(goodsImg, UrlTool.GOODSIMG + data.getImg1());
        } else {
            goodsImg.setImageResource(R.mipmap.qiugou);
        }
        goodsName.setText(data.getName());
        goodsPrice.setText(String.format("%.2f", data.getPrice()));
        price.setText(String.format("%.2f", data.getPrice()));

    }





    private PwdFragment fragment;

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

    private void createDialogFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(PwdFragment.TYPE, type);
        fragment = new PwdFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(BuyActivity.this);
        fragment.show(getSupportFragmentManager(), "Pwd");
    }

    //还可以输入密码次数
    private int count = 5;

    @Override
    public void onInputFinish(String result) {
        if (result.equals("123456")) {
            fragment.dismiss();
            Intent intent = new Intent(this,PaySuccessActivity.class);
            intent.putExtra("gid",gid);
            intent.putExtra("uid", FfeiApplication.user.getId());
            startActivity(intent);
            finish();
        }else {
            showPwdError();
        }
    }

    private void showPwdError() {
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


}
